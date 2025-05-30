package com.cibertec.api_reservas_mesas.dto;

import com.cibertec.api_reservas_mesas.model.EstadoReserva;

import jakarta.validation.constraints.NotNull;

public class ReservaEstadoDTO {
	
	@NotNull(message = "El campo estado no puede ser nulo")
	private EstadoReserva estado;

	public EstadoReserva getEstado() {
		return estado;
	}
	
}
