/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.servicios.implementacion;

import java.util.ArrayList;

import javax.persistence.EntityNotFoundException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import proy.datos.entidades.Proceso;
import proy.datos.entidades.Tarea;
import proy.datos.filtros.Filtro;
import proy.datos.servicios.ProcesoService;
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
	public ArrayList<Proceso> obtenerProcesos(Filtro<Proceso> filtro) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		return filtro.listar(session);
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void guardarProceso(Proceso proceso) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			proceso.setEstado(AttachEstado.attachEstado(session, proceso.getEstado()));
			session.save(proceso);
		} catch(Exception e){
			e.printStackTrace();
			throw new SaveUpdateException();
		}
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void actualizarProcesos(ArrayList<Proceso> procesos) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		try{
			Proceso proceso;
			for(int i = 0; i < procesos.size(); i++){
				proceso = procesos.get(i);
				proceso.setEstado(AttachEstado.attachEstado(session, proceso.getEstado()));
				session.update(proceso);
				if(i % 20 == 0){
					//flush a batch of inserts and release memory:
					session.flush();
					session.clear();
				}
			}
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
	public ArrayList<Tarea> obtenerTareas(Filtro<Tarea> filtro) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		return filtro.listar(session);
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void guardarTarea(Tarea tarea) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			tarea.setEstado(AttachEstado.attachEstadoTarea(session, tarea.getEstado()));
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
			tarea.setEstado(AttachEstado.attachEstadoTarea(session, tarea.getEstado()));
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

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void bajaTareas(ArrayList<Tarea> tareas) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			Tarea tarea;
			for(int i = 0; i < tareas.size(); i++){
				tarea = tareas.get(i);
				session.delete(tarea);
				if(i % 20 == 0){
					//flush a batch of inserts and release memory:
					session.flush();
					session.clear();
				}
			}
		} catch(EntityNotFoundException e){
			e.printStackTrace();
			throw new ObjNotFoundException("eliminar");
		} catch(Exception e){
			e.printStackTrace();
			throw new DeleteException();
		}
	}

}
