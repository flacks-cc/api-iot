package com.pruebas.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "tarjetas")
public class Tarjeta {

	@Id
	private String _id;
	private String uid;
	private int puntos;
	private Cliente cliente;

	public static class Cliente {

		private String id;
		private String nombre_usuario;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getNombre_usuario() {
			return nombre_usuario;
		}

		public void setNombre_usuario(String nombre_usuario) {
			this.nombre_usuario = nombre_usuario;
		}
	}

	private List<HistorialPuntos> historial_puntos;

	public static class HistorialPuntos {

		private Date fecha;
		private String tipo;
		private String descripcion;
		private int cantidad;

		public Date getFecha() {
			return fecha;
		}

		public void setFecha(Date fecha) {
			this.fecha = fecha;
		}

		public String getTipo() {
			return tipo;
		}

		public void setTipo(String tipo) {
			this.tipo = tipo;
		}

		public String getDescripcion() {
			return descripcion;
		}

		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}

		public int getCantidad() {
			return cantidad;
		}

		public void setCantidad(int cantidad) {
			this.cantidad = cantidad;
		}
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getPuntos() {
		return puntos;
	}

	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<HistorialPuntos> getHistorial_puntos() {
		return historial_puntos;
	}

	public void setHistorial_puntos(List<HistorialPuntos> historial_puntos) {
		this.historial_puntos = historial_puntos;
	}
}