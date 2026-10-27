package com.veterinaria.repository;

import com.veterinaria.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    List<Mascota> findByPropietarioId(Long propietarioId);

    @Query("select m.especie, count(m) from Mascota m group by m.especie")
    List<Object[]> contarPorEspecie();
}
