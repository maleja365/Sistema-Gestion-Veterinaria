package com.veterinaria.service;

import com.veterinaria.dto.Dtos.CitaRequest;
import com.veterinaria.exception.ApiException;
import com.veterinaria.model.Cita;
import com.veterinaria.model.Mascota;
import com.veterinaria.model.Usuario;
import com.veterinaria.repository.CitaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
public class CitaService {

    private static final Set<String> ESTADOS_VET = Set.of("Confirmada", "Atendida", "Cancelada");

    private final CitaRepository repository;
    private final MascotaService mascotas;
    private final UsuarioService usuarios;

    public CitaService(CitaRepository repository, MascotaService mascotas, UsuarioService usuarios) {
        this.repository = repository;
        this.mascotas = mascotas;
        this.usuarios = usuarios;
    }

    /** Propietario: sus citas. Veterinario: las citas que le asignaron. */
    public List<Cita> listar(Usuario usuario) {
        List<Cita> lista = UsuarioService.PROPIETARIO.equals(usuario.getRol())
                ? repository.findByMascotaPropietarioId(usuario.getId())
                : repository.findByVeterinarioId(usuario.getId());
        return lista.stream()
                .sorted(Comparator.comparing(Cita::getFechaHora).reversed())
                .toList();
    }

    /** Caso de uso del propietario: Solicitar cita. */
    public Cita solicitar(Usuario propietario, CitaRequest r) {
        Mascota mascota = mascotas.obtener(propietario, r.mascotaId()); // valida que sea suya
        Usuario vet = usuarios.veterinario(r.veterinarioId());

        Cita c = new Cita();
        c.setMascota(mascota);
        c.setVeterinario(vet);
        c.setFechaHora(r.fechaHora());
        c.setMotivo(r.motivo().trim());
        c.setEstado("Pendiente");
        return repository.save(c);
    }

    /** Caso de uso del veterinario: Gestionar citas. */
    public Cita cambiarEstado(Usuario vet, Long id, String estado) {
        Cita c = repository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "La cita no existe."));
        if (!c.getVeterinario().getId().equals(vet.getId())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Esa cita no está asignada a ti.");
        }
        if (!ESTADOS_VET.contains(estado)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Estado no válido.");
        }
        if ("Atendida".equals(c.getEstado()) || "Cancelada".equals(c.getEstado())) {
            throw new ApiException(HttpStatus.CONFLICT, "La cita ya está " + c.getEstado().toLowerCase() + ".");
        }
        c.setEstado(estado);
        return repository.save(c);
    }
}
