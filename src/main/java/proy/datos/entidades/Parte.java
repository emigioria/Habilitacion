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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import proy.comun.FormateadorString;
import proy.datos.clases.EstadoStr;

@NamedQuery(name = "listarPartes", query = "SELECT p FROM Parte p WHERE p.estado.nombre = :est ORDER BY p.nombre ")
@Entity
@Table(name = "parte")
public class Parte {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "codigo")
	private Long codigo;

	@Version
	@Column(name = "version")
	private Long version;

	@Column(name = "nombre", length = 100, nullable = false)
	private String nombre;

	@Column(name = "cantidad", nullable = false)
	private Integer cantidad;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "codestado", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "parte_codestado_fk"), nullable = false)
	private Estado estado;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "codmaquina", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "parte_codmaquina_fk"), nullable = false)
	private Maquina maquina;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parte", orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<Pieza> piezas;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parte", orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<Proceso> procesos;

	@Transient
	private FormateadorString formater = new FormateadorString();

	public Parte() {
		super();
		estado = new Estado(EstadoStr.ALTA);
		piezas = new HashSet<>();
		procesos = new HashSet<>();
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

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Maquina getMaquina() {
		return maquina;
	}

	public void setMaquina(Maquina maquina) {
		this.maquina = maquina;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Set<Pieza> getPiezas() {
		piezas.size();
		return piezas;
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
		Parte other = (Parte) obj;
		if(codigo != null && codigo.equals(other.codigo)){
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return formater.primeraMayuscula(this.getNombre());
	}
}
