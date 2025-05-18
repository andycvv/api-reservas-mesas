package com.cibertec.api_reservas_mesas.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Ubicacion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nombre;
	
	@OneToMany(mappedBy = "ubicacion")
	@JsonIgnore
	private List<Mesa> mesas;

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

	public List<Mesa> getMesas() {
		return mesas;
	}

	public void setMesas(List<Mesa> mesas) {
		this.mesas = mesas;
	}
	
	
}
