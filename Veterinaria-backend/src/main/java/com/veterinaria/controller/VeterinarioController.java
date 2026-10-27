package com.veterinaria.controller;

import com.veterinaria.dto.Dtos.UsuarioResponse;
import com.veterinaria.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/veterinarios")
@Tag(name = "Propietario", description = "Acciones del rol PROPIETARIO")
public class VeterinarioController {

    private final UsuarioService usuarios;

    public VeterinarioController(UsuarioService usuarios) {
        this.usuarios = usuarios;
    }

    @Operation(summary = "Listar veterinarios disponibles para solicitar una cita")
    @GetMapping
    public List<UsuarioResponse> listar() {
        return usuarios.listarVeterinarios().stream().map(UsuarioResponse::de).toList();
    }
}
