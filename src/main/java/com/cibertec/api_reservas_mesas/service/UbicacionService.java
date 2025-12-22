package com.cibertec.api_reservas_mesas.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cibertec.api_reservas_mesas.dto.UbicacionCreacionDTO;
import com.cibertec.api_reservas_mesas.dto.UbicacionDTO;
import com.cibertec.api_reservas_mesas.dto.UbicacionEdicionDTO;
import com.cibertec.api_reservas_mesas.exception.RegistroEnUsoException;
import com.cibertec.api_reservas_mesas.model.Ubicacion;
import com.cibertec.api_reservas_mesas.repository.MesaRepository;
import com.cibertec.api_reservas_mesas.repository.UbicacionRepository;

@Service
public class UbicacionService {
    @Autowired
    private UbicacionRepository repo;

    @Autowired
    private MesaRepository mesaRepo;

    public Page<UbicacionDTO> getAll(Pageable pageable) {
        Page<Ubicacion> page = repo.findAll(pageable);
        return page.map(this::toDto);
    }

    public List<UbicacionDTO> getActivos() {
        List<Ubicacion> ubicaciones = repo.findByEstadoTrue();
        return ubicaciones.stream().map(this::toDto).collect(Collectors.toList());
    }

    public UbicacionDTO getById(int id) {
        Ubicacion u = repo.findById(id).orElse(null);
        return u == null ? null : toDto(u);
    }

    public void create(UbicacionCreacionDTO dto) {
        Ubicacion u = new Ubicacion();
        u.setNombre(dto.getNombre());
        u.setEstado(true);
        repo.save(u);
    }

    public boolean update(int id, UbicacionEdicionDTO dto) {
        Ubicacion u = repo.findById(id).orElse(null);
        if (u == null) return false;
        u.setNombre(dto.getNombre());
        u.setEstado(dto.isEstado());
        repo.save(u);
        return true;
    }

    public boolean delete(int id) {
        Ubicacion u = repo.findById(id).orElse(null);
        if (u == null) return false;
        if (mesaRepo.existsByUbicacionId(id)) {
            throw new RegistroEnUsoException("Ubicacion", id, "No se puede eliminar: hay mesas asociadas a esta ubicación.");
        }
        repo.deleteById(id);
        return true;
    }

    private UbicacionDTO toDto(Ubicacion u) {
        UbicacionDTO dto = new UbicacionDTO();
        dto.setId(u.getId());
        dto.setNombre(u.getNombre());
        dto.setEstado(u.getEstado());
        return dto;
    }
}
