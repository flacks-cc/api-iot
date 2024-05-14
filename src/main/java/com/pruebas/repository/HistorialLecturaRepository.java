package com.pruebas.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.pruebas.model.HistorialLectura;

public interface HistorialLecturaRepository extends MongoRepository<HistorialLectura, String> {
	Optional<HistorialLectura> findByUid(String uid);
	
	Optional<HistorialLectura> findByFechaHoraFormateada(String fechaHoraFormateada);
			
	List<HistorialLectura> findAll();

    Optional<HistorialLectura> findFirstByOrderByFechaHoraDesc();
    
}
