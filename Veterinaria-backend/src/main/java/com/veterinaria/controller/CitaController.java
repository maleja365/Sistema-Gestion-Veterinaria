package com.veterinaria.controller;

import com.veterinaria.dto.Dtos.*;
import com.veterinaria.service.CitaService;
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
@RequestMapping("/api/citas")
@Tag(name = "Citas", description = "Propietario: solicita y consulta. Veterinario: gestiona las asignadas.")
public class CitaController {

    private final CitaService citas;
    private final UsuarioService usuarios;

    public CitaController(CitaService citas, UsuarioService usuarios) {
        this.citas = citas;
        this.usuarios = usuarios;
    }

    @Operation(summary = "Listar citas (PROPIETARIO: las de sus mascotas; VETERINARIO: las asignadas)")
    @GetMapping
    public List<CitaResponse> listar(Authentication auth) {
        return citas.listar(usuarios.actual(auth.getName())).stream().map(CitaResponse::de).toList();
    }

    @Operation(summary = "Solicitar cita (solo PROPIETARIO)")
    @PostMapping
    public ResponseEntity<CitaResponse> solicitar(@Valid @RequestBody CitaRequest request, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CitaResponse.de(citas.solicitar(usuarios.actual(auth.getName()), request)));
    }

    @Operation(summary = "Confirmar, atender o cancelar una cita (solo VETERINARIO asignado)")
    @PatchMapping("/{id}/estado")
    public CitaResponse cambiarEstado(@PathVariable Long id, @Valid @RequestBody EstadoRequest request,
                                      Authentication auth) {
        return CitaResponse.de(citas.cambiarEstado(usuarios.actual(auth.getName()), id, request.estado()));
    }
}
