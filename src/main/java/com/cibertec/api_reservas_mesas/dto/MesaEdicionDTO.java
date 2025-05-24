package com.cibertec.api_reservas_mesas.dto;

public class MesaEdicionDTO {

	private int numero;
	private int capacidad;
	private int ubicacionId;
	private boolean estado;
	
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
