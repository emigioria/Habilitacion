package proy.datos.servicios.implementacion;

import org.hibernate.Query;
import org.hibernate.Session;

public interface FiltroHibernate {

	String getConsulta();

	public String getSelect(String nombreEntidad);

	public String getFrom(String nombreEntidad);

	public String getWhere(String nombreEntidad);

	public String getOrden(String nombreEntidad);

	void setParametros(Query query);

	void updateParametros(Session session);

}
