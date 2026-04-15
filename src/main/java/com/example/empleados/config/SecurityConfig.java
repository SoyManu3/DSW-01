package com.example.empleados.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final AuthenticationStatusEntryPoint authenticationStatusEntryPoint;

    public SecurityConfig(AuthenticationStatusEntryPoint authenticationStatusEntryPoint) {
        this.authenticationStatusEntryPoint = authenticationStatusEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        http
            .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/actuator/health")
                        .permitAll()
                .requestMatchers("/api/v1/auth/login").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/v1/empleados/**").authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/v1/empleados/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/v1/empleados/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/empleados/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/v1/departamentos/**").authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/v1/departamentos/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/v1/departamentos/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/departamentos/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .userDetailsService(userDetailsService)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationStatusEntryPoint))
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
