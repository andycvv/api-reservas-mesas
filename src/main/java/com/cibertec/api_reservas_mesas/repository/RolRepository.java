package com.cibertec.api_reservas_mesas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cibertec.api_reservas_mesas.model.ERol;
import com.cibertec.api_reservas_mesas.model.Rol;


public interface RolRepository extends JpaRepository<Rol, Integer> {
	Rol findByNombre(ERol nombre);
}
