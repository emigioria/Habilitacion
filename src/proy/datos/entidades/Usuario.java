/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
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

	@Column(name = "dni", length = 10, nullable = false, unique = true)
	private String dni;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "codestado", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "usuario_codestado_fk"), nullable = false)
	private Estado estado;

	public Usuario() {
		super();
		estado = new Estado(EstadoStr.ALTA);
	}

	public Usuario(String nombre, String apellido, String dni) {
		this();
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apellido == null) ? 0 : apellido.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((dni == null) ? 0 : dni.hashCode());
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
		Usuario other = (Usuario) obj;
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
		if(apellido == null){
			if(other.apellido != null){
				return false;
			}
		}
		else if(!apellido.equals(other.apellido)){
			return false;
		}
		if(dni == null){
			if(other.dni != null){
				return false;
			}
		}
		else if(!dni.equals(other.dni)){
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
		return FormateadorString.primeraMayuscula(this.getNombre()) + " " + FormateadorString.primeraMayuscula(this.getApellido());
	}
}
