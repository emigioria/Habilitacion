/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.servicios.implementacion;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import proy.datos.entidades.Proceso;
import proy.datos.entidades.Tarea;
import proy.datos.servicios.Filtro;
import proy.datos.servicios.ProcesoService;
import proy.excepciones.ConsultaException;
import proy.excepciones.DeleteException;
import proy.excepciones.ObjNotFoundException;
import proy.excepciones.PersistenciaException;
import proy.excepciones.SaveUpdateException;

@Repository
public class ProcesoServiceImpl implements ProcesoService {

	private SessionFactory sessionFactory;

	@Autowired
	public ProcesoServiceImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = PersistenciaException.class)
	public ArrayList<Proceso> obtenerProcesos(Filtro filtro) throws PersistenciaException {
		ArrayList<Proceso> resultado = new ArrayList<Proceso>();
		try{
			Session session = getSessionFactory().getCurrentSession();
			Query query = session.createQuery(filtro.getConsulta());
			filtro.setParametros(query);
			filtro.updateParametros(session);
			List<?> var = query.list();
			if(var instanceof List){
				for(int i = 0; i < ((List<?>) var).size(); i++){
					Object item = ((List<?>) var).get(i);
					if(item instanceof Proceso){
						resultado.add((Proceso) item);
					}
				}
			}
		} catch(Exception e){
			e.printStackTrace();
			throw new ConsultaException();
		}
		return resultado;
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void guardarProceso(Proceso proceso) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			session.save(proceso);
		} catch(Exception e){
			e.printStackTrace();
			throw new SaveUpdateException();
		}
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void actualizarProceso(Proceso proceso) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		try{
			session.update(proceso);
		} catch(EntityNotFoundException e){
			e.printStackTrace();
			throw new ObjNotFoundException("modificar");
		} catch(Exception e){
			e.printStackTrace();
			throw new SaveUpdateException();
		}
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void bajaProceso(Proceso proceso) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			session.delete(proceso);
		} catch(EntityNotFoundException e){
			e.printStackTrace();
			throw new ObjNotFoundException("eliminar");
		} catch(Exception e){
			e.printStackTrace();
			throw new DeleteException();
		}
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = PersistenciaException.class)
	public ArrayList<Tarea> obtenerTareas(Filtro filtro) throws PersistenciaException {
		ArrayList<Tarea> resultado = new ArrayList<Tarea>();
		try{
			Session session = getSessionFactory().getCurrentSession();
			Query query = session.createQuery(filtro.getConsulta());
			filtro.setParametros(query);
			filtro.updateParametros(session);
			List<?> var = query.list();
			if(var instanceof List){
				for(int i = 0; i < ((List<?>) var).size(); i++){
					Object item = ((List<?>) var).get(i);
					if(item instanceof Tarea){
						resultado.add((Tarea) item);
					}
				}
			}
		} catch(Exception e){
			e.printStackTrace();
			throw new ConsultaException();
		}
		return resultado;
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void guardarTarea(Tarea tarea) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			session.save(tarea);
		} catch(Exception e){
			e.printStackTrace();
			throw new SaveUpdateException();
		}
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void actualizarTarea(Tarea tarea) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		try{
			session.update(tarea);
		} catch(EntityNotFoundException e){
			e.printStackTrace();
			throw new ObjNotFoundException("modificar");
		} catch(Exception e){
			e.printStackTrace();
			throw new SaveUpdateException();
		}
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void bajaTarea(Tarea tarea) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			session.delete(tarea);
		} catch(EntityNotFoundException e){
			e.printStackTrace();
			throw new ObjNotFoundException("eliminar");
		} catch(Exception e){
			e.printStackTrace();
			throw new DeleteException();
		}
	}

}
