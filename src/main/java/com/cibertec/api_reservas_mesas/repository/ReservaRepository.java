package com.cibertec.api_reservas_mesas.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cibertec.api_reservas_mesas.dto.ReservaConsultaDTO;
import com.cibertec.api_reservas_mesas.dto.ReservaListadoDTO;
import com.cibertec.api_reservas_mesas.model.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer>{
	boolean existsByMesaIdAndFechaAndHorarioId(Integer mesaId, LocalDate fecha, Integer horarioId);
	
	@Query("SELECT new com.cibertec.api_reservas_mesas.dto.ReservaListadoDTO(" +
		       "r.id, c.nombre, c.dni, c.telefono, r.fecha, h.horaInicio, h.horaFin, m.numero, m.capacidad) " +
		       "FROM Reserva r " +
		       "JOIN r.cliente c " +
		       "JOIN r.horario h " +
		       "JOIN r.mesa m " +
		       "WHERE r.estado = com.cibertec.api_reservas_mesas.model.EstadoReserva.PENDIENTE " +
		       "AND r.fecha = CURRENT_DATE")
	List<ReservaListadoDTO> listarReservasPendientesDeHoy();
	
	@Query("SELECT new com.cibertec.api_reservas_mesas.dto.ReservaConsultaDTO(r.id, r.fecha, h.horaInicio, h.horaFin, m.numero, m.capacidad, r.estado) " +
	           "FROM Reserva r " +
	           "JOIN r.horario h " +
	           "JOIN r.mesa m " +
	           "WHERE r.cliente.id = :id")
	List<ReservaConsultaDTO> listarReservasPorClienteId(@Param("id") Integer id);
}
