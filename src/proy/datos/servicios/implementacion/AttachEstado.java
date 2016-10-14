package proy.datos.servicios.implementacion;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import proy.datos.entidades.Estado;
import proy.datos.entidades.EstadoTarea;

public abstract class AttachEstado {

	public static Estado attachEstado(Session session, Estado estadoEntidad) {
		Criteria criteria = session.createCriteria(Estado.class);
		Estado estado = (Estado) criteria.add(Restrictions.eq(Estado.COLUMNA_NOMBRE, estadoEntidad.getNombre())).uniqueResult();
		if(estado == null){
			session.save(estadoEntidad);
			return estadoEntidad;
		}
		return estado;
	}

	public static EstadoTarea attachEstadoTarea(Session session, EstadoTarea estadoEntidad) {
		Criteria criteria = session.createCriteria(Estado.class);
		EstadoTarea estado = (EstadoTarea) criteria.add(Restrictions.eq(EstadoTarea.COLUMNA_NOMBRE, estadoEntidad.getNombre())).uniqueResult();
		if(estado == null){
			session.save(estadoEntidad);
			return estadoEntidad;
		}
		return estado;
	}
}
