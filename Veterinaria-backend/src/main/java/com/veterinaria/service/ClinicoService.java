package com.veterinaria.service;

import com.veterinaria.dto.Dtos.DiagnosticoRequest;
import com.veterinaria.dto.Dtos.VacunaRequest;
import com.veterinaria.model.Diagnostico;
import com.veterinaria.model.Mascota;
import com.veterinaria.model.Usuario;
import com.veterinaria.model.Vacuna;
import com.veterinaria.repository.DiagnosticoRepository;
import com.veterinaria.repository.VacunaRepository;
import com.veterinaria.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/** Historial médico (diagnósticos) y vacunas de las mascotas. */
@Service
public class ClinicoService {

    private final DiagnosticoRepository diagnosticos;
    private final VacunaRepository vacunas;
    private final MascotaService mascotas;

    public ClinicoService(DiagnosticoRepository diagnosticos, VacunaRepository vacunas,
                          MascotaService mascotas) {
        this.diagnosticos = diagnosticos;
        this.vacunas = vacunas;
        this.mascotas = mascotas;
    }

    // Registrar: solo veterinario
    public Diagnostico registrarDiagnostico(Usuario vet, DiagnosticoRequest r) {
        Mascota m = mascotas.obtener(vet, r.mascotaId());
        Diagnostico d = new Diagnostico();
        d.setMascota(m);
        d.setVeterinario(vet);
        d.setDescripcion(r.descripcion().trim());
        d.setTratamiento(r.tratamiento() == null || r.tratamiento().isBlank() ? null : r.tratamiento().trim());
        return diagnosticos.save(d);
    }

    public Vacuna registrarVacuna(Usuario vet, VacunaRequest r) {
        Mascota m = mascotas.obtener(vet, r.mascotaId());
        if (r.proximaDosis() != null && r.proximaDosis().isBefore(r.fechaAplicacion())) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "La próxima dosis no puede ser anterior a la fecha de aplicación.");
        }
        Vacuna v = new Vacuna();
        v.setMascota(m);
        v.setVeterinario(vet);
        v.setNombre(r.nombre().trim());
        v.setFechaAplicacion(r.fechaAplicacion());
        v.setProximaDosis(r.proximaDosis());
        return vacunas.save(v);
    }

    // Consultar: propietario (solo sus mascotas) y veterinario
    public List<Diagnostico> historial(Usuario usuario, Long mascotaId) {
        mascotas.obtener(usuario, mascotaId);
        return diagnosticos.findByMascotaIdOrderByFechaDesc(mascotaId);
    }

    public List<Vacuna> vacunas(Usuario usuario, Long mascotaId) {
        mascotas.obtener(usuario, mascotaId);
        return vacunas.findByMascotaIdOrderByFechaAplicacionDesc(mascotaId);
    }
}
