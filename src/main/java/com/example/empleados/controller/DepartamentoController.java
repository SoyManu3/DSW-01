package com.example.empleados.controller;

import com.example.empleados.controller.dto.DepartamentoCreateRequest;
import com.example.empleados.controller.dto.DepartamentoResponse;
import com.example.empleados.controller.dto.DepartamentoUpdateRequest;
import com.example.empleados.controller.dto.PaginatedDepartamentosResponse;
import com.example.empleados.controller.dto.PaginatedEmpleadosDepartamentoResponse;
import com.example.empleados.service.DepartamentoService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/departamentos")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    @PostMapping
    public ResponseEntity<DepartamentoResponse> crear(@Valid @RequestBody DepartamentoCreateRequest request) {
        DepartamentoResponse response = departamentoService.crear(request);
        URI location = URI.create("/api/v1/departamentos/" + response.getId());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedDepartamentosResponse> listar(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(departamentoService.listar(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartamentoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(departamentoService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartamentoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody DepartamentoUpdateRequest request) {
        return ResponseEntity.ok(departamentoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        departamentoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/empleados")
    public ResponseEntity<PaginatedEmpleadosDepartamentoResponse> listarEmpleados(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(departamentoService.listarEmpleados(id, page));
    }
}
