package com.veterinaria.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.List;

/**
 * Backend puro (API REST). El frontend vive en Visual Studio Code (Live Server)
 * y se conecta por CORS usando la sesión (cookie JSESSIONID).
 *
 * Roles: ADMIN, VETERINARIO, PROPIETARIO.
 */
@Configuration
public class SecurityConfig {

    @Value("${app.cors.origenes:http://localhost:*,http://127.0.0.1:*}")
    private List<String> origenesPermitidos;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   SecurityContextRepository contextRepository) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .securityContext(sc -> sc.securityContextRepository(contextRepository))

            .authorizeHttpRequests(auth -> auth

                // Swagger (documentación)
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**",
                        "/v3/api-docs", "/v3/api-docs/**").permitAll()

                .requestMatchers("/error").permitAll()

                // Registrarse e iniciar sesión (cualquiera)
                .requestMatchers(HttpMethod.POST, "/api/auth/registro", "/api/auth/login").permitAll()
                .requestMatchers("/api/auth/**").authenticated()

                // ADMIN: gestionar veterinarios + estadísticas (nada más)
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // PROPIETARIO: sus mascotas y sus citas
                .requestMatchers("/api/veterinarios").hasRole("PROPIETARIO")
                .requestMatchers(HttpMethod.POST, "/api/mascotas").hasRole("PROPIETARIO")
                .requestMatchers(HttpMethod.PUT, "/api/mascotas/*").hasRole("PROPIETARIO")
                .requestMatchers(HttpMethod.DELETE, "/api/mascotas/*").hasRole("PROPIETARIO")
                .requestMatchers(HttpMethod.POST, "/api/citas").hasRole("PROPIETARIO")

                // VETERINARIO: gestionar citas, registrar diagnóstico y vacunas
                .requestMatchers(HttpMethod.PATCH, "/api/citas/*/estado").hasRole("VETERINARIO")
                .requestMatchers(HttpMethod.POST, "/api/diagnosticos", "/api/vacunas").hasRole("VETERINARIO")

                // Consultas compartidas entre PROPIETARIO y VETERINARIO
                // (el servicio limita al propietario a lo suyo)
                .requestMatchers(HttpMethod.GET, "/api/mascotas", "/api/mascotas/*/historial",
                        "/api/mascotas/*/vacunas", "/api/citas").hasAnyRole("PROPIETARIO", "VETERINARIO")

                // Todo lo demás, bloqueado
                .anyRequest().denyAll()
            )

            // La API responde JSON, no redirige a páginas de login
            .exceptionHandling(e -> e
                .authenticationEntryPoint((req, res, ex) ->
                        json(res, 401, "Debes iniciar sesión para continuar."))
                .accessDeniedHandler((req, res, ex) ->
                        json(res, 403, "Tu rol no tiene permiso para realizar esta acción."))
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOriginPatterns(origenesPermitidos);
        cors.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cors.setAllowedHeaders(List.of("*"));
        cors.setAllowCredentials(true); // necesario para enviar la cookie de sesión

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

    private void json(HttpServletResponse res, int status, String mensaje) throws IOException {
        res.setStatus(status);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write("{\"mensaje\":\"" + mensaje + "\"}");
    }
}
