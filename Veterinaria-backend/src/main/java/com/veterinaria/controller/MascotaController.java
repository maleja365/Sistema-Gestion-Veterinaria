package com.veterinaria.controller;

import com.veterinaria.dto.Dtos.*;
import com.veterinaria.service.MascotaService;
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
@RequestMapping("/api/mascotas")
@Tag(name = "Mascotas", description = "Propietario: registrar/actualizar/eliminar las suyas. Veterinario: consultar.")
public class MascotaController {

    private final MascotaService mascotas;
    private final UsuarioService usuarios;

    public MascotaController(MascotaService mascotas, UsuarioService usuarios) {
        this.mascotas = mascotas;
        this.usuarios = usuarios;
    }

    @Operation(summary = "Listar mascotas (PROPIETARIO: las suyas; VETERINARIO: todas)")
    @GetMapping
    public List<MascotaResponse> listar(Authentication auth) {
        return mascotas.listar(usuarios.actual(auth.getName())).stream().map(MascotaResponse::de).toList();
    }

    @Operation(summary = "Registrar mascota (solo PROPIETARIO)")
    @PostMapping
    public ResponseEntity<MascotaResponse> crear(@Valid @RequestBody MascotaRequest request, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MascotaResponse.de(mascotas.crear(usuarios.actual(auth.getName()), request)));
    }

    @Operation(summary = "Actualizar mascota (solo PROPIETARIO dueño)")
    @PutMapping("/{id}")
    public MascotaResponse actualizar(@PathVariable Long id, @Valid @RequestBody MascotaRequest request,
                                      Authentication auth) {
        return MascotaResponse.de(mascotas.actualizar(usuarios.actual(auth.getName()), id, request));
    }

    @Operation(summary = "Eliminar mascota (solo PROPIETARIO dueño)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, Authentication auth) {
        mascotas.eliminar(usuarios.actual(auth.getName()), id);
        return ResponseEntity.noContent().build();
    }
}
