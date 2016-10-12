/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "pausa")
public class Pausa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "codigo")
	private Long codigo;

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "codtarea", foreignKey = @ForeignKey(name = "pausa_codtarea_fk"))
	private Tarea tarea;

	@Version
	@Column(name = "version")
	private Long version;

	@Column(name = "causa", length = 500, nullable = false)
	private String causa;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_hora_inicio", nullable = false)
	private Date fechaHoraInicio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_hora_fin", nullable = false)
	private Date fechaHoraFin;

	public Pausa() {
		super();
	}

	public Pausa(Tarea tarea, String causa, Date fechaHoraInicio, Date fechaHoraFin) {
		this();
		this.tarea = tarea;
		this.causa = causa;
		this.fechaHoraInicio = fechaHoraInicio;
		this.fechaHoraFin = fechaHoraFin;
	}

	public Long getId() {
		return codigo;
	}

	public Tarea getTarea() {
		return tarea;
	}

	public void setTarea(Tarea tarea) {
		this.tarea = tarea;
	}

	public String getCausa() {
		return causa;
	}

	public void setCausa(String causa) {
		this.causa = causa;
	}

	public Date getFechaHoraInicio() {
		return fechaHoraInicio;
	}

	public void setFechaHoraInicio(Date fechaHoraInicio) {
		this.fechaHoraInicio = fechaHoraInicio;
	}

	public Date getFechaHoraFin() {
		return fechaHoraFin;
	}

	public void setFechaHoraFin(Date fechaHoraFin) {
		this.fechaHoraFin = fechaHoraFin;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((causa == null) ? 0 : causa.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((fechaHoraFin == null) ? 0 : fechaHoraFin.hashCode());
		result = prime * result + ((fechaHoraInicio == null) ? 0 : fechaHoraInicio.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		if(getClass() != obj.getClass()){
			return false;
		}
		Pausa other = (Pausa) obj;
		if(causa == null){
			if(other.causa != null){
				return false;
			}
		}
		else if(!causa.equals(other.causa)){
			return false;
		}
		if(codigo == null){
			if(other.codigo != null){
				return false;
			}
		}
		else if(!codigo.equals(other.codigo)){
			return false;
		}
		if(fechaHoraFin == null){
			if(other.fechaHoraFin != null){
				return false;
			}
		}
		else if(!fechaHoraFin.equals(other.fechaHoraFin)){
			return false;
		}
		if(fechaHoraInicio == null){
			if(other.fechaHoraInicio != null){
				return false;
			}
		}
		else if(!fechaHoraInicio.equals(other.fechaHoraInicio)){
			return false;
		}
		if(version == null){
			if(other.version != null){
				return false;
			}
		}
		else if(!version.equals(other.version)){
			return false;
		}
		return true;
	}
}
