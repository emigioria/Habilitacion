/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import proy.comun.ConversorTiempos;
import proy.datos.clases.EstadoTareaStr;
import proy.datos.entidades.Pausa;
import proy.datos.entidades.Proceso;
import proy.datos.entidades.Tarea;
import proy.datos.filtros.Filtro;
import proy.datos.filtros.implementacion.FiltroProceso;
import proy.datos.filtros.implementacion.FiltroTarea;
import proy.datos.servicios.ProcesoService;
import proy.excepciones.ObjNotFoundException;
import proy.excepciones.PersistenciaException;
import proy.logica.gestores.resultados.ResultadoCrearProceso;
import proy.logica.gestores.resultados.ResultadoCrearProceso.ErrorCrearProceso;
import proy.logica.gestores.resultados.ResultadoCrearTarea;
import proy.logica.gestores.resultados.ResultadoCrearTarea.ErrorCrearTarea;
import proy.logica.gestores.resultados.ResultadoEliminarProceso;
import proy.logica.gestores.resultados.ResultadoEliminarProcesos;
import proy.logica.gestores.resultados.ResultadoEliminarTarea;
import proy.logica.gestores.resultados.ResultadoEliminarTarea.ErrorEliminarTarea;
import proy.logica.gestores.resultados.ResultadoEliminarTareas;
import proy.logica.gestores.resultados.ResultadoEliminarTareas.ErrorEliminarTareas;
import proy.logica.gestores.resultados.ResultadoModificarEstadoTarea;
import proy.logica.gestores.resultados.ResultadoModificarEstadoTarea.ErrorModificarEstadoTarea;
import proy.logica.gestores.resultados.ResultadoModificarProceso;
import proy.logica.gestores.resultados.ResultadoModificarProceso.ErrorModificarProceso;
import proy.logica.gestores.resultados.ResultadoModificarTarea;
import proy.logica.gestores.resultados.ResultadoModificarTarea.ErrorModificarTarea;

@Service
public class ProcesoGestor {

	@Resource
	private ProcesoService persistidorProceso;

	@Resource
	private ConversorTiempos conversorTiempos;

	public ArrayList<String> listarDescripciones(Filtro<String> filtro) throws PersistenciaException {
		return persistidorProceso.obtenerDescripciones(filtro);
	}

	public ArrayList<String> listarTipos(Filtro<String> filtro) throws PersistenciaException {
		return persistidorProceso.obtenerTipos(filtro);
	}

	public ArrayList<Proceso> listarProcesos(Filtro<Proceso> filtro) throws PersistenciaException {
		return persistidorProceso.obtenerProcesos(filtro);
	}

	public ResultadoCrearProceso crearProceso(Proceso proceso) throws PersistenciaException {
		ResultadoCrearProceso resultado = validarCrearProceso(proceso);
		if(!resultado.hayErrores()){
			persistidorProceso.guardarProceso(proceso);
		}
		return resultado;
	}

