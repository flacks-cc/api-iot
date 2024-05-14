package com.pruebas.controller;

import com.pruebas.exception.ResourceNotFoundException;
import com.pruebas.model.SistemaPuntos;
import com.pruebas.repository.SistemaPuntosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SistemaPuntosController {

	@Autowired
	private SistemaPuntosRepository sistemaPuntosRepository;

	@GetMapping("/sistemaPuntos/getAllPointsSystem")
	public ResponseEntity<List<SistemaPuntos>> getAllPointsSystem() {
		System.out.println("GET request received: getAllPointsSystem");
		List<SistemaPuntos> sistemaPuntos = sistemaPuntosRepository.findAll();
		return ResponseEntity.ok().body(sistemaPuntos);
	}

	@GetMapping("/sistemaPuntos/getSpecificPointsSystem/{nombre}")
	public ResponseEntity<SistemaPuntos> getSpecificPointsSystem(@PathVariable(value = "nombre") String nombre)
			throws ResourceNotFoundException {
		System.out.println("GET request received: getSpecificPointsSystem");
		SistemaPuntos sistemaPuntos = sistemaPuntosRepository.findByNombre(nombre)
				.orElseThrow(() -> new ResourceNotFoundException("No se encontraron sistemas de puntos con el nombre: " + nombre));
		return ResponseEntity.ok().body(sistemaPuntos);
	}

	@GetMapping("/sistemaPuntos/getPointsSystemByKeyword/{keyword}")
	public ResponseEntity<List<SistemaPuntos>> getPointsSystemByKeyword(@PathVariable(value = "keyword") String keyword)
			throws ResourceNotFoundException {
		System.out.println("GET request received: getPointsSystemByKeyword");
		List<SistemaPuntos> sistemaPuntos;
		switch (keyword) {
		case "visita":
			sistemaPuntos = sistemaPuntosRepository.findByNombreContaining("visita");
			break;
		case "servicio":
			sistemaPuntos = sistemaPuntosRepository.findByNombreContaining("servicio");
			break;
		case "producto":
			sistemaPuntos = sistemaPuntosRepository.findByNombreContaining("producto");
			break;
		case "recompensa":
			sistemaPuntos = sistemaPuntosRepository.findByNombreContaining("recompensa");
			break;
		default:
			throw new ResourceNotFoundException(
					"No se encontró ningún sistema de puntos que contenga la palabra clave: " + keyword);
		}
		return ResponseEntity.ok().body(sistemaPuntos);
	}

	@PostMapping("/sistemaPuntos/addPointsSystem")
	public ResponseEntity<SistemaPuntos> addPointsSystem(@RequestBody SistemaPuntos sistemaPuntos) {
		System.out.println("GET request received: addPointsSystem");
		SistemaPuntos nuevoSistemaPuntos = sistemaPuntosRepository.save(sistemaPuntos);
		return ResponseEntity.ok().body(nuevoSistemaPuntos);
	}

	@PutMapping("/sistemaPuntos/updatePointsSystem/{nombre}")
	public ResponseEntity<SistemaPuntos> updatePointsSystem(@PathVariable(value = "nombre") String nombre,
			@RequestBody SistemaPuntos puntosDetails) throws ResourceNotFoundException {
		System.out.println("GET request received: updatePointsSystem");
		SistemaPuntos sistemaPuntos = sistemaPuntosRepository.findByNombre(nombre)
				.orElseThrow(() -> new ResourceNotFoundException("No se encontraron sistemas de puntos con el nombre: " + nombre));

		sistemaPuntos.setPrecio(puntosDetails.getPrecio());
		sistemaPuntos.setPuntos(puntosDetails.getPuntos());
		sistemaPuntos.setDescripcion(puntosDetails.getDescripcion());

		final SistemaPuntos updatedPuntos = sistemaPuntosRepository.save(sistemaPuntos);
		return ResponseEntity.ok().body(updatedPuntos);
	}

	@DeleteMapping("/sistemaPuntos/deletePointsSystem/{nombre}")
	public Map<String, Boolean> deletePointsSystem(@PathVariable(value = "nombre") String nombre)
			throws ResourceNotFoundException {
		System.out.println("GET request received: deletePointsSystem");
		SistemaPuntos sistemaPuntos = sistemaPuntosRepository.findByNombre(nombre)
				.orElseThrow(() -> new ResourceNotFoundException("No se encontraron sistemas de puntos con el nombre: " + nombre));

		sistemaPuntosRepository.delete(sistemaPuntos);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
