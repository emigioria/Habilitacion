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
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.datos.entidades.Proceso;
import proy.datos.filtros.Filtro;

public class FiltroPieza extends Filtro<Pieza> {

	private String nombreEntidad = "a";
	private Boolean conTareas;
	private String consulta = "";
	private String namedQuery = "";
	private EstadoStr estado = EstadoStr.ALTA;
	private ArrayList<Material> materiales;
	private Parte parte;
	private ArrayList<Pieza> piezas;
	private ArrayList<Proceso> procesos;
	private ArrayList<String> nombres;
	public String nombreContiene;
	public String codigoPlano;

	public static class Builder {

		private FiltroPieza filtro;

		public Builder() {
			super();
			filtro = new FiltroPieza();
		}

		public Builder nombreEntidad(String nombreEntidad) {
			filtro.nombreEntidad = nombreEntidad;
			return this;
		}

		public Builder material(Material material) {
			if(material != null){
				filtro.materiales = new ArrayList<>();
				filtro.materiales.add(material);
			}
			return this;
		}

		public Builder materiales(ArrayList<Material> materiales) {
			if(materiales != null && !materiales.isEmpty()){
				filtro.materiales = materiales;
			}
			return this;
		}

		public Builder parte(Parte parte) {
			filtro.parte = parte;
			return this;
		}

		public Builder piezas(ArrayList<Pieza> piezas) {
			if(piezas != null && !piezas.isEmpty()){
				filtro.piezas = piezas;
			}
			return this;
		}

		public Builder procesos(ArrayList<Proceso> procesos) {
			if(procesos != null && !procesos.isEmpty()){
				filtro.procesos = procesos;
			}
			return this;
		}

		public Builder conTareas() {
			filtro.conTareas = true;
			return this;
		}

		public Builder nombreContiene(String nombreContiene) {
			if(nombreContiene != null && !nombreContiene.isEmpty()){
				filtro.nombreContiene = nombreContiene;
			}
			return this;
		}

		public Builder nombre(String nombre) {
			if(nombre != null && !nombre.isEmpty()){
				filtro.nombres = new ArrayList<>();
				filtro.nombres.add(nombre);
			}
			return this;
		}

		public Builder nombres(ArrayList<String> nombres) {
			if(nombres != null && !nombres.isEmpty()){
				filtro.nombres = nombres;
			}
			return this;
		}

		public Builder codigoPlano(String codigoPlano) {
			if(codigoPlano != null && !codigoPlano.isEmpty()){
				filtro.codigoPlano = codigoPlano;
			}
			return this;
		}

		public FiltroPieza build() {
			filtro.setConsulta();
			filtro.setNamedQuery();
			return filtro;
		}
	}

	private FiltroPieza() {
		super(Pieza.class);
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
		if(this.parte != null){
			return;
		}
		if(this.piezas != null){
			return;
		}
		if(this.conTareas != null){
			return;
		}
		if(this.procesos != null){
			return;
		}
		if(this.nombres != null){
			return;
		}
		if(this.nombreContiene != null){
			return;
		}
		if(this.codigoPlano != null){
			return;
		}
		namedQuery = "listarPiezas";
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
			from = " FROM Pieza " + this.nombreEntidad + " inner join " + this.nombreEntidad + ".procesos proc inner join proc.tareas tar";
		}
		else if(this.procesos != null){
			from = " FROM Pieza " + this.nombreEntidad + " inner join " + this.nombreEntidad + ".procesos proc";
		}
		else{
			from = " FROM Pieza " + this.nombreEntidad;
		}
		return from;
	}

	private String getWhere() {
		String where =
				((this.estado != null) ? (this.nombreEntidad + ".estado.nombre = :est AND ") : (""))
						+ ((this.materiales != null) ? (this.nombreEntidad + ".material in (:mts) AND ") : (""))
						+ ((this.parte != null) ? (this.nombreEntidad + ".parte = :par AND ") : (""))
						+ ((this.piezas != null) ? (this.nombreEntidad + " in (:pzs) AND ") : (""))
						+ ((this.procesos != null) ? ("proc in (:prs) AND ") : (""))
						+ ((this.nombres != null) ? (this.nombreEntidad + ".nombre in (:nms) AND ") : (""))
						+ ((this.nombreContiene != null) ? (this.nombreEntidad + ".nombre LIKE :nom AND ") : (""))
						+ ((this.codigoPlano != null) ? (this.nombreEntidad + ".codigoPlano LIKE :cdp AND ") : (""));

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
		if(materiales != null){
			query.setParameterList("mts", materiales);
		}
		if(parte != null){
			query.setParameter("par", parte);
		}
		if(piezas != null){
			query.setParameterList("pzs", piezas);
		}
		if(procesos != null){
			query.setParameter("prs", procesos);
		}
		if(nombres != null){
			query.setParameterList("nms", nombres);
		}
		if(nombreContiene != null){
			query.setParameter("nom", "%" + nombreContiene + "%");
		}
		if(codigoPlano != null){
			query.setParameter("cdp", "%" + codigoPlano + "%");
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
		if(materiales != null){
			for(Material material: materiales){
				session.update(material);
			}
		}
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
