/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.filtros.implementacion;

import java.util.ArrayList;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.query.Query;

import proy.datos.clases.EstadoTareaStr;
import proy.datos.filtros.Filtro;

public class FiltroFechaFinalizadaTarea extends Filtro<Date> {

	private String nombreEntidad = "a";
	private String consulta = "";
	private String namedQuery = "";
	private EstadoTareaStr noEstado;
	private ArrayList<EstadoTareaStr> estados;

	public static class Builder {

		private FiltroFechaFinalizadaTarea filtro;

		public Builder() {
			super();
			filtro = new FiltroFechaFinalizadaTarea();
		}

		public Builder nombreEntidad(String nombreEntidad) {
			filtro.nombreEntidad = nombreEntidad;
			return this;
		}

		public Builder noEstado(EstadoTareaStr noEstado) {
			filtro.noEstado = noEstado;
			return this;
		}

		public Builder estado(EstadoTareaStr estado) {
			if(estado != null){
				filtro.estados = new ArrayList<>();
				filtro.estados.add(estado);
			}
			return this;
		}

		public Builder estados(ArrayList<EstadoTareaStr> estados) {
			if(estados != null && !estados.isEmpty()){
				filtro.estados = estados;
			}
			return this;
		}

		public FiltroFechaFinalizadaTarea build() {
			filtro.setConsulta();
			filtro.setNamedQuery();
			return filtro;
		}
	}

	private FiltroFechaFinalizadaTarea() {
		super(Date.class);
	}

	private void setConsulta() {
		consulta = this.getSelect() + this.getFrom() + this.getWhere() + this.getGroupBy() + this.getHaving() + this.getOrderBy();
	}

	private void setNamedQuery() {
		if(this.noEstado != null){
			return;
		}
		if(this.estados != null){
			return;
		}
		namedQuery = "listarTareas";
	}

	private String getSelect() {
		String select = "SELECT DISTINCT " + this.nombreEntidad + ".fechaHoraFin ";
		return select;
	}

	private String getFrom() {
		String from = " FROM Tarea " + this.nombreEntidad;
		return from;
	}

	private String getWhere() {
		String where =
				((this.noEstado != null) ? (this.nombreEntidad + ".estado.nombre != :nEs AND ") : ("")) +
						((this.estados != null) ? (this.nombreEntidad + ".estado.nombre in (:ets) AND ") : (""));

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
		String orderBy = " ORDER BY " + this.nombreEntidad + ".fechaHoraFin DESC ";
		return orderBy;
	}

	@Override
	public Query<Date> setParametros(Query<Date> query) {
		if(noEstado != null){
			query.setParameter("nEs", noEstado);
		}
		if(estados != null){
			query.setParameterList("ets", estados);
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
