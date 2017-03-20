/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
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

import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Material;
import proy.datos.filtros.Filtro;
import proy.datos.servicios.MaterialService;
import proy.excepciones.DeleteException;
import proy.excepciones.ObjNotFoundException;
import proy.excepciones.PersistenciaException;
import proy.excepciones.SaveUpdateException;

@Repository
public class MaterialServiceImpl implements MaterialService {

	private SessionFactory sessionFactory;

	@Resource
	private AttachEstado attachEstado;

	@Autowired
	public MaterialServiceImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = PersistenciaException.class)
	public ArrayList<Herramienta> obtenerHerramientas(Filtro<Herramienta> filtro) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		return filtro.listar(session);
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void guardarHerramientas(ArrayList<Herramienta> herramientas) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			Herramienta herramienta;
			for(int i = 0; i < herramientas.size(); i++){
				herramienta = herramientas.get(i);
				herramienta.setEstado(attachEstado.attachEstado(session, herramienta.getEstado()));
				session.save(herramienta);
				if(i % 20 == 0){
					//flush a batch of inserts and release memory:
					session.flush();
					session.clear();
				}
			}
		} catch(Exception e){
			e.printStackTrace();
			throw new SaveUpdateException();
		}
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void actualizarHerramienta(Herramienta herramienta) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			herramienta.setEstado(attachEstado.attachEstado(session, herramienta.getEstado()));
			session.update(herramienta);
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
	public void bajaHerramienta(Herramienta herramienta) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			herramienta.setEstado(attachEstado.attachEstado(session, herramienta.getEstado()));
			session.delete(herramienta);
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
	public ArrayList<Material> obtenerMateriales(Filtro<Material> filtro) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		return filtro.listar(session);
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void guardarMateriales(ArrayList<Material> materiales) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			Material material;
			for(int i = 0; i < materiales.size(); i++){
				material = materiales.get(i);
				material.setEstado(attachEstado.attachEstado(session, material.getEstado()));
				session.save(material);
				if(i % 20 == 0){
					//flush a batch of inserts and release memory:
					session.flush();
					session.clear();
				}
			}
		} catch(Exception e){
			e.printStackTrace();
			throw new SaveUpdateException();
		}
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void actualizarMaterial(Material material) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		try{
			material.setEstado(attachEstado.attachEstado(session, material.getEstado()));
			session.update(material);
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
	public void bajaMaterial(Material material) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			session.delete(material);
		} catch(EntityNotFoundException e){
			e.printStackTrace();
			throw new ObjNotFoundException("eliminar");
		} catch(Exception e){
			e.printStackTrace();
			throw new DeleteException();
		}
	}

}
