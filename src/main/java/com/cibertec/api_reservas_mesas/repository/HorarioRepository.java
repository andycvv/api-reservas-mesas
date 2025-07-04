package com.cibertec.api_reservas_mesas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cibertec.api_reservas_mesas.model.Horario;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Integer>{

}