	private ResultadoCrearProceso validarCrearProceso(Proceso proceso) throws PersistenciaException {
		Set<ErrorCrearProceso> errores = new HashSet<>();

		//Veo si el proceso no tiene parte
		if(proceso.getParte() == null){
			errores.add(ErrorCrearProceso.PARTE_INCOMPLETA);
		}

		//Veo si el proceso no tiene descripcion
		if(proceso.getDescripcion() == null || proceso.getDescripcion().isEmpty()){
			errores.add(ErrorCrearProceso.DESCRIPCION_PROCESO_INCOMPLETA);
		}

		//Veo si el proceso no tiene tipo
		if(proceso.getTipo() == null || proceso.getTipo().isEmpty()){
			errores.add(ErrorCrearProceso.TIPO_PROCESO_INCOMPLETO);
		}

		//Veo si el proceso no tiene tiempo teórico de preparación
		if(proceso.getTiempoTeoricoProceso() == null || proceso.getTiempoTeoricoProceso() < 1){
			errores.add(ErrorCrearProceso.TIEMPO_TEORICO_PROCESO_INCOMPLETO);
		}

		//Veo si el proceso no tiene tiempo teórico de proceso
		if(proceso.getTiempoTeoricoPreparacion() == null || proceso.getTiempoTeoricoPreparacion() < 1){
			errores.add(ErrorCrearProceso.TIEMPO_TEORICO_PREPARACION_INCOMPLETO);
		}

		if(!errores.contains(ErrorCrearProceso.PARTE_INCOMPLETA) && !errores.contains(ErrorCrearProceso.DESCRIPCION_PROCESO_INCOMPLETA) && !errores.contains(ErrorCrearProceso.TIPO_PROCESO_INCOMPLETO)){
			//Veo si hay otro proceso con la misma máquina, parte, descripción y tipo
			List<Proceso> procesosCoincidentes = persistidorProceso.obtenerProcesos(new FiltroProceso.Builder().parte(proceso.getParte()).descripcionExacta(proceso.getDescripcion()).tipoExacto(proceso.getTipo()).build());
			if(!procesosCoincidentes.isEmpty()){
				errores.add(ErrorCrearProceso.MAQUINA_PARTE_DESCRIPCION_Y_TIPO_REPETIDO);
			}
		}

		return new ResultadoCrearProceso(errores.toArray(new ErrorCrearProceso[0]));
	}

	public ResultadoModificarProceso modificarProceso(Proceso proceso) throws PersistenciaException {
		ResultadoModificarProceso resultado = validarModificarProceso(proceso);
		if(!resultado.hayErrores()){
			List<Proceso> procesoAnteriorList = listarProcesos(new FiltroProceso.Builder().id(proceso.getId()).build());
			if(procesoAnteriorList.size() == 1){
				Proceso procesoAnterior = procesoAnteriorList.get(0);
				if(!proceso.cambioPoco(procesoAnterior)){
					if(!proceso.getTareas().isEmpty()){
						Proceso procesoNuevo = new Proceso();
						procesoNuevo.setDescripcion(proceso.getDescripcion());
						procesoNuevo.setEstado(proceso.getEstado());
						procesoNuevo.setObservaciones(proceso.getObservaciones());
						procesoNuevo.setParte(proceso.getParte());
						procesoNuevo.setTiempoTeoricoPreparacion(proceso.getTiempoTeoricoPreparacion());
						procesoNuevo.setTiempoTeoricoProceso(proceso.getTiempoTeoricoProceso());
						procesoNuevo.setTipo(proceso.getTipo());
						procesoNuevo.getHerramientas().clear();
						procesoNuevo.getHerramientas().addAll(proceso.getHerramientas());
						procesoNuevo.getPiezas().clear();
						procesoNuevo.getPiezas().addAll(proceso.getPiezas());

						persistidorProceso.bajaTareas(this.listarTareas(new FiltroTarea.Builder().noEstado(EstadoTareaStr.FINALIZADA).proceso(proceso).build()));
						this.eliminarProceso(procesoAnterior);
						persistidorProceso.guardarProceso(procesoNuevo);
						return resultado;
					}
				}
			}
			persistidorProceso.actualizarProceso(proceso);
		}
		return resultado;
	}

