/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.filtros.implementacion;

import org.hibernate.Query;
import org.hibernate.Session;

import proy.datos.entidades.Maquina;
import proy.datos.filtros.Filtro;

public class FiltroMaquina extends Filtro<Maquina> {

	private String consulta = "";
	private String namedQuery = "";
	private String nombre;

	public static class Builder {

		private String nombreEntidad = "a";
		private String nombre = null;

		public Builder() {
			super();
		}

		public Builder nombreEntidad(String nombreEntidad) {
			this.nombreEntidad = nombreEntidad;
			return this;
		}

		public Builder nombre(String nombre) {
			this.nombre = nombre;
			return this;
		}

		public FiltroMaquina build() {
			return new FiltroMaquina(this);
		}
	}

	private FiltroMaquina(Builder builder) {
		super(Maquina.class);
		this.nombre = builder.nombre;

		setConsulta(builder);
		setNamedQuery(builder);
	}

	private void setConsulta(Builder builder) {
		consulta = this.getSelect(builder) + this.getFrom(builder) + this.getWhere(builder) + this.getGroupBy(builder) + this.getHaving(builder) + this.getOrderBy(builder);
	}

	private void setNamedQuery(Builder builder) {
		if(nombre != null){
			return;
		}
		namedQuery = "listarMaquinas";
	}

	private String getSelect(Builder builder) {
		String select = "SELECT " + builder.nombreEntidad;
		return select;
	}

	private String getFrom(Builder builder) {
		String from = " FROM Maquina " + builder.nombreEntidad;
		return from;
	}

	private String getWhere(Builder builder) {
		String where = ((builder.nombre != null) ? (builder.nombreEntidad + ".nombre LIKE :nom AND ") : (""));

		if(!where.isEmpty()){
			where = " WHERE " + where;
			where = where.substring(0, where.length() - 4);
		}
		return where;
	}

	private String getGroupBy(Builder builder) {
		String groupBy = "";
		return groupBy;
	}

	private String getHaving(Builder builder) {
		String having = "";
		return having;
	}

	private String getOrderBy(Builder builder) {
		String orderBy = " ORDER BY " + builder.nombreEntidad + ".nombre ";
		return orderBy;
	}

	@Override
	public Query setParametros(Query query) {
		if(nombre != null){
			query.setParameter("nom", "%" + nombre + "%");
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
