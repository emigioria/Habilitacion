/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.filtros;

import org.hibernate.Query;
import org.hibernate.Session;

import proy.datos.servicios.Filtro;

public class FiltroTarea extends Filtro {

	public FiltroTarea() {
		super();
	}

	@Override
	public String getConsulta() {
		String nombreEntidad = "t";
		String consulta = this.getSelect(nombreEntidad) + this.getFrom(nombreEntidad) + this.getWhere(nombreEntidad) + this.getOrden(nombreEntidad);
		return consulta;
	}

	@Override
	public String getSelect(String nombreEntidad) {
		String select = "SELECT " + nombreEntidad;
		return select;
	}

	@Override
	public String getFrom(String nombreEntidad) {
		String from = " FROM Tarea " + nombreEntidad;
		return from;
	}

	@Override
	public String getWhere(String nombreEntidad) {
		String where =
				"";
		return where;
	}

	@Override
	public String getOrden(String nombreEntidad) {
		String orden = "";
		return orden;
	}

	@Override
	public void setParametros(Query query) {

	}

	@Override
	public void updateParametros(Session session) {

	}
}