	private ResultadoModificarProceso validarModificarProceso(Proceso proceso) throws PersistenciaException {
		Set<ErrorModificarProceso> errores = new HashSet<>();

		//Veo si el proceso no tiene parte
		if(proceso.getParte() == null){
			errores.add(ErrorModificarProceso.PARTE_INCOMPLETA);
		}

		//Veo si el proceso no tiene descripcion
		if(proceso.getDescripcion() == null || proceso.getDescripcion().isEmpty()){
			errores.add(ErrorModificarProceso.DESCRIPCION_PROCESO_INCOMPLETA);
		}

		//Veo si el proceso no tiene tipo
		if(proceso.getTipo() == null || proceso.getTipo().isEmpty()){
			errores.add(ErrorModificarProceso.TIPO_PROCESO_INCOMPLETO);
		}

		//Veo si el proceso no tiene tiempo teórico de preparación
		if(proceso.getTiempoTeoricoProceso() == null || proceso.getTiempoTeoricoProceso() < 1){
			errores.add(ErrorModificarProceso.TIEMPO_TEORICO_PROCESO_INCOMPLETO);
		}

		//Veo si el proceso no tiene tiempo teórico de proceso
		if(proceso.getTiempoTeoricoPreparacion() == null || proceso.getTiempoTeoricoPreparacion() < 1){
			errores.add(ErrorModificarProceso.TIEMPO_TEORICO_PREPARACION_INCOMPLETO);
		}

		if(!errores.contains(ErrorModificarProceso.PARTE_INCOMPLETA) && !errores.contains(ErrorModificarProceso.DESCRIPCION_PROCESO_INCOMPLETA) && !errores.contains(ErrorModificarProceso.TIPO_PROCESO_INCOMPLETO)){
			//Veo si hay otro proceso con la misma máquina, parte, descripción y tipo
			List<Proceso> procesosCoincidentes = persistidorProceso.obtenerProcesos(new FiltroProceso.Builder().parte(proceso.getParte()).descripcionExacta(proceso.getDescripcion()).tipoExacto(proceso.getTipo()).build());
			procesosCoincidentes.remove(proceso);
			if(!procesosCoincidentes.isEmpty()){
				errores.add(ErrorModificarProceso.MAQUINA_PARTE_DESCRIPCION_Y_TIPO_REPETIDO);
			}
		}

		return new ResultadoModificarProceso(errores.toArray(new ErrorModificarProceso[0]));
	}

	public ResultadoEliminarProceso eliminarProceso(Proceso proceso) throws PersistenciaException {
		ResultadoEliminarProceso resultadoEliminarProceso = validarEliminarProceso(proceso);
		if(!resultadoEliminarProceso.hayErrores()){
			//si el proceso tiene tareas asociadas, se le da baja lógica
			ArrayList<Tarea> tareasDelProceso = persistidorProceso.obtenerTareas(new FiltroTarea.Builder().proceso(proceso).build());

			if(!tareasDelProceso.isEmpty()){
				//si el proceso tiene tareas asociadas, se le da de baja lógica
				proceso.darDeBaja();
				persistidorProceso.actualizarProceso(proceso);
			}
			else{
				//sino de baja física
				try{
					persistidorProceso.bajaProceso(proceso);
				} catch(ObjNotFoundException e){
					//Si no se encontró ya fue eliminado previamente.
				}
			}
		}
		return resultadoEliminarProceso;
	}

	private ResultadoEliminarProceso validarEliminarProceso(Proceso proceso) throws PersistenciaException {
		return new ResultadoEliminarProceso();
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
		return resultado;
	}

	private ResultadoCrearTarea validarCrearTarea(Tarea tarea) {
		Set<ErrorCrearTarea> errores = new HashSet<>();

		//Veo si la tarea no tiene operario
		if(tarea.getOperario() == null){
			errores.add(ErrorCrearTarea.OPERARIO_INCOMPLETO);
		}

		//Veo si la tarea no tiene proceso
		if(tarea.getProceso() == null){
			errores.add(ErrorCrearTarea.PROCESO_INCOMPLETO);
		}

		//Veo si la tarea no tiene cantidad teórica
		if(tarea.getCantidadTeorica() == null || tarea.getCantidadTeorica() < 1){
			errores.add(ErrorCrearTarea.CANTIDAD_INCOMPLETA);
		}

		//Veo si la tarea no tiene fecha o tiene una fecha anterior a hoy
		Date hoy = conversorTiempos.getDate(conversorTiempos.getLocalDate(new Date()));
		if(tarea.getFechaPlanificada() == null){
			errores.add(ErrorCrearTarea.FECHA_INCOMPLETA);
		}
		else if(tarea.getFechaPlanificada().before(hoy)){
			errores.add(ErrorCrearTarea.FECHA_ANTERIOR_A_HOY);
		}

		return new ResultadoCrearTarea(errores.toArray(new ErrorCrearTarea[0]));
	}

