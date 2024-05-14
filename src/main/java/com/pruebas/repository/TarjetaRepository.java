package com.pruebas.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.pruebas.model.Tarjeta;

public interface TarjetaRepository extends MongoRepository<Tarjeta, String> {
	Optional<Tarjeta> findByUid(String uid);

	List<Tarjeta> findAll();

	boolean existsByUid(String uid);
}