/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.filtros;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;

import proy.datos.clases.EstadoStr;
import proy.datos.entidades.Proceso;
import proy.datos.servicios.Filtro;

public class FiltroHerramienta extends Filtro {

	private String consulta = "";
	private String namedQuery = "";
	private EstadoStr estado;
	private String nombre;
	private ArrayList<Proceso> procesos;

	public static class Builder {

		private EstadoStr estado = EstadoStr.ALTA;
		private String nombreEntidad = "h";
		private String nombre;
		private ArrayList<Proceso> procesos;

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

		public Builder procesos(ArrayList<Proceso> procesos) {
			if(procesos != null && !procesos.isEmpty()){
				this.procesos = procesos;
			}
			return this;
		}

		public FiltroHerramienta build() {
			return new FiltroHerramienta(this);
		}

	}

	private FiltroHerramienta(Builder builder) {
		this.nombre = builder.nombre;
		this.estado = builder.estado;
		this.procesos = builder.procesos;

		setConsulta(builder);
		setNamedQuery(builder);
	}

	private void setConsulta(Builder builder) {
		consulta = this.getSelect(builder) + this.getFrom(builder) + this.getWhere(builder) + this.getGroupBy(builder) + this.getHaving(builder) + this.getOrderBy(builder);
	}

	private void setNamedQuery(Builder builder) {
		if(builder.estado != EstadoStr.ALTA){
			return;
		}
		if(builder.nombre != null){
			return;
		}
		if(builder.procesos != null){
			return;
		}
		namedQuery = "listarHerramientas";
	}

	private String getSelect(Builder builder) {
		String select = "SELECT " + builder.nombreEntidad;
		return select;
	}

	private String getFrom(Builder builder) {
		String from;
		if(procesos != null){
			from = " FROM Herramienta " + builder.nombreEntidad + " inner join " + builder.nombreEntidad + ".procesos proc";
		}
		else{
			from = " FROM Herramienta " + builder.nombreEntidad;
		}
		return from;
	}

	private String getWhere(Builder builder) {
		String where =
				((builder.nombre != null) ? (builder.nombreEntidad + ".nombre LIKE :nom AND ") : (""))
						+ ((builder.estado != null) ? (builder.nombreEntidad + ".estado.nombre = :est AND ") : (""))
						+ ((builder.procesos != null) ? ("proc in :prs AND ") : (""));

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
		if(nombre != null){
			query.setParameter("nom", "%" + nombre + "%");
		}
		if(procesos != null){
			query.setParameter("prs", procesos);
		}
		return query;
	}

	@Override
	public void updateParametros(Session session) {
		if(procesos != null){
			for(Proceso proceso: procesos){
				session.update(proceso);
			}
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
