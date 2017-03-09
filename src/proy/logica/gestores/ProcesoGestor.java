/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Service;

import proy.datos.entidades.Proceso;
import proy.datos.entidades.Tarea;
import proy.datos.filtros.Filtro;
import proy.datos.servicios.ProcesoService;
import proy.excepciones.PersistenciaException;
import proy.logica.gestores.resultados.ResultadoCrearProceso;
import proy.logica.gestores.resultados.ResultadoCrearTarea;
import proy.logica.gestores.resultados.ResultadoEliminarProceso;
import proy.logica.gestores.resultados.ResultadoEliminarProcesos;
import proy.logica.gestores.resultados.ResultadoEliminarTarea;
import proy.logica.gestores.resultados.ResultadoEliminarTareas;
import proy.logica.gestores.resultados.ResultadoModificarProceso;
import proy.logica.gestores.resultados.ResultadoModificarTarea;

@Service
public class ProcesoGestor {

	@Resource
	private ProcesoService persistidorProceso;

	public ArrayList<Proceso> listarProcesos(Filtro<Proceso> filtro) throws PersistenciaException {
		return persistidorProceso.obtenerProcesos(filtro);
	}

	public ResultadoCrearProceso crearProceso(Proceso proceso) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoModificarProceso modificarProceso(Proceso proceso) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoEliminarProceso eliminarProceso(Proceso proceso) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoEliminarProcesos validarEliminarProcesos(ArrayList<Proceso> procesos) {
		return new ResultadoEliminarProcesos();
	}

	public ArrayList<Tarea> listarTareas(Filtro<Tarea> filtro) throws PersistenciaException {
		return persistidorProceso.obtenerTareas(filtro);
	}

	public ResultadoCrearTarea crearTarea(Tarea tarea) throws PersistenciaException {
		ResultadoCrearTarea resultado = validarCrearTarea(tarea);
		if(!resultado.hayErrores()){
			persistidorProceso.guardarTarea(tarea);
		}
		throw new NotYetImplementedException();
	}

	private ResultadoCrearTarea validarCrearTarea(Tarea tarea) {
		// TODO validar tarea para crearla
		return new ResultadoCrearTarea();
	}

	public ResultadoModificarTarea modificarTarea(Tarea tarea) throws PersistenciaException {
		ResultadoModificarTarea resultado = validarModificarTarea(tarea);
		if(!resultado.hayErrores()){
			persistidorProceso.actualizarTarea(tarea);
		}
		return resultado;
	}

	private ResultadoModificarTarea validarModificarTarea(Tarea tarea) {
		// TODO validar tarea para modificarla
		return new ResultadoModificarTarea();
	}

	public ResultadoEliminarTareas eliminarTareas(ArrayList<Tarea> tareas) throws PersistenciaException {
		ResultadoEliminarTareas resultado = validarEliminarTareas(tareas);
		if(!resultado.hayErrores()){
			persistidorProceso.bajaTareas(tareas);
		}
		return resultado;
	}

	private ResultadoEliminarTareas validarEliminarTareas(ArrayList<Tarea> tareas) {
		//No hay validaciones hasta el momento
		return new ResultadoEliminarTareas();
	}

	public ResultadoEliminarTarea eliminarTarea(Tarea tarea) throws PersistenciaException {
		ResultadoEliminarTarea resultado = validarEliminarTarea(tarea);
		if(!resultado.hayErrores()){
			persistidorProceso.bajaTarea(tarea);
		}
		return resultado;
	}

	private ResultadoEliminarTarea validarEliminarTarea(Tarea tarea) {
		// TODO validar tarea para eliminarla
		return new ResultadoEliminarTarea();
	}

	public ResultadoModificarTarea comenzarTarea(Tarea tarea) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoModificarTarea pausarTarea(Tarea tarea) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoModificarTarea reanudarTarea(Tarea tarea) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoModificarTarea terminarTarea(Tarea tarea) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoModificarTarea cancelarTarea(Tarea tarea) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

}
