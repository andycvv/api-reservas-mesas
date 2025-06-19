package com.cibertec.api_reservas_mesas.controller;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.api_reservas_mesas.dto.CrearUsuarioDTO;
import com.cibertec.api_reservas_mesas.model.ERol;
import com.cibertec.api_reservas_mesas.model.Rol;
import com.cibertec.api_reservas_mesas.model.Usuario;
import com.cibertec.api_reservas_mesas.repository.RolRepository;
import com.cibertec.api_reservas_mesas.repository.UsuarioRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private RolRepository rolRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/registrar")
	public ResponseEntity<?> registrarCliente(@Valid @RequestBody CrearUsuarioDTO dto) {
		Rol rol = rolRepository.findByNombre(ERol.CLIENTE);
		
		if (rol == null) return ResponseEntity.badRequest().build();
		
		Optional<Usuario> existente = usuarioRepository.findByDni(dto.getDni());
	    if (existente.isPresent()) {
	        return ResponseEntity.badRequest().body("Ya existe un usuario con ese DNI");
	    }
		
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setClave(passwordEncoder.encode(dto.getClave()));
        usuario.setApellidoMaterno(dto.getApellidoMaterno());
        usuario.setApellidoPaterno(dto.getApellidoPaterno());
        usuario.setTelefono(dto.getTelefono());
        usuario.setDni(dto.getDni());
        usuario.setRoles(Set.of(rol));
        usuario.setEstado(true);

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(usuario);
	}
}
