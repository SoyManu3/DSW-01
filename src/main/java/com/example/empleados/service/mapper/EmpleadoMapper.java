package com.example.empleados.service.mapper;

import com.example.empleados.controller.dto.EmpleadoResponse;
import com.example.empleados.domain.Empleado;
import org.springframework.stereotype.Component;

@Component
public class EmpleadoMapper {

    public EmpleadoResponse toResponse(Empleado empleado) {
        return new EmpleadoResponse(
                empleado.getClave(),
                empleado.getNombre(),
                empleado.getDireccion(),
                empleado.getTelefono(),
                empleado.getCorreo(),
                empleado.isActivo());
    }
}
