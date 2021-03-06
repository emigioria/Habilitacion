/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import proy.comun.FormateadorString;
import proy.datos.clases.EstadoStr;

@Entity
@Table(name = "estado")
public class Estado {

	public static final String COLUMNA_NOMBRE = "nombre";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "codigo")
	private Long codigo;

	@Version
	@Column(name = "version")
	private Long version;

	@Enumerated(EnumType.STRING)
	@Column(name = COLUMNA_NOMBRE, length = 15, nullable = false, unique = true)
	private EstadoStr nombre;

	@Transient
	private FormateadorString formater = new FormateadorString();

	private Estado() {
		super();
	}

	public Estado(EstadoStr estado) {
		this();
		this.nombre = estado;
	}

	public Long getId() {
		return codigo;
	}

	public EstadoStr getNombre() {
		return nombre;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
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
		Estado other = (Estado) obj;
		if(codigo != null && codigo.equals(other.codigo)){
			return true;
		}
		if(nombre != null && nombre.equals(other.nombre)){
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return formater.primeraMayuscula(this.getNombre().toString());
	}
}
