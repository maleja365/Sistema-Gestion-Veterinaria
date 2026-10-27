package com.veterinaria.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    /** Perro, Gato, Conejo u Otro */
    @Column(nullable = false, length = 50)
    private String especie;

    @Column(length = 80)
    private String raza;

    private LocalDate fechaNacimiento;

    @Column(length = 20)
    private String sexo;

    /** Dueño de la mascota (usuario con rol PROPIETARIO). */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "propietario_id", nullable = false)
    private Usuario propietario;

    public Mascota() {}

    public Long getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }

    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public Usuario getPropietario() { return propietario; }
    public void setPropietario(Usuario propietario) { this.propietario = propietario; }
}
