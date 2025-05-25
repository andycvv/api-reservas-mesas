package com.cibertec.api_reservas_mesas.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class MesaEdicionDTO {
	@NotNull(message = "El campo número es obligatorio")
	@Min(value = 1, message = "El campo número debe ser mayor o igual a 1")
	private Integer numero;
	@NotNull(message = "El campo capacidad es obligatorio")
	@Min(value = 1, message = "El campo capacidad debe ser mayor o igual a 1")
	private Integer capacidad;
	@NotNull(message = "El campo ubicacionId es obligatorio")
	@Min(value = 1, message = "El campo ubicacionId debe ser mayor o igual a 1")
	private Integer ubicacionId;
	@NotNull(message = "El campo estado es obligatorio")
	private Boolean estado;
	
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public int getCapacidad() {
		return capacidad;
	}
	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}
	public int getUbicacionId() {
		return ubicacionId;
	}
	public void setUbicacionId(int ubicacionId) {
		this.ubicacionId = ubicacionId;
	}
	public boolean getEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
}
