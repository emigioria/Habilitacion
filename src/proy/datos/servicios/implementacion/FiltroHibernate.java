/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.servicios.implementacion;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import proy.excepciones.ConsultaException;
import proy.excepciones.PersistenciaException;

public interface FiltroHibernate<T> {

	public String getConsultaDinamica();

	public String getNamedQueryName();

	public Query setParametros(Query query);

	public void updateParametros(Session session);

	public Class<? extends T> getClase();

	public default ArrayList<T> listar(Session session) throws PersistenciaException {
		ArrayList<T> resultado = new ArrayList<>();
		try{
			Query query = null;
			if(!this.getNamedQueryName().isEmpty()){
				query = session.getNamedQuery(this.getNamedQueryName());
			}
			else{
				query = session.createQuery(this.getConsultaDinamica());
			}
			this.setParametros(query);
			this.updateParametros(session);
			List<?> var = query.list();
			for(Object o: var){
				try{
					resultado.add(getClase().cast(o));
				} catch(ClassCastException e){
					//no agrega objetos no casteables
				}
			}
		} catch(Exception e){
			throw new ConsultaException(e);
		}
		return resultado;
	}
}
