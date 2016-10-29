/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.filtros;

import org.hibernate.Query;
import org.hibernate.Session;

import proy.datos.clases.EstadoStr;
import proy.datos.entidades.Maquina;
import proy.datos.servicios.Filtro;

public class FiltroParte extends Filtro {

	private String consulta = "";
	private String namedQuery = "";
	private EstadoStr estado;
	private Maquina maquina;

	public static class Builder {

		private String nombreEntidad = "a";
		private EstadoStr estado = EstadoStr.ALTA;
		private Maquina maquina;

		public Builder() {
			super();
		}

		public Builder nombreEntidad(String nombreEntidad) {
			this.nombreEntidad = nombreEntidad;
			return this;
		}

		public Builder maquina(Maquina maquina) {
			this.maquina = maquina;
			return this;
		}

		public FiltroParte build() {
			return new FiltroParte(this);
		}
	}

	private FiltroParte(Builder builder) {
		this.estado = builder.estado;
		this.maquina = builder.maquina;

		setConsulta(builder);
		setNamedQuery(builder);
	}

	private void setConsulta(Builder builder) {
		consulta = this.getSelect(builder) + this.getFrom(builder) + this.getWhere(builder) + this.getGroupBy(builder) + this.getHaving(builder) + this.getOrderBy(builder);
	}

	private void setNamedQuery(Builder builder) {
		if(estado != EstadoStr.ALTA){

		}
		if(maquina != null){
			return;
		}
		namedQuery = "listarPartes";
	}

	private String getSelect(Builder builder) {
		String select = "SELECT " + builder.nombreEntidad;
		return select;
	}

	private String getFrom(Builder builder) {
		String from = " FROM Parte " + builder.nombreEntidad;
		return from;
	}

	private String getWhere(Builder builder) {
		String where =
				((builder.estado != null) ? (builder.nombreEntidad + ".estado.nombre = :est AND ") : (""))
						+ ((builder.maquina != null) ? (builder.nombreEntidad + ".maquina = :maq AND ") : (""));

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
		if(estado != null){
			query.setParameter("est", estado);
		}
		if(maquina != null){
			query.setParameter("maq", maquina);
		}
		return query;
	}

	@Override
	public void updateParametros(Session session) {
		if(maquina != null){
			session.update(maquina);
		}
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
