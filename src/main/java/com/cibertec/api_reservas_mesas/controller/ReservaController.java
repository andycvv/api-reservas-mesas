package com.cibertec.api_reservas_mesas.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.api_reservas_mesas.dto.ReservaConsultaDTO;
import com.cibertec.api_reservas_mesas.dto.ReservaCreacionDTO;
import com.cibertec.api_reservas_mesas.dto.ReservaEstadoDTO;
import com.cibertec.api_reservas_mesas.dto.ReservaListadoDTO;
import com.cibertec.api_reservas_mesas.model.EstadoReserva;
import com.cibertec.api_reservas_mesas.model.Horario;
import com.cibertec.api_reservas_mesas.model.Mesa;
import com.cibertec.api_reservas_mesas.model.Reserva;
import com.cibertec.api_reservas_mesas.model.Rol;
import com.cibertec.api_reservas_mesas.model.Usuario;
import com.cibertec.api_reservas_mesas.repository.HorarioRepository;
import com.cibertec.api_reservas_mesas.repository.MesaRepository;
import com.cibertec.api_reservas_mesas.repository.ReservaRepository;
import com.cibertec.api_reservas_mesas.repository.UsuarioRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/reservas")
public class ReservaController {
	@Autowired
	private ReservaRepository reservaRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private MesaRepository mesaRepository;
	@Autowired
	private HorarioRepository horarioRepository;
	
	@GetMapping("/pendientes")
	public ResponseEntity<List<ReservaListadoDTO>> getPendientes(){
		return ResponseEntity.ok(reservaRepository.listarReservasPendientes());
	}
	
	@GetMapping("/cliente/{id}")
	public ResponseEntity<List<ReservaConsultaDTO>> getPorCliente(@PathVariable Integer id){
		return ResponseEntity.ok(reservaRepository.listarReservasPorClienteId(id));
	}
	
	@PostMapping
	public ResponseEntity<?> post(@RequestBody @Valid ReservaCreacionDTO dto) {
		Usuario cliente = usuarioRepository.findById(dto.getClienteId()).orElse(null);
		if (cliente == null) {
			return ResponseEntity.notFound().build();
		}
		if (!cliente.getRol().equals(Rol.CLIENTE)) {
		    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		        .body("El usuario no tiene rol de CLIENTE");
		}
		
        if (dto.getFecha().isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest()
            		.body("No se puede reservar en una fecha pasada.");
        }
        
        if (!dto.getFecha().isAfter(LocalDate.now())) {
            return ResponseEntity.badRequest()
            		.body("Solo se permiten reservas con al menos 1 día de anticipación.");
        }
		
		Mesa mesa = mesaRepository.findById(dto.getMesaId()).orElse(null);
		if (mesa == null) {
			return ResponseEntity.notFound().build();
		}
		if (!mesa.getEstado()) {
			return ResponseEntity.badRequest()
					.body("La mesa seleccionada no está activa.");
		}
		
		Horario horario = horarioRepository.findById(dto.getHorarioId()).orElse(null);
		if (horario == null) {
			return ResponseEntity.notFound().build();
		}
        if (!horario.getEstado()) {
            return ResponseEntity.badRequest()
            		.body("El horario seleccionado no está activo.");
        }
		
	    boolean existeReserva = reservaRepository.existsByMesaIdAndFechaAndHorarioId(
	    		dto.getMesaId(),
	    		dto.getFecha(),
	    		dto.getHorarioId()
	    );
	    if (existeReserva) {
	    	return ResponseEntity.badRequest()
	    		    .body("La mesa ya está reservada para esa fecha y horario.");
	    }
		
		Reserva reserva = new Reserva();
		reserva.setFecha(dto.getFecha());
		reserva.setEstado(EstadoReserva.PENDIENTE);
		reserva.setCliente(cliente);
		reserva.setHorario(horario);
		reserva.setMesa(mesa);
		
		reservaRepository.save(reserva);
		return ResponseEntity.ok().build();
	}
	
	@PatchMapping("/estado/{id}")
	public ResponseEntity<?> patch(@PathVariable int id, @RequestBody @Valid ReservaEstadoDTO reservaEstadoDTO){
		Reserva reserva = reservaRepository.findById(id).orElse(null);
		
		if (reserva == null) {
			return ResponseEntity.notFound().build();
		}
		
		reserva.setEstado(reservaEstadoDTO.getEstado());
		reservaRepository.save(reserva);
		
		return ResponseEntity.ok().build();
	}
	
}
