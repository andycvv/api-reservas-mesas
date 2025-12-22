package com.cibertec.api_reservas_mesas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.cibertec.api_reservas_mesas.dto.UbicacionCreacionDTO;
import com.cibertec.api_reservas_mesas.dto.UbicacionDTO;
import com.cibertec.api_reservas_mesas.dto.UbicacionEdicionDTO;
import com.cibertec.api_reservas_mesas.exception.RegistroEnUsoException;
import com.cibertec.api_reservas_mesas.model.Ubicacion;
import com.cibertec.api_reservas_mesas.repository.MesaRepository;
import com.cibertec.api_reservas_mesas.repository.UbicacionRepository;

@ExtendWith(MockitoExtension.class)
public class UbicacionServiceTest {

    @Mock
    private UbicacionRepository repo;

    @Mock
    private MesaRepository mesaRepo;

    @InjectMocks
    private UbicacionService service;

    @Test
    public void testGetByIdReturnsDto() {
        Ubicacion u = new Ubicacion();
        u.setId(1);
        u.setNombre("Sala A");
        u.setEstado(true);

        when(repo.findById(1)).thenReturn(Optional.of(u));

        UbicacionDTO dto = service.getById(1);

        assertEquals("Sala A", dto.getNombre());
    }

    @Test
    public void testDeleteThrowsWhenMesasExist() {
        Ubicacion u = new Ubicacion();
        u.setId(2);
        u.setNombre("Terraza");
        u.setEstado(true);

        when(repo.findById(2)).thenReturn(Optional.of(u));
        when(mesaRepo.existsByUbicacionId(2)).thenReturn(true);

        assertThrows(RegistroEnUsoException.class, () -> service.delete(2));
    }
}