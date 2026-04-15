package com.example.empleados.config;

import com.example.empleados.service.AuthenticationAuditService;
import com.example.empleados.service.EmpleadoAuthPolicyService;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationFailureDisabledEvent;
import org.springframework.security.authentication.event.AuthenticationFailureLockedEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEventsListener {

    private final EmpleadoAuthPolicyService authPolicyService;
    private final AuthenticationAuditService authenticationAuditService;

    public AuthenticationEventsListener(
            EmpleadoAuthPolicyService authPolicyService,
            AuthenticationAuditService authenticationAuditService) {
        this.authPolicyService = authPolicyService;
        this.authenticationAuditService = authenticationAuditService;
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        String correo = event.getAuthentication().getName();
        authPolicyService.registrarAutenticacionExitosa(correo);
        authenticationAuditService.registrar(correo, "SUCCESS", null);
    }

    @EventListener
    public void onFailure(AuthenticationFailureBadCredentialsEvent event) {
        String correo = event.getAuthentication().getName();
        authPolicyService.registrarIntentoFallido(correo);
        authenticationAuditService.registrar(correo, "INVALID_CREDENTIALS", null);
    }

    @EventListener
    public void onLocked(AuthenticationFailureLockedEvent event) {
        String correo = event.getAuthentication().getName();
        authenticationAuditService.registrar(correo, "BLOCKED", null);
    }

    @EventListener
    public void onDisabled(AuthenticationFailureDisabledEvent event) {
        String correo = event.getAuthentication().getName();
        authenticationAuditService.registrar(correo, "INACTIVE", null);
    }
}