	public ResultadoModificarTarea modificarTarea(Tarea tarea) throws PersistenciaException {
		ResultadoModificarTarea resultado = validarModificarTarea(tarea);
		if(!resultado.hayErrores()){
			persistidorProceso.actualizarTarea(tarea);
		}
		return resultado;
	}

	private ResultadoModificarTarea validarModificarTarea(Tarea tarea) {
		Set<ErrorModificarTarea> errores = new HashSet<>();

		//Veo si la tarea no tiene operario
		if(tarea.getOperario() == null){
			errores.add(ErrorModificarTarea.OPERARIO_INCOMPLETO);
		}

		//Veo si la tarea no tiene proceso
		if(tarea.getProceso() == null){
			errores.add(ErrorModificarTarea.PROCESO_INCOMPLETO);
		}

		//Veo si la tarea no tiene cantidad teórica
		if(tarea.getCantidadTeorica() == null || tarea.getCantidadTeorica() < 1){
			errores.add(ErrorModificarTarea.CANTIDAD_INCOMPLETA);
		}

		//Veo si la tarea no tiene fecha o tiene una fecha anterior a hoy
		Date hoy = conversorTiempos.getDate(conversorTiempos.getLocalDate(new Date()));
		if(tarea.getFechaPlanificada() == null){
			errores.add(ErrorModificarTarea.FECHA_INCOMPLETA);
		}
		else if(tarea.getFechaPlanificada().before(hoy)){
			errores.add(ErrorModificarTarea.FECHA_ANTERIOR_A_HOY);
		}

		//Veo si la tarea está planificada
		if(!EstadoTareaStr.PLANIFICADA.equals(tarea.getEstado().getNombre())){
			errores.add(ErrorModificarTarea.TAREA_NO_PLANIFICADA);
		}

		return new ResultadoModificarTarea(errores.toArray(new ErrorModificarTarea[0]));
	}

	public ResultadoEliminarTareas eliminarTareas(ArrayList<Tarea> tareas) throws PersistenciaException {
		ResultadoEliminarTareas resultado = validarEliminarTareas(tareas);
		if(!resultado.hayErrores()){
			persistidorProceso.bajaTareas(tareas);
		}
		return resultado;
	}

	private ResultadoEliminarTareas validarEliminarTareas(ArrayList<Tarea> tareas) {
		Set<ErrorEliminarTareas> errores = new HashSet<>();
		for(Tarea tarea: tareas){
			if(EstadoTareaStr.FINALIZADA.equals(tarea.getEstado().getNombre())){
				errores.add(ErrorEliminarTareas.HAY_TAREA_FINALIZADA);
			}
		}
		return new ResultadoEliminarTareas(errores.toArray(new ErrorEliminarTareas[0]));
	}

	public ResultadoEliminarTarea eliminarTarea(Tarea tarea) throws PersistenciaException {
		ResultadoEliminarTarea resultado = validarEliminarTarea(tarea);
		if(!resultado.hayErrores()){
			persistidorProceso.bajaTarea(tarea);
		}
		return resultado;
	}

	private ResultadoEliminarTarea validarEliminarTarea(Tarea tarea) {
		Set<ErrorEliminarTarea> errores = new HashSet<>();
		if(!EstadoTareaStr.PLANIFICADA.equals(tarea.getEstado().getNombre())){
			errores.add(ErrorEliminarTarea.TAREA_NO_PLANIFICADA);
		}
		return new ResultadoEliminarTarea(errores.toArray(new ErrorEliminarTarea[0]));
	}

	public ResultadoModificarEstadoTarea comenzarTarea(Tarea tarea) throws PersistenciaException {
		ResultadoModificarEstadoTarea resultado = validarComenzarTarea(tarea);
		if(!resultado.hayErrores()){
			tarea.comenzar();
			persistidorProceso.actualizarTarea(tarea);
		}
		return resultado;
	}

