/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.filtros.implementacion;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;

import proy.datos.clases.EstadoStr;
import proy.datos.entidades.Operario;
import proy.datos.filtros.Filtro;

public class FiltroOperario extends Filtro<Operario> {

	private String nombreEntidad = "a";
	private String consulta = "";
	private String namedQuery = "";
	private String nombre;
	private String apellido;
	private String dni;
	private EstadoStr estado = EstadoStr.ALTA;
	public ArrayList<String> dnis;

	public static class Builder {

		private FiltroOperario filtro;

		public Builder() {
			super();
			filtro = new FiltroOperario();
		}

		public Builder nombreEntidad(String nombreEntidad) {
			filtro.nombreEntidad = nombreEntidad;
			return this;
		}

		public Builder nombre(String nombre) {
			if(nombre != null && !nombre.isEmpty()){
				filtro.nombre = nombre;
			}
			return this;
		}

		public Builder apellido(String apellido) {
			if(apellido != null && !apellido.isEmpty()){
				filtro.apellido = apellido;
			}
			return this;
		}

		public Builder dni(String dni) {
			if(dni != null && !dni.isEmpty()){
				filtro.dni = dni;
			}
			return this;
		}

		public Builder dnis(ArrayList<String> dnis) {
			if(dnis != null && !dnis.isEmpty()){
				filtro.dnis = dnis;
			}
			return this;
		}

		public Builder estado(EstadoStr estado) {
			filtro.estado = estado;
			return this;
		}

		public FiltroOperario build() {
			filtro.setConsulta();
			filtro.setNamedQuery();
			return filtro;
		}
	}

	private FiltroOperario() {
		super(Operario.class);
	}

	private void setConsulta() {
		consulta = this.getSelect() + this.getFrom() + this.getWhere() + this.getGroupBy() + this.getHaving() + this.getOrderBy();
	}

	private void setNamedQuery() {
		if(this.nombre != null){
			return;
		}
		if(this.apellido != null){
			return;
		}
		if(this.dni != null){
			return;
		}
		if(this.dnis != null){
			return;
		}
		if(this.estado != null){
			return;
		}
		namedQuery = "listarOperarios";
	}

	private String getSelect() {
		String select = "SELECT " + this.nombreEntidad;
		return select;
	}

	private String getFrom() {
		String from = " FROM Operario " + this.nombreEntidad;
		return from;
	}

	private String getWhere() {
		String where =
				((this.nombre != null) ? (this.nombreEntidad + ".nombre LIKE :nom AND ") : ("")) +
						((this.apellido != null) ? (this.nombreEntidad + ".apellido LIKE :ape AND ") : ("")) +
						((this.dni != null) ? (this.nombreEntidad + ".dni LIKE :dni AND ") : ("")) +
						((this.estado != null) ? (this.nombreEntidad + ".estado.nombre = :est AND ") : ("")) +
						((this.dnis != null) ? (this.nombreEntidad + ".dni in (:dns) AND ") : (""));

		if(!where.isEmpty()){
			where = " WHERE " + where;
			where = where.substring(0, where.length() - 4);
		}
		return where;
	}

	private String getGroupBy() {
		String groupBy = "";
		return groupBy;
	}

	private String getHaving() {
		String having = "";
		return having;
	}

	private String getOrderBy() {
		String orderBy = " ORDER BY " + this.nombreEntidad + ".nombre ASC";
		return orderBy;
	}

	@Override
	public Query setParametros(Query query) {
		if(estado != null){
			query.setParameter("est", estado);
		}
		if(nombre != null){
			query.setParameter("nom", "%" + nombre + "%");
		}
		if(apellido != null){
			query.setParameter("ape", "%" + apellido + "%");
		}
		if(dni != null){
			query.setParameter("dni", "%" + dni + "%");
		}
		if(dnis != null){
			query.setParameterList("dns", dnis);
		}
		return query;
	}

	@Override
	public void updateParametros(Session session) {

	}

	@Override
	public String getConsultaDinamica() {
		return consulta;
	}

	@Override
	public String getNamedQueryName() {
		return namedQuery;
	}
}
