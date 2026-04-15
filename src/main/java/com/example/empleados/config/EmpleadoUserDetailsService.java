package com.example.empleados.config;

import com.example.empleados.domain.Empleado;
import com.example.empleados.domain.RolEmpleado;
import com.example.empleados.repository.EmpleadoRepository;
import com.example.empleados.service.EmpleadoAuthPolicyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoUserDetailsService implements UserDetailsService {

    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoAuthPolicyService authPolicyService;
    private final String bootstrapUser;
    private final String bootstrapPassword;
    private final PasswordEncoder passwordEncoder;

    public EmpleadoUserDetailsService(
            EmpleadoRepository empleadoRepository,
            EmpleadoAuthPolicyService authPolicyService,
            @Value("${APP_BASIC_AUTH_USER:admin}") String bootstrapUser,
            @Value("${APP_BASIC_AUTH_PASSWORD:admin123}") String bootstrapPassword,
            PasswordEncoder passwordEncoder) {
        this.empleadoRepository = empleadoRepository;
        this.authPolicyService = authPolicyService;
        this.bootstrapUser = bootstrapUser;
        this.bootstrapPassword = bootstrapPassword;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String normalized = username == null ? "" : username.trim().toLowerCase();

        Empleado empleado = empleadoRepository.findByCorreoIgnoreCase(normalized).orElse(null);

        if (empleado == null && bootstrapUser.equalsIgnoreCase(normalized)) {
            return User.withUsername(bootstrapUser)
                    .password(passwordEncoder.encode(bootstrapPassword))
                    .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))
                    .build();
        }

        if (empleado == null) {
            throw new UsernameNotFoundException("Credenciales inválidas");
        }

        authPolicyService.desbloquearSiCorresponde(empleado);
        boolean accountNonLocked = !authPolicyService.estaBloqueado(empleado);

        RolEmpleado rol = empleado.getRol() == null ? RolEmpleado.EMPLEADO : empleado.getRol();

        return User.withUsername(empleado.getCorreo())
                .password(empleado.getPasswordHash())
                .accountLocked(!accountNonLocked)
                .disabled(!empleado.isActivo())
            .authorities(new SimpleGrantedAuthority(rol.toAuthority()))
                .build();
    }
}
