/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.filtros;

import proy.datos.servicios.implementacion.FiltroHibernate;

public abstract class Filtro<T> implements FiltroHibernate<T> {

	private Class<T> clase;

	public Filtro(Class<T> clase) {
		this.clase = clase;
	}

	@Override
	public final Class<T> getClase() {
		return clase;
	}
}
