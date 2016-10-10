/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.filtros;

import org.hibernate.Query;
import org.hibernate.Session;

import proy.datos.servicios.Filtro;

public class FiltroAdministrador extends Filtro {

	private String consulta = "";
	private String namedQuery = "";
	private String dni;

	public static class Builder {

		private String nombreEntidad = "a";
		private String dni;

		public Builder() {
			super();
		}

		public Builder nombreEntidad(String nombreEntidad) {
			this.nombreEntidad = nombreEntidad;
			return this;
		}

		public Builder dni(String dni) {
			this.dni = dni;
			return this;
		}

		private Boolean parametrosPorDefecto() {
			if(dni != null){
				return false;
			}
			return true;
		}

		public FiltroAdministrador build() {
			return new FiltroAdministrador(this);
		}
	}

	private FiltroAdministrador(Builder builder) {
		setConsulta(builder);
		setNamedQuery(builder);
		this.dni = builder.dni;
	}

	private void setConsulta(Builder builder) {
		consulta = this.getSelect(builder) + this.getFrom(builder) + this.getWhere(builder) + this.getGroupBy(builder) + this.getHaving(builder) + this.getOrderBy(builder);
	}

	private void setNamedQuery(Builder builder) {
		if(builder.parametrosPorDefecto()){
			namedQuery = "listarAdministradores";
		}
	}

	private String getSelect(Builder builder) {
		String select = "SELECT " + builder.nombreEntidad;
		return select;
	}

	private String getFrom(Builder builder) {
		String from = " FROM Administrador " + builder.nombreEntidad;
		return from;
	}

	private String getWhere(Builder builder) {
		String where =
				((builder.dni != null) ? (builder.nombreEntidad + ".dni = :dni AND ") : (""));

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
		String orderBy = " ORDER BY " + builder.nombreEntidad + ".dni ASC";
		return orderBy;
	}

	@Override
	public Query setParametros(Query query) {
		if(dni != null){
			query.setParameter("dni", dni);
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
