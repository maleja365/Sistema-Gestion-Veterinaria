package com.veterinaria.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "veterinario_id", nullable = false)
    private Usuario veterinario;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false, length = 250)
    private String motivo;

    /** Pendiente, Confirmada, Atendida o Cancelada */
    @Column(nullable = false, length = 20)
    private String estado = "Pendiente";

    public Cita() {}

    public Long getId() { return id; }

    public Mascota getMascota() { return mascota; }
    public void setMascota(Mascota mascota) { this.mascota = mascota; }

    public Usuario getVeterinario() { return veterinario; }
    public void setVeterinario(Usuario veterinario) { this.veterinario = veterinario; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
