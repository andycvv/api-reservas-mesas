package com.cibertec.api_reservas_mesas.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UbicacionEdicionDTO {
	@NotNull(message = "El campo nombre es obligatorio")
	@Size(min = 3, message = "El campo nombre debe tener como mínimo 3 caracteres")
	private String nombre;
	@NotNull(message = "El campo estado es obligatorio")
	private Boolean estado;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
}
