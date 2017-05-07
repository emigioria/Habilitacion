/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.servicios.implementacion;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.springframework.stereotype.Service;

import proy.datos.entidades.Estado;
import proy.datos.entidades.EstadoTarea;

@Service
public class AttachEstado {

	public Estado attachEstado(Session session, Estado estadoEntidad) {
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Estado> query = cb.createQuery(Estado.class);
		Root<Estado> root = query.from(Estado.class);
		query.select(root).where(cb.equal(root.get(Estado.COLUMNA_NOMBRE), estadoEntidad.getNombre()));
		Estado estado = session.createQuery(query).getSingleResult();

		if(estado == null){
			session.save(estadoEntidad);
			return estadoEntidad;
		}
		return estado;
	}

	public EstadoTarea attachEstadoTarea(Session session, EstadoTarea estadoEntidad) {
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<EstadoTarea> query = cb.createQuery(EstadoTarea.class);
		Root<EstadoTarea> root = query.from(EstadoTarea.class);
		query.select(root).where(cb.equal(root.get(EstadoTarea.COLUMNA_NOMBRE), estadoEntidad.getNombre()));
		EstadoTarea estado = session.createQuery(query).getSingleResult();

		if(estado == null){
			session.save(estadoEntidad);
			return estadoEntidad;
		}
		return estado;
	}
}
