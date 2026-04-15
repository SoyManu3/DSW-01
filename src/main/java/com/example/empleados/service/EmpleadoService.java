package com.example.empleados.service;

import com.example.empleados.controller.dto.EmpleadoCreateRequest;
import com.example.empleados.controller.dto.EmpleadoResponse;
import com.example.empleados.controller.dto.EmpleadoUpdateRequest;
import com.example.empleados.controller.dto.PaginatedEmpleadosResponse;
import com.example.empleados.domain.Departamento;
import com.example.empleados.domain.Empleado;
import com.example.empleados.domain.EmpleadoId;
import com.example.empleados.repository.DepartamentoRepository;
import com.example.empleados.repository.EmpleadoRepository;
import com.example.empleados.service.exception.EmpleadoBlockedException;
import com.example.empleados.service.exception.EmpleadoConflictException;
import com.example.empleados.service.mapper.EmpleadoMapper;
import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoService {

    private static final String PREFIJO = "EMP";
    private static final int PAGE_SIZE = 10;

    private final EmpleadoRepository empleadoRepository;
    private final DepartamentoRepository departamentoRepository;
    private final EmpleadoMapper empleadoMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmpleadoAuthPolicyService authPolicyService;

    public EmpleadoService(
            EmpleadoRepository empleadoRepository,
            DepartamentoRepository departamentoRepository,
            EmpleadoMapper empleadoMapper,
            PasswordEncoder passwordEncoder,
            EmpleadoAuthPolicyService authPolicyService) {
        this.empleadoRepository = empleadoRepository;
        this.departamentoRepository = departamentoRepository;
        this.empleadoMapper = empleadoMapper;
        this.passwordEncoder = passwordEncoder;
        this.authPolicyService = authPolicyService;
    }

    @Transactional
    public EmpleadoResponse crear(EmpleadoCreateRequest request) {
        String correo = request.getCorreo().trim().toLowerCase();
        if (empleadoRepository.existsByCorreoIgnoreCase(correo)) {
            throw new EmpleadoConflictException("El correo ya está registrado");
        }
        authPolicyService.validarPassword(request.getPassword());

        Long siguienteNumero = empleadoRepository
                .findTopByIdPrefijoOrderByIdNumeroAutonumericoDesc(PREFIJO)
                .map(e -> e.getId().getNumeroAutonumerico() + 1)
                .orElse(1L);

        EmpleadoId id = new EmpleadoId(PREFIJO, siguienteNumero);
        if (empleadoRepository.existsById(id)) {
            throw new EmpleadoConflictException("Conflicto en generación de clave");
        }

        Departamento departamento = departamentoRepository.findById(request.getDepartamentoId())
            .orElseThrow(() -> new NoSuchElementException("Departamento no encontrado"));

        Empleado empleado = new Empleado(
                id,
                request.getNombre().trim(),
                request.getDireccion().trim(),
            request.getTelefono().trim(),
            correo,
            passwordEncoder.encode(request.getPassword()),
            true,
            0,
            null,
            departamento);

        Empleado guardado = empleadoRepository.save(empleado);
        return empleadoMapper.toResponse(guardado);
    }

    @Transactional
    public EmpleadoResponse actualizar(String clave, EmpleadoUpdateRequest request) {
        EmpleadoId id = parseClave(clave);
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Empleado no encontrado"));

        if (!empleado.isActivo()) {
            throw new EmpleadoBlockedException("Cuenta inactiva");
        }

        String correo = request.getCorreo().trim().toLowerCase();
        empleadoRepository.findByCorreoIgnoreCase(correo)
                .filter(existing -> !existing.getId().equals(empleado.getId()))
                .ifPresent(existing -> {
                    throw new EmpleadoConflictException("El correo ya está registrado");
                });

        empleado.setNombre(request.getNombre().trim());
        empleado.setDireccion(request.getDireccion().trim());
        empleado.setTelefono(request.getTelefono().trim());
        empleado.setCorreo(correo);

        Departamento departamento = departamentoRepository.findById(request.getDepartamentoId())
            .orElseThrow(() -> new NoSuchElementException("Departamento no encontrado"));
        empleado.setDepartamento(departamento);

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            authPolicyService.validarPassword(request.getPassword());
            empleado.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        Empleado actualizado = empleadoRepository.save(empleado);
        return empleadoMapper.toResponse(actualizado);
    }

    @Transactional
    public void eliminar(String clave) {
        EmpleadoId id = parseClave(clave);
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Empleado no encontrado"));
        empleadoRepository.delete(empleado);
    }

    public EmpleadoResponse obtenerPorClave(String clave) {
        EmpleadoId id = parseClave(clave);
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Empleado no encontrado"));
        return empleadoMapper.toResponse(empleado);
    }

    public PaginatedEmpleadosResponse listar(int page) {
        int safePage = Math.max(page, 0);
        PageRequest pageRequest = PageRequest.of(
                safePage,
                PAGE_SIZE,
                Sort.by("id.numeroAutonumerico").ascending());

        Page<Empleado> empleadosPage = empleadoRepository.findAll(pageRequest);
        return new PaginatedEmpleadosResponse(
                empleadosPage.getContent().stream().map(empleadoMapper::toResponse).toList(),
                empleadosPage.getNumber(),
                empleadosPage.getSize(),
                empleadosPage.getTotalElements(),
                empleadosPage.getTotalPages());
    }

    private EmpleadoId parseClave(String clave) {
        if (clave == null || !clave.matches("^EMP-[1-9][0-9]*$")) {
            throw new IllegalArgumentException("Clave inválida. Debe cumplir EMP-<n>");
        }
        String[] partes = clave.split("-");
        Long numero = Long.parseLong(partes[1]);
        return new EmpleadoId(partes[0], numero);
    }
}
