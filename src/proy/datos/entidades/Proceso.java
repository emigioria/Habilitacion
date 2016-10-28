/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.entidades;

import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

import proy.comun.FormateadorString;
import proy.datos.clases.EstadoStr;

@NamedQuery(name = "listarProcesos", query = "SELECT p FROM Proceso p WHERE p.estado.nombre = :est ORDER BY p.descripcion , p.tipo")
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

	@Column(name = "tiempo_proc", length = 100)
	private String tiempoTeoricoProceso;

	@Column(name = "tiempo_prep", length = 100)
	private String tiempoTeoricoPreparacion;

	@Column(name = "observaciones", length = 500)
	private String observaciones;

	@Column(name = "tipo", length = 100)
	private String tipo;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "codestado", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "proceso_codestado_fk"), nullable = false)
	private Estado estado;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "codparte", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "proceso_codparte_fk"))
	private Parte parte;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "pieza_proceso", joinColumns = @JoinColumn(name = "codproceso"), foreignKey = @ForeignKey(name = "pieza_proceso_codproceso_fk"), inverseJoinColumns = @JoinColumn(name = "codpieza"), inverseForeignKey = @ForeignKey(name = "pieza_proceso_codpieza_fk"))
	private Set<Pieza> piezas;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "herramienta_proceso", joinColumns = @JoinColumn(name = "codproceso"), foreignKey = @ForeignKey(name = "herramienta_proceso_codproceso_fk"), inverseJoinColumns = @JoinColumn(name = "codherramienta"), inverseForeignKey = @ForeignKey(name = "herramienta_proceso_codherramienta_fk"))
	private Set<Herramienta> herramientas;

	public Proceso() {
		super();
		herramientas = new HashSet<>();
		piezas = new HashSet<>();
		estado = new Estado(EstadoStr.ALTA);
	}

	public Proceso(String descripcion, String tiempoTeoricoProceso, String tiempoTeoricoPreparacion, String observaciones, String tipo, Estado estado, Parte parte) {
		this();
		this.descripcion = descripcion;
		this.tiempoTeoricoProceso = tiempoTeoricoProceso;
		this.tiempoTeoricoPreparacion = tiempoTeoricoPreparacion;
		this.observaciones = observaciones;
		this.tipo = tipo;
		this.estado = estado;
		this.parte = parte;
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

	public String getTiempoTeoricoProceso() {
		return tiempoTeoricoProceso;
	}

	public void setTiempoTeoricoProceso(String tiempoTeoricoProceso) {
		this.tiempoTeoricoProceso = tiempoTeoricoProceso;
	}

	public String getTiempoTeoricoPreparacion() {
		return tiempoTeoricoPreparacion;
	}

	public void setTiempoTeoricoPreparacion(String tiempoTeoricoPreparacion) {
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
		return piezas;
	}

	public Set<Herramienta> getHerramientas() {
		return herramientas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((observaciones == null) ? 0 : observaciones.hashCode());
		result = prime * result + ((tiempoTeoricoPreparacion == null) ? 0 : tiempoTeoricoPreparacion.hashCode());
		result = prime * result + ((tiempoTeoricoProceso == null) ? 0 : tiempoTeoricoProceso.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
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
		Proceso other = (Proceso) obj;
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
		if(descripcion == null){
			if(other.descripcion != null){
				return false;
			}
		}
		else if(!descripcion.equals(other.descripcion)){
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
		if(observaciones == null){
			if(other.observaciones != null){
				return false;
			}
		}
		else if(!observaciones.equals(other.observaciones)){
			return false;
		}
		if(tiempoTeoricoPreparacion == null){
			if(other.tiempoTeoricoPreparacion != null){
				return false;
			}
		}
		else if(!tiempoTeoricoPreparacion.equals(other.tiempoTeoricoPreparacion)){
			return false;
		}
		if(tiempoTeoricoProceso == null){
			if(other.tiempoTeoricoProceso != null){
				return false;
			}
		}
		else if(!tiempoTeoricoProceso.equals(other.tiempoTeoricoProceso)){
			return false;
		}
		if(tipo == null){
			if(other.tipo != null){
				return false;
			}
		}
		else if(!tipo.equals(other.tipo)){
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

	@Override
	public String toString() {
		return FormateadorString.primeraMayuscula(this.getDescripcion()) + " - " + FormateadorString.primeraMayuscula(this.getTipo());
	}
}
