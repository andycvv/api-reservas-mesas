package com.cibertec.api_reservas_mesas.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.api_reservas_mesas.dto.PageableResponse;
import com.cibertec.api_reservas_mesas.dto.ReservaConsultaDTO;
import com.cibertec.api_reservas_mesas.dto.ReservaCreacionDTO;
import com.cibertec.api_reservas_mesas.dto.ReservaEstadoDTO;
import com.cibertec.api_reservas_mesas.dto.ReservaListadoDTO;
import com.cibertec.api_reservas_mesas.mapper.PageableMapper;
import com.cibertec.api_reservas_mesas.model.EstadoReserva;
import com.cibertec.api_reservas_mesas.model.Horario;
import com.cibertec.api_reservas_mesas.model.Mesa;
import com.cibertec.api_reservas_mesas.model.Reserva;
import com.cibertec.api_reservas_mesas.model.ERol;
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
	@Autowired
	private PageableMapper pageableMapper;
	
	@GetMapping("/pendientes")
	@PreAuthorize("hasRole('RECEPCIONISTA')")
	public ResponseEntity<PageableResponse<ReservaListadoDTO>> getPendientes(
			@PageableDefault(size = 10, sort = {"fecha","horario.horaInicio"}, direction = Direction.DESC) Pageable pageable,
			@RequestParam(required = false) String nombre,
			@RequestParam(required = false) Integer numero
	){
		Page<ReservaListadoDTO> pagina = reservaRepository.listarReservasPendientesFiltros(nombre, numero, pageable);
		PageableResponse<ReservaListadoDTO> response = pageableMapper.toPaginacionResponse(pagina);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/mis-reservas")
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<PageableResponse<ReservaConsultaDTO>> getPorCliente(
			Authentication authentication,
			@PageableDefault(size = 10, sort = {"fecha","horario.horaInicio"}, direction = Direction.DESC) Pageable pageable,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
			@RequestParam(required = false) EstadoReserva estado
	){
		String dni = authentication.getName();
		
		Usuario u = usuarioRepository.findByDni(dni).orElseThrow(() -> new 
				UsernameNotFoundException("Usuario no encontrado con DNI: " + dni));
		
		Page<ReservaConsultaDTO> pagina = reservaRepository.listarReservasPorClienteIdFiltros(u.getId(), fecha, estado, pageable);
		return ResponseEntity.ok(pageableMapper.toPaginacionResponse(pagina));
	}
	
	@PostMapping
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<?> post(@RequestBody @Valid ReservaCreacionDTO dto, 
			Authentication authentication) {
		
	    String dni = authentication.getName();

	    Usuario u = usuarioRepository.findByDni(dni)
	        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con DNI: " + dni));
		
	    long cantidadPendientes = reservaRepository.countByClienteIdAndEstado(u.getId(), EstadoReserva.PENDIENTE);

	    if (cantidadPendientes >= 3) {
	        return ResponseEntity.badRequest()
	            .body("No puede registrar más reservas. Ya tiene 3 reservas pendientes.");
	    }
	    
        if (dto.getFecha().isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest()
            		.body("No se puede reservar en una fecha pasada.");
        }
        
//        if (!dto.getFecha().isAfter(LocalDate.now())) {
//            return ResponseEntity.badRequest()
//            		.body("Solo se permiten reservas con al menos 1 día de anticipación.");
//        }
		
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
		reserva.setCliente(u);
		reserva.setHorario(horario);
		reserva.setMesa(mesa);
		
		reservaRepository.save(reserva);
		return ResponseEntity.ok().build();
	}
	
	@PatchMapping("/estado/{id}")
	@PreAuthorize("hasRole('RECEPCIONISTA')")
	public ResponseEntity<?> patch(@PathVariable int id, Authentication authentication,
			@RequestBody @Valid ReservaEstadoDTO reservaEstadoDTO){
	    String dni = authentication.getName();

	    Usuario u = usuarioRepository.findByDni(dni)
	        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con DNI: " + dni));
		
		Reserva reserva = reservaRepository.findById(id).orElse(null);
		
		if (reserva == null) {
			return ResponseEntity.notFound().build();
		}
		
		reserva.setEstado(reservaEstadoDTO.getEstado());
		reserva.setAsistente(u);
		reservaRepository.save(reserva);
		
		return ResponseEntity.ok().build();
	}
	
	@PatchMapping("/cancelar/{id}")
	@PreAuthorize("hasRole('CLIENTE')")
	public ResponseEntity<?> cancelar(@PathVariable int id){
		Reserva reserva = reservaRepository.findById(id).orElse(null);
		
		if (reserva == null) {
			return ResponseEntity.notFound().build();
		}
		
		reserva.setEstado(EstadoReserva.CANCELADA);
		reservaRepository.save(reserva);
		
		return ResponseEntity.ok().build();
	}
	
}