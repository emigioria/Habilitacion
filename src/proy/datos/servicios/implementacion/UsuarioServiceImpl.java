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

import proy.datos.entidades.Administrador;
import proy.datos.entidades.Comentario;
import proy.datos.entidades.Operario;
import proy.datos.servicios.Filtro;
import proy.datos.servicios.UsuarioService;
import proy.excepciones.DeleteException;
import proy.excepciones.ObjNotFoundException;
import proy.excepciones.PersistenciaException;
import proy.excepciones.SaveUpdateException;

@Repository
public class UsuarioServiceImpl implements UsuarioService {

	private SessionFactory sessionFactory;

	@Autowired
	public UsuarioServiceImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = PersistenciaException.class)
	public ArrayList<Administrador> obtenerAdministradores(Filtro filtro) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		return filtro.listar(session, Administrador.class);
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void guardarComentario(Comentario comentario) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			session.save(comentario);
		} catch(Exception e){
			e.printStackTrace();
			throw new SaveUpdateException();
		}
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = PersistenciaException.class)
	public ArrayList<Comentario> obtenerComentarios(Filtro filtro) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		return filtro.listar(session, Comentario.class);
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = PersistenciaException.class)
	public ArrayList<Operario> obtenerOperarios(Filtro filtro) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		return filtro.listar(session, Operario.class);
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void guardarOperario(Operario operario) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			operario.setEstado(AttachEstado.attachEstado(session, operario.getEstado()));
			session.save(operario);
		} catch(Exception e){
			e.printStackTrace();
			throw new SaveUpdateException();
		}
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void actualizarOperario(Operario operario) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		try{
			operario.setEstado(AttachEstado.attachEstado(session, operario.getEstado()));
			session.update(operario);
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
	public void bajaOperario(Operario operario) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			session.delete(operario);
		} catch(EntityNotFoundException e){
			e.printStackTrace();
			throw new ObjNotFoundException("eliminar");
		} catch(Exception e){
			e.printStackTrace();
			throw new DeleteException();
		}
	}

}
