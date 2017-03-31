/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
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

	private String nombreEntidad = "a";
	private String consulta = "";
	private String namedQuery = "";
	private String nombre;

	public static class Builder {

		private FiltroMaquina filtro;

		public Builder() {
			super();
			filtro = new FiltroMaquina();
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

		public FiltroMaquina build() {
			filtro.setConsulta();
			filtro.setNamedQuery();
			return filtro;
		}
	}

	private FiltroMaquina() {
		super(Maquina.class);
	}

	private void setConsulta() {
		consulta = this.getSelect() + this.getFrom() + this.getWhere() + this.getGroupBy() + this.getHaving() + this.getOrderBy();
	}

	private void setNamedQuery() {
		if(nombre != null){
			return;
		}
		namedQuery = "listarMaquinas";
	}

	private String getSelect() {
		String select = "SELECT " + this.nombreEntidad;
		return select;
	}

	private String getFrom() {
		String from = " FROM Maquina " + this.nombreEntidad;
		return from;
	}

	private String getWhere() {
		String where = ((this.nombre != null) ? (this.nombreEntidad + ".nombre LIKE :nom AND ") : (""));

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
		String orderBy = " ORDER BY " + this.nombreEntidad + ".nombre ";
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
