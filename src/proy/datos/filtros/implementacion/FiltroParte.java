/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.filtros.implementacion;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;

import proy.datos.clases.EstadoStr;
import proy.datos.entidades.Maquina;
import proy.datos.entidades.Parte;
import proy.datos.filtros.Filtro;

public class FiltroParte extends Filtro<Parte> {

	private String nombreEntidad = "a";
	private Boolean conTareas;
	private String consulta = "";
	private String namedQuery = "";
	private EstadoStr estado = EstadoStr.ALTA;
	private Maquina maquina;
	private ArrayList<String> nombres;

	public static class Builder {

		private FiltroParte filtro;

		public Builder() {
			super();
			filtro = new FiltroParte();
		}

		public Builder nombreEntidad(String nombreEntidad) {
			filtro.nombreEntidad = nombreEntidad;
			return this;
		}

		public Builder maquina(Maquina maquina) {
			filtro.maquina = maquina;
			return this;
		}

		public Builder conTareas() {
			filtro.conTareas = true;
			return this;
		}

		public Builder nombres(ArrayList<String> nombres) {
			if(nombres != null && !nombres.isEmpty()){
				filtro.nombres = nombres;
			}
			return this;
		}

		public FiltroParte build() {
			filtro.setConsulta();
			filtro.setNamedQuery();
			return filtro;
		}
	}

	private FiltroParte() {
		super(Parte.class);
	}

	private void setConsulta() {
		consulta = this.getSelect() + this.getFrom() + this.getWhere() + this.getGroupBy() + this.getHaving() + this.getOrderBy();
	}

	private void setNamedQuery() {
		if(this.estado != EstadoStr.ALTA){
			return;
		}
		if(this.maquina != null){
			return;
		}
		if(this.conTareas != null){
			return;
		}
		if(this.nombres != null){
			return;
		}
		namedQuery = "listarPartes";
	}

	private String getSelect() {
		String select;
		if(this.conTareas != null && this.conTareas){
			select = "SELECT DISTINCT " + this.nombreEntidad;
		}
		else{
			select = "SELECT " + this.nombreEntidad;
		}
		return select;
	}

	private String getFrom() {
		String from;
		if(this.conTareas != null && this.conTareas){
			from = " FROM Parte " + this.nombreEntidad + " inner join " + this.nombreEntidad + ".procesos proc inner join proc.tareas tar";
		}
		else{
			from = " FROM Parte " + this.nombreEntidad;
		}
		return from;
	}

	private String getWhere() {
		String where =
				((this.estado != null) ? (this.nombreEntidad + ".estado.nombre = :est AND ") : (""))
						+ ((this.maquina != null) ? (this.nombreEntidad + ".maquina = :maq AND ") : (""))
						+ ((this.nombres != null) ? (this.nombreEntidad + ".nombre in (:nms) AND ") : (""));

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
		String orderBy = " ORDER BY " + this.nombreEntidad + ".nombre ";
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
		if(nombres != null){
			query.setParameterList("nms", nombres);
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
