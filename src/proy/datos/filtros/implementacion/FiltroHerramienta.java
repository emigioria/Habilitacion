/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.filtros.implementacion;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.query.Query;

import proy.datos.clases.EstadoStr;
import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Proceso;
import proy.datos.filtros.Filtro;

public class FiltroHerramienta extends Filtro<Herramienta> {

	private String nombreEntidad = "h";
	private String consulta = "";
	private String namedQuery = "";
	private EstadoStr estado = EstadoStr.ALTA;
	private String nombreContiene;
	private ArrayList<Proceso> procesos;
	private ArrayList<String> nombres;

	public static class Builder {

		private FiltroHerramienta filtro;

		public Builder() {
			super();
			filtro = new FiltroHerramienta();
		}

		public Builder nombreEntidad(String nombreEntidad) {
			filtro.nombreEntidad = nombreEntidad;
			return this;
		}

		public Builder nombreContiene(String nombreContiene) {
			if(nombreContiene != null && !nombreContiene.isEmpty()){
				filtro.nombreContiene = nombreContiene;
			}
			return this;
		}

		public Builder procesos(ArrayList<Proceso> procesos) {
			if(procesos != null && !procesos.isEmpty()){
				filtro.procesos = procesos;
			}
			return this;
		}

		public Builder nombres(ArrayList<String> nombres) {
			if(nombres != null && !nombres.isEmpty()){
				filtro.nombres = nombres;
			}
			return this;
		}

		public FiltroHerramienta build() {
			filtro.setConsulta();
			filtro.setNamedQuery();
			return filtro;
		}

	}

	private FiltroHerramienta() {
		super(Herramienta.class);
	}

	private void setConsulta() {
		consulta = this.getSelect() + this.getFrom() + this.getWhere() + this.getGroupBy() + this.getHaving() + this.getOrderBy();
	}

	private void setNamedQuery() {
		if(this.estado != EstadoStr.ALTA){
			return;
		}
		if(this.nombreContiene != null){
			return;
		}
		if(this.procesos != null){
			return;
		}
		if(this.nombres != null){
			return;
		}
		namedQuery = "listarHerramientas";
	}

	private String getSelect() {
		String select = "SELECT " + this.nombreEntidad;
		return select;
	}

	private String getFrom() {
		String from;
		if(procesos != null){
			from = " FROM Herramienta " + this.nombreEntidad + " inner join " + this.nombreEntidad + ".procesos proc";
		}
		else{
			from = " FROM Herramienta " + this.nombreEntidad;
		}
		return from;
	}

	private String getWhere() {
		String where =
				((this.nombreContiene != null) ? (this.nombreEntidad + ".nombre LIKE :nom AND ") : (""))
						+ ((this.estado != null) ? (this.nombreEntidad + ".estado.nombre = :est AND ") : (""))
						+ ((this.procesos != null) ? ("proc in (:prs) AND ") : (""))
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
	public Query<Herramienta> setParametros(Query<Herramienta> query) {
		if(estado != null){
			query.setParameter("est", estado);
		}
		if(nombreContiene != null){
			query.setParameter("nom", "%" + nombreContiene + "%");
		}
		if(procesos != null){
			query.setParameter("prs", procesos);
		}
		if(nombres != null){
			query.setParameterList("nms", nombres);
		}
		return query;
	}

	@Override
	public void updateParametros(Session session) {
		if(procesos != null){
			for(Proceso proceso: procesos){
				session.update(proceso);
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
