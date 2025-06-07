package com.cibertec.api_reservas_mesas.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.cibertec.api_reservas_mesas.model.EstadoReserva;

public class ReservaConsultaDTO {

	private Integer id;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private int numeroMesa;
    private int capacidadMesa;
    private EstadoReserva estado;
    
	public ReservaConsultaDTO(Integer id, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, int numeroMesa,
			int capacidadMesa, EstadoReserva estado) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.numeroMesa = numeroMesa;
		this.capacidadMesa = capacidadMesa;
		this.estado = estado;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
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
	public int getNumeroMesa() {
		return numeroMesa;
	}
	public void setNumeroMesa(int numeroMesa) {
		this.numeroMesa = numeroMesa;
	}
	public int getCapacidadMesa() {
		return capacidadMesa;
	}
	public void setCapacidadMesa(int capacidadMesa) {
		this.capacidadMesa = capacidadMesa;
	}
	public EstadoReserva getEstado() {
		return estado;
	}
	public void setEstado(EstadoReserva estado) {
		this.estado = estado;
	}
	
}
