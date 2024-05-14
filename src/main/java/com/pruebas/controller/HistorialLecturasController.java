package com.pruebas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pruebas.exception.ResourceNotFoundException;
import com.pruebas.model.HistorialLectura;
import com.pruebas.repository.HistorialLecturaRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class HistorialLecturasController {

	@Autowired
	private HistorialLecturaRepository historialLecturaRepository;

	@GetMapping("/historialLecturas/getAllReadingsHistory")
	public ResponseEntity<List<HistorialLectura>> getAllReadingsHistory() {
		System.out.println("GET request received: getAllReadingsHistory");
		List<HistorialLectura> historialLecturas = historialLecturaRepository.findAll();
		return ResponseEntity.ok().body(historialLecturas);
	}

	@GetMapping("/historialLecturas/getSpecificReadingHistory/{uid}")
	public ResponseEntity<HistorialLectura> getSpecificReadingHistory(@PathVariable(value = "uid") String uid)
			throws ResourceNotFoundException {
		System.out.println("GET request received: getSpecificReadingHistory");
		HistorialLectura HistorialLectura = historialLecturaRepository.findByUid(uid)
				.orElseThrow(() -> new ResourceNotFoundException("No se encontró una lectura con el uid: " + uid));
		return ResponseEntity.ok().body(HistorialLectura);
	}

	@GetMapping("/historialLecturas/getLastReadingHistory")
	public ResponseEntity<HistorialLectura> getLastReadingHistory() {
		System.out.println("GET request received: getLastReadingHistory");
		Optional<HistorialLectura> readingOptional = historialLecturaRepository.findFirstByOrderByFechaHoraDesc();

		if (readingOptional.isPresent()) {
			return ResponseEntity.ok().body(readingOptional.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/historialLecturas/getCurrentReadingHistory/{fechaHoraFormateada}")
	public ResponseEntity<?> getCurrentReadingHistory(
	        @PathVariable(value = "fechaHoraFormateada") String fechaHoraFormateada) {
	    System.out.println("GET request received: getCurrentReadingHistory");

	    // Parsea la fecha formateada recibida a LocalDateTime
	    LocalDateTime fechaFormateada = LocalDateTime.parse(fechaHoraFormateada);

	    // Calcula el rango de tiempo: 5 segundos antes y después de la fecha formateada
	    LocalDateTime fechaInicio = fechaFormateada.minusSeconds(5);
	    LocalDateTime fechaFin = fechaFormateada.plusSeconds(5);

	    // Convierte las fechas a Date para la comparación en la consulta
	    Date fechaInicioDate = Date.from(fechaInicio.atZone(ZoneId.systemDefault()).toInstant());
	    Date fechaFinDate = Date.from(fechaFin.atZone(ZoneId.systemDefault()).toInstant());

	    // Busca el registro más cercano dentro del rango de tiempo
	    Optional<HistorialLectura> lecturaCercanaOptional = historialLecturaRepository.findAll().stream()
	            .filter(lectura -> lectura.getFechaHora().after(fechaInicioDate) && lectura.getFechaHora().before(fechaFinDate))
	            .min(Comparator.comparingLong(lectura -> Math.abs(lectura.getFechaHora().getTime() - fechaFormateada.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())));

	    // Verifica si se encontró un registro
	    if (lecturaCercanaOptional.isPresent()) {
	        return ResponseEntity.ok().body(lecturaCercanaOptional.get());
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("No se encontró ningún registro dentro del rango de tiempo especificado.");
	    }
	}

	@PostMapping("/historialLecturas/addReadingHistory")
	public ResponseEntity<HistorialLectura> addReadingHistory(@RequestBody HistorialLectura HistorialLectura) {
		System.out.println("POST request received: addReadingHistory");
		HistorialLectura nuevaHistorialLectura = historialLecturaRepository.save(HistorialLectura);
		return ResponseEntity.ok().body(nuevaHistorialLectura);
	}

	@PutMapping("/historialLecturas/updateReadingHistory/{uid}")
	public ResponseEntity<HistorialLectura> updateReadingHistory(@PathVariable(value = "uid") String uid,
			@RequestBody HistorialLectura HistorialLecturaDetails) throws ResourceNotFoundException {
		System.out.println("PUT request received: updateReadingHistory");
		HistorialLectura HistorialLectura = historialLecturaRepository.findByUid(uid)
				.orElseThrow(() -> new ResourceNotFoundException("No se encontró una lectura con el uid: " + uid));

		HistorialLectura.set_id(HistorialLecturaDetails.get_id());
		HistorialLectura.setUid(HistorialLecturaDetails.getUid());
		HistorialLectura.setFechaHora(HistorialLecturaDetails.getFechaHora());
		HistorialLectura.setResultado(HistorialLecturaDetails.getResultado());

		final HistorialLectura updatedHistorialLectura = historialLecturaRepository.save(HistorialLectura);
		return ResponseEntity.ok().body(updatedHistorialLectura);
	}

	@DeleteMapping("/historialLecturas/deleteReadingHistory/{uid}")
	public Map<String, Boolean> deleteReadingHistory(@PathVariable(value = "uid") String uid)
			throws ResourceNotFoundException {
		System.out.println("DELETE request received: deleteReadingHistory");
		HistorialLectura HistorialLectura = historialLecturaRepository.findByUid(uid)
				.orElseThrow(() -> new ResourceNotFoundException("No se encontró una lectura con el uid: " + uid));

		historialLecturaRepository.delete(HistorialLectura);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
	
	@GetMapping("/historialLecturas/getCurrentServerTime")
    public LocalDateTime getCurrentServerTime() {
        System.out.println("GET request received: getCurrentServerTime");
        return LocalDateTime.now();
    }
}
