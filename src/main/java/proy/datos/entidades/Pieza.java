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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import proy.comun.FormateadorString;
import proy.datos.clases.EstadoStr;

@NamedQuery(name = "listarPiezas", query = "SELECT p FROM Pieza p WHERE p.estado.nombre = :est ORDER BY p.nombre ")
@Entity
@Table(name = "pieza")
public class Pieza {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "codigo")
	private Long codigo;

	@Version
	@Column(name = "version")
	private Long version;

	@Column(name = "nombre", length = 100, nullable = false)
	private String nombre;

	@Column(name = "codigo_plano", length = 100)
	private String codigoPlano;

	@Column(name = "cantidad", nullable = false)
	private Integer cantidad;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "codestado", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "pieza_codestado_fk"), nullable = false)
	private Estado estado;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "codparte", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "pieza_codparte_fk"), nullable = false)
	private Parte parte;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "codmaterial", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "pieza_codmaterial_fk"), nullable = false)
	private Material material;

	@ManyToMany(mappedBy = "piezas", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Proceso> procesos;

	@Transient
	private FormateadorString formater = new FormateadorString();

	public Pieza() {
		super();
		procesos = new HashSet<>();
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

	public String getCodigoPlano() {
		return codigoPlano;
	}

	public void setCodigoPlano(String codigoPlano) {
		this.codigoPlano = codigoPlano;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
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

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public Set<Proceso> getProcesos() {
		procesos.size();
		return procesos;
	}

	public void darDeBaja() {
		if(!EstadoStr.BAJA.equals(this.getEstado().getNombre())){
			this.setEstado(new Estado(EstadoStr.BAJA));
		}
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
		Pieza other = (Pieza) obj;
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
