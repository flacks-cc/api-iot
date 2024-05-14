package com.pruebas.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sistema_puntos")
public class SistemaPuntos {

	@Id
	private String _id;
    private String nombre;
    private Integer precio;
    private Integer puntos;
    private String descripcion;
    
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Integer getPrecio() {
		return precio;
	}
	public void setPrecio(Integer precio) {
		this.precio = precio;
	}
	public Integer getPuntos() {
		return puntos;
	}
	public void setPuntos(Integer puntos) {
		this.puntos = puntos;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
    
    
}