	private ResultadoModificarEstadoTarea validarComenzarTarea(Tarea tarea) throws PersistenciaException {
		Set<ErrorModificarEstadoTarea> errores = new HashSet<>();
		if(!EstadoTareaStr.PLANIFICADA.equals(tarea.getEstado().getNombre())){
			errores.add(ErrorModificarEstadoTarea.ERROR_ESTADO_TRANSICION);
		}
		if(tarea.getFechaHoraInicio() == null){
			errores.add(ErrorModificarEstadoTarea.DATOS_INCOMPLETOS);
		}
		else if(tarea.getFechaHoraInicio().after(new Date())){
			errores.add(ErrorModificarEstadoTarea.DATOS_INVALIDOS);
		}
		if(tarea.getCantidadReal() != null || tarea.getFechaHoraFin() != null || tarea.getObservacionesOperario() != null || !tarea.getPausas().isEmpty()){
			errores.add(ErrorModificarEstadoTarea.DATOS_INVALIDOS);
		}
		return new ResultadoModificarEstadoTarea(errores.toArray(new ErrorModificarEstadoTarea[0]));
	}

	public ResultadoModificarEstadoTarea pausarTarea(Tarea tarea) throws PersistenciaException {
		ResultadoModificarEstadoTarea resultado = validarPausarTarea(tarea);
		if(!resultado.hayErrores()){
			tarea.pausar();
			persistidorProceso.actualizarTarea(tarea);
		}
		return resultado;
	}

	private ResultadoModificarEstadoTarea validarPausarTarea(Tarea tarea) throws PersistenciaException {
		Set<ErrorModificarEstadoTarea> errores = new HashSet<>();
		if(!EstadoTareaStr.EJECUTANDO.equals(tarea.getEstado().getNombre())){
			errores.add(ErrorModificarEstadoTarea.ERROR_ESTADO_TRANSICION);
		}
		if(tarea.getFechaHoraInicio() == null || tarea.getPausas().isEmpty()){
			errores.add(ErrorModificarEstadoTarea.DATOS_INCOMPLETOS);
		}

		Pausa ultimaPausa = null;
		for(Pausa pausa: tarea.getPausas()){
			if(pausa.getFechaHoraFin() == null){
				if(ultimaPausa != null){
					errores.add(ErrorModificarEstadoTarea.DATOS_INVALIDOS);
				}
				else{
					ultimaPausa = pausa;
				}
			}

			if(pausa.getCausa() == null || pausa.getCausa().isEmpty()){
				errores.add(ErrorModificarEstadoTarea.DATOS_INVALIDOS);
			}
			if(pausa.getFechaHoraInicio() == null){
				errores.add(ErrorModificarEstadoTarea.DATOS_INVALIDOS);
			}
		}

		if(tarea.getCantidadReal() != null || tarea.getFechaHoraFin() != null || tarea.getObservacionesOperario() != null){
			errores.add(ErrorModificarEstadoTarea.DATOS_INVALIDOS);
		}
		return new ResultadoModificarEstadoTarea(errores.toArray(new ErrorModificarEstadoTarea[0]));
	}

	public ResultadoModificarEstadoTarea reanudarTarea(Tarea tarea) throws PersistenciaException {
		ResultadoModificarEstadoTarea resultado = validarReanudarTarea(tarea);
		if(!resultado.hayErrores()){
			tarea.reanudar();
			persistidorProceso.actualizarTarea(tarea);
		}
		return resultado;
	}

