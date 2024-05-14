package com.pruebas.repository;

import com.pruebas.model.SistemaPuntos;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface SistemaPuntosRepository extends MongoRepository<SistemaPuntos, String> {
	Optional<SistemaPuntos> findByNombre(String nombre);

	List<SistemaPuntos> findByNombreContaining(String nombre);
}
