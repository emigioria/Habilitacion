package proy.datos.servicios.implementacion;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import proy.excepciones.ConnectionException;

/**
 * Se encarga de gestionar la conexi�n de la aplicaci�n con la base de datos a trav�s de hibernate
 *
 * @author Acosta - Gioria - Moretti - Rebechi
 *
 */
public abstract class HibernateUtil {

	private static SessionFactory sessionFactory;

	/**
	 * Establece la conexi�n y crea el sessionFactory
	 *
	 * @throws ConnectionException
	 *             error en la conexi�n con la base de datos
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
	 * Obtiene la conexi�n existente. Si no existe pide su creaci�n
	 *
	 * @return el sessionFactory
	 * @throws ConnectionException
	 *             error en la conexi�n con la base de datos
	 */
	public static SessionFactory getSessionFactory() throws ConnectionException {
		if(sessionFactory == null || sessionFactory.isClosed()){
			buildSessionFactory();
		}
		return sessionFactory;
	}

	/**
	 * Cierra la conexi�n
	 */
	public static void close() {
		if(sessionFactory != null && !sessionFactory.isClosed()){
			sessionFactory.close();
			sessionFactory = null;
		}
	}
}