/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.servicios.implementacion;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.query.Query;

import proy.excepciones.ConsultaException;
import proy.excepciones.PersistenciaException;

public interface FiltroHibernate<T> {

	public String getConsultaDinamica();

	public String getNamedQueryName();

	public Query<T> setParametros(Query<T> query);

	public void updateParametros(Session session);

	public Class<T> getClase();

	public default ArrayList<T> listar(Session session) throws PersistenciaException {
		ArrayList<T> resultado = new ArrayList<>();
		try{
			Query<T> query = null;
			if(!this.getNamedQueryName().isEmpty()){
				query = session.createNamedQuery(this.getNamedQueryName(), getClase());
			}
			else{
				query = session.createQuery(this.getConsultaDinamica(), getClase());
			}
			this.setParametros(query);
			this.updateParametros(session);
			resultado = new ArrayList<>(query.list());
		} catch(Exception e){
			throw new ConsultaException(e);
		}
		return resultado;
	}
}
