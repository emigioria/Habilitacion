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
import proy.datos.entidades.Maquina;
import proy.datos.entidades.Parte;
import proy.datos.servicios.Filtro;

public class FiltroParte extends Filtro {

	private String consulta = "";
	private String namedQuery = "";
	private EstadoStr estado;
	private Maquina maquina;
	private ArrayList<Parte> partes;
	private ArrayList<String> nombres;
	private Boolean sinUnir = false;

	public static class Builder {

		private String nombreEntidad = "a";
		private EstadoStr estado = EstadoStr.ALTA;
		private Maquina maquina;
		private ArrayList<Parte> partes;
		private Boolean conTareas;
		private ArrayList<String> nombres;
		private Boolean sinUnir = false;

		public Builder() {
			super();
		}

		public Builder nombreEntidad(String nombreEntidad) {
			this.nombreEntidad = nombreEntidad;
			return this;
		}

		public Builder maquina(Maquina maquina) {
			this.maquina = maquina;
			return this;
		}

		public Builder partes(ArrayList<Parte> partes) {
			if(partes != null && !partes.isEmpty()){
				this.partes = partes;
			}
			return this;
		}

		public Builder conTareas() {
			this.conTareas = true;
			return this;
		}

		public Builder nombres(ArrayList<String> nombres) {
			this.nombres = nombres;
			return this;
		}

		public Builder sinUnir() {
			this.sinUnir = true;
			return this;
		}

		public FiltroParte build() {
			return new FiltroParte(this);
		}
	}

	private FiltroParte(Builder builder) {
		this.estado = builder.estado;
		this.maquina = builder.maquina;
		this.partes = builder.partes;
		this.nombres = builder.nombres;
		this.sinUnir = builder.sinUnir;

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
		if(builder.maquina != null){
			return;
		}
		if(builder.partes != null){
			return;
		}
		if(builder.conTareas != null){
			return;
		}
		if(builder.nombres != null){
			return;
		}
		namedQuery = "listarPartes";
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
			from = " FROM Parte " + builder.nombreEntidad + " inner join " + builder.nombreEntidad + ".procesos proc inner join proc.tareas tar";
		}
		else{
			from = " FROM Parte " + builder.nombreEntidad;
		}
		return from;
	}

	private String getWhere(Builder builder) {
		String where =
				((builder.estado != null) ? (builder.nombreEntidad + ".estado.nombre = :est AND ") : (""))
						+ ((builder.maquina != null) ? (builder.nombreEntidad + ".maquina = :maq AND ") : (""))
						+ ((builder.partes != null) ? (builder.nombreEntidad + " in (:pts) AND ") : (""))
						+ ((builder.nombres != null) ? (builder.nombreEntidad + ".nombre in (:nms) AND ") : (""));

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
		if(maquina != null){
			query.setParameter("maq", maquina);
		}
		if(partes != null){
			query.setParameterList("pts", partes);
		}
		if(nombres != null){
			query.setParameterList("nms", nombres);
		}
		return query;
	}

	@Override
	public void updateParametros(Session session) {
		if(sinUnir){
			return;
		}
		if(maquina != null){
			session.update(maquina);
		}
		if(partes != null){
			for(Parte parte: partes){
				session.update(parte);
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
