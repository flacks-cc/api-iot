package com.pruebas.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "historial_lecturas")
public class HistorialLectura {

	@Id
	private String _id;
	private String uid;
	private Date fechaHora;
	private String fechaHoraFormateada;
	private String resultado;

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
	
	public Date getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
	}

	public String getFechaHoraFormateada() {
		return fechaHoraFormateada;
	}

	public void setFechaHoraFormateada(String fechaHoraFormateada) {
		this.fechaHoraFormateada = fechaHoraFormateada;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
}
