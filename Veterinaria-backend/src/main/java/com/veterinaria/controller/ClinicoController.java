package com.veterinaria.controller;

import com.veterinaria.dto.Dtos.*;
import com.veterinaria.service.ClinicoService;
import com.veterinaria.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Historial y vacunas", description = "Veterinario registra; propietario y veterinario consultan")
public class ClinicoController {

    private final ClinicoService clinico;
    private final UsuarioService usuarios;

    public ClinicoController(ClinicoService clinico, UsuarioService usuarios) {
        this.clinico = clinico;
        this.usuarios = usuarios;
    }

    @Operation(summary = "Registrar diagnóstico (solo VETERINARIO)")
    @PostMapping("/diagnosticos")
    public ResponseEntity<DiagnosticoResponse> diagnostico(@Valid @RequestBody DiagnosticoRequest request,
                                                           Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(DiagnosticoResponse.de(
                clinico.registrarDiagnostico(usuarios.actual(auth.getName()), request)));
    }

    @Operation(summary = "Registrar vacuna (solo VETERINARIO)")
    @PostMapping("/vacunas")
    public ResponseEntity<VacunaResponse> vacuna(@Valid @RequestBody VacunaRequest request, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(VacunaResponse.de(
                clinico.registrarVacuna(usuarios.actual(auth.getName()), request)));
    }

    @Operation(summary = "Consultar historial médico de una mascota")
    @GetMapping("/mascotas/{id}/historial")
    public List<DiagnosticoResponse> historial(@PathVariable Long id, Authentication auth) {
        return clinico.historial(usuarios.actual(auth.getName()), id).stream()
                .map(DiagnosticoResponse::de).toList();
    }

    @Operation(summary = "Consultar vacunas de una mascota")
    @GetMapping("/mascotas/{id}/vacunas")
    public List<VacunaResponse> vacunas(@PathVariable Long id, Authentication auth) {
        return clinico.vacunas(usuarios.actual(auth.getName()), id).stream()
                .map(VacunaResponse::de).toList();
    }
}
