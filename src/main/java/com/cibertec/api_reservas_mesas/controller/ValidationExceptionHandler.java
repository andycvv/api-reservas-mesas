package com.cibertec.api_reservas_mesas.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cibertec.api_reservas_mesas.exception.RangoHorarioInvalidoException;

@RestControllerAdvice
public class ValidationExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarErrores(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errores.put(error.getField(), error.getDefaultMessage());
        });
        return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(RangoHorarioInvalidoException.class)
	public ResponseEntity<Map<String, String>> handleRangoHorarioInvalido(RangoHorarioInvalidoException ex) {
        Map<String, String> errores = new HashMap<>();
        errores.put(ex.getCampo(), ex.getMessage());
        return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
	}
}
