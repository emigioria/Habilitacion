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
import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Maquina;
import proy.datos.entidades.Operario;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.datos.entidades.Proceso;
import proy.datos.filtros.Filtro;

public class FiltroFechaFinalizadaTarea extends Filtro<Date> {

	private String nombreEntidad = "a";
	private String consulta = "";
	private String namedQuery = "";
	private EstadoTareaStr noEstado;
	private ArrayList<EstadoTareaStr> estados;
	private ArrayList<Herramienta> herramientas;
	private Maquina maquina;
	private Operario operario;
	private Date fechaPlanificadaInicio;
	private Date fechaPlanificadaFin;
	private ArrayList<Parte> partes;
	private ArrayList<Pieza> piezas;
	private Proceso proceso;

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

		public Builder maquina(Maquina maquina) {
			filtro.maquina = maquina;
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
		if(this.maquina != null){
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
		String select = "SELECT DISTINCT " + this.nombreEntidad + ".fechaHoraFin ";
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
		else if(this.partes != null){
			from = " FROM Tarea " + this.nombreEntidad + " left join " + this.nombreEntidad + ".proceso proc";
		}
		else{
			from = " FROM Tarea " + this.nombreEntidad;
		}
		return from;
	}

	private String getWhere() {
		String where =
				((this.noEstado != null) ? (this.nombreEntidad + ".estado.nombre != :nEs AND ") : ("")) +
						((this.estados != null) ? (this.nombreEntidad + ".estado.nombre in (:ets) AND ") : ("")) +
						((this.herramientas != null) ? ("herr in (:hes) AND ") : ("")) +
						((this.operario != null) ? (this.nombreEntidad + ".operario = :ope AND ") : ("")) +
						((this.fechaPlanificadaInicio != null) ? (this.nombreEntidad + ".fechaPlanificada >= :fpi AND ") : ("")) +
						((this.fechaPlanificadaFin != null) ? (this.nombreEntidad + ".fechaPlanificada <= :fpf AND ") : ("")) +
						((this.maquina != null) ? (this.nombreEntidad + ".proceso.parte.maquina = :maq AND ") : ("")) +
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
		if(herramientas != null){
			query.setParameterList("hes", herramientas);
		}
		if(operario != null){
			query.setParameter("ope", operario);
		}
		if(fechaPlanificadaInicio != null){
			query.setParameter("fpi", fechaPlanificadaInicio);
		}
		if(fechaPlanificadaFin != null){
			query.setParameter("fpf", fechaPlanificadaFin);
		}
		if(maquina != null){
			query.setParameter("maq", maquina);
		}
		if(partes != null){
			query.setParameterList("prs", partes);
		}
		if(piezas != null){
			query.setParameterList("pis", piezas);
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
		if(maquina != null){
			session.update(maquina);
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
