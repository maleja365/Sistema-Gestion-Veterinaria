package com.veterinaria.controller;

import com.veterinaria.dto.Dtos.*;
import com.veterinaria.service.EstadisticaService;
import com.veterinaria.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Todo lo que está aquí es SOLO para el rol ADMIN (ver SecurityConfig). */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Administrador", description = "Gestionar veterinarios y consultar estadísticas (rol ADMIN)")
public class AdminController {

    private final UsuarioService usuarios;
    private final EstadisticaService estadisticas;

    public AdminController(UsuarioService usuarios, EstadisticaService estadisticas) {
        this.usuarios = usuarios;
        this.estadisticas = estadisticas;
    }

    @Operation(summary = "Listar veterinarios")
    @GetMapping("/veterinarios")
    public List<UsuarioResponse> listar() {
        return usuarios.listarVeterinarios().stream().map(UsuarioResponse::de).toList();
    }

    @Operation(summary = "Registrar un veterinario")
    @PostMapping("/veterinarios")
    public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody VeterinarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UsuarioResponse.de(usuarios.crearVeterinario(request)));
    }

    @Operation(summary = "Actualizar un veterinario (contraseña opcional)")
    @PutMapping("/veterinarios/{id}")
    public UsuarioResponse actualizar(@PathVariable Long id, @Valid @RequestBody VeterinarioRequest request) {
        return UsuarioResponse.de(usuarios.actualizarVeterinario(id, request));
    }

    @Operation(summary = "Eliminar un veterinario sin registros asociados")
    @DeleteMapping("/veterinarios/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarios.eliminarVeterinario(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Consultar estadísticas generales")
    @GetMapping("/estadisticas")
    public EstadisticasResponse estadisticas() {
        return estadisticas.generar();
    }
}
