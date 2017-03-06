/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.filtros.implementacion;

import org.hibernate.Query;
import org.hibernate.Session;

import proy.datos.entidades.Comentario;
import proy.datos.filtros.Filtro;

public class FiltroComentario extends Filtro<Comentario> {

	private String nombreEntidad = "c";
	private String consulta = "";
	private String namedQuery = "";

	public static class Builder {

		private FiltroComentario filtro;

		public Builder() {
			super();
			filtro = new FiltroComentario();
		}

		public Builder nombreEntidad(String nombreEntidad) {
			filtro.nombreEntidad = nombreEntidad;
			return this;
		}

		public FiltroComentario build() {
			filtro.setConsulta();
			filtro.setNamedQuery();
			return filtro;
		}
	}

	private FiltroComentario() {
		super(Comentario.class);
	}

	private void setConsulta() {
		consulta = this.getSelect() + this.getFrom() + this.getWhere() + this.getGroupBy() + this.getHaving() + this.getOrderBy();
	}

	private void setNamedQuery() {
		namedQuery = "listarComentarios";
	}

	private String getSelect() {
		String select = "SELECT " + this.nombreEntidad;
		return select;
	}

	private String getFrom() {
		String from = " FROM Comentario " + this.nombreEntidad;
		return from;
	}

	private String getWhere() {
		String where = "";
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
		String orderBy = " ORDER BY " + this.nombreEntidad + ".fechaComentario ";
		return orderBy;
	}

	@Override
	public Query setParametros(Query query) {
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
