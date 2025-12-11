package com.cibertec.api_reservas_mesas.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
import com.cibertec.api_reservas_mesas.dto.PageableResponse;
import com.cibertec.api_reservas_mesas.mapper.PageableMapper;
import com.cibertec.api_reservas_mesas.model.Ubicacion;
import com.cibertec.api_reservas_mesas.repository.UbicacionRepository;
import com.cibertec.api_reservas_mesas.repository.MesaRepository;
import com.cibertec.api_reservas_mesas.exception.RegistroEnUsoException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/ubicaciones")
public class UbicacionController {
    @Autowired
    private UbicacionRepository repo;
    @Autowired
    private MesaRepository mesaRepo;
    @Autowired
    private PageableMapper pageableMapper;
    
    @GetMapping
    public ResponseEntity<PageableResponse<UbicacionDTO>> get(
            @PageableDefault(size = 10, sort = "nombre", direction = Direction.ASC) Pageable pageable
    ) {
        Page<Ubicacion> page = repo.findAll(pageable);
        Page<UbicacionDTO> dtoPage = page.map(u -> {
            UbicacionDTO dto = new UbicacionDTO();
            dto.setId(u.getId());
            dto.setNombre(u.getNombre());
            dto.setEstado(u.getEstado());
            return dto;
        });
        return ResponseEntity.ok(pageableMapper.toPaginacionResponse(dtoPage));
    }
    
    @GetMapping("/activos")
    public ResponseEntity<List<UbicacionDTO>> getActivos() {
        List<Ubicacion> ubicaciones = repo.findByEstadoTrue();
        List<UbicacionDTO> ubicacionesDTO = ubicaciones.stream().map(u -> {
            UbicacionDTO dto = new UbicacionDTO();
            dto.setId(u.getId());
            dto.setNombre(u.getNombre());
            dto.setEstado(u.getEstado());
            return dto;
        }).toList();
        return ResponseEntity.ok(ubicacionesDTO);
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
        
        if (mesaRepo.existsByUbicacionId(id)) {
            throw new RegistroEnUsoException("Ubicacion", id, "No se puede eliminar: hay mesas asociadas a esta ubicación.");
        }
        
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}