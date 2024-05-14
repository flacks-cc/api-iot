package com.pruebas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pruebas.exception.ResourceNotFoundException;
import com.pruebas.model.HistorialLectura;
import com.pruebas.model.Tarjeta;
import com.pruebas.repository.HistorialLecturaRepository;
import com.pruebas.repository.TarjetaRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class TarjetasController {

	@Autowired
	private TarjetaRepository tarjetaRepository;
	@Autowired
	private HistorialLecturaRepository historialLecturaRepository;

	@GetMapping("/tarjetas/getAllCards")
	public ResponseEntity<List<Tarjeta>> getAllCards() {
		System.out.println("GET request received: getAllCards");
		List<Tarjeta> tarjetas = tarjetaRepository.findAll();
		return ResponseEntity.ok().body(tarjetas);
	}

	@GetMapping("/tarjetas/getSpecificCard/{uid}")
	public ResponseEntity<Tarjeta> getSpecificCard(@PathVariable(value = "uid") String uid)
			throws ResourceNotFoundException {
		System.out.println("GET request received: getSpecificCard");
		Tarjeta tarjeta = tarjetaRepository.findByUid(uid)
				.orElseThrow(() -> new ResourceNotFoundException("No se encontró una tarjeta con el uid: " + uid));
		return ResponseEntity.ok().body(tarjeta);
	}

	@GetMapping("/tarjetas/getCardAndUser/{uid}")
	public ResponseEntity<Map<String, String>> getCardAndUser(@PathVariable(value = "uid") String uid)
			throws ResourceNotFoundException {
		System.out.println("GET request received: getCardAndUser");
		Optional<Tarjeta> optionalTarjeta = tarjetaRepository.findByUid(uid);

		if (optionalTarjeta.isPresent()) {
			Tarjeta tarjeta = optionalTarjeta.get();

			// Crear un mapa para almacenar los datos a devolver en formato JSON
			Map<String, String> result = new HashMap<>();
			result.put("uid", tarjeta.getUid());
			result.put("nombre_usuario", tarjeta.getCliente().getNombre_usuario());
			String resultado = "Tarjeta registrada";

			addReadingHistory(uid, resultado);

			return ResponseEntity.ok().body(result);
		} else {
			Map<String, String> emptyResult = new HashMap<>();
			String resultado = "Tarjeta no registrada";
			addReadingHistory(uid, resultado);
			return ResponseEntity.ok().body(emptyResult);
		}
	}

	// Método para agregar la lectura al historial de lectura
	private void addReadingHistory(String uid, String resultado) {
		HistorialLectura historialLectura = new HistorialLectura();
		historialLectura.setUid(uid);
		historialLectura.setFechaHora(new Date());
		historialLectura.setFechaHoraFormateada(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date()));
		historialLectura.setResultado(resultado);

		historialLecturaRepository.save(historialLectura);
	}

	@PostMapping("/tarjetas/addCard")
	public ResponseEntity<Tarjeta> addTarjeta(@RequestBody Tarjeta tarjeta) {
		System.out.println("POST request received: addTarjeta");
		Tarjeta nuevaTarjeta = tarjetaRepository.save(tarjeta);
		return ResponseEntity.ok().body(nuevaTarjeta);
	}

	@PutMapping("/tarjetas/updateCard/{uid}")
	public ResponseEntity<Tarjeta> updateCard(@PathVariable(value = "uid") String uid,
			@RequestBody Tarjeta tarjetaDetails) throws ResourceNotFoundException {
		System.out.println("PUT request received: updateCard");
		Tarjeta tarjeta = tarjetaRepository.findByUid(uid)
				.orElseThrow(() -> new ResourceNotFoundException("No se encontró una tarjeta con el uid: " + uid));

		tarjeta.setUid(tarjetaDetails.getUid());
		tarjeta.setCliente(tarjetaDetails.getCliente());
		tarjeta.setPuntos(tarjetaDetails.getPuntos());
		tarjeta.setHistorial_puntos(tarjetaDetails.getHistorial_puntos());

		final Tarjeta updatedTarjeta = tarjetaRepository.save(tarjeta);
		return ResponseEntity.ok().body(updatedTarjeta);
	}

	@DeleteMapping("/tarjetas/deleteCard/{uid}")
	public Map<String, Boolean> deleteCard(@PathVariable(value = "uid") String uid) throws ResourceNotFoundException {
		System.out.println("DELETE request received: deleteCard");
		Tarjeta tarjeta = tarjetaRepository.findByUid(uid)
				.orElseThrow(() -> new ResourceNotFoundException("No se encontró una tarjeta con el uid: " + uid));

		tarjetaRepository.delete(tarjeta);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	@PostMapping("/tarjetas/addPoints/{uid}")
	public ResponseEntity<Map<String, Object>> addPoints(@PathVariable(value = "uid") String uid,
			@RequestBody Map<String, Object> requestBody) throws ResourceNotFoundException {

		int cantidad = (int) requestBody.get("cantidad");
		String descripcion = (String) requestBody.get("descripcion");

		System.out.println("POST request received: addPoints");
		Tarjeta tarjeta = tarjetaRepository.findByUid(uid)
				.orElseThrow(() -> new ResourceNotFoundException("No se encontró una tarjeta con el uid: " + uid));

		int puntosActuales = tarjeta.getPuntos();
		tarjeta.setPuntos(puntosActuales + cantidad);

		// Guardar el historial de puntos
		Tarjeta.HistorialPuntos historialPuntos = new Tarjeta.HistorialPuntos();
		historialPuntos.setFecha(new Date());
		historialPuntos.setTipo("adición");
		historialPuntos.setDescripcion(descripcion);
		historialPuntos.setCantidad(cantidad);
		tarjeta.getHistorial_puntos().add(historialPuntos);

		final Tarjeta updatedTarjeta = tarjetaRepository.save(tarjeta);

		Map<String, Object> response = new HashMap<>();
		response.put("uid", updatedTarjeta.getUid());
		response.put("nombre_usuario", updatedTarjeta.getCliente().getNombre_usuario());
		response.put("puntos", updatedTarjeta.getPuntos());
		response.put("historial_puntos",
				updatedTarjeta.getHistorial_puntos().get(updatedTarjeta.getHistorial_puntos().size() - 1));
		return ResponseEntity.ok().body(response);
	}

	@PostMapping("/tarjetas/redeemPoints/{uid}")
	public ResponseEntity<Map<String, Object>> redeemPoints(@PathVariable(value = "uid") String uid,
			@RequestBody Map<String, Object> requestBody) throws ResourceNotFoundException {

		int cantidad = (int) requestBody.get("cantidad");
		String descripcion = (String) requestBody.get("descripcion");

		System.out.println("POST request received: redeemPoints");
		Tarjeta tarjeta = tarjetaRepository.findByUid(uid)
				.orElseThrow(() -> new ResourceNotFoundException("No se encontró una tarjeta con el uid: " + uid));

		int puntosActuales = tarjeta.getPuntos();
		if (puntosActuales >= cantidad) {
			tarjeta.setPuntos(puntosActuales - cantidad);

			// Guardar el historial de puntos
			Tarjeta.HistorialPuntos historialPuntos = new Tarjeta.HistorialPuntos();
			historialPuntos.setFecha(new Date());
			historialPuntos.setTipo("canje");
			historialPuntos.setDescripcion(descripcion);
			historialPuntos.setCantidad(cantidad);
			tarjeta.getHistorial_puntos().add(historialPuntos);

			final Tarjeta updatedTarjeta = tarjetaRepository.save(tarjeta);
			
			Map<String, Object> response = new HashMap<>();
			response.put("uid", updatedTarjeta.getUid());
			response.put("nombre_usuario", updatedTarjeta.getCliente().getNombre_usuario());
			response.put("puntos", updatedTarjeta.getPuntos());
			response.put("historial_puntos",
					updatedTarjeta.getHistorial_puntos().get(updatedTarjeta.getHistorial_puntos().size() - 1));
			return ResponseEntity.ok().body(response);
		} else {
			// Manejar el caso donde el usuario no tiene suficientes puntos para canjear
			Map<String, Object> errorResponse = new HashMap<>();
	        errorResponse.put("mensaje", "No tienes suficientes puntos para canjear");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}
}
