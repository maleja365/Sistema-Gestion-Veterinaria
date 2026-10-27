package com.veterinaria.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vacunas")
public class Vacuna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "veterinario_id", nullable = false)
    private Usuario veterinario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private LocalDate fechaAplicacion;

    private LocalDate proximaDosis;

    public Vacuna() {}

    public Long getId() { return id; }

    public Mascota getMascota() { return mascota; }
    public void setMascota(Mascota mascota) { this.mascota = mascota; }

    public Usuario getVeterinario() { return veterinario; }
    public void setVeterinario(Usuario veterinario) { this.veterinario = veterinario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDate getFechaAplicacion() { return fechaAplicacion; }
    public void setFechaAplicacion(LocalDate fechaAplicacion) { this.fechaAplicacion = fechaAplicacion; }

    public LocalDate getProximaDosis() { return proximaDosis; }
    public void setProximaDosis(LocalDate proximaDosis) { this.proximaDosis = proximaDosis; }
}
