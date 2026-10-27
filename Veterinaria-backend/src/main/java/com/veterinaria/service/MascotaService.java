package com.veterinaria.service;

import com.veterinaria.dto.Dtos.MascotaRequest;
import com.veterinaria.exception.ApiException;
import com.veterinaria.model.Mascota;
import com.veterinaria.model.Usuario;
import com.veterinaria.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class MascotaService {

    private static final Set<String> SEXOS = Set.of("Macho", "Hembra", "No especificado");

    private final MascotaRepository repository;
    private final CitaRepository citas;
    private final DiagnosticoRepository diagnosticos;
    private final VacunaRepository vacunas;

    public MascotaService(MascotaRepository repository, CitaRepository citas,
                          DiagnosticoRepository diagnosticos, VacunaRepository vacunas) {
        this.repository = repository;
        this.citas = citas;
        this.diagnosticos = diagnosticos;
        this.vacunas = vacunas;
    }

    /** Propietario: solo las suyas. Veterinario: todas (consultar mascotas). */
    public List<Mascota> listar(Usuario usuario) {
        if (UsuarioService.PROPIETARIO.equals(usuario.getRol())) {
            return repository.findByPropietarioId(usuario.getId());
        }
        return repository.findAll();
    }

    /** Devuelve la mascota si el usuario tiene derecho a verla. */
    public Mascota obtener(Usuario usuario, Long id) {
        Mascota m = repository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "La mascota no existe."));
        if (UsuarioService.PROPIETARIO.equals(usuario.getRol())
                && !m.getPropietario().getId().equals(usuario.getId())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Esa mascota no te pertenece.");
        }
        return m;
    }

    public Mascota crear(Usuario propietario, MascotaRequest r) {
        Mascota m = new Mascota();
        m.setPropietario(propietario);
        aplicar(m, r);
        return repository.save(m);
    }

    public Mascota actualizar(Usuario propietario, Long id, MascotaRequest r) {
        Mascota m = obtener(propietario, id);
        aplicar(m, r);
        return repository.save(m);
    }

    @Transactional
    public void eliminar(Usuario propietario, Long id) {
        Mascota m = obtener(propietario, id);
        citas.deleteByMascotaId(id);
        diagnosticos.deleteByMascotaId(id);
        vacunas.deleteByMascotaId(id);
        repository.delete(m);
    }

    private void aplicar(Mascota m, MascotaRequest r) {
        if (r.especie() == null || r.especie().isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La especie es obligatoria.");
        }
        if (r.especie().trim().length() > 50) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La especie admite máximo 50 caracteres.");
        }
        if (r.sexo() != null && !r.sexo().isBlank() && !SEXOS.contains(r.sexo())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Selecciona un sexo válido.");
        }
        m.setNombre(r.nombre().trim());
        m.setEspecie(r.especie().trim());
        m.setRaza(r.raza() == null || r.raza().isBlank() ? null : r.raza().trim());
        m.setFechaNacimiento(r.fechaNacimiento());
        m.setSexo(r.sexo() == null || r.sexo().isBlank() ? null : r.sexo());
    }
}