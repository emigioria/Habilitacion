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
import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.datos.entidades.Proceso;
import proy.datos.filtros.Filtro;

public class FiltroProceso extends Filtro<Proceso> {

	private String nombreEntidad = "a";
	private String consulta = "";
	private String namedQuery = "";
	private EstadoStr estado = EstadoStr.ALTA;
	private Parte parte;
	private ArrayList<Pieza> piezas;
	private ArrayList<Herramienta> herramientas;

	public static class Builder {

		private FiltroProceso filtro;

		public Builder() {
			super();
			filtro = new FiltroProceso();
		}

		public Builder nombreEntidad(String nombreEntidad) {
			filtro.nombreEntidad = nombreEntidad;
			return this;
		}

		public Builder parte(Parte parte) {
			filtro.parte = parte;
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

		public FiltroProceso build() {
			filtro.setConsulta();
			filtro.setNamedQuery();
			return filtro;
		}
	}

	private FiltroProceso() {
		super(Proceso.class);
	}

	private void setConsulta() {
		consulta = this.getSelect() + this.getFrom() + this.getWhere() + this.getGroupBy() + this.getHaving() + this.getOrderBy();
	}

	private void setNamedQuery() {
		if(this.estado != EstadoStr.ALTA){
			return;
		}
		if(this.parte != null){
			return;
		}
		if(this.piezas != null){
			return;
		}
		if(this.herramientas != null){
			return;
		}
		namedQuery = "listarProcesos";
	}

	private String getSelect() {
		String select;
		if(this.piezas != null && herramientas != null){
			select = "SELECT DISTINCT" + this.nombreEntidad;
		}
		else{
			select = "SELECT " + this.nombreEntidad;
		}
		return select;
	}

	private String getFrom() {
		String from;
		if(this.piezas != null && herramientas != null){
			from = " FROM Proceso " + this.nombreEntidad + " left join " + this.nombreEntidad + ".piezas piez, " + this.nombreEntidad + ".herramientas herr";
		}
		else if(this.piezas != null){
			from = " FROM Proceso " + this.nombreEntidad + " inner join " + this.nombreEntidad + ".piezas piez";
		}
		else if(herramientas != null){
			from = " FROM Proceso " + this.nombreEntidad + " inner join " + this.nombreEntidad + ".herramientas herr";
		}
		else{
			from = " FROM Proceso " + this.nombreEntidad;
		}
		return from;
	}

	private String getWhere() {
		String where =
				((this.estado != null) ? (this.nombreEntidad + ".estado.nombre = :est AND ") : (""))
						+ ((this.parte != null) ? (this.nombreEntidad + ".parte = :par AND ") : (""))
						+ ((this.piezas != null) ? ("piez in (:pzs) AND ") : (""))
						+ ((this.herramientas != null) ? ("herr in (:hes) AND ") : (""));

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
		String orderBy = " ORDER BY " + this.nombreEntidad + ".descripcion , " + this.nombreEntidad + ".tipo";
		return orderBy;
	}

	@Override
	public Query setParametros(Query query) {
		if(estado != null){
			query.setParameter("est", estado);
		}
		if(parte != null){
			query.setParameter("par", parte);
		}
		if(piezas != null){
			query.setParameterList("pzs", piezas);
		}
		if(herramientas != null){
			query.setParameterList("hes", herramientas);
		}
		return query;
	}

	@Override
	public void updateParametros(Session session) {
		if(parte != null){
			session.update(parte);
		}
		if(piezas != null){
			for(Pieza pieza: piezas){
				session.update(pieza);
			}
		}
		if(herramientas != null){
			for(Herramienta herramienta: herramientas){
				session.update(herramienta);
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
