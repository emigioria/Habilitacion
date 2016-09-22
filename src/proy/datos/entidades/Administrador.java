/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@PrimaryKeyJoinColumn(name = "codusuario", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "administrador_codusuario_fk"))
@Table(name = "administrador")
public class Administrador extends Usuario {

	@Column(name = "contrasenia", length = 100, nullable = false)
	private String contrasenia;

	@Column(name = "sal", length = 100, nullable = false)
	private String sal;

	public Administrador() {
		super();
	}

	public Administrador(String nombre, String apellido, String contrasenia, String sal) {
		super(nombre, apellido);
		this.contrasenia = contrasenia;
		this.sal = sal;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public String getSal() {
		return sal;
	}

	public void setSal(String sal) {
		this.sal = sal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((contrasenia == null) ? 0 : contrasenia.hashCode());
		result = prime * result + ((sal == null) ? 0 : sal.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		if(!super.equals(obj)){
			return false;
		}
		if(getClass() != obj.getClass()){
			return false;
		}
		Administrador other = (Administrador) obj;
		if(contrasenia == null){
			if(other.contrasenia != null){
				return false;
			}
		}
		else if(!contrasenia.equals(other.contrasenia)){
			return false;
		}
		if(sal == null){
			if(other.sal != null){
				return false;
			}
		}
		else if(!sal.equals(other.sal)){
			return false;
		}
		return true;
	}

}
