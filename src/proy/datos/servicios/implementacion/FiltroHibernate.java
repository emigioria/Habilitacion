/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
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

public interface FiltroHibernate {

	public String getConsultaDinamica();

	public String getNamedQueryName();

	public Query setParametros(Query query);

	public void updateParametros(Session session);

	public static ArrayList<Object> listar(FiltroHibernate filtro, Session session) throws PersistenciaException {
		ArrayList<Object> resultado = new ArrayList<>();
		try{
			Query query = null;
			if(!filtro.getNamedQueryName().isEmpty()){
				query = session.getNamedQuery(filtro.getNamedQueryName());
			}
			else{
				query = session.createQuery(filtro.getConsultaDinamica());
			}
			filtro.setParametros(query);
			filtro.updateParametros(session);
			List<?> var = query.list();
			if(var instanceof List){
				for(int i = 0; i < ((List<?>) var).size(); i++){
					Object item = ((List<?>) var).get(i);
					resultado.add(item);
				}
			}
		} catch(Exception e){
			e.printStackTrace();
			throw new ConsultaException();
		}
		return resultado;
	}
}
