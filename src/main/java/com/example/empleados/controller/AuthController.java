package com.example.empleados.controller;

import com.example.empleados.controller.dto.LoginRequest;
import com.example.empleados.controller.dto.LoginResponse;
import com.example.empleados.service.exception.EmpleadoBlockedException;
import com.example.empleados.service.exception.InvalidCredentialsException;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            String usuario = request.usuario().trim();
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuario, request.contrasena()));

            String rol = authentication.getAuthorities().stream()
                    .map(authority -> authority.getAuthority())
                    .findFirst()
                    .map(value -> value.replaceFirst("^ROLE_", ""))
                    .orElse("EMPLEADO");

            return ResponseEntity.ok(new LoginResponse(UUID.randomUUID().toString(), usuario, rol));
        } catch (LockedException ex) {
            throw new EmpleadoBlockedException("Cuenta bloqueada temporalmente");
        } catch (DisabledException ex) {
            throw new EmpleadoBlockedException("Cuenta inactiva");
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException("Credenciales incorrectas");
        }
    }
}
