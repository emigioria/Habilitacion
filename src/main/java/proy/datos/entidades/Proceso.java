/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.entidades;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import proy.comun.FormateadorString;
import proy.datos.clases.EstadoStr;
import proy.datos.clases.EstadoTareaStr;

@NamedQueries(value = {
		@NamedQuery(name = "listarProcesos", query = "SELECT p FROM Proceso p WHERE p.estado.nombre = :est ORDER BY p.descripcion , p.tipo"),
		@NamedQuery(name = "listarDescripcionesProcesos", query = "SELECT DISTINCT p.descripcion FROM Proceso p WHERE p.estado.nombre = :est ORDER BY p.descripcion"),
		@NamedQuery(name = "listarTiposProcesos", query = "SELECT DISTINCT p.tipo FROM Proceso p WHERE p.estado.nombre = :est ORDER BY p.tipo")
})
@Entity
@Table(name = "proceso")
public class Proceso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "codigo")
	private Long codigo;

	@Version
	@Column(name = "version")
	private Long version;

	@Column(name = "descripcion", length = 100)
	private String descripcion;

	@Column(name = "tiempo_proc")
	private Long tiempoTeoricoProceso;

	@Column(name = "tiempo_prep")
	private Long tiempoTeoricoPreparacion;

	@Column(name = "observaciones", length = 500)
	private String observaciones;

	@Column(name = "tipo", length = 100)
	private String tipo;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "codestado", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "proceso_codestado_fk"), nullable = false)
	private Estado estado;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "codparte", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "proceso_codparte_fk"), nullable = false)
	private Parte parte;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "pieza_proceso", joinColumns = @JoinColumn(name = "codproceso"), foreignKey = @ForeignKey(name = "pieza_proceso_codproceso_fk"), inverseJoinColumns = @JoinColumn(name = "codpieza"), inverseForeignKey = @ForeignKey(name = "pieza_proceso_codpieza_fk"))
	private Set<Pieza> piezas;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "herramienta_proceso", joinColumns = @JoinColumn(name = "codproceso"), foreignKey = @ForeignKey(name = "herramienta_proceso_codproceso_fk"), inverseJoinColumns = @JoinColumn(name = "codherramienta"), inverseForeignKey = @ForeignKey(name = "herramienta_proceso_codherramienta_fk"))
	private Set<Herramienta> herramientas;

	@ManyToMany(mappedBy = "proceso", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Tarea> tareas;

	@Transient
	private FormateadorString formater = new FormateadorString();

	public Proceso() {
		super();
		herramientas = new HashSet<>();
		piezas = new HashSet<>();
		estado = new Estado(EstadoStr.ALTA);
		tareas = new HashSet<>();
	}

	public Long getId() {
		return codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Long getTiempoTeoricoProceso() {
		return tiempoTeoricoProceso;
	}

	public void setTiempoTeoricoProceso(Long tiempoTeoricoProceso) {
		this.tiempoTeoricoProceso = tiempoTeoricoProceso;
	}

	public Long getTiempoTeoricoPreparacion() {
		return tiempoTeoricoPreparacion;
	}

	public void setTiempoTeoricoPreparacion(Long tiempoTeoricoPreparacion) {
		this.tiempoTeoricoPreparacion = tiempoTeoricoPreparacion;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Parte getParte() {
		return parte;
	}

	public void setParte(Parte parte) {
		this.parte = parte;
	}

	public Set<Pieza> getPiezas() {
		piezas.size();
		return piezas;
	}

	public Set<Herramienta> getHerramientas() {
		herramientas.size();
		return herramientas;
	}

	public Set<Tarea> getTareas() {
		tareas.size();
		return tareas;
	}

	public void darDeBaja() {
		this.setEstado(new Estado(EstadoStr.BAJA));
	}

	public Long getTiempoPromedioProceso() {
		Long suma = 0L, tiempoTotalEjecutando, tiempoTeoricoPreparacion, tiempoSinPreparacionEjecutando;
		Integer cantidadTareas = 0, cantidadRealizada;
		for(Tarea t: this.getTareas()){
			if(EstadoTareaStr.FINALIZADA.equals(t.getEstado().getNombre())){
				tiempoTotalEjecutando = t.getTiempoEjecutando();
				tiempoTeoricoPreparacion = getTiempoTeoricoPreparacion();
				tiempoSinPreparacionEjecutando = tiempoTotalEjecutando - tiempoTeoricoPreparacion;
				cantidadRealizada = t.getCantidadReal();
				cantidadTareas += cantidadRealizada;
				suma += tiempoSinPreparacionEjecutando + tiempoTeoricoPreparacion * cantidadRealizada;
			}
		}
		if(cantidadTareas > 1){
			suma = suma / cantidadTareas;
		}
		return suma;
	}

	public Boolean cambioPoco(Proceso procesoAnterior) {
		if(descripcion == null){
			if(procesoAnterior.descripcion != null){
				return false;
			}
		}
		else if(!descripcion.equals(procesoAnterior.descripcion)){
			return false;
		}
		if(estado == null){
			if(procesoAnterior.estado != null){
				return false;
			}
		}
		else if(!estado.equals(procesoAnterior.estado)){
			return false;
		}
		if(tipo == null){
			if(procesoAnterior.tipo != null){
				return false;
			}
		}
		else if(!tipo.equals(procesoAnterior.tipo)){
			return false;
		}
		if(version == null){
			if(procesoAnterior.version != null){
				return false;
			}
		}
		else if(!version.equals(procesoAnterior.version)){
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
		Proceso other = (Proceso) obj;
		if(codigo != null && codigo.equals(other.codigo)){
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return formater.primeraMayuscula(this.getDescripcion()) + " - " + formater.primeraMayuscula(this.getTipo());
	}
}
