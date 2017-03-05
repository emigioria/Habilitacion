/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import proy.comun.FormateadorString;
import proy.datos.clases.EstadoStr;

@NamedQuery(name = "listarHerramientas", query = "SELECT h FROM Herramienta h WHERE h.estado.nombre = :est ORDER BY h.nombre ")
@Entity
@Table(name = "herramienta")
public class Herramienta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "codigo")
	private Long codigo;

	@Version
	@Column(name = "version")
	private Long version;

	@Column(name = "nombre", length = 100, nullable = false)
	private String nombre;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.REFRESH }, optional = false)
	@JoinColumn(name = "codestado", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "herramienta_codestado_fk"), nullable = false)
	private Estado estado;

	@ManyToMany(mappedBy = "herramientas", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Proceso> procesos;

	@Transient
	private FormateadorString formater = new FormateadorString();

	public Herramienta() {
		super();
		procesos = new HashSet<>();
		estado = new Estado(EstadoStr.ALTA);
	}

	public Herramienta(String nombre) {
		this();
		this.nombre = nombre;
	}

	public Long getId() {
		return codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Set<Proceso> getProcesos() {
		procesos.size();
		return procesos;
	}

	public void darDeBaja() {
		this.setEstado(new Estado(EstadoStr.BAJA));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
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
		Herramienta other = (Herramienta) obj;
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
		if(estado == null){
			if(other.estado != null){
				return false;
			}
		}
		else if(!estado.equals(other.estado)){
			return false;
		}
		if(nombre == null){
			if(other.nombre != null){
				return false;
			}
		}
		else if(!nombre.equals(other.nombre)){
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
		return formater.primeraMayuscula(this.getNombre());
	}
}
