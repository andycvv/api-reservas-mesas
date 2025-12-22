package com.cibertec.api_reservas_mesas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
import com.cibertec.api_reservas_mesas.service.UbicacionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/ubicaciones")
public class UbicacionController {
    @Autowired
    private UbicacionService service;
    @Autowired
    private PageableMapper pageableMapper;
    
    @GetMapping
    public ResponseEntity<PageableResponse<UbicacionDTO>> get(
            @PageableDefault(size = 10, sort = "nombre", direction = Direction.ASC) Pageable pageable
    ) {
        Page<UbicacionDTO> dtoPage = service.getAll(pageable);
        return ResponseEntity.ok(pageableMapper.toPaginacionResponse(dtoPage));
    }
    
    @GetMapping("/activos")
    public ResponseEntity<List<UbicacionDTO>> getActivos() {
        List<UbicacionDTO> ubicacionesDTO = service.getActivos();
        return ResponseEntity.ok(ubicacionesDTO);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UbicacionDTO> getById(@PathVariable int id) {
        UbicacionDTO dto = service.getById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
    
    @PostMapping
    public ResponseEntity<Void> post(@RequestBody @Valid UbicacionCreacionDTO ubicacionCreacionDTO) {
        service.create(ubicacionCreacionDTO);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Void> put(@PathVariable int id, @RequestBody @Valid UbicacionEdicionDTO ubicacionEdicionDTO) {
        boolean updated = service.update(id, ubicacionEdicionDTO);
        if (!updated) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean deleted = service.delete(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();
    }
}