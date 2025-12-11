package com.cibertec.api_reservas_mesas.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.cibertec.api_reservas_mesas.dto.PageableResponse;
import com.cibertec.api_reservas_mesas.mapper.PageableMapper;
import com.cibertec.api_reservas_mesas.exception.RangoHorarioInvalidoException;
import com.cibertec.api_reservas_mesas.model.Horario;
import com.cibertec.api_reservas_mesas.repository.HorarioRepository;
import com.cibertec.api_reservas_mesas.repository.ReservaRepository;
import com.cibertec.api_reservas_mesas.exception.RegistroEnUsoException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/horarios")
public class HorarioController {
	@Autowired
	private HorarioRepository horarioRepository;
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private PageableMapper pageableMapper;
	
	@GetMapping
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<PageableResponse<HorarioDTO>> get(
			@PageableDefault(size = 10, sort = "horaInicio", direction = Direction.ASC) Pageable pageable
	) {
		Page<Horario> page = horarioRepository.findAll(pageable);
		Page<HorarioDTO> dtoPage = page.map(h -> {
			HorarioDTO dto = new HorarioDTO();
			dto.setId(h.getId());
			dto.setHoraInicio(h.getHoraInicio());
			dto.setHoraFin(h.getHoraFin());
			dto.setEstado(h.getEstado());
			return dto;
		});
		return ResponseEntity.ok(pageableMapper.toPaginacionResponse(dtoPage));
	}
	
	@GetMapping("/activos")
	@PreAuthorize("hasAnyRole('CLIENTE', 'ADMINISTRADOR')")
	public ResponseEntity<List<HorarioDTO>> getActivos() {
		List<Horario> horarios = horarioRepository.findByEstadoTrue();
		
		List<HorarioDTO> horariosDTO = horarios.stream().map(h -> {
			HorarioDTO dto = new HorarioDTO();
			dto.setId(h.getId());
			dto.setHoraInicio(h.getHoraInicio());
			dto.setHoraFin(h.getHoraFin());
			dto.setEstado(h.getEstado());
			return dto;
		}).toList();
		
		return ResponseEntity.ok(horariosDTO);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
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
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<Void> post(@RequestBody @Valid HorarioCreacionDTO horarioCreacionDTO) {
		if (!horarioCreacionDTO.getHoraInicio().isBefore(horarioCreacionDTO.getHoraFin())) {
			throw new RangoHorarioInvalidoException("horaInicio", 
					"La hora de inicio debe ser anterior a la hora de fin");
		}
		
		Horario horario = new Horario();
		horario.setHoraInicio(horarioCreacionDTO.getHoraInicio());
		horario.setHoraFin(horarioCreacionDTO.getHoraFin());
		horario.setEstado(true);
		
		horarioRepository.save(horario);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<Void> put(@PathVariable int id, @RequestBody @Valid HorarioEdicionDTO horarioEdicionDTO) {
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
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<Void> delete(@PathVariable int id) {
		Horario horario = horarioRepository.findById(id).orElse(null);
		
		if (horario == null) {
			return ResponseEntity.notFound().build();
		}

		if (reservaRepository.existsByHorarioId(id)) {
			throw new RegistroEnUsoException("Horario", id, "No se puede eliminar: hay reservas asociadas a este horario.");
		}
		
		horarioRepository.deleteById(horario.getId());
		return ResponseEntity.ok().build();
	}
	
}