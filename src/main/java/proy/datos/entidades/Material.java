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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import proy.comun.FormateadorString;
import proy.datos.clases.EstadoStr;

@NamedQuery(name = "listarMateriales", query = "SELECT m FROM Material m WHERE m.estado.nombre = :est ORDER BY m.nombre ")
@Entity
@Table(name = "material")
public class Material {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "codigo")
	private Long codigo;

	@Version
	@Column(name = "version")
	private Long version;

	@Column(name = "nombre", length = 100, nullable = false)
	private String nombre;

	@Column(name = "medidas", length = 100)
	private String medidas;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "codestado", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "material_codestado_fk"), nullable = false)
	private Estado estado;

	@Transient
	private FormateadorString formater = new FormateadorString();

	public Material() {
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

	public String getMedidas() {
		return medidas;
	}

	public void setMedidas(String medidas) {
		this.medidas = medidas;
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
		Material other = (Material) obj;
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
