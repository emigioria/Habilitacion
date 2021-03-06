/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.servicios;

import java.util.ArrayList;
import java.util.Date;

import proy.datos.entidades.Proceso;
import proy.datos.entidades.Tarea;
import proy.datos.filtros.Filtro;
import proy.excepciones.PersistenciaException;

public interface ProcesoService {

	public ArrayList<Proceso> obtenerProcesos(Filtro<Proceso> filtro) throws PersistenciaException;

	public ArrayList<String> obtenerDescripciones(Filtro<String> filtro) throws PersistenciaException;

	public ArrayList<String> obtenerTipos(Filtro<String> filtro) throws PersistenciaException;

	public void guardarProceso(Proceso proceso) throws PersistenciaException;

	public void actualizarProceso(Proceso proceso) throws PersistenciaException;

	public void bajaProceso(Proceso proceso) throws PersistenciaException;

	public ArrayList<Tarea> obtenerTareas(Filtro<Tarea> filtro) throws PersistenciaException;

	public ArrayList<Date> obtenerFechasFinTareas(Filtro<Date> filtro) throws PersistenciaException;

	public void guardarTarea(Tarea tarea) throws PersistenciaException;

	public void actualizarTarea(Tarea tarea) throws PersistenciaException;

	public void bajaTarea(Tarea tarea) throws PersistenciaException;

	public void bajaTareas(ArrayList<Tarea> tareas) throws PersistenciaException;

}
