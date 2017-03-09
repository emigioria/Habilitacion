package proy.datos.servicios.implementacion;

import java.util.ArrayList;

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
				herramienta.setEstado(AttachEstado.attachEstado(session, herramienta.getEstado()));
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
	public void actualizarHerramientas(ArrayList<Herramienta> herramientas) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			Herramienta herramienta;
			for(int i = 0; i < herramientas.size(); i++){
				herramienta = herramientas.get(i);
				herramienta.setEstado(AttachEstado.attachEstado(session, herramienta.getEstado()));
				session.update(herramienta);
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
	public void bajaHerramientas(ArrayList<Herramienta> herramientas) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			Herramienta herramienta;
			for(int i = 0; i < herramientas.size(); i++){
				herramienta = herramientas.get(i);
				herramienta.setEstado(AttachEstado.attachEstado(session, herramienta.getEstado()));
				session.delete(herramienta);
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
