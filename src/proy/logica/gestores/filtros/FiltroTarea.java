/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.filtros;

import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;

import proy.datos.clases.EstadoTareaStr;
import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Operario;
import proy.datos.servicios.Filtro;

public class FiltroTarea extends Filtro {

	private String consulta = "";
	private String namedQuery = "";
	private EstadoTareaStr noEstado;
	private Herramienta herramienta;
	private Operario operario;
	private Date fecha;

	public static class Builder {

		private String nombreEntidad = "a";
		private EstadoTareaStr noEstado;
		private Herramienta herramienta;
		private Operario operario;
		private Date fecha;

		public Builder() {
			super();
		}

		public Builder nombreEntidad(String nombreEntidad) {
			this.nombreEntidad = nombreEntidad;
			return this;
		}

		public Builder herramienta(Herramienta herramienta) {
			this.herramienta = herramienta;
			return this;
		}

		public Builder noEstado(EstadoTareaStr noEstado) {
			this.noEstado = noEstado;
			return this;
		}

		public Builder operario(Operario operario) {
			this.operario = operario;
			return this;
		}

		public Builder fecha(Date fecha) {
			this.fecha = fecha;
			return this;
		}

		public FiltroTarea build() {
			return new FiltroTarea(this);
		}
	}

	private FiltroTarea(Builder builder) {
		this.noEstado = builder.noEstado;
		this.herramienta = builder.herramienta;
		this.operario = builder.operario;
		this.fecha = builder.fecha;

		setConsulta(builder);
		setNamedQuery(builder);
	}

	private void setConsulta(Builder builder) {
		consulta = this.getSelect(builder) + this.getFrom(builder) + this.getWhere(builder) + this.getGroupBy(builder) + this.getHaving(builder) + this.getOrderBy(builder);
	}

	private void setNamedQuery(Builder builder) {
		if(builder.noEstado != null){
			return;
		}
		if(builder.herramienta != null){
			return;
		}
		namedQuery = "listarTareas";
	}

	private String getSelect(Builder builder) {
		String select;
		if(builder.herramienta != null){
			select = "SELECT DISTINCT " + builder.nombreEntidad;
		}
		else{
			select = "SELECT " + builder.nombreEntidad;
		}
		return select;
	}

	private String getFrom(Builder builder) {
		String from;
		if(builder.herramienta != null){
			from = " FROM Tarea " + builder.nombreEntidad + " left join " + builder.nombreEntidad + ".proceso proc left join proc.herramientas herr";
		}
		else{
			from = " FROM Tarea " + builder.nombreEntidad;
		}
		return from;
	}

	private String getWhere(Builder builder) {
		String where =
				((builder.noEstado != null) ? (builder.nombreEntidad + ".estado.nombre != :nEs AND ") : ("")) +
						((builder.herramienta != null) ? ("herr = :her AND ") : ("")) +
						((builder.operario != null) ? (builder.nombreEntidad + ".operario = :op AND ") : ("")) +
						((builder.fecha != null) ? (builder.nombreEntidad + ".fecha = :fec AND ") : (""));

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
		String orderBy = " ORDER BY " + builder.nombreEntidad + ".fechaPlanificada ASC";
		return orderBy;
	}

	@Override
	public Query setParametros(Query query) {
		if(noEstado != null){
			query.setParameter("nEs", noEstado);
		}
		if(herramienta != null){
			query.setParameter("her", herramienta);
		}
		if(operario != null){
			query.setParameter("op", operario);
		}
		if(fecha != null){
			query.setParameter("fec", fecha);
		}
		return query;
	}

	@Override
	public void updateParametros(Session session) {
		session.update(herramienta);
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
