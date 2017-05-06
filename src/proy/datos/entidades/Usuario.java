/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import proy.comun.FormateadorString;
import proy.datos.clases.EstadoStr;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "usuario")
public abstract class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "codigo")
	private Long codigo;

	@Version
	@Column(name = "version")
	private Long version;

	@Column(name = "nombre", length = 100, nullable = false)
	private String nombre;

	@Column(name = "apellido", length = 100, nullable = false)
	private String apellido;

	@Column(name = "dni", length = 10, nullable = false)
	private String dni;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "codestado", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "usuario_codestado_fk"), nullable = false)
	private Estado estado;

	@Transient
	private FormateadorString formater = new FormateadorString();

	public Usuario() {
		super();
		estado = new Estado(EstadoStr.ALTA);
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

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getDNI() {
		return dni;
	}

	public void setDNI(String dni) {
		this.dni = dni;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
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
		Usuario other = (Usuario) obj;
		if(codigo != null && codigo.equals(other.codigo)){
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return formater.nombrePropio(this.getNombre() + " " + this.getApellido());
	}
}
