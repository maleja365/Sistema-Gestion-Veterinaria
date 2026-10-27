package com.veterinaria.dto;

import com.veterinaria.model.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/** Objetos de entrada/salida de la API (así no se expone la entidad ni la contraseña). */
public final class Dtos {

    private Dtos() {}

    // ---------- Autenticación / usuarios ----------
    public record LoginRequest(
            @NotBlank(message = "El correo es obligatorio.") String correo,
            @NotBlank(message = "La contraseña es obligatoria.") String password) {}

    public record RegistroRequest(
            @NotBlank(message = "El nombre es obligatorio.")
            @Size(max = 100, message = "El nombre admite máximo 100 caracteres.") String nombre,
            @NotBlank(message = "El correo es obligatorio.")
            @Email(message = "Ingresa un correo electrónico válido.")
            @Size(max = 120, message = "El correo admite máximo 120 caracteres.") String correo,
            @NotBlank(message = "La contraseña es obligatoria.")
            @Size(min = 8, max = 100, message = "La contraseña debe tener al menos 8 caracteres.") String password) {}

    /** En la edición la contraseña es opcional (si va vacía no se cambia). */
    public record VeterinarioRequest(
            @NotBlank(message = "El nombre es obligatorio.")
            @Size(max = 100, message = "El nombre admite máximo 100 caracteres.") String nombre,
            @NotBlank(message = "El correo es obligatorio.")
            @Email(message = "Ingresa un correo electrónico válido.")
            @Size(max = 120, message = "El correo admite máximo 120 caracteres.") String correo,
            String password) {}

    public record UsuarioResponse(Long id, String nombre, String correo, String rol) {
        public static UsuarioResponse de(Usuario u) {
            return new UsuarioResponse(u.getId(), u.getNombre(), u.getCorreo(), u.getRol());
        }
    }

    // ---------- Mascotas ----------
    public record MascotaRequest(
            @NotBlank(message = "El nombre de la mascota es obligatorio.")
            @Size(max = 100, message = "El nombre admite máximo 100 caracteres.") String nombre,
            @NotBlank(message = "La especie es obligatoria.") String especie,
            @Size(max = 80, message = "La raza admite máximo 80 caracteres.") String raza,
            @PastOrPresent(message = "La fecha de nacimiento no puede ser futura.") LocalDate fechaNacimiento,
            String sexo) {}

    public record MascotaResponse(Long id, String nombre, String especie, String raza,
                                  LocalDate fechaNacimiento, String sexo,
                                  Long propietarioId, String propietarioNombre) {
        public static MascotaResponse de(Mascota m) {
            return new MascotaResponse(m.getId(), m.getNombre(), m.getEspecie(), m.getRaza(),
                    m.getFechaNacimiento(), m.getSexo(),
                    m.getPropietario().getId(), m.getPropietario().getNombre());
        }
    }

    // ---------- Citas ----------
    public record CitaRequest(
            @NotNull(message = "Selecciona una mascota.") Long mascotaId,
            @NotNull(message = "Selecciona un veterinario.") Long veterinarioId,
            @NotNull(message = "La fecha y hora son obligatorias.")
            @Future(message = "La cita debe ser en una fecha futura.") LocalDateTime fechaHora,
            @NotBlank(message = "El motivo de la cita es obligatorio.")
            @Size(max = 250, message = "El motivo admite máximo 250 caracteres.") String motivo) {}

    public record EstadoRequest(
            @NotBlank(message = "El estado es obligatorio.") String estado) {}

    public record CitaResponse(Long id, Long mascotaId, String mascotaNombre, String mascotaEspecie,
                               String propietarioNombre, Long veterinarioId, String veterinarioNombre,
                               LocalDateTime fechaHora, String motivo, String estado) {
        public static CitaResponse de(Cita c) {
            return new CitaResponse(c.getId(), c.getMascota().getId(), c.getMascota().getNombre(),
                    c.getMascota().getEspecie(), c.getMascota().getPropietario().getNombre(),
                    c.getVeterinario().getId(), c.getVeterinario().getNombre(),
                    c.getFechaHora(), c.getMotivo(), c.getEstado());
        }
    }

    // ---------- Historial médico y vacunas ----------
    public record DiagnosticoRequest(
            @NotNull(message = "Selecciona una mascota.") Long mascotaId,
            @NotBlank(message = "El diagnóstico es obligatorio.")
            @Size(max = 500, message = "El diagnóstico admite máximo 500 caracteres.") String descripcion,
            @Size(max = 500, message = "El tratamiento admite máximo 500 caracteres.") String tratamiento) {}

    public record DiagnosticoResponse(Long id, Long mascotaId, String veterinarioNombre,
                                      LocalDateTime fecha, String descripcion, String tratamiento) {
        public static DiagnosticoResponse de(Diagnostico d) {
            return new DiagnosticoResponse(d.getId(), d.getMascota().getId(),
                    d.getVeterinario().getNombre(), d.getFecha(), d.getDescripcion(), d.getTratamiento());
        }
    }

    public record VacunaRequest(
            @NotNull(message = "Selecciona una mascota.") Long mascotaId,
            @NotBlank(message = "El nombre de la vacuna es obligatorio.")
            @Size(max = 100, message = "El nombre admite máximo 100 caracteres.") String nombre,
            @NotNull(message = "La fecha de aplicación es obligatoria.")
            @PastOrPresent(message = "La fecha de aplicación no puede ser futura.") LocalDate fechaAplicacion,
            LocalDate proximaDosis) {}

    public record VacunaResponse(Long id, Long mascotaId, String nombre, String veterinarioNombre,
                                 LocalDate fechaAplicacion, LocalDate proximaDosis) {
        public static VacunaResponse de(Vacuna v) {
            return new VacunaResponse(v.getId(), v.getMascota().getId(), v.getNombre(),
                    v.getVeterinario().getNombre(), v.getFechaAplicacion(), v.getProximaDosis());
        }
    }

    // ---------- Estadísticas (solo administrador) ----------
    public record EstadisticasResponse(long propietarios, long veterinarios, long mascotas, long citas,
                                       Map<String, Long> citasPorEstado,
                                       Map<String, Long> mascotasPorEspecie) {}
}
