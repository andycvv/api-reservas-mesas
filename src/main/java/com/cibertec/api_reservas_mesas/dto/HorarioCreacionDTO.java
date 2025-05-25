package com.cibertec.api_reservas_mesas.dto;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public class HorarioCreacionDTO {
	@NotNull(message = "El campo horaInicio es obligatorio")
	private LocalTime horaInicio;
	@NotNull(message = "El campo horaFin es obligatorio")
	private LocalTime horaFin;
	
	public LocalTime getHoraInicio() {
		return horaInicio;
	}
	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}
	public LocalTime getHoraFin() {
		return horaFin;
	}
	public void setHoraFin(LocalTime horaFin) {
		this.horaFin = horaFin;
	}
}
