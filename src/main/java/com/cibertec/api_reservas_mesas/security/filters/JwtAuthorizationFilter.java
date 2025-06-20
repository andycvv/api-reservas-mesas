package com.cibertec.api_reservas_mesas.security.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cibertec.api_reservas_mesas.security.jwt.JwtUtils;
import com.cibertec.api_reservas_mesas.service.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain)
			throws ServletException, IOException {
		
		String tokenHeader = request.getHeader("Authorization");
		
		if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
			String token = tokenHeader.substring(7);
			
			if (jwtUtils.isTokenValid(token)) {
				String dni = jwtUtils.getUsernameFromToken(token);
				UserDetails userDetails = userDetailsService.loadUserByUsername(dni);
				
				UsernamePasswordAuthenticationToken authenticationToken =
						new UsernamePasswordAuthenticationToken(dni, null, userDetails.getAuthorities());
				
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}
		
		filterChain.doFilter(request, response);
	}
	
}
