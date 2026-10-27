package com.veterinaria.repository;

import com.veterinaria.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByMascotaPropietarioId(Long propietarioId);

    List<Cita> findByVeterinarioId(Long veterinarioId);

    boolean existsByVeterinarioId(Long veterinarioId);

    void deleteByMascotaId(Long mascotaId);

    @Query("select c.estado, count(c) from Cita c group by c.estado")
    List<Object[]> contarPorEstado();
}
