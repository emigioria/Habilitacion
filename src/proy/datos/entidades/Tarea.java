/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.entidades;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

@NamedQuery(name = "listarTareas", query = "SELECT t FROM Tarea t ORDER BY t.fechaPlanificada ASC")
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

	@Column(name = "cantidad_teorica", nullable = false)
	private Integer cantidadTeorica;

	@Column(name = "cantidad_real")
	private Integer cantidadReal;

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_planificada", nullable = false)
	private Date fechaPlanificada;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_hora_inicio")
	private Date fechaHoraInicio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_hora_fin")
	private Date fechaHoraFin;

	@Column(name = "observaciones_tarea", length = 500)
	private String observacionesTarea;

	@Column(name = "observaciones_operario", length = 500)
	private String observacionesOperario;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "codestado", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "tarea_codestado_fk"), nullable = false)
	private EstadoTarea estado;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "codproceso", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "tarea_codProceso_fk"), nullable = false)
	private Proceso proceso;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "codoperario", referencedColumnName = "codusuario", foreignKey = @ForeignKey(name = "tarea_codoperario_fk"), nullable = false)
	private Operario operario;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "tarea")
	private Set<Pausa> pausas;

	public Tarea() {
		super();
		pausas = new HashSet<>();
		estado = new EstadoTarea(EstadoTareaStr.PLANIFICADA);
	}

	public Long getId() {
		return codigo;
	}

	public Integer getCantidadTeorica() {
		return cantidadTeorica;
	}

	public void setCantidadTeorica(Integer cantidadTeorica) {
		this.cantidadTeorica = cantidadTeorica;
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

	public String getObservacionesTarea() {
		return observacionesTarea;
	}

	public void setObservacionesTarea(String observacionesTarea) {
		this.observacionesTarea = observacionesTarea;
	}

	public String getObservacionesOperario() {
		return observacionesOperario;
	}

	public void setObservacionesOperario(String observacionesOperario) {
		this.observacionesOperario = observacionesOperario;
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

	public Set<Pausa> getPausas() {
		pausas.size();
		return pausas;
	}

	public Long getTiempoEjecutando() {
		Long tiempoTotal = 0L;
		Long tiempoPausas = 0L;
		switch(this.getEstado().getNombre()) {
		case FINALIZADA:
			tiempoTotal = (this.getFechaHoraFin().getTime() - this.getFechaHoraInicio().getTime());
			for(Pausa p: this.getPausas()){
				tiempoPausas += p.getTiempo();
			}
			break;
		case EJECUTANDO:
			tiempoTotal = (new Date().getTime() - this.getFechaHoraInicio().getTime());
			for(Pausa p: this.getPausas()){
				tiempoPausas += p.getTiempo();
			}
			break;
		case PAUSADA:
			Pausa ultimaPausa = null;
			for(Pausa p: this.getPausas()){
				if(p.getFechaHoraFin() == null){
					ultimaPausa = p;
				}
				else{
					tiempoPausas += p.getTiempo();
				}
			}
			tiempoTotal = (ultimaPausa.getFechaHoraInicio().getTime() - this.getFechaHoraInicio().getTime());
			break;
		case PLANIFICADA:
			break;
		}
		return tiempoTotal - tiempoPausas;
	}

	public Long getTiempoTotalTarea() {
		return this.getProceso().getTiempoTeoricoPreparacion() + this.getProceso().getTiempoTeoricoProceso() * this.getCantidadTeorica();
	}

	public void comenzar() {
		this.setEstado(new EstadoTarea(EstadoTareaStr.EJECUTANDO));
	}

	public void reanudar() {
		this.comenzar();
	}

	public void pausar() {
		this.setEstado(new EstadoTarea(EstadoTareaStr.PAUSADA));
	}

	public void terminar() {
		this.setEstado(new EstadoTarea(EstadoTareaStr.FINALIZADA));
	}

	public void cancelar() {
		this.setEstado(new EstadoTarea(EstadoTareaStr.PLANIFICADA));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cantidadReal == null) ? 0 : cantidadReal.hashCode());
		result = prime * result + ((cantidadTeorica == null) ? 0 : cantidadTeorica.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((fechaHoraFin == null) ? 0 : fechaHoraFin.hashCode());
		result = prime * result + ((fechaHoraInicio == null) ? 0 : fechaHoraInicio.hashCode());
		result = prime * result + ((fechaPlanificada == null) ? 0 : fechaPlanificada.hashCode());
		result = prime * result + ((observacionesTarea == null) ? 0 : observacionesTarea.hashCode());
		result = prime * result + ((observacionesOperario == null) ? 0 : observacionesOperario.hashCode());
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
		if(codigo == null){
			if(other.codigo != null){
				return false;
			}
		}
		else if(!codigo.equals(other.codigo)){
			return false;
		}
		else{
			return true;
		}
		if(cantidadReal == null){
			if(other.cantidadReal != null){
				return false;
			}
		}
		else if(!cantidadReal.equals(other.cantidadReal)){
			return false;
		}
		if(cantidadTeorica == null){
			if(other.cantidadTeorica != null){
				return false;
			}
		}
		else if(!cantidadTeorica.equals(other.cantidadTeorica)){
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
		if(observacionesTarea == null){
			if(other.observacionesTarea != null){
				return false;
			}
		}
		else if(!observacionesTarea.equals(other.observacionesTarea)){
			return false;
		}
		if(observacionesOperario == null){
			if(other.observacionesOperario != null){
				return false;
			}
		}
		else if(!observacionesOperario.equals(other.observacionesOperario)){
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
