package com.veterinaria.service;

import com.veterinaria.dto.Dtos.EstadisticasResponse;
import com.veterinaria.repository.*;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class EstadisticaService {

    private final UsuarioRepository usuarios;
    private final MascotaRepository mascotas;
    private final CitaRepository citas;

    public EstadisticaService(UsuarioRepository usuarios, MascotaRepository mascotas, CitaRepository citas) {
        this.usuarios = usuarios;
        this.mascotas = mascotas;
        this.citas = citas;
    }

    public EstadisticasResponse generar() {
        return new EstadisticasResponse(
                usuarios.countByRol(UsuarioService.PROPIETARIO),
                usuarios.countByRol(UsuarioService.VETERINARIO),
                mascotas.count(),
                citas.count(),
                aMapa(citas.contarPorEstado()),
                aMapa(mascotas.contarPorEspecie()));
    }

    private Map<String, Long> aMapa(List<Object[]> filas) {
        Map<String, Long> mapa = new LinkedHashMap<>();
        for (Object[] f : filas) {
            mapa.put(String.valueOf(f[0]), ((Number) f[1]).longValue());
        }
        return mapa;
    }
}
