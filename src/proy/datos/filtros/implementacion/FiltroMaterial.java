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

	private String nombreEntidad = "a";
	private Boolean conPiezas;
	private String consulta = "";
	private String namedQuery = "";
	private EstadoStr estado = EstadoStr.ALTA;
	private ArrayList<Material> materiales;
	private ArrayList<String> nombres;

	public static class Builder {

		private FiltroMaterial filtro;

		public Builder() {
			super();
			filtro = new FiltroMaterial();
		}

		public Builder nombreEntidad(String nombreEntidad) {
			filtro.nombreEntidad = nombreEntidad;
			return this;
		}

		public Builder materiales(ArrayList<Material> materiales) {
			if(materiales != null && !materiales.isEmpty()){
				filtro.materiales = materiales;
			}
			return this;
		}

		public Builder conPiezas() {
			filtro.conPiezas = true;
			return this;
		}

		public Builder nombres(ArrayList<String> nombres) {
			filtro.nombres = nombres;
			return this;
		}

		public FiltroMaterial build() {
			filtro.setConsulta();
			filtro.setNamedQuery();
			return filtro;
		}
	}

	private FiltroMaterial() {
		super(Material.class);
	}

	private void setConsulta() {
		consulta = this.getSelect() + this.getFrom() + this.getWhere() + this.getGroupBy() + this.getHaving() + this.getOrderBy();
	}

	private void setNamedQuery() {
		if(this.materiales != null){
			return;
		}
		if(this.estado != EstadoStr.ALTA){
			return;
		}
		if(this.conPiezas != null){
			return;
		}
		if(this.nombres != null){
			return;
		}
		namedQuery = "listarMateriales";
	}

	private String getSelect() {
		String select;
		if(this.conPiezas != null && this.conPiezas){
			select = "SELECT DISTINCT " + this.nombreEntidad;
		}
		else{
			select = "SELECT " + this.nombreEntidad;
		}
		return select;
	}

	private String getFrom() {
		String from;
		if(this.conPiezas != null && this.conPiezas){
			from = " FROM Material " + this.nombreEntidad + ", Pieza piez ";
		}
		else{
			from = " FROM Material " + this.nombreEntidad;
		}
		return from;
	}

	private String getWhere() {
		String where =
				((this.estado != null) ? (this.nombreEntidad + ".estado.nombre = :est AND ") : (""))
						+ ((this.materiales != null) ? (this.nombreEntidad + " in (:mts) AND ") : (""))
						+ ((this.conPiezas != null) ? ((this.conPiezas) ? (this.nombreEntidad + " = piez.material AND ") : ("")) : (""))
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
