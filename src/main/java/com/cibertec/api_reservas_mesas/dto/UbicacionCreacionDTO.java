package com.cibertec.api_reservas_mesas.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UbicacionCreacionDTO {
	@NotNull(message = "El campo nombre es obligatorio")
	@Size(min = 3, message = "El campo nombre debe tener como mínimo 3 caracteres")
	private String nombre;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
