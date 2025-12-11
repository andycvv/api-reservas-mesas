package com.cibertec.api_reservas_mesas.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cibertec.api_reservas_mesas.dto.ReservaConsultaDTO;
import com.cibertec.api_reservas_mesas.dto.ReservaListadoDTO;
import com.cibertec.api_reservas_mesas.model.Reserva;
import com.cibertec.api_reservas_mesas.model.EstadoReserva;


@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer>{
	boolean existsByMesaIdAndFechaAndHorarioId(Integer mesaId, LocalDate fecha, Integer horarioId);
	Integer countByClienteIdAndEstado(Integer clienteId, EstadoReserva estado);
	boolean existsByMesaId(Integer mesaId);
	boolean existsByHorarioId(Integer horarioId);
	
	@Query(value = "SELECT new com.cibertec.api_reservas_mesas.dto.ReservaListadoDTO(" +
		       "r.id, c.nombre, c.dni, c.telefono, r.fecha, h.horaInicio, h.horaFin, m.numero, m.capacidad) " +
		       "FROM Reserva r " +
		       "JOIN r.cliente c " +
		       "JOIN r.horario h " +
		       "JOIN r.mesa m " +
		       "WHERE (:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
		       "AND (:numero IS NULL OR m.numero = :numero) " +
		       "AND r.fecha >= CURRENT_DATE",
		countQuery = "SELECT COUNT(r) FROM Reserva r JOIN r.cliente c JOIN r.mesa m WHERE (:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND (:numero IS NULL OR m.numero = :numero) AND r.fecha >= CURRENT_DATE")
	Page<ReservaListadoDTO> listarReservasPendientesFiltros(@Param("nombre") String nombre, @Param("numero") Integer numero, Pageable pageable);
	
	@Query(value = "SELECT new com.cibertec.api_reservas_mesas.dto.ReservaConsultaDTO(r.id, r.fecha, h.horaInicio, h.horaFin, m.numero, m.capacidad, r.estado) " +
		           "FROM Reserva r " +
		           "JOIN r.horario h " +
		           "JOIN r.mesa m " +
		           "WHERE r.cliente.id = :id " +
		           "AND (:fecha IS NULL OR r.fecha = :fecha) " +
		           "AND (:estado IS NULL OR r.estado = :estado)",
		   countQuery = "SELECT COUNT(r) FROM Reserva r WHERE r.cliente.id = :id AND (:fecha IS NULL OR r.fecha = :fecha) AND (:estado IS NULL OR r.estado = :estado)")
	Page<ReservaConsultaDTO> listarReservasPorClienteIdFiltros(@Param("id") Integer id, @Param("fecha") LocalDate fecha, @Param("estado") EstadoReserva estado, Pageable pageable);
	
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.fecha BETWEEN :inicio AND :fin")
    int contarTotalReservas(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.estado = 'CANCELADA' AND r.fecha BETWEEN :inicio AND :fin")
    int contarCancelaciones(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    @Query("SELECT FUNCTION('DAYOFWEEK', r.fecha), COUNT(r) " +
           "FROM Reserva r WHERE r.fecha BETWEEN :inicio AND :fin " +
           "GROUP BY FUNCTION('DAYOFWEEK', r.fecha)")
    List<Object[]> contarReservasPorDia(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);
}