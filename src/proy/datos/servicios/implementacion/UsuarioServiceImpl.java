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

import proy.datos.entidades.Administrador;
import proy.datos.entidades.Comentario;
import proy.datos.entidades.Operario;
import proy.datos.servicios.Filtro;
import proy.datos.servicios.UsuarioService;
import proy.excepciones.ConsultaException;
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
		ArrayList<Administrador> resultado = new ArrayList<Administrador>();
		try{
			Session session = getSessionFactory().getCurrentSession();
			Query query = session.createQuery(filtro.getConsulta());
			filtro.setParametros(query);
			filtro.updateParametros(session);
			List<?> var = query.list();
			if(var instanceof List){
				for(int i = 0; i < ((List<?>) var).size(); i++){
					Object item = ((List<?>) var).get(i);
					if(item instanceof Administrador){
						resultado.add((Administrador) item);
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
		ArrayList<Comentario> resultado = new ArrayList<Comentario>();
		try{
			Session session = getSessionFactory().getCurrentSession();
			Query query = session.createQuery(filtro.getConsulta());
			filtro.setParametros(query);
			filtro.updateParametros(session);
			List<?> var = query.list();
			if(var instanceof List){
				for(int i = 0; i < ((List<?>) var).size(); i++){
					Object item = ((List<?>) var).get(i);
					if(item instanceof Comentario){
						resultado.add((Comentario) item);
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
	@Transactional(readOnly = true, rollbackFor = PersistenciaException.class)
	public ArrayList<Operario> obtenerOperarios(Filtro filtro) throws PersistenciaException {
		ArrayList<Operario> resultado = new ArrayList<Operario>();
		try{
			Session session = getSessionFactory().getCurrentSession();
			Query query = session.createQuery(filtro.getConsulta());
			filtro.setParametros(query);
			filtro.updateParametros(session);
			List<?> var = query.list();
			if(var instanceof List){
				for(int i = 0; i < ((List<?>) var).size(); i++){
					Object item = ((List<?>) var).get(i);
					if(item instanceof Operario){
						resultado.add((Operario) item);
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
	public void guardarOperario(Operario operario) throws PersistenciaException {
		try{
			Session session = getSessionFactory().getCurrentSession();
			session.save(operario);
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