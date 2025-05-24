package com.cibertec.api_reservas_mesas.dto;

import java.time.LocalTime;

public class HorarioDTO {
	private Integer id;
	private LocalTime horaInicio;
	private LocalTime horaFin;
	private boolean estado;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	public boolean getEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
}
