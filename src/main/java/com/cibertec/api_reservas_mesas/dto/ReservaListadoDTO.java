package com.cibertec.api_reservas_mesas.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservaListadoDTO {

	private Integer id;
	private String nombreCliente;
	private String dniCliente;
	private String telefonoCliente;
	private LocalDate fecha;
	private LocalTime horaInicio;
	private LocalTime horaFin;
	private int numeroMesa;
	private int capacidadMesa;
	
	public ReservaListadoDTO() {
		super();
	}

	public ReservaListadoDTO(Integer id, String nombreCliente, String dniCliente, String telefonoCliente,
			LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, int numeroMesa, int capacidadMesa) {
		super();
		this.id = id;
		this.nombreCliente = nombreCliente;
		this.dniCliente = dniCliente;
		this.telefonoCliente = telefonoCliente;
		this.fecha = fecha;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.numeroMesa = numeroMesa;
		this.capacidadMesa = capacidadMesa;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getDniCliente() {
		return dniCliente;
	}

	public void setDniCliente(String dniCliente) {
		this.dniCliente = dniCliente;
	}

	public String getTelefonoCliente() {
		return telefonoCliente;
	}

	public void setTelefonoCliente(String telefonoCliente) {
		this.telefonoCliente = telefonoCliente;
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
	
}
