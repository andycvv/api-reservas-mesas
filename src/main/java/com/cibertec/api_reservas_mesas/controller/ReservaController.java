package com.cibertec.api_reservas_mesas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.api_reservas_mesas.dto.ReservaCreacionDTO;
import com.cibertec.api_reservas_mesas.model.EstadoReserva;
import com.cibertec.api_reservas_mesas.model.Horario;
import com.cibertec.api_reservas_mesas.model.Mesa;
import com.cibertec.api_reservas_mesas.model.Reserva;
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
	
	@PostMapping
	public ResponseEntity<Void> post(@RequestBody @Valid ReservaCreacionDTO reservaCreacionDTO) {
		Usuario cliente = usuarioRepository.findById(reservaCreacionDTO.getClienteId()).orElse(null);
		
		if (cliente == null) {
			return ResponseEntity.notFound().build();
		}
		
		Mesa mesa = mesaRepository.findById(reservaCreacionDTO.getMesaId()).orElse(null);
		
		if (mesa == null) {
			return ResponseEntity.notFound().build();
		}
		
		Horario horario = horarioRepository.findById(reservaCreacionDTO.getHorarioId()).orElse(null);
		
		if (horario == null) {
			return ResponseEntity.notFound().build();
		}
		
		Reserva reserva = new Reserva();
		reserva.setFecha(reservaCreacionDTO.getFecha());
		reserva.setEstado(EstadoReserva.PENDIENTE);
		reserva.setCliente(cliente);
		reserva.setHorario(horario);
		reserva.setMesa(mesa);
		
		reservaRepository.save(reserva);
		return ResponseEntity.ok().build();
	}
}
