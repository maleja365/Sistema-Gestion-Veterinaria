package com.veterinaria.repository;

import com.veterinaria.model.Vacuna;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacunaRepository extends JpaRepository<Vacuna, Long> {

    List<Vacuna> findByMascotaIdOrderByFechaAplicacionDesc(Long mascotaId);

    boolean existsByVeterinarioId(Long veterinarioId);

    void deleteByMascotaId(Long mascotaId);
}
