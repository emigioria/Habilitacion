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
import proy.datos.entidades.Administrador;
import proy.datos.filtros.Filtro;

public class FiltroAdministrador extends Filtro<Administrador> {

	private String nombreEntidad = "a";
	private String consulta = "";
	private String namedQuery = "";
	private String dni;
	private EstadoStr estado = EstadoStr.ALTA;

	public static class Builder {

		private FiltroAdministrador filtro;

		public Builder() {
			super();
			filtro = new FiltroAdministrador();
		}

		public Builder nombreEntidad(String nombreEntidad) {
			filtro.nombreEntidad = nombreEntidad;
			return this;
		}

		public Builder dni(String dni) {
			if(dni != null && !dni.isEmpty()){
				filtro.dni = dni;
			}
			return this;
		}

		public FiltroAdministrador build() {
			filtro.setConsulta();
			filtro.setNamedQuery();
			return filtro;
		}
	}

	private FiltroAdministrador() {
		super(Administrador.class);
	}

	private void setConsulta() {
		consulta = this.getSelect() + this.getFrom() + this.getWhere() + this.getGroupBy() + this.getHaving() + this.getOrderBy();
	}

	private void setNamedQuery() {
		if(this.dni != null){
			return;
		}
		if(this.estado != EstadoStr.ALTA){
			return;
		}
		namedQuery = "listarAdministradores";
	}

	private String getSelect() {
		String select = "SELECT " + this.nombreEntidad;
		return select;
	}

	private String getFrom() {
		String from = " FROM Administrador " + this.nombreEntidad;
		return from;
	}

	private String getWhere() {
		String where =
				((this.dni != null) ? (this.nombreEntidad + ".dni = :dni AND ") : ("")) +
						((this.estado != null) ? (this.nombreEntidad + ".estado.nombre = :est AND ") : (""));

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
		String orderBy = " ORDER BY " + this.nombreEntidad + ".dni ASC";
		return orderBy;
	}

	@Override
	public Query<Administrador> setParametros(Query<Administrador> query) {
		if(dni != null){
			query.setParameter("dni", dni);
		}
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
