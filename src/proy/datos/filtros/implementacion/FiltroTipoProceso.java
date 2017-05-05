/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.filtros.implementacion;

import org.hibernate.Session;
import org.hibernate.query.Query;

import proy.datos.clases.EstadoStr;
import proy.datos.filtros.Filtro;

public class FiltroTipoProceso extends Filtro<String> {

	private String nombreEntidad = "a";
	private String consulta = "";
	private String namedQuery = "";
	private EstadoStr estado = EstadoStr.ALTA;

	public static class Builder {

		private FiltroTipoProceso filtro;

		public Builder() {
			super();
			filtro = new FiltroTipoProceso();
		}

		public Builder nombreEntidad(String nombreEntidad) {
			filtro.nombreEntidad = nombreEntidad;
			return this;
		}

		public FiltroTipoProceso build() {
			filtro.setConsulta();
			filtro.setNamedQuery();
			return filtro;
		}
	}

	private FiltroTipoProceso() {
		super(String.class);
	}

	private void setConsulta() {
		consulta = this.getSelect() + this.getFrom() + this.getWhere() + this.getGroupBy() + this.getHaving() + this.getOrderBy();
	}

	private void setNamedQuery() {
		if(this.estado != EstadoStr.ALTA){
			return;
		}
		namedQuery = "listarTiposProcesos";
	}

	private String getSelect() {
		String select = "SELECT DISTINCT " + this.nombreEntidad + ".tipo ";
		return select;
	}

	private String getFrom() {
		String from = " FROM Proceso " + this.nombreEntidad;
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
		String orderBy = " ORDER BY " + this.nombreEntidad + ".tipo ASC";
		return orderBy;
	}

	@Override
	public Query<String> setParametros(Query<String> query) {
		if(estado != null){
			query.setParameter("est", estado);
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
