/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.filtros.implementacion;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.query.Query;

import proy.datos.entidades.Comentario;
import proy.datos.entidades.Operario;
import proy.datos.filtros.Filtro;

public class FiltroComentario extends Filtro<Comentario> {

	private String nombreEntidad = "c";
	private String consulta = "";
	private String namedQuery = "";
	private Operario operario;
	private Date fechaInicio;
	private Date fechaFin;

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

		public Builder operario(Operario operario) {
			filtro.operario = operario;
			return this;
		}

		public Builder fechaInicio(Date fechaInicio) {
			filtro.fechaInicio = fechaInicio;
			return this;
		}

		public Builder fechaFin(Date fechaFin) {
			filtro.fechaFin = fechaFin;
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
		if(operario != null){
			return;
		}
		if(fechaInicio != null){
			return;
		}
		if(fechaFin != null){
			return;
		}
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
		String where =
				((this.operario != null) ? (this.nombreEntidad + ".operario = :ope AND ") : (""))
						+ ((this.fechaInicio != null) ? (this.nombreEntidad + ".fechaComentario >= :fei AND ") : (""))
						+ ((this.fechaFin != null) ? (this.nombreEntidad + ".fechaComentario <= :fef AND ") : (""));

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
		String orderBy = " ORDER BY " + this.nombreEntidad + ".fechaComentario desc";
		return orderBy;
	}

	@Override
	public Query<Comentario> setParametros(Query<Comentario> query) {
		if(operario != null){
			query.setParameter("ope", operario);
		}
		if(fechaInicio != null){
			query.setParameter("fei", fechaInicio);
		}
		if(fechaFin != null){
			query.setParameter("fef", fechaFin);
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
