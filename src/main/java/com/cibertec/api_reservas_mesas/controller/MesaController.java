package com.cibertec.api_reservas_mesas.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.api_reservas_mesas.dto.MesaCreacionDTO;
import com.cibertec.api_reservas_mesas.dto.MesaDTO;
import com.cibertec.api_reservas_mesas.dto.MesaEdicionDTO;
import com.cibertec.api_reservas_mesas.dto.MesaListadoDTO;
import com.cibertec.api_reservas_mesas.dto.PageableResponse;
import com.cibertec.api_reservas_mesas.mapper.PageableMapper;
import com.cibertec.api_reservas_mesas.model.Mesa;
import com.cibertec.api_reservas_mesas.model.Ubicacion;
import com.cibertec.api_reservas_mesas.repository.MesaRepository;
import com.cibertec.api_reservas_mesas.repository.UbicacionRepository;
import com.cibertec.api_reservas_mesas.repository.ReservaRepository;
import com.cibertec.api_reservas_mesas.exception.RegistroEnUsoException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/mesas")
public class MesaController {

	@Autowired
	private MesaRepository mesaRepository;
	@Autowired
	private UbicacionRepository ubicacionRepository;
	@Autowired
	private ReservaRepository reservaRepository;
	@Autowired
	private PageableMapper pageableMapper;
	
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RECEPCIONISTA')")
	public ResponseEntity<PageableResponse<MesaListadoDTO>> get(
			@PageableDefault(size = 10, sort = "numero", direction = Direction.ASC) Pageable pageable
	) {
		Page<Mesa> page = mesaRepository.findAll(pageable);
		Page<MesaListadoDTO> dtoPage = page.map(m -> {
			MesaListadoDTO dto = new MesaListadoDTO();
			dto.setId(m.getId());
			dto.setNumero(m.getNumero());
			dto.setCapacidad(m.getCapacidad());
			dto.setEstado(m.getEstado());
			dto.setUbicacion(m.getUbicacion().getNombre());
			return dto;
		});
		return ResponseEntity.ok(pageableMapper.toPaginacionResponse(dtoPage));
	}
	
	@GetMapping("/activos")
	@PreAuthorize("hasAnyRole('ADMINISTRADOR')")
	public ResponseEntity<List<MesaListadoDTO>> getActivos() {
		List<Mesa> mesas = mesaRepository.findByEstadoTrue();
		
		List<MesaListadoDTO> mesasDTO = mesas.stream().map(m -> {
			MesaListadoDTO dto = new MesaListadoDTO();
			dto.setId(m.getId());
			dto.setNumero(m.getNumero());
			dto.setCapacidad(m.getCapacidad());
			dto.setEstado(m.getEstado());
			dto.setUbicacion(m.getUbicacion().getNombre());
			return dto;
		}).toList();
		
		return ResponseEntity.ok(mesasDTO);
	}
	
	@GetMapping("/disponibles")
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<List<MesaListadoDTO>> getDisponibles(
	           @RequestParam("fecha") LocalDate fecha,
	           @RequestParam("horarioId") Integer horarioId,
	           @RequestParam("capacidad") int capacidad
	) {
		List<Mesa> mesas = mesaRepository.findMesasDisponibles(fecha, horarioId, capacidad);
		List<MesaListadoDTO> mesasDTO = mesas.stream().map(m -> {
			MesaListadoDTO dto = new MesaListadoDTO();
			dto.setId(m.getId());
			dto.setNumero(m.getNumero());
			dto.setCapacidad(m.getCapacidad());
			dto.setEstado(m.getEstado());
			dto.setUbicacion(m.getUbicacion().getNombre());
			return dto;
		}).toList();
		return ResponseEntity.ok(mesasDTO);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<MesaDTO> getId(@PathVariable int id) {
		Mesa m = mesaRepository.findById(id).orElse(null);
		
		if(m == null) {
			return ResponseEntity.notFound().build();
		}
		
		MesaDTO mesaDTO = new MesaDTO();
		mesaDTO.setId(m.getId());
		mesaDTO.setNumero(m.getNumero());
		mesaDTO.setCapacidad(m.getCapacidad());
		mesaDTO.setUbicacionId(m.getUbicacion().getId());
		mesaDTO.setEstado(m.getEstado());
		
		return ResponseEntity.ok(mesaDTO);
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<Void> post(@RequestBody @Valid MesaCreacionDTO mesaCreacionDTO) {
		Ubicacion u = ubicacionRepository
				.findById(mesaCreacionDTO.getUbicacionId())
				.orElse(null);
		
		if (u == null) {
			return ResponseEntity.notFound().build();
		}
		
		Mesa m = new Mesa();
		
		m.setNumero(mesaCreacionDTO.getNumero());
		m.setCapacidad(mesaCreacionDTO.getCapacidad());
		m.setEstado(true);
		m.setUbicacion(u);
		
		mesaRepository.save(m);
		
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<Void> put(@PathVariable int id, @RequestBody @Valid MesaEdicionDTO mesaEdicionDTO){
		Mesa m = mesaRepository.findById(id).orElse(null);
		
		if(m == null) {
			return ResponseEntity.notFound().build();
		}
		
		Ubicacion u = ubicacionRepository.findById(mesaEdicionDTO.getUbicacionId()).orElse(null);
		
		if(u == null) {
			return ResponseEntity.notFound().build();
		}
		
		m.setNumero(mesaEdicionDTO.getNumero());
		m.setCapacidad(mesaEdicionDTO.getCapacidad());
		m.setUbicacion(u);
		m.setEstado(mesaEdicionDTO.getEstado());
		
		mesaRepository.save(m);
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<Void> delete(@PathVariable int id) {
		Mesa m = mesaRepository.findById(id).orElse(null);
		
		if (m == null) {
			return ResponseEntity.notFound().build();
		}

		if (reservaRepository.existsByMesaId(id)) {
			throw new RegistroEnUsoException("Mesa", id, "No se puede eliminar: hay reservas asociadas a esta mesa.");
		}
		
		mesaRepository.deleteById(id);
		
		return ResponseEntity.ok().build();
	}
}