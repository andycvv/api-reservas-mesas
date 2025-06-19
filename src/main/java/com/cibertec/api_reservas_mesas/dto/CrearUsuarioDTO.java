package com.cibertec.api_reservas_mesas.dto;

import java.util.Set;

import com.cibertec.api_reservas_mesas.model.Rol;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CrearUsuarioDTO {
	@NotNull(message = "El campo nombre es obligatorio")
	@Size(min = 3, message = "El campo nombre debe tener como mínimo 3 caracteres")
	private String nombre;
	@NotNull(message = "El campo clave es obligatorio")
	@Size(min = 3, message = "El campo clave debe tener como mínimo 3 caracteres")
	private String clave;
	@NotNull(message = "El apellidoPaterno clave es obligatorio")
	@Size(min = 3, message = "El campo apellidoPaterno debe tener como mínimo 3 caracteres")
	private String apellidoPaterno;
	@NotNull(message = "El campo apellidoMaterno es obligatorio")
	@Size(min = 3, message = "El campo apellidoMaterno debe tener como mínimo 3 caracteres")
	private String apellidoMaterno;
	@NotNull(message = "El campo telefono es obligatorio")
	@Size(min = 3, message = "El campo telefono debe tener como mínimo 3 caracteres")
	private String telefono;
	@NotNull(message = "El campo dni es obligatorio")
	@Size(min = 8, max = 8, message = "El campo dni debe tener 8 dígitos")
	private String dni;
	private Set<Integer> idRoles;
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
	public Set<Integer> getRoles() {
		return idRoles;
	}
	public void setRoles(Set<Integer> roles) {
		this.idRoles = roles;
	}
}
