package com.cibertec.api_reservas_mesas.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.api_reservas_mesas.dto.ReporteReservaPorFechaDTO;
import com.cibertec.api_reservas_mesas.repository.ReservaRepository;

@RestController
@RequestMapping("/reportes")
public class ReporteController {
	@Autowired
    private ReservaRepository reservaRepository;
	
	@GetMapping("/reservas-por-fecha")
	public ResponseEntity<?> generarReporte(
			@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate inicio, 
			@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate fin
	) {
	    LocalDate hoy = LocalDate.now();

	    if (inicio.isAfter(hoy) || fin.isAfter(hoy)) {
	        return ResponseEntity.badRequest().body("El rango de fechas no puede incluir hoy ni fechas futuras.");
	    }
	    
        int total = reservaRepository.contarTotalReservas(inicio, fin);
        int cancelaciones = reservaRepository.contarCancelaciones(inicio, fin);
        int dias = (int) ChronoUnit.DAYS.between(inicio, fin) + 1;
        double promedio = dias > 0 ? (double) total / dias : 0;

        List<Object[]> resultados = reservaRepository.contarReservasPorDia(inicio, fin);

        Map<String, Integer> mapaDias = new LinkedHashMap<>();
        List<String> nombresDias = List.of("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado");

        // Inicializar en cero todos los días
        for (String nombre : nombresDias) {
            mapaDias.put(nombre, 0);
        }

        for (Object[] fila : resultados) {
            int diaNumero = ((Number) fila[0]).intValue(); // 1 = Domingo
            int cantidad = ((Number) fila[1]).intValue();
            String nombreDia = nombresDias.get(diaNumero - 1);
            mapaDias.put(nombreDia, cantidad);
        }

        ReporteReservaPorFechaDTO dto = new ReporteReservaPorFechaDTO();
        dto.setReservasPorDia(mapaDias);
        dto.setTotalReservas(total);
        dto.setCancelaciones(cancelaciones);
        dto.setNumeroDeDias(dias);
        dto.setPromedioDiario(promedio);

        return ResponseEntity.ok(dto);
    }
}
