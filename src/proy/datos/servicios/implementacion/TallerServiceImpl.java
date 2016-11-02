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

import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Maquina;
import proy.datos.entidades.Material;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.datos.servicios.Filtro;
import proy.datos.servicios.TallerService;
import proy.excepciones.DeleteException;
import proy.excepciones.ObjNotFoundException;
import proy.excepciones.PersistenciaException;
import proy.excepciones.SaveUpdateException;

@Repository
public class TallerServiceImpl implements TallerService {

	private SessionFactory sessionFactory;

	@Autowired
	public TallerServiceImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = PersistenciaException.class)
	public ArrayList<Maquina> obtenerMaquinas(Filtro filtro) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		ArrayList<Maquina> resultado = FiltroHibernate.listar(filtro, session, Maquina.class);
		return resultado;
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void guardarMaquina(Maquina maquina) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			session.save(maquina);
		} catch(Exception e){
			e.printStackTrace();
			throw new SaveUpdateException();
		}
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void actualizarMaquina(Maquina maquina) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		try{
			session.update(maquina);
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
	public void bajaMaquina(Maquina maquina) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			session.delete(maquina);
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
	public ArrayList<Parte> obtenerPartes(Filtro filtro) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		ArrayList<Parte> resultado = FiltroHibernate.listar(filtro, session, Parte.class);
		return resultado;
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void guardarParte(Parte parte) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		try{
			parte.setEstado(AttachEstado.attachEstado(session, parte.getEstado()));
			session.save(parte);
		} catch(Exception e){
			e.printStackTrace();
			throw new SaveUpdateException();
		}
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void actualizarPartes(ArrayList<Parte> partes) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		try{
			Parte parte;
			for(int i = 0; i < partes.size(); i++){
				parte = partes.get(i);
				parte.setEstado(AttachEstado.attachEstado(session, parte.getEstado()));
				session.update(parte);
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
	public void bajaPartes(ArrayList<Parte> partes) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			Parte parte;
			for(int i = 0; i < partes.size(); i++){
				parte = partes.get(i);
				session.delete(parte);
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

	@Override
	@Transactional(readOnly = true, rollbackFor = PersistenciaException.class)
	public ArrayList<Pieza> obtenerPiezas(Filtro filtro) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		ArrayList<Pieza> resultado = FiltroHibernate.listar(filtro, session, Pieza.class);
		return resultado;
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void guardarPieza(Pieza pieza) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			pieza.setEstado(AttachEstado.attachEstado(session, pieza.getEstado()));
			session.save(pieza);
		} catch(Exception e){
			e.printStackTrace();
			throw new SaveUpdateException();
		}
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void actualizarPiezas(ArrayList<Pieza> piezas) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		try{
			Pieza pieza;
			for(int i = 0; i < piezas.size(); i++){
				pieza = piezas.get(i);
				pieza.setEstado(AttachEstado.attachEstado(session, pieza.getEstado()));
				session.update(pieza);
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
	public void bajaPiezas(ArrayList<Pieza> piezas) throws PersistenciaException {
		try{
			Pieza pieza;
			for(int i = 0; i < piezas.size(); i++){
				pieza = piezas.get(i);
				Session session = getSessionFactory().getCurrentSession();
				session.delete(pieza);
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

	@Override
	@Transactional(readOnly = true, rollbackFor = PersistenciaException.class)
	public ArrayList<Herramienta> obtenerHerramientas(Filtro filtro) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		ArrayList<Herramienta> resultado = FiltroHibernate.listar(filtro, session, Herramienta.class);
		return resultado;
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void guardarHerramienta(Herramienta herramienta) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			herramienta.setEstado(AttachEstado.attachEstado(session, herramienta.getEstado()));
			session.save(herramienta);
		} catch(Exception e){
			e.printStackTrace();
			throw new SaveUpdateException();
		}
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void actualizarHerramienta(Herramienta herramienta) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		try{
			herramienta.setEstado(AttachEstado.attachEstado(session, herramienta.getEstado()));
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
	public ArrayList<Material> obtenerMateriales(Filtro filtro) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		ArrayList<Material> resultado = FiltroHibernate.listar(filtro, session, Material.class);
		return resultado;
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void guardarMateriales(ArrayList<Material> materiales) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			Material material;
			for(int i = 0; i < materiales.size(); i++){
				material = materiales.get(i);
				material.setEstado(AttachEstado.attachEstado(session, material.getEstado()));
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
	public void actualizarMateriales(ArrayList<Material> materiales) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		try{
			Material material;
			for(int i = 0; i < materiales.size(); i++){
				material = materiales.get(i);
				material.setEstado(AttachEstado.attachEstado(session, material.getEstado()));
				session.update(material);
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
	public void bajaMateriales(ArrayList<Material> materiales) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			Material material;
			for(int i = 0; i < materiales.size(); i++){
				material = materiales.get(i);
				session.delete(material);
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
