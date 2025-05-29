package com.cibertec.api_reservas_mesas.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nombre;
	private String clave;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String telefono;
	private String dni;
	private boolean estado;
	@Enumerated(EnumType.STRING)
	private Rol rol;

	@OneToMany(mappedBy = "cliente")
	@JsonIgnore
	private List<Reserva> reservasCliente;
	
	@OneToMany(mappedBy = "asistente")
	@JsonIgnore
	private List<Reserva> reservasConfirmadas;

	@OneToMany(mappedBy = "asistente")
	@JsonIgnore
	private List<Asistencia> asistencias;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public List<Reserva> getReservasCliente() {
		return reservasCliente;
	}

	public void setReservasCliente(List<Reserva> reservasCliente) {
		this.reservasCliente = reservasCliente;
	}

	public List<Reserva> getReservasConfirmadas() {
		return reservasConfirmadas;
	}

	public void setReservasConfirmadas(List<Reserva> reservasConfirmadas) {
		this.reservasConfirmadas = reservasConfirmadas;
	}

	public List<Asistencia> getAsistencias() {
		return asistencias;
	}

	public void setAsistencias(List<Asistencia> asistencias) {
		this.asistencias = asistencias;
	}
}
