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

import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Maquina;
import proy.datos.entidades.Material;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.datos.servicios.Filtro;
import proy.datos.servicios.TallerService;
import proy.excepciones.ConsultaException;
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
		ArrayList<Maquina> resultado = new ArrayList<Maquina>();
		try{
			Session session = getSessionFactory().getCurrentSession();
			Query query = session.createQuery(filtro.getConsulta());
			filtro.setParametros(query);
			filtro.updateParametros(session);
			List<?> var = query.list();
			if(var instanceof List){
				for(int i = 0; i < ((List<?>) var).size(); i++){
					Object item = ((List<?>) var).get(i);
					if(item instanceof Maquina){
						resultado.add((Maquina) item);
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
		ArrayList<Parte> resultado = new ArrayList<Parte>();
		try{
			Session session = getSessionFactory().getCurrentSession();
			Query query = session.createQuery(filtro.getConsulta());
			filtro.setParametros(query);
			filtro.updateParametros(session);
			List<?> var = query.list();
			if(var instanceof List){
				for(int i = 0; i < ((List<?>) var).size(); i++){
					Object item = ((List<?>) var).get(i);
					if(item instanceof Parte){
						resultado.add((Parte) item);
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
	public void guardarParte(Parte parte) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			session.save(parte);
		} catch(Exception e){
			e.printStackTrace();
			throw new SaveUpdateException();
		}
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void actualizarParte(Parte parte) throws PersistenciaException {
		Session session = getSessionFactory().getCurrentSession();
		try{
			session.update(parte);
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
	public void bajaParte(Parte parte) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			session.delete(parte);
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
		ArrayList<Pieza> resultado = new ArrayList<Pieza>();
		try{
			Session session = getSessionFactory().getCurrentSession();
			Query query = session.createQuery(filtro.getConsulta());
			filtro.setParametros(query);
			filtro.updateParametros(session);
			List<?> var = query.list();
			if(var instanceof List){
				for(int i = 0; i < ((List<?>) var).size(); i++){
					Object item = ((List<?>) var).get(i);
					if(item instanceof Pieza){
						resultado.add((Pieza) item);
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
	public void guardarPieza(Pieza pieza) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			session.save(pieza);
		} catch(Exception e){
			e.printStackTrace();
			throw new SaveUpdateException();
		}
	}

	@Override
	@Transactional(rollbackFor = PersistenciaException.class)
	public void bajaPieza(Pieza pieza) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			session.delete(pieza);
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
		ArrayList<Herramienta> resultado = new ArrayList<Herramienta>();
		try{
			Session session = getSessionFactory().getCurrentSession();
			Query query = session.createQuery(filtro.getConsulta());
			filtro.setParametros(query);
			filtro.updateParametros(session);
			List<?> var = query.list();
			if(var instanceof List){
				for(int i = 0; i < ((List<?>) var).size(); i++){
					Object item = ((List<?>) var).get(i);
					if(item instanceof Herramienta){
						resultado.add((Herramienta) item);
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
	public void guardarHerramienta(Herramienta herramienta) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			session.save(herramienta);
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
		ArrayList<Material> resultado = new ArrayList<Material>();
		try{
			Session session = getSessionFactory().getCurrentSession();
			Query query = session.createQuery(filtro.getConsulta());
			filtro.setParametros(query);
			filtro.updateParametros(session);
			List<?> var = query.list();
			if(var instanceof List){
				for(int i = 0; i < ((List<?>) var).size(); i++){
					Object item = ((List<?>) var).get(i);
					if(item instanceof Material){
						resultado.add((Material) item);
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
	public void guardarMaterial(Material material) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			session.save(material);
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
