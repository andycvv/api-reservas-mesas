package com.cibertec.api_reservas_mesas.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.api_reservas_mesas.dto.HorarioCreacionDTO;
import com.cibertec.api_reservas_mesas.dto.HorarioDTO;
import com.cibertec.api_reservas_mesas.dto.HorarioEdicionDTO;
import com.cibertec.api_reservas_mesas.model.Horario;
import com.cibertec.api_reservas_mesas.repository.HorarioRepository;

@RestController
@RequestMapping("/horarios")
public class HorarioController {
	@Autowired
	private HorarioRepository horarioRepository;
	
	@GetMapping
	public ResponseEntity<List<HorarioDTO>> get() {
		List<HorarioDTO> horarios = new ArrayList<>();
		
		for (Horario h : horarioRepository.findAll()) {
			HorarioDTO dto = new HorarioDTO();
			dto.setId(h.getId());
			dto.setHoraInicio(h.getHoraInicio());
			dto.setHoraFin(h.getHoraFin());
			dto.setEstado(h.getEstado());
			
			horarios.add(dto);
		}
		
		return ResponseEntity.ok(horarios);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<HorarioDTO> getById(@PathVariable int id) {
		Horario horario = horarioRepository.findById(id).orElse(null);
		
		if (horario == null) {
			return ResponseEntity.notFound().build();
		}
		
		HorarioDTO dto = new HorarioDTO();
		dto.setId(horario.getId());
		dto.setHoraInicio(horario.getHoraInicio());
		dto.setHoraFin(horario.getHoraFin());
		dto.setEstado(horario.getEstado());
		
		return ResponseEntity.ok(dto);
	}
	
	@PostMapping
	public ResponseEntity<Void> post(@RequestBody HorarioCreacionDTO horarioCreacionDTO) {
		Horario horario = new Horario();
		horario.setHoraInicio(horarioCreacionDTO.getHoraInicio());
		horario.setHoraFin(horarioCreacionDTO.getHoraFin());
		horario.setEstado(true);
		
		horarioRepository.save(horario);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> put(@PathVariable int id, @RequestBody HorarioEdicionDTO horarioEdicionDTO) {
		Horario horario = horarioRepository.findById(id).orElse(null);
		
		if (horario == null) {
			return ResponseEntity.notFound().build();
		}
		
		horario.setHoraInicio(horarioEdicionDTO.getHoraInicio());
		horario.setHoraFin(horarioEdicionDTO.getHoraFin());
		horario.setEstado(horarioEdicionDTO.getEstado());
		
		horarioRepository.save(horario);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable int id) {
		Horario horario = horarioRepository.findById(id).orElse(null);
		
		if (horario == null) {
			return ResponseEntity.notFound().build();
		}
		
		horarioRepository.deleteById(horario.getId());
		return ResponseEntity.ok().build();
	}
	
}
