package com.veterinaria.service;

import com.veterinaria.dto.Dtos.RegistroRequest;
import com.veterinaria.dto.Dtos.VeterinarioRequest;
import com.veterinaria.exception.ApiException;
import com.veterinaria.model.Usuario;
import com.veterinaria.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class UsuarioService {

    public static final String ADMIN = "ADMIN";
    public static final String VETERINARIO = "VETERINARIO";
    public static final String PROPIETARIO = "PROPIETARIO";

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;
    private final CitaRepository citas;
    private final DiagnosticoRepository diagnosticos;
    private final VacunaRepository vacunas;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder encoder,
                          CitaRepository citas, DiagnosticoRepository diagnosticos,
                          VacunaRepository vacunas) {
        this.repository = repository;
        this.encoder = encoder;
        this.citas = citas;
        this.diagnosticos = diagnosticos;
        this.vacunas = vacunas;
    }

    /** Usuario autenticado a partir del correo de la sesión. */
    public Usuario actual(String correo) {
        return repository.findByCorreo(correo)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Sesión no válida."));
    }

    // ---- Caso de uso: Registrarse (solo crea PROPIETARIO) ----
    public Usuario registrarPropietario(RegistroRequest r) {
        return crear(r.nombre(), r.correo(), r.password(), PROPIETARIO);
    }

    // ---- Casos de uso del administrador: Gestionar veterinarios ----
    public List<Usuario> listarVeterinarios() {
        return repository.findByRol(VETERINARIO);
    }

    public Usuario crearVeterinario(VeterinarioRequest r) {
        if (r.password() == null || r.password().length() < 8) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "La contraseña debe tener al menos 8 caracteres.");
        }
        return crear(r.nombre(), r.correo(), r.password(), VETERINARIO);
    }

    public Usuario actualizarVeterinario(Long id, VeterinarioRequest r) {
        Usuario vet = veterinario(id);
        String correo = normalizar(r.correo());

        repository.findByCorreo(correo).ifPresent(otro -> {
            if (!otro.getId().equals(id)) {
                throw new ApiException(HttpStatus.CONFLICT, "Ese correo ya está registrado.");
            }
        });

        if (r.password() != null && !r.password().isBlank()) {
            if (r.password().length() < 8) {
                throw new ApiException(HttpStatus.BAD_REQUEST,
                        "La nueva contraseña debe tener al menos 8 caracteres.");
            }
            vet.setPassword(encoder.encode(r.password()));
        }
        vet.setNombre(r.nombre().trim());
        vet.setCorreo(correo);
        return repository.save(vet);
    }

    public void eliminarVeterinario(Long id) {
        Usuario vet = veterinario(id);
        if (citas.existsByVeterinarioId(id) || diagnosticos.existsByVeterinarioId(id)
                || vacunas.existsByVeterinarioId(id)) {
            throw new ApiException(HttpStatus.CONFLICT,
                    "No se puede eliminar: el veterinario tiene citas o registros clínicos asociados.");
        }
        repository.delete(vet);
    }

    public Usuario veterinario(Long id) {
        return repository.findById(id)
                .filter(u -> VETERINARIO.equals(u.getRol()))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "El veterinario no existe."));
    }

    // ---- util ----
    private Usuario crear(String nombre, String correo, String password, String rol) {
        String email = normalizar(correo);
        if (repository.existsByCorreo(email)) {
            throw new ApiException(HttpStatus.CONFLICT, "Ya existe un usuario con ese correo.");
        }
        Usuario u = new Usuario();
        u.setNombre(nombre.trim());
        u.setCorreo(email);
        u.setPassword(encoder.encode(password));
        u.setRol(rol);
        return repository.save(u);
    }

    private String normalizar(String correo) {
        return correo.trim().toLowerCase(Locale.ROOT);
    }
}
