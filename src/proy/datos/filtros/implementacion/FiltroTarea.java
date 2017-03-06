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

	private String nombreEntidad = "a";
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

		private FiltroTarea filtro;

		public Builder() {
			super();
			filtro = new FiltroTarea();
		}

		public Builder nombreEntidad(String nombreEntidad) {
			filtro.nombreEntidad = nombreEntidad;
			return this;
		}

		public Builder herramienta(Herramienta herramienta) {
			if(herramienta != null){
				filtro.herramientas = new ArrayList<>();
				filtro.herramientas.add(herramienta);
			}
			return this;
		}

		public Builder herramientas(ArrayList<Herramienta> herramientas) {
			if(herramientas != null && !herramientas.isEmpty()){
				filtro.herramientas = herramientas;
			}
			return this;
		}

		public Builder noEstado(EstadoTareaStr noEstado) {
			filtro.noEstado = noEstado;
			return this;
		}

		public Builder estado(EstadoTareaStr estado) {
			filtro.estado = estado;
			return this;
		}

		public Builder operario(Operario operario) {
			filtro.operario = operario;
			return this;
		}

		public Builder fechaPlanificadaInicio(Date fechaPlanificadaInicio) {
			filtro.fechaPlanificadaInicio = fechaPlanificadaInicio;
			return this;
		}

		public Builder fechaPlanificadaFin(Date fechaPlanificadaFin) {
			filtro.fechaPlanificadaFin = fechaPlanificadaFin;
			return this;
		}

		public Builder parte(Parte parte) {
			if(parte != null){
				filtro.partes = new ArrayList<>();
				filtro.partes.add(parte);
			}
			return this;
		}

		public Builder partes(ArrayList<Parte> partes) {
			if(partes != null && !partes.isEmpty()){
				filtro.partes = partes;
			}
			return this;
		}

		public Builder pieza(Pieza pieza) {
			if(pieza != null){
				filtro.piezas = new ArrayList<>();
				filtro.piezas.add(pieza);
			}
			return this;
		}

		public Builder piezas(ArrayList<Pieza> piezas) {
			if(piezas != null && !piezas.isEmpty()){
				filtro.piezas = piezas;
			}
			return this;
		}

		public Builder proceso(Proceso proceso) {
			filtro.proceso = proceso;
			return this;
		}

		public FiltroTarea build() {
			filtro.setConsulta();
			filtro.setNamedQuery();
			return filtro;
		}
	}

	private FiltroTarea() {
		super(Tarea.class);
	}

	private void setConsulta() {
		consulta = this.getSelect() + this.getFrom() + this.getWhere() + this.getGroupBy() + this.getHaving() + this.getOrderBy();
	}

	private void setNamedQuery() {
		if(this.noEstado != null){
			return;
		}
		if(this.estado != null){
			return;
		}
		if(this.herramientas != null){
			return;
		}
		if(this.operario != null){
			return;
		}
		if(this.fechaPlanificadaInicio != null){
			return;
		}
		if(this.fechaPlanificadaFin != null){
			return;
		}
		if(this.partes != null){
			return;
		}
		if(this.piezas != null){
			return;
		}
		if(this.proceso != null){
			return;
		}
		namedQuery = "listarTareas";
	}

	private String getSelect() {
		String select;
		if(this.herramientas != null || this.partes != null || this.piezas != null){
			select = "SELECT DISTINCT " + this.nombreEntidad;
		}
		else{
			select = "SELECT " + this.nombreEntidad;
		}
		return select;
	}

	private String getFrom() {
		String from;
		if(this.herramientas != null && this.piezas != null){
			from = " FROM Tarea " + this.nombreEntidad + " left join " + this.nombreEntidad + ".proceso proc left join proc.herramientas herr, Pieza pies";
		}
		else if(this.herramientas != null){
			from = " FROM Tarea " + this.nombreEntidad + " left join " + this.nombreEntidad + ".proceso proc left join proc.herramientas herr";
		}
		else if(this.piezas != null){
			from = " FROM Tarea " + this.nombreEntidad + " left join " + this.nombreEntidad + ".proceso proc left join proc.piezas pies";
		}
		else{
			from = " FROM Tarea " + this.nombreEntidad;
		}
		return from;
	}

	private String getWhere() {
		String where =
				((this.noEstado != null) ? (this.nombreEntidad + ".estado.nombre != :nEs AND ") : ("")) +
						((this.estado != null) ? (this.nombreEntidad + ".estado.nombre = :est AND ") : ("")) +
						((this.herramientas != null) ? ("herr in (:hes) AND ") : ("")) +
						((this.operario != null) ? (this.nombreEntidad + ".operario = :op AND ") : ("")) +
						((this.fechaPlanificadaInicio != null) ? (this.nombreEntidad + ".fechaPlanificada >= :fpi AND ") : ("")) +
						((this.fechaPlanificadaFin != null) ? (this.nombreEntidad + ".fechaPlanificada <= :fpf AND ") : ("")) +
						((this.partes != null) ? ("proc.parte in (:prs) AND ") : ("")) +
						((this.piezas != null) ? ("pies in (:pis) AND ") : ("")) +
						((this.proceso != null) ? (this.nombreEntidad + ".proceso = :pro AND ") : (""));

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
		String orderBy = " ORDER BY " + this.nombreEntidad + ".fechaPlanificada ASC";
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
