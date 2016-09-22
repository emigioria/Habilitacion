/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.servicios.implementacion;

import org.hibernate.Query;
import org.hibernate.Session;

public interface FiltroHibernate {

	String getConsulta();

	public String getSelect(String nombreEntidad);

	public String getFrom(String nombreEntidad);

	public String getWhere(String nombreEntidad);

	public String getOrden(String nombreEntidad);

	void setParametros(Query query);

	void updateParametros(Session session);

}
