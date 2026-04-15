package com.example.empleados.service;

import com.example.empleados.controller.dto.DepartamentoCreateRequest;
import com.example.empleados.controller.dto.DepartamentoResponse;
import com.example.empleados.controller.dto.DepartamentoUpdateRequest;
import com.example.empleados.controller.dto.EmpleadoDepartamentoResponse;
import com.example.empleados.controller.dto.PaginatedDepartamentosResponse;
import com.example.empleados.controller.dto.PaginatedEmpleadosDepartamentoResponse;
import com.example.empleados.domain.Departamento;
import com.example.empleados.domain.Empleado;
import com.example.empleados.repository.DepartamentoRepository;
import com.example.empleados.repository.EmpleadoRepository;
import com.example.empleados.service.exception.DepartamentoConEmpleadosException;
import com.example.empleados.service.exception.DepartamentoDuplicadoException;
import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class DepartamentoService {

    private static final int PAGE_SIZE = 10;

    private final DepartamentoRepository departamentoRepository;
    private final EmpleadoRepository empleadoRepository;

    public DepartamentoService(
            DepartamentoRepository departamentoRepository,
            EmpleadoRepository empleadoRepository) {
        this.departamentoRepository = departamentoRepository;
        this.empleadoRepository = empleadoRepository;
    }

    @Transactional
    public DepartamentoResponse crear(DepartamentoCreateRequest request) {
        String nombre = normalizeNombre(request.getNombre());
        if (departamentoRepository.existsByNombre(nombre)) {
            throw new DepartamentoDuplicadoException("El nombre de departamento ya existe");
        }

        Departamento guardado = departamentoRepository.save(new Departamento(null, nombre));
        return toDepartamentoResponse(guardado);
    }

    public PaginatedDepartamentosResponse listar(int page) {
        int safePage = Math.max(page, 0);
        PageRequest pageRequest = PageRequest.of(safePage, PAGE_SIZE, Sort.by("id").ascending());
        Page<Departamento> departamentosPage = departamentoRepository.findAll(pageRequest);

        return new PaginatedDepartamentosResponse(
                departamentosPage.getContent().stream().map(this::toDepartamentoResponse).toList(),
                departamentosPage.getNumber(),
                departamentosPage.getSize(),
                departamentosPage.getTotalElements(),
                departamentosPage.getTotalPages());
    }

    public DepartamentoResponse obtenerPorId(Long id) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Departamento no encontrado"));
        return toDepartamentoResponse(departamento);
    }

    @Transactional
    public DepartamentoResponse actualizar(Long id, DepartamentoUpdateRequest request) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Departamento no encontrado"));

        String nombre = normalizeNombre(request.getNombre());
        if (!departamento.getNombre().equals(nombre) && departamentoRepository.existsByNombre(nombre)) {
            throw new DepartamentoDuplicadoException("El nombre de departamento ya existe");
        }

        departamento.setNombre(nombre);
        Departamento actualizado = departamentoRepository.save(departamento);
        return toDepartamentoResponse(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Departamento no encontrado"));

        if (empleadoRepository.existsByDepartamentoId(id)) {
            throw new DepartamentoConEmpleadosException("No se puede eliminar un departamento con empleados asociados");
        }

        departamentoRepository.delete(departamento);
    }

    public PaginatedEmpleadosDepartamentoResponse listarEmpleados(Long departamentoId, int page) {
        Departamento departamento = departamentoRepository.findById(departamentoId)
                .orElseThrow(() -> new NoSuchElementException("Departamento no encontrado"));

        int safePage = Math.max(page, 0);
        PageRequest pageRequest = PageRequest.of(
                safePage,
                PAGE_SIZE,
                Sort.by("id.numeroAutonumerico").ascending());

        Page<Empleado> empleadosPage = empleadoRepository.findByDepartamentoIdOrderByIdNumeroAutonumericoAsc(
                departamento.getId(),
                pageRequest);

        return new PaginatedEmpleadosDepartamentoResponse(
                empleadosPage.getContent().stream()
                        .map(this::toEmpleadoDepartamentoResponse)
                        .toList(),
                empleadosPage.getNumber(),
                empleadosPage.getSize(),
                empleadosPage.getTotalElements(),
                empleadosPage.getTotalPages());
    }

    private DepartamentoResponse toDepartamentoResponse(Departamento departamento) {
        long empleadosCount = empleadoRepository.countByDepartamentoId(departamento.getId());
        return new DepartamentoResponse(departamento.getId(), departamento.getNombre(), empleadosCount);
    }

    private EmpleadoDepartamentoResponse toEmpleadoDepartamentoResponse(Empleado empleado) {
        Long departamentoId = empleado.getDepartamento() != null ? empleado.getDepartamento().getId() : null;
        return new EmpleadoDepartamentoResponse(
                empleado.getClave(),
                empleado.getNombre(),
                empleado.getCorreo(),
                departamentoId);
    }

    private String normalizeNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del departamento es obligatorio");
        }
        String normalized = nombre.trim();
        if (normalized.length() > 100) {
            throw new IllegalArgumentException("El nombre del departamento no puede exceder 100 caracteres");
        }
        return normalized;
    }
}
