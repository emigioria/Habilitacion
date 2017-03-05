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

	private String consulta = "";
	private String namedQuery = "";

	public static class Builder {

		private String nombreEntidad = "c";

		public Builder() {
			super();
		}

		public Builder nombreEntidad(String nombreEntidad) {
			this.nombreEntidad = nombreEntidad;
			return this;
		}

		public FiltroComentario build() {
			return new FiltroComentario(this);
		}
	}

	private FiltroComentario(Builder builder) {
		super(Comentario.class);
		setConsulta(builder);
		setNamedQuery(builder);
	}

	private void setConsulta(Builder builder) {
		consulta = this.getSelect(builder) + this.getFrom(builder) + this.getWhere(builder) + this.getGroupBy(builder) + this.getHaving(builder) + this.getOrderBy(builder);
	}

	private void setNamedQuery(Builder builder) {
		namedQuery = "listarComentarios";
	}

	private String getSelect(Builder builder) {
		String select = "SELECT " + builder.nombreEntidad;
		return select;
	}

	private String getFrom(Builder builder) {
		String from = " FROM Comentario " + builder.nombreEntidad;
		return from;
	}

	private String getWhere(Builder builder) {
		String where = "";
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
		String orderBy = " ORDER BY " + builder.nombreEntidad + ".fechaComentario ";
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