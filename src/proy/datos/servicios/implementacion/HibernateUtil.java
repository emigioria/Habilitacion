package proy.datos.servicios.implementacion;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import proy.excepciones.ConnectionException;

/**
 * Se encarga de gestionar la conexión de la aplicación con la base de datos a través de hibernate
 *
 * @author Acosta - Gioria - Moretti - Rebechi
 *
 */
public abstract class HibernateUtil {

	private static SessionFactory sessionFactory;

	/**
	 * Establece la conexión y crea el sessionFactory
	 *
	 * @throws ConnectionException
	 *             error en la conexión con la base de datos
	 */
	private static void buildSessionFactory() throws ConnectionException {
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		try{
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		} catch(Exception e){
			e.printStackTrace();
			StandardServiceRegistryBuilder.destroy(registry);
			throw new ConnectionException();
		}
	}

	/**
	 * Obtiene la conexión existente. Si no existe pide su creación
	 *
	 * @return el sessionFactory
	 * @throws ConnectionException
	 *             error en la conexión con la base de datos
	 */
	public static SessionFactory getSessionFactory() throws ConnectionException {
		if(sessionFactory == null || sessionFactory.isClosed()){
			buildSessionFactory();
		}
		return sessionFactory;
	}

	/**
	 * Cierra la conexión
	 */
	public static void close() {
		if(sessionFactory != null && !sessionFactory.isClosed()){
			sessionFactory.close();
			sessionFactory = null;
		}
	}
}