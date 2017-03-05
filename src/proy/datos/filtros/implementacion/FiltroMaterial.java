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
import proy.datos.entidades.Material;
import proy.datos.filtros.Filtro;

public class FiltroMaterial extends Filtro<Material> {

	private String consulta = "";
	private String namedQuery = "";
	private EstadoStr estado;
	private ArrayList<Material> materiales;
	private ArrayList<String> nombres;

	public static class Builder {

		private String nombreEntidad = "a";
		private ArrayList<Material> materiales;
		private Boolean conPiezas;
		private EstadoStr estado = EstadoStr.ALTA;
		private ArrayList<String> nombres;

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

		public Builder conPiezas() {
			this.conPiezas = true;
			return this;
		}

		public Builder nombres(ArrayList<String> nombres) {
			this.nombres = nombres;
			return this;
		}

		public FiltroMaterial build() {
			return new FiltroMaterial(this);
		}
	}

	private FiltroMaterial(Builder builder) {
		super(Material.class);
		this.materiales = builder.materiales;
		this.estado = builder.estado;
		this.nombres = builder.nombres;

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
		if(builder.conPiezas != null){
			return;
		}
		if(builder.nombres != null){
			return;
		}
		namedQuery = "listarMateriales";
	}

	private String getSelect(Builder builder) {
		String select;
		if(builder.conPiezas != null && builder.conPiezas){
			select = "SELECT DISTINCT " + builder.nombreEntidad;
		}
		else{
			select = "SELECT " + builder.nombreEntidad;
		}
		return select;
	}

	private String getFrom(Builder builder) {
		String from;
		if(builder.conPiezas != null && builder.conPiezas){
			from = " FROM Material " + builder.nombreEntidad + ", Pieza piez ";
		}
		else{
			from = " FROM Material " + builder.nombreEntidad;
		}
		return from;
	}

	private String getWhere(Builder builder) {
		String where =
				((builder.estado != null) ? (builder.nombreEntidad + ".estado.nombre = :est AND ") : (""))
						+ ((builder.materiales != null) ? (builder.nombreEntidad + " in (:mts) AND ") : (""))
						+ ((builder.conPiezas != null) ? ((builder.conPiezas) ? (builder.nombreEntidad + " = piez.material AND ") : ("")) : (""))
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
		if(materiales != null){
			query.setParameterList("mts", materiales);
		}
		if(estado != null){
			query.setParameter("est", estado);
		}
		if(nombres != null){
			query.setParameterList("nms", nombres);
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