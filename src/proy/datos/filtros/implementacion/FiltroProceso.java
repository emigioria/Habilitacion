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

	private String consulta = "";
	private String namedQuery = "";
	private EstadoStr estado;
	private Parte parte;
	private ArrayList<Pieza> piezas;
	private ArrayList<Herramienta> herramientas;

	public static class Builder {

		private String nombreEntidad = "a";
		private EstadoStr estado = EstadoStr.ALTA;
		private Parte parte;
		private ArrayList<Pieza> piezas;
		private ArrayList<Herramienta> herramientas;

		public Builder() {
			super();
		}

		public Builder nombreEntidad(String nombreEntidad) {
			this.nombreEntidad = nombreEntidad;
			return this;
		}

		public Builder parte(Parte parte) {
			this.parte = parte;
			return this;
		}

		public Builder piezas(ArrayList<Pieza> piezas) {
			if(piezas != null && !piezas.isEmpty()){
				this.piezas = piezas;
			}
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

		public FiltroProceso build() {
			return new FiltroProceso(this);
		}
	}

	private FiltroProceso(Builder builder) {
		super(Proceso.class);
		this.estado = builder.estado;
		this.parte = builder.parte;
		this.piezas = builder.piezas;
		this.herramientas = builder.herramientas;

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
		if(builder.parte != null){
			return;
		}
		if(builder.piezas != null){
			return;
		}
		if(builder.herramientas != null){
			return;
		}
		namedQuery = "listarProcesos";
	}

	private String getSelect(Builder builder) {
		String select;
		if(builder.piezas != null && herramientas != null){
			select = "SELECT DISTINCT" + builder.nombreEntidad;
		}
		else{
			select = "SELECT " + builder.nombreEntidad;
		}
		return select;
	}

	private String getFrom(Builder builder) {
		String from;
		if(builder.piezas != null && herramientas != null){
			from = " FROM Proceso " + builder.nombreEntidad + " left join " + builder.nombreEntidad + ".piezas piez, " + builder.nombreEntidad + ".herramientas herr";
		}
		if(builder.piezas != null){
			from = " FROM Proceso " + builder.nombreEntidad + " inner join " + builder.nombreEntidad + ".piezas piez";
		}
		if(herramientas != null){
			from = " FROM Proceso " + builder.nombreEntidad + " inner join " + builder.nombreEntidad + ".herramientas herr";
		}
		else{
			from = " FROM Proceso " + builder.nombreEntidad;
		}
		return from;
	}

	private String getWhere(Builder builder) {
		String where =
				((builder.estado != null) ? (builder.nombreEntidad + ".estado.nombre = :est AND ") : (""))
						+ ((builder.parte != null) ? (builder.nombreEntidad + ".parte = :par AND ") : (""))
						+ ((builder.piezas != null) ? ("piez in (:pzs) AND ") : (""))
						+ ((builder.herramientas != null) ? ("herr in (:hes) AND ") : (""));

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
		String orderBy = " ORDER BY " + builder.nombreEntidad + ".descripcion , " + builder.nombreEntidad + ".tipo";
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
