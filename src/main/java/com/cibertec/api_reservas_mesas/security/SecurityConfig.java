package com.cibertec.api_reservas_mesas.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.cibertec.api_reservas_mesas.security.filters.JwtAuthenticationFilter;
import com.cibertec.api_reservas_mesas.security.filters.JwtAuthorizationFilter;
import com.cibertec.api_reservas_mesas.security.jwt.JwtUtils;
import com.cibertec.api_reservas_mesas.service.UserDetailsServiceImpl;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	JwtAuthorizationFilter authorizationFilter;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http,
			AuthenticationManager authenticationManager,
			CorsConfigurationSource corsConfigurationSource) throws Exception {
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils); 
		jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
		jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");
		
		return http
				.csrf(config -> config.disable())
				.cors(cors -> cors.configurationSource(corsConfigurationSource))
				.authorizeHttpRequests(auth -> {
					auth.requestMatchers("/auth/**").permitAll();
					auth.requestMatchers("/ubicaciones/**").hasRole("ADMINISTRADOR");
					auth.requestMatchers("/reportes/**").hasRole("ADMINISTRADOR");
					auth.anyRequest().authenticated();
				})
				.sessionManagement(session -> {
					session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
				})
				.addFilter(jwtAuthenticationFilter)
				.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http, 
			PasswordEncoder passwordEncoder) throws Exception {
		return http
				.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder)
				.and().build();
	}
}
