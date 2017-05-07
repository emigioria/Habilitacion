/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.servicios.implementacion;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import proy.datos.entidades.Maquina;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.datos.filtros.Filtro;
import proy.datos.servicios.TallerService;
import proy.excepciones.DeleteException;
import proy.excepciones.ObjNotFoundException;
import proy.excepciones.PersistenciaException;
import proy.excepciones.SaveUpdateException;

@Repository
public class TallerServiceImpl implements TallerService {

	private SessionFactory sessionFactory;

	@Resource
	private AttachEstado attachEstado;

	@Autowired
	public TallerServiceImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = PersistenciaException.class)
	public ArrayList<Maquina> obtenerMaquinas(Filtro<Maquina> filtro) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		return filtro.listar(session);
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void guardarMaquina(Maquina maquina) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			for(Parte parte: maquina.getPartes()){
				parte.setEstado(attachEstado.attachEstado(session, parte.getEstado()));
				for(Pieza pieza: parte.getPiezas()){
					pieza.setEstado(attachEstado.attachEstado(session, pieza.getEstado()));
				}
			}
			session.save(maquina);
		} catch(Exception e){
			throw new SaveUpdateException(e);
		}
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void actualizarMaquina(Maquina maquina) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		try{
			for(Parte parte: maquina.getPartes()){
				parte.setEstado(attachEstado.attachEstado(session, parte.getEstado()));
				for(Pieza pieza: parte.getPiezas()){
					pieza.setEstado(attachEstado.attachEstado(session, pieza.getEstado()));
				}
			}
			session.update(maquina);
		} catch(EntityNotFoundException e){
			throw new ObjNotFoundException("modificar", e);
		} catch(Exception e){
			throw new SaveUpdateException(e);
		}
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void bajaMaquina(Maquina maquina) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			session.delete(maquina);
		} catch(EntityNotFoundException e){
			throw new ObjNotFoundException("eliminar", e);
		} catch(Exception e){
			throw new DeleteException(e);
		}
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = PersistenciaException.class)
	public ArrayList<Parte> obtenerPartes(Filtro<Parte> filtro) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		return filtro.listar(session);
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void actualizarParte(Parte parte) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		try{
			parte.setEstado(attachEstado.attachEstado(session, parte.getEstado()));
			session.update(parte);
		} catch(EntityNotFoundException e){
			throw new ObjNotFoundException("modificar", e);
		} catch(Exception e){
			throw new SaveUpdateException(e);
		}
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void bajaParte(Parte parte) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			session.delete(parte);
		} catch(EntityNotFoundException e){
			throw new ObjNotFoundException("eliminar", e);
		} catch(Exception e){
			throw new DeleteException(e);
		}
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = PersistenciaException.class)
	public ArrayList<Pieza> obtenerPiezas(Filtro<Pieza> filtro) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		return filtro.listar(session);
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void actualizarPieza(Pieza pieza) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		try{
			pieza.setEstado(attachEstado.attachEstado(session, pieza.getEstado()));
			session.update(pieza);
		} catch(EntityNotFoundException e){
			throw new ObjNotFoundException("modificar", e);
		} catch(Exception e){
			throw new SaveUpdateException(e);
		}
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void bajaPieza(Pieza pieza) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			session.delete(pieza);
		} catch(EntityNotFoundException e){
			throw new ObjNotFoundException("eliminar", e);
		} catch(Exception e){
			throw new DeleteException(e);
		}
	}
}
