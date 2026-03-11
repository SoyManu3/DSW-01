package com.example.empleados.controller;

import com.example.empleados.controller.dto.EmpleadoCreateRequest;
import com.example.empleados.controller.dto.EmpleadoResponse;
import com.example.empleados.controller.dto.EmpleadoUpdateRequest;
import com.example.empleados.controller.dto.PaginatedEmpleadosResponse;
import com.example.empleados.service.EmpleadoService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @PostMapping
    public ResponseEntity<EmpleadoResponse> crear(@Valid @RequestBody EmpleadoCreateRequest request) {
        EmpleadoResponse response = empleadoService.crear(request);
        URI location = URI.create("/api/v1/empleados/" + response.getClave());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedEmpleadosResponse> listar(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(empleadoService.listar(page));
    }

    @GetMapping("/{clave}")
    public ResponseEntity<EmpleadoResponse> obtenerPorClave(@PathVariable String clave) {
        return ResponseEntity.ok(empleadoService.obtenerPorClave(clave));
    }

    @PutMapping("/{clave}")
    public ResponseEntity<EmpleadoResponse> actualizar(
            @PathVariable String clave,
            @Valid @RequestBody EmpleadoUpdateRequest request) {
        return ResponseEntity.ok(empleadoService.actualizar(clave, request));
    }

    @DeleteMapping("/{clave}")
    public ResponseEntity<Void> eliminar(@PathVariable String clave) {
        empleadoService.eliminar(clave);
        return ResponseEntity.noContent().build();
    }
}
