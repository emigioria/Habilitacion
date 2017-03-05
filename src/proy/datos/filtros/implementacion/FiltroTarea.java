/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.filtros.implementacion;

import java.util.ArrayList;
import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;

import proy.datos.clases.EstadoTareaStr;
import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Operario;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.datos.entidades.Proceso;
import proy.datos.entidades.Tarea;
import proy.datos.filtros.Filtro;

public class FiltroTarea extends Filtro<Tarea> {

	private String consulta = "";
	private String namedQuery = "";
	private EstadoTareaStr noEstado;
	private EstadoTareaStr estado;
	private ArrayList<Herramienta> herramientas;
	private Operario operario;
	private Date fechaPlanificadaInicio;
	private Date fechaPlanificadaFin;
	private ArrayList<Parte> partes;
	private ArrayList<Pieza> piezas;
	private Proceso proceso;

	public static class Builder {

		private String nombreEntidad = "a";
		private EstadoTareaStr noEstado;
		private EstadoTareaStr estado;
		private ArrayList<Herramienta> herramientas;
		private Operario operario;
		private Date fechaPlanificadaInicio;
		private Date fechaPlanificadaFin;
		private ArrayList<Parte> partes;
		private ArrayList<Pieza> piezas;
		private Proceso proceso;

		public Builder() {
			super();
		}

		public Builder nombreEntidad(String nombreEntidad) {
			this.nombreEntidad = nombreEntidad;
			return this;
		}

		public Builder herramienta(Herramienta herramienta) {
			if(herramienta != null){
				this.herramientas = new ArrayList<>();
				this.herramientas.add(herramienta);
			}
			return this;
		}

		public Builder herramientas(ArrayList<Herramienta> herramientas) {
			if(herramientas != null && !herramientas.isEmpty()){
				this.herramientas = herramientas;
			}
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
			if(parte != null){
				this.partes = new ArrayList<>();
				this.partes.add(parte);
			}
			return this;
		}

		public Builder partes(ArrayList<Parte> partes) {
			if(partes != null && !partes.isEmpty()){
				this.partes = partes;
			}
			return this;
		}

		public Builder pieza(Pieza pieza) {
			if(pieza != null){
				this.piezas = new ArrayList<>();
				this.piezas.add(pieza);
			}
			return this;
		}

		public Builder piezas(ArrayList<Pieza> piezas) {
			if(piezas != null && !piezas.isEmpty()){
				this.piezas = piezas;
			}
			return this;
		}

		public Builder proceso(Proceso proceso) {
			this.proceso = proceso;
			return this;
		}

		public FiltroTarea build() {
			return new FiltroTarea(this);
		}
	}

	private FiltroTarea(Builder builder) {
		super(Tarea.class);
		this.noEstado = builder.noEstado;
		this.estado = builder.estado;
		this.herramientas = builder.herramientas;
		this.operario = builder.operario;
		this.fechaPlanificadaInicio = builder.fechaPlanificadaInicio;
		this.fechaPlanificadaFin = builder.fechaPlanificadaFin;
		this.partes = builder.partes;
		this.piezas = builder.piezas;
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
		if(builder.herramientas != null){
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
		if(builder.partes != null){
			return;
		}
		if(builder.piezas != null){
			return;
		}
		if(builder.proceso != null){
			return;
		}
		namedQuery = "listarTareas";
	}

	private String getSelect(Builder builder) {
		String select;
		if(builder.herramientas != null || builder.partes != null || builder.piezas != null){
			select = "SELECT DISTINCT " + builder.nombreEntidad;
		}
		else{
			select = "SELECT " + builder.nombreEntidad;
		}
		return select;
	}

	private String getFrom(Builder builder) {
		String from;
		if(builder.herramientas != null && builder.piezas != null){
			from = " FROM Tarea " + builder.nombreEntidad + " left join " + builder.nombreEntidad + ".proceso proc left join proc.herramientas herr, Pieza pies";
		}
		else if(builder.herramientas != null){
			from = " FROM Tarea " + builder.nombreEntidad + " left join " + builder.nombreEntidad + ".proceso proc left join proc.herramientas herr";
		}
		else if(builder.piezas != null){
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
						((builder.herramientas != null) ? ("herr in (:hes) AND ") : ("")) +
						((builder.operario != null) ? (builder.nombreEntidad + ".operario = :op AND ") : ("")) +
						((builder.fechaPlanificadaInicio != null) ? (builder.nombreEntidad + ".fechaPlanificada >= :fpi AND ") : ("")) +
						((builder.fechaPlanificadaFin != null) ? (builder.nombreEntidad + ".fechaPlanificada <= :fpf AND ") : ("")) +
						((builder.partes != null) ? ("proc.parte in (:prs) AND ") : ("")) +
						((builder.piezas != null) ? ("pies in (:pis) AND ") : ("")) +
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
		if(herramientas != null){
			query.setParameter("hes", herramientas);
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
		if(partes != null){
			query.setParameter("pas", partes);
		}
		if(piezas != null){
			query.setParameter("pis", piezas);
		}
		if(proceso != null){
			query.setParameter("pro", proceso);
		}
		return query;
	}

	@Override
	public void updateParametros(Session session) {
		if(operario != null){
			session.update(operario);
		}
		if(herramientas != null){
			for(Herramienta herramienta: herramientas){
				session.update(herramienta);
			}
		}
		if(partes != null){
			for(Parte parte: partes){
				session.update(parte);
			}
		}
		if(piezas != null){
			for(Pieza pieza: piezas){
				session.update(pieza);
			}
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
