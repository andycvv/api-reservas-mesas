package com.cibertec.api_reservas_mesas.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.api_reservas_mesas.dto.UbicacionCreacionDTO;
import com.cibertec.api_reservas_mesas.dto.UbicacionDTO;
import com.cibertec.api_reservas_mesas.dto.UbicacionEdicionDTO;
import com.cibertec.api_reservas_mesas.model.Ubicacion;
import com.cibertec.api_reservas_mesas.repository.UbicacionRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/ubicaciones")
public class UbicacionController {
	@Autowired
	private UbicacionRepository repo;
	
	@GetMapping
	public ResponseEntity<List<UbicacionDTO>> get() {
		List<UbicacionDTO> ubicaciones = new ArrayList<>();
		
		for (Ubicacion u : repo.findAll()) {
			UbicacionDTO dto = new UbicacionDTO();
			dto.setId(u.getId());
			dto.setNombre(u.getNombre());
			dto.setEstado(u.getEstado());
			
			ubicaciones.add(dto);
		}
		
		return ResponseEntity.ok(ubicaciones);
	}
	
	@GetMapping("/activos")
	public ResponseEntity<List<UbicacionDTO>> getActivos() {
		List<UbicacionDTO> ubicaciones = new ArrayList<>();
		
		for (Ubicacion u : repo.findAll()) {
			if(u.getEstado()) {
				UbicacionDTO dto = new UbicacionDTO();
				dto.setId(u.getId());
				dto.setNombre(u.getNombre());
				dto.setEstado(u.getEstado());
				
				ubicaciones.add(dto);
			}
		}
		
		return ResponseEntity.ok(ubicaciones);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UbicacionDTO> getById(@PathVariable int id) {
		Ubicacion u = repo.findById(id).orElse(null);
		
		if (u == null) {
			return ResponseEntity.notFound().build();
		}
		
		UbicacionDTO dto = new UbicacionDTO();
		dto.setId(u.getId());
		dto.setNombre(u.getNombre());
		dto.setEstado(u.getEstado());
		
		return ResponseEntity.ok(dto);
	}
	
	@PostMapping
	public ResponseEntity<Void> post(@RequestBody @Valid UbicacionCreacionDTO ubicacionCreacionDTO) {
		Ubicacion u = new Ubicacion();
		u.setNombre(ubicacionCreacionDTO.getNombre());
		u.setEstado(true);
		
		repo.save(u);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> put(@PathVariable int id, @RequestBody @Valid UbicacionEdicionDTO ubicacionEdicionDTO) {
		Ubicacion u = repo.findById(id).orElse(null);
		
		if (u == null) {
			return ResponseEntity.notFound().build();
		}
		
		u.setNombre(ubicacionEdicionDTO.getNombre());
		u.setEstado(ubicacionEdicionDTO.isEstado());
		
		repo.save(u);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable int id) {
		Ubicacion u = repo.findById(id).orElse(null);
		
		if (u == null) {
			return ResponseEntity.notFound().build();
		}
		
		repo.deleteById(id);
		return ResponseEntity.ok().build();
	}
}
