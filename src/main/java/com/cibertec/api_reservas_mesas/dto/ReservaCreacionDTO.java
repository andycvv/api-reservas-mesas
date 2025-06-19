package com.cibertec.api_reservas_mesas.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ReservaCreacionDTO {
	@NotNull(message = "El campo fecha es obligatorio")
	private LocalDate fecha;
	@NotNull(message = "El campo mesaId es obligatorio")
	@Min(value = 1, message = "El campo mesaId debe ser mayor o igual a 1")
	private Integer mesaId;
	@NotNull(message = "El campo horarioId es obligatorio")
	@Min(value = 1, message = "El campo horarioId debe ser mayor o igual a 1")
	private Integer horarioId;
	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	public Integer getMesaId() {
		return mesaId;
	}
	public void setMesaId(Integer mesaId) {
		this.mesaId = mesaId;
	}
	public Integer getHorarioId() {
		return horarioId;
	}
	public void setHorarioId(Integer horarioId) {
		this.horarioId = horarioId;
	}
}
