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
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.datos.entidades.Proceso;
import proy.datos.servicios.Filtro;

public class FiltroTarea extends Filtro {

	private String consulta = "";
	private String namedQuery = "";
	private EstadoTareaStr noEstado;
	private EstadoTareaStr estado;
	private Herramienta herramienta;
	private Operario operario;
	private Date fechaPlanificadaInicio;
	private Date fechaPlanificadaFin;
	private Parte parte;
	private Pieza pieza;
	private Proceso proceso;

	public static class Builder {

		private String nombreEntidad = "a";
		private EstadoTareaStr noEstado;
		private EstadoTareaStr estado;
		private Herramienta herramienta;
		private Operario operario;
		private Date fechaPlanificadaInicio;
		private Date fechaPlanificadaFin;
		private Parte parte;
		private Pieza pieza;
		private Proceso proceso;

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

		public Builder estado(EstadoTareaStr estado) {
			this.estado = estado;
			return this;
		}

		public Builder operario(Operario operario) {
			this.operario = operario;
			return this;
		}

		public Builder fechaPlanificadaInicio(Date fechaPlanificadaInicio) {
			this.fechaPlanificadaInicio = fechaPlanificadaInicio;
			return this;
		}

		public Builder fechaPlanificadaFin(Date fechaPlanificadaFin) {
			this.fechaPlanificadaFin = fechaPlanificadaFin;
			return this;
		}

		public Builder parte(Parte parte) {
			this.parte = parte;
			return this;
		}

		public Builder pieza(Pieza pieza) {
			this.pieza = pieza;
			return this;
		}

		public Builder proces(Proceso proceso) {
			this.proceso = proceso;
			return this;
		}

		public FiltroTarea build() {
			return new FiltroTarea(this);
		}
	}

	private FiltroTarea(Builder builder) {
		this.noEstado = builder.noEstado;
		this.estado = builder.estado;
		this.herramienta = builder.herramienta;
		this.operario = builder.operario;
		this.fechaPlanificadaInicio = builder.fechaPlanificadaInicio;
		this.fechaPlanificadaFin = builder.fechaPlanificadaFin;
		this.parte = builder.parte;
		this.pieza = builder.pieza;
		this.proceso = builder.proceso;

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
		if(builder.estado != null){
			return;
		}
		if(builder.herramienta != null){
			return;
		}
		if(builder.operario != null){
			return;
		}
		if(builder.fechaPlanificadaInicio != null){
			return;
		}
		if(builder.fechaPlanificadaFin != null){
			return;
		}
		if(builder.parte != null){
			return;
		}
		if(builder.pieza != null){
			return;
		}
		if(builder.proceso != null){
			return;
		}
		namedQuery = "listarTareas";
	}

	private String getSelect(Builder builder) {
		String select;
		if(builder.herramienta != null || builder.parte != null || builder.pieza != null){
			select = "SELECT DISTINCT " + builder.nombreEntidad;
		}
		else{
			select = "SELECT " + builder.nombreEntidad;
		}
		return select;
	}

	private String getFrom(Builder builder) {
		String from;
		if(builder.herramienta != null && builder.pieza != null){
			from = " FROM Tarea " + builder.nombreEntidad + " left join " + builder.nombreEntidad + ".proceso proc left join proc.herramientas herr, Pieza pies";
		}
		else if(builder.herramienta != null){
			from = " FROM Tarea " + builder.nombreEntidad + " left join " + builder.nombreEntidad + ".proceso proc left join proc.herramientas herr";
		}
		else if(builder.pieza != null){
			from = " FROM Tarea " + builder.nombreEntidad + " left join " + builder.nombreEntidad + ".proceso proc left join proc.piezas pies";
		}
		else{
			from = " FROM Tarea " + builder.nombreEntidad;
		}
		return from;
	}

	private String getWhere(Builder builder) {
		String where =
				((builder.noEstado != null) ? (builder.nombreEntidad + ".estado.nombre != :nEs AND ") : ("")) +
						((builder.estado != null) ? (builder.nombreEntidad + ".estado.nombre = :est AND ") : ("")) +
						((builder.herramienta != null) ? ("herr = :her AND ") : ("")) +
						((builder.operario != null) ? (builder.nombreEntidad + ".operario = :op AND ") : ("")) +
						((builder.fechaPlanificadaInicio != null) ? (builder.nombreEntidad + ".fechaPlanificada >= :fpi AND ") : ("")) +
						((builder.fechaPlanificadaFin != null) ? (builder.nombreEntidad + ".fechaPlanificada <= :fpf AND ") : ("")) +
						((builder.parte != null) ? ("proc.parte = :par AND ") : ("")) +
						((builder.pieza != null) ? ("pies = :pie AND ") : ("")) +
						((builder.proceso != null) ? (builder.nombreEntidad + ".proceso = :pro AND ") : (""));

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
		if(estado != null){
			query.setParameter("est", estado);
		}
		if(herramienta != null){
			query.setParameter("her", herramienta);
		}
		if(operario != null){
			query.setParameter("op", operario);
		}
		if(fechaPlanificadaInicio != null){
			query.setParameter("fpi", fechaPlanificadaInicio);
		}
		if(fechaPlanificadaFin != null){
			query.setParameter("fpf", fechaPlanificadaFin);
		}
		if(parte != null){
			query.setParameter("par", parte);
		}
		if(pieza != null){
			query.setParameter("pie", pieza);
		}
		if(proceso != null){
			query.setParameter("pro", proceso);
		}
		return query;
	}

	@Override
	public void updateParametros(Session session) {
		if(herramienta != null){
			session.update(herramienta);
		}
		if(parte != null){
			session.update(parte);
		}
		if(pieza != null){
			session.update(pieza);
		}
		if(proceso != null){
			session.update(proceso);
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
