package com.cibertec.api_reservas_mesas.service;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cibertec.api_reservas_mesas.model.Usuario;
import com.cibertec.api_reservas_mesas.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 Usuario usuario = usuarioRepository.findByDni(username).orElseThrow(() -> 
			new UsernameNotFoundException("No existe el usuario con DNI: " + username));
		 
		 Collection<? extends GrantedAuthority> authorities = usuario.getRoles().stream()
				 .map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getNombre().name())))
				 .collect(Collectors.toSet());
		 
		 return new User(usuario.getDni(), 
				 usuario.getClave(),
				 true,
				 true,
				 true,
				 true,
				 authorities);
	}
	
}
