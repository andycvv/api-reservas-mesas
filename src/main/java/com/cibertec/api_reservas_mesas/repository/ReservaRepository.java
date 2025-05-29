package com.cibertec.api_reservas_mesas.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cibertec.api_reservas_mesas.model.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer>{
	boolean existsByMesaIdAndFechaAndHorarioId(Integer mesaId, LocalDate fecha, Integer horarioId);
}