	private ResultadoModificarEstadoTarea validarReanudarTarea(Tarea tarea) throws PersistenciaException {
		Set<ErrorModificarEstadoTarea> errores = new HashSet<>();
		if(!EstadoTareaStr.PAUSADA.equals(tarea.getEstado().getNombre())){
			errores.add(ErrorModificarEstadoTarea.ERROR_ESTADO_TRANSICION);
		}
		if(tarea.getFechaHoraInicio() == null || tarea.getPausas().isEmpty()){
			errores.add(ErrorModificarEstadoTarea.DATOS_INCOMPLETOS);
		}

		for(Pausa pausa: tarea.getPausas()){
			if(pausa.getFechaHoraInicio() == null){
				errores.add(ErrorModificarEstadoTarea.DATOS_INVALIDOS);
			}
			if(pausa.getFechaHoraFin() == null){
				errores.add(ErrorModificarEstadoTarea.DATOS_INVALIDOS);
			}
			if(pausa.getCausa() == null || pausa.getCausa().isEmpty()){
				errores.add(ErrorModificarEstadoTarea.DATOS_INVALIDOS);
			}
		}

		if(tarea.getCantidadReal() != null || tarea.getFechaHoraFin() != null || tarea.getObservacionesOperario() != null){
			errores.add(ErrorModificarEstadoTarea.DATOS_INVALIDOS);
		}
		return new ResultadoModificarEstadoTarea(errores.toArray(new ErrorModificarEstadoTarea[0]));
	}

	public ResultadoModificarEstadoTarea terminarTarea(Tarea tarea) throws PersistenciaException {
		ResultadoModificarEstadoTarea resultado = validarTerminarTarea(tarea);
		if(!resultado.hayErrores()){
			tarea.terminar();
			persistidorProceso.actualizarTarea(tarea);
		}
		return resultado;
	}

	private ResultadoModificarEstadoTarea validarTerminarTarea(Tarea tarea) throws PersistenciaException {
		Set<ErrorModificarEstadoTarea> errores = new HashSet<>();
		if(!EstadoTareaStr.EJECUTANDO.equals(tarea.getEstado().getNombre())){
			errores.add(ErrorModificarEstadoTarea.ERROR_ESTADO_TRANSICION);
		}
		if(tarea.getFechaHoraInicio() == null || tarea.getCantidadReal() == null || tarea.getFechaHoraFin() == null){
			errores.add(ErrorModificarEstadoTarea.DATOS_INCOMPLETOS);
		}

		for(Pausa pausa: tarea.getPausas()){
			if(pausa.getFechaHoraInicio() == null){
				errores.add(ErrorModificarEstadoTarea.DATOS_INVALIDOS);
			}
			if(pausa.getFechaHoraFin() == null){
				errores.add(ErrorModificarEstadoTarea.DATOS_INVALIDOS);
			}
			if(pausa.getCausa() == null || pausa.getCausa().isEmpty()){
				errores.add(ErrorModificarEstadoTarea.DATOS_INVALIDOS);
			}
		}

		if(tarea.getCantidadReal() != null && tarea.getCantidadReal() < 1){
			errores.add(ErrorModificarEstadoTarea.DATOS_INVALIDOS);
		}

		return new ResultadoModificarEstadoTarea(errores.toArray(new ErrorModificarEstadoTarea[0]));
	}

	public ResultadoModificarEstadoTarea cancelarTarea(Tarea tarea) throws PersistenciaException {
		ResultadoModificarEstadoTarea resultado = validarCancelarTarea(tarea);
		if(!resultado.hayErrores()){
			tarea.cancelar();
			persistidorProceso.actualizarTarea(tarea);
		}
		return resultado;
	}

	private ResultadoModificarEstadoTarea validarCancelarTarea(Tarea tarea) throws PersistenciaException {
		Set<ErrorModificarEstadoTarea> errores = new HashSet<>();
		if(!EstadoTareaStr.EJECUTANDO.equals(tarea.getEstado().getNombre()) && !EstadoTareaStr.PAUSADA.equals(tarea.getEstado().getNombre())){
			errores.add(ErrorModificarEstadoTarea.ERROR_ESTADO_TRANSICION);
		}
		if(tarea.getFechaHoraInicio() != null && tarea.getCantidadReal() != null || tarea.getFechaHoraFin() != null || tarea.getObservacionesOperario() != null || !tarea.getPausas().isEmpty()){
			errores.add(ErrorModificarEstadoTarea.DATOS_INVALIDOS);
		}
		return new ResultadoModificarEstadoTarea(errores.toArray(new ErrorModificarEstadoTarea[0]));
	}
}
