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
import proy.datos.entidades.Material;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.datos.servicios.Filtro;

public class FiltroPieza extends Filtro {

	private String consulta = "";
	private String namedQuery = "";
	private EstadoStr estado;
	private ArrayList<Material> materiales;
	private Parte parte;
	private ArrayList<Pieza> piezas;
	
	public static class Builder {

		private String nombreEntidad = "a";
		private EstadoStr estado = EstadoStr.ALTA;
		private ArrayList<Material> materiales = null;
		private Parte parte = null;
		private ArrayList<Pieza> piezas;
		private Boolean conTareas;

		public Builder() {
			super();
		}

		public Builder nombreEntidad(String nombreEntidad) {
			this.nombreEntidad = nombreEntidad;
			return this;
		}

		public Builder materiales(ArrayList<Material> materiales) {
			if(materiales != null && !materiales.isEmpty()){
				this.materiales = materiales;
			}
			return this;
		}

		public Builder parte(Parte parte) {
			this.parte = parte;
			return this;
		}
		
		public Builder piezas(ArrayList<Pieza> piezas){
			this.piezas = piezas;
			return this;
		}
		
		public Builder conTareas(){
			this.conTareas = true;
			return this;
		}

		public FiltroPieza build() {
			return new FiltroPieza(this);
		}
	}

	private FiltroPieza(Builder builder) {
		this.estado = builder.estado;
		this.materiales = builder.materiales;
		this.parte = builder.parte;
		this.piezas = builder.piezas;

		setConsulta(builder);
		setNamedQuery(builder);
	}

	private void setConsulta(Builder builder) {
		consulta = this.getSelect(builder) + this.getFrom(builder) + this.getWhere(builder) + this.getGroupBy(builder) + this.getHaving(builder) + this.getOrderBy(builder);
	}

	private void setNamedQuery(Builder builder) {
		if(builder.materiales != null){
			return;
		}
		if(builder.estado != EstadoStr.ALTA){
			return;
		}
		if(builder.parte != null){
			return;
		}
		if(builder.piezas != null){
			return;
		}
		if(builder.conTareas != null){
			return;
		}
		namedQuery = "listarPiezas";
	}

	private String getSelect(Builder builder) {
		String select;
		if(builder.conTareas != null && builder.conTareas){
			select = "SELECT DISTINCT " + builder.nombreEntidad;
		}
		else{
			select = "SELECT " + builder.nombreEntidad;
		}
		return select;
	}

	private String getFrom(Builder builder) {
		String from;
		if(builder.conTareas != null && builder.conTareas){
			from = " FROM Pieza " + builder.nombreEntidad + " inner join " + builder.nombreEntidad + ".procesos proc inner join proc.tareas tar";
		}
		else{
			from = " FROM Pieza " + builder.nombreEntidad;
		}
		return from;
	}

	private String getWhere(Builder builder) {
		String where =
				((builder.estado != null) ? (builder.nombreEntidad + ".estado.nombre = :est AND ") : (""))
						+ ((builder.materiales != null) ? (builder.nombreEntidad + ".material in :mts AND ") : (""))
						+ ((builder.parte != null) ? (builder.nombreEntidad + ".parte = :par AND ") : (""))
						+ ((builder.piezas != null) ? (builder.nombreEntidad + " in :pzs AND ") : (""));

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
		if(materiales != null){
			query.setParameterList("mts", materiales);
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
		if(materiales != null){
			for(Material material: materiales){
				session.update(material);
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
