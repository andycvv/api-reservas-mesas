package com.cibertec.api_reservas_mesas.dto;

public class MesaDTO {
	private int id;
	private int numero;
	private int capacidad;
	private boolean estado;
	private int ubicacionId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public int getUbicacionId() {
		return ubicacionId;
	}
	public void setUbicacionId(int ubicacionId) {
		this.ubicacionId = ubicacionId;
	}
	
	
}
