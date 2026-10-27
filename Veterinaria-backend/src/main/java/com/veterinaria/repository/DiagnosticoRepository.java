package com.veterinaria.repository;

import com.veterinaria.model.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {

    List<Diagnostico> findByMascotaIdOrderByFechaDesc(Long mascotaId);

    boolean existsByVeterinarioId(Long veterinarioId);

    void deleteByMascotaId(Long mascotaId);
}
