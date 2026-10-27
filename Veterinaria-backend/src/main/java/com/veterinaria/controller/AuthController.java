package com.veterinaria.controller;

import com.veterinaria.dto.Dtos.*;
import com.veterinaria.exception.ApiException;
import com.veterinaria.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Registrarse, iniciar sesión y cerrar sesión (todos los roles)")
public class AuthController {

    private final UsuarioService usuarios;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public AuthController(UsuarioService usuarios, AuthenticationManager authenticationManager,
                          SecurityContextRepository securityContextRepository) {
        this.usuarios = usuarios;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @Operation(summary = "Registrarse (solo crea usuarios con rol PROPIETARIO)")
    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponse> registro(@Valid @RequestBody RegistroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UsuarioResponse.de(usuarios.registrarPropietario(request)));
    }

    @Operation(summary = "Iniciar sesión (crea la sesión y devuelve el usuario con su rol)")
    @PostMapping("/login")
    public UsuarioResponse login(@Valid @RequestBody LoginRequest request,
                                 HttpServletRequest http, HttpServletResponse response) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(
                            request.correo().trim().toLowerCase(Locale.ROOT), request.password()));

            if (http.getSession(false) != null) {
                http.changeSessionId(); // evita fijación de sesión
            }
            SecurityContext contexto = SecurityContextHolder.createEmptyContext();
            contexto.setAuthentication(auth);
            SecurityContextHolder.setContext(contexto);
            securityContextRepository.saveContext(contexto, http, response);

            return UsuarioResponse.de(usuarios.actual(auth.getName()));
        } catch (AuthenticationException e) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Correo o contraseña incorrectos.");
        }
    }

    @Operation(summary = "Usuario con sesión activa (el frontend lo usa para saber el rol)")
    @GetMapping("/me")
    public UsuarioResponse me(Authentication auth) {
        return UsuarioResponse.de(usuarios.actual(auth.getName()));
    }

    @Operation(summary = "Cerrar sesión")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest http) {
        if (http.getSession(false) != null) {
            http.getSession(false).invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }
}
