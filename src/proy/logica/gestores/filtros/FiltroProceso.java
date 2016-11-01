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
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.datos.servicios.Filtro;

public class FiltroProceso extends Filtro {

	private String consulta = "";
	private String namedQuery = "";
	private EstadoStr estado;
	private Parte parte;
	private ArrayList<Pieza> piezas;

	public static class Builder {

		private String nombreEntidad = "a";
		private EstadoStr estado = EstadoStr.ALTA;
		private Parte parte;
		private ArrayList<Pieza> piezas;

		public Builder() {
			super();
		}

		public Builder nombreEntidad(String nombreEntidad) {
			this.nombreEntidad = nombreEntidad;
			return this;
		}
		
		public Builder parte(Parte parte){
			this.parte = parte;
			return this;
		}
		
		public Builder piezas(ArrayList<Pieza> piezas){
			this.piezas = piezas;
			return this;
		}

		public FiltroProceso build() {
			return new FiltroProceso(this);
		}
	}

	private FiltroProceso(Builder builder) {
		this.estado = builder.estado;
		this.parte = builder.parte;
		this.piezas = builder.piezas;

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
		namedQuery = "listarProcesos";
	}

	private String getSelect(Builder builder) {
		String select = "SELECT DISTINCT" + builder.nombreEntidad;
		return select;
	}

	private String getFrom(Builder builder) {
		String from;
		if(piezas != null){
			from = " FROM Proceso " + builder.nombreEntidad + " INNER JOIN " + builder.nombreEntidad + ".piezas piez";
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
				+ ((builder.piezas != null) ? ("piez in :pzs AND ") : (""));

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
