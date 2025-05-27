package com.cibertec.api_reservas_mesas.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Asistencia {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@CreationTimestamp
	private LocalDateTime fechaAsistencia;
	@Enumerated(EnumType.STRING)
	private EstadoAsistencia estado;
	
	@ManyToOne
	@JoinColumn(name = "asistente_id")
	private Usuario asistente;
	
	@OneToOne
	@JoinColumn(name = "reserva_id")
	private Reserva reserva;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getFechaAsistencia() {
		return fechaAsistencia;
	}

	public void setFechaAsistencia(LocalDateTime fechaAsistencia) {
		this.fechaAsistencia = fechaAsistencia;
	}

	public EstadoAsistencia getEstado() {
		return estado;
	}

	public void setEstado(EstadoAsistencia estado) {
		this.estado = estado;
	}

	public Usuario getAsistente() {
		return asistente;
	}

	public void setAsistente(Usuario asistente) {
		this.asistente = asistente;
	}

	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}
}
