/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.servicios;

import java.util.ArrayList;

import proy.datos.entidades.Administrador;
import proy.datos.entidades.Comentario;
import proy.datos.entidades.Operario;
import proy.datos.filtros.Filtro;
import proy.excepciones.PersistenciaException;

public interface UsuarioService {

	public ArrayList<Administrador> obtenerAdministradores(Filtro<Administrador> filtro) throws PersistenciaException;

	public void guardarComentario(Comentario comentario) throws PersistenciaException;

	public ArrayList<Comentario> obtenerComentarios(Filtro<Comentario> filtro) throws PersistenciaException;

	public ArrayList<Operario> obtenerOperarios(Filtro<Operario> filtro) throws PersistenciaException;

	public void guardarOperarios(ArrayList<Operario> operarios) throws PersistenciaException;

	public void actualizarOperario(Operario operario) throws PersistenciaException;

	public void bajaOperario(Operario operario) throws PersistenciaException;

}
