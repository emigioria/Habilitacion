/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.entidades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import proy.datos.clases.EstadoTareaStr;

@NamedQuery(name = "listarTareas", query = "SELECT t FROM Tarea t")
@Entity
@Table(name = "tarea")
public class Tarea {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "codigo")
	private Long codigo;

	@Version
	@Column(name = "version")
	private Long version;

	@Column(name = "cantidad_solicitada", nullable = false)
	private Integer cantidadSolicitada;

	@Column(name = "cantidad_real", nullable = false)
	private Integer cantidadReal;

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_planificada", nullable = false)
	private Date fechaPlanificada;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_hora_inicio", nullable = false)
	private Date fechaHoraInicio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_hora_fin", nullable = false)
	private Date fechaHoraFin;

	@Column(name = "observaciones", length = 500)
	private String observaciones;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "codestado", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "tarea_codestado_fk"), nullable = false)
	private EstadoTarea estado;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "codproceso", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "tarea_codProceso_fk"))
	private Proceso proceso;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "codoperario", referencedColumnName = "codusuario", foreignKey = @ForeignKey(name = "tarea_codoperario_fk"))
	private Operario operario;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "tarea")
	private List<Pausa> pausas;

	public Tarea() {
		super();
		pausas = new ArrayList<>();
		estado = new EstadoTarea(EstadoTareaStr.PLANIFICADA);
	}

	public Tarea(Integer cantidadSolicitada, Integer cantidadReal, Date fechaPlanificada, Date fechaHoraInicio, Date fechaHoraFin, String observaciones, EstadoTarea estado, Proceso proceso, Operario operario) {
		this();
		this.cantidadSolicitada = cantidadSolicitada;
		this.cantidadReal = cantidadReal;
		this.fechaPlanificada = fechaPlanificada;
		this.fechaHoraInicio = fechaHoraInicio;
		this.fechaHoraFin = fechaHoraFin;
		this.observaciones = observaciones;
		this.estado = estado;
		this.proceso = proceso;
		this.operario = operario;
	}

	public Long getId() {
		return codigo;
	}

	public Integer getCantidadTeorica() {
		return cantidadSolicitada;
	}

	public void setCantidadTeorica(Integer cantidadSolicitada) {
		this.cantidadSolicitada = cantidadSolicitada;
	}

	public Integer getCantidadReal() {
		return cantidadReal;
	}

	public void setCantidadReal(Integer cantidadReal) {
		this.cantidadReal = cantidadReal;
	}

	public Date getFechaPlanificada() {
		return fechaPlanificada;
	}

	public void setFechaPlanificada(Date fechaPlanificada) {
		this.fechaPlanificada = fechaPlanificada;
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

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public EstadoTarea getEstado() {
		return estado;
	}

	public void setEstado(EstadoTarea estado) {
		this.estado = estado;
	}

	public Proceso getProceso() {
		return proceso;
	}

	public void setProceso(Proceso proceso) {
		this.proceso = proceso;
	}

	public Operario getOperario() {
		return operario;
	}

	public void setOperario(Operario operario) {
		this.operario = operario;
	}

	public List<Pausa> getPausas() {
		pausas.size();
		return pausas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cantidadReal == null) ? 0 : cantidadReal.hashCode());
		result = prime * result + ((cantidadSolicitada == null) ? 0 : cantidadSolicitada.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((fechaHoraFin == null) ? 0 : fechaHoraFin.hashCode());
		result = prime * result + ((fechaHoraInicio == null) ? 0 : fechaHoraInicio.hashCode());
		result = prime * result + ((fechaPlanificada == null) ? 0 : fechaPlanificada.hashCode());
		result = prime * result + ((observaciones == null) ? 0 : observaciones.hashCode());
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
		Tarea other = (Tarea) obj;
		if(cantidadReal == null){
			if(other.cantidadReal != null){
				return false;
			}
		}
		else if(!cantidadReal.equals(other.cantidadReal)){
			return false;
		}
		if(cantidadSolicitada == null){
			if(other.cantidadSolicitada != null){
				return false;
			}
		}
		else if(!cantidadSolicitada.equals(other.cantidadSolicitada)){
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
		if(estado == null){
			if(other.estado != null){
				return false;
			}
		}
		else if(!estado.equals(other.estado)){
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
		if(fechaPlanificada == null){
			if(other.fechaPlanificada != null){
				return false;
			}
		}
		else if(!fechaPlanificada.equals(other.fechaPlanificada)){
			return false;
		}
		if(observaciones == null){
			if(other.observaciones != null){
				return false;
			}
		}
		else if(!observaciones.equals(other.observaciones)){
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
