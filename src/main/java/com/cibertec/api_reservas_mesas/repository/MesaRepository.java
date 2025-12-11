package com.cibertec.api_reservas_mesas.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cibertec.api_reservas_mesas.model.Mesa;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Integer>{
    @Query(value = """
            SELECT m FROM Mesa m
            WHERE m.estado = true
              AND m.capacidad >= :capacidad
              AND m.id NOT IN (
                  SELECT r.mesa.id FROM Reserva r
                  WHERE r.fecha = :fecha
                    AND r.horario.id = :horarioId
              )
        """,
        countQuery = "SELECT COUNT(m) FROM Mesa m WHERE m.estado = true AND m.capacidad >= :capacidad AND m.id NOT IN (SELECT r.mesa.id FROM Reserva r WHERE r.fecha = :fecha AND r.horario.id = :horarioId)")
    List<Mesa> findMesasDisponibles(
            @Param("fecha") LocalDate fecha,
            @Param("horarioId") Integer horarioId,
            @Param("capacidad") int capacidad
        );

    boolean existsByUbicacionId(Integer ubicacionId);
    
    List<Mesa> findByEstadoTrue();
}