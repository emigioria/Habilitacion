/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import proy.datos.clases.DatosLogin;
import proy.datos.clases.EstadoTareaStr;
import proy.datos.entidades.Comentario;
import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Maquina;
import proy.datos.entidades.Material;
import proy.datos.entidades.Operario;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.datos.entidades.Proceso;
import proy.datos.entidades.Tarea;
import proy.datos.filtros.Filtro;
import proy.datos.filtros.implementacion.FiltroTarea;
import proy.excepciones.PersistenciaException;
import proy.logica.gestores.MaterialGestor;
import proy.logica.gestores.ProcesoGestor;
import proy.logica.gestores.TallerGestor;
import proy.logica.gestores.UsuarioGestor;
import proy.logica.gestores.resultados.ResultadoAutenticacion;
import proy.logica.gestores.resultados.ResultadoCrearComentario;
import proy.logica.gestores.resultados.ResultadoCrearHerramientas;
import proy.logica.gestores.resultados.ResultadoCrearMaquina;
import proy.logica.gestores.resultados.ResultadoCrearMateriales;
import proy.logica.gestores.resultados.ResultadoCrearOperarios;
import proy.logica.gestores.resultados.ResultadoCrearParte;
import proy.logica.gestores.resultados.ResultadoCrearPieza;
import proy.logica.gestores.resultados.ResultadoCrearProceso;
import proy.logica.gestores.resultados.ResultadoCrearTarea;
import proy.logica.gestores.resultados.ResultadoEliminarHerramienta;
import proy.logica.gestores.resultados.ResultadoEliminarHerramienta.ErrorEliminarHerramienta;
import proy.logica.gestores.resultados.ResultadoEliminarMaquina;
import proy.logica.gestores.resultados.ResultadoEliminarMaterial;
import proy.logica.gestores.resultados.ResultadoEliminarOperario;
import proy.logica.gestores.resultados.ResultadoEliminarOperario.ErrorEliminarOperario;
import proy.logica.gestores.resultados.ResultadoEliminarPartes;
import proy.logica.gestores.resultados.ResultadoEliminarPartes.ErrorEliminarPartes;
import proy.logica.gestores.resultados.ResultadoEliminarPiezas;
import proy.logica.gestores.resultados.ResultadoEliminarPiezas.ErrorEliminarPiezas;
import proy.logica.gestores.resultados.ResultadoEliminarProceso;
import proy.logica.gestores.resultados.ResultadoEliminarTarea;
import proy.logica.gestores.resultados.ResultadoEliminarTareas;
import proy.logica.gestores.resultados.ResultadoModificarMaquina;
import proy.logica.gestores.resultados.ResultadoModificarPartes;
import proy.logica.gestores.resultados.ResultadoModificarProceso;
import proy.logica.gestores.resultados.ResultadoModificarTarea;

@Service
public class CoordinadorJavaFX {

	@Resource
	private UsuarioGestor gestorUsuario;

	@Resource
	private TallerGestor gestorTaller;

	@Resource
	private MaterialGestor gestorMaterial;

	@Resource
	private ProcesoGestor gestorProceso;

	public ResultadoAutenticacion autenticarAdministrador(DatosLogin login) throws PersistenciaException {
		return gestorUsuario.autenticarAdministrador(login);
	}

	public ResultadoCrearComentario crearComentario(Comentario comentario) throws PersistenciaException {
		return gestorUsuario.crearComentario(comentario);
	}

	public ArrayList<Comentario> listarComentarios(Filtro<Comentario> filtro) throws PersistenciaException {
		return gestorUsuario.listarComentarios(filtro);
	}

	public ArrayList<Operario> listarOperarios(Filtro<Operario> filtro) throws PersistenciaException {
		return gestorUsuario.listarOperarios(filtro);
	}

	public ResultadoCrearOperarios crearOperarios(ArrayList<Operario> operarios) throws PersistenciaException {
		return gestorUsuario.crearOperarios(operarios);
	}

	public ResultadoEliminarOperario eliminarOperario(Operario operario) throws PersistenciaException {
		//Se eliminan las tareas del operario
		ResultadoEliminarTareas resultadoEliminarTareas = gestorProceso.eliminarTareas(gestorProceso.listarTareas(new FiltroTarea.Builder().noEstado(EstadoTareaStr.FINALIZADA).operario(operario).build()));
		if(resultadoEliminarTareas.hayErrores()){
			return new ResultadoEliminarOperario(resultadoEliminarTareas, ErrorEliminarOperario.ERROR_AL_ELIMINAR_TAREAS);
		}

		//Se continúa con la eliminación del operario
		return gestorUsuario.eliminarOperario(operario);
	}

	public ArrayList<Maquina> listarMaquinas(Filtro<Maquina> filtro) throws PersistenciaException {
		return gestorTaller.listarMaquinas(filtro);
	}

	public ResultadoCrearMaquina crearMaquina(Maquina maquina) throws PersistenciaException {
		return gestorTaller.crearMaquina(maquina);
	}

	public ResultadoModificarMaquina modificarMaquina(Maquina maquina) throws PersistenciaException {
		return gestorTaller.modificarMaquina(maquina);
	}

	public ResultadoEliminarMaquina eliminarMaquina(Maquina maquina) throws PersistenciaException {
		return gestorTaller.eliminarMaquina(maquina);
	}

	public ArrayList<Parte> listarPartes(Filtro<Parte> filtro) throws PersistenciaException {
		return gestorTaller.listarPartes(filtro);
	}

	public ResultadoCrearParte crearParte(Parte parte) throws PersistenciaException {
		return gestorTaller.crearParte(parte);
	}

	public ResultadoModificarPartes modificarParte(ArrayList<Parte> partes) throws PersistenciaException {
		return gestorTaller.modificarPartes(partes);
	}

	public ResultadoEliminarPartes eliminarPartes(ArrayList<Parte> partes) throws PersistenciaException {
		ResultadoEliminarTareas resultadoEliminarTareas = gestorProceso.eliminarTareas(gestorProceso.listarTareas(new FiltroTarea.Builder().noEstado(EstadoTareaStr.FINALIZADA).partes(partes).build()));
		if(resultadoEliminarTareas.hayErrores()){
			return new ResultadoEliminarPartes(resultadoEliminarTareas, ErrorEliminarPartes.ERROR_AL_ELIMINAR_TAREAS);
		}

		return gestorTaller.eliminarPartes(partes);
	}

	public Boolean tieneTareasNoTerminadasAsociadas(Parte parte) throws PersistenciaException {
		ArrayList<Tarea> tareasNoTerminadasAsociadas = gestorProceso.listarTareas(new FiltroTarea.Builder().noEstado(EstadoTareaStr.FINALIZADA).parte(parte).build());
		return tareasNoTerminadasAsociadas.size() > 0;
	}

	public ArrayList<Pieza> listarPiezas(Filtro<Pieza> filtro) throws PersistenciaException {
		return gestorTaller.listarPiezas(filtro);
	}

	public ResultadoCrearPieza crearPieza(Pieza pieza) throws PersistenciaException {
		return gestorTaller.crearPieza(pieza);
	}

	public ResultadoEliminarPiezas eliminarPiezas(ArrayList<Pieza> piezasAEliminar) throws PersistenciaException {
		ResultadoEliminarTareas resultadoEliminarTareas = gestorProceso.eliminarTareas(gestorProceso.listarTareas(new FiltroTarea.Builder().noEstado(EstadoTareaStr.FINALIZADA).piezas(piezasAEliminar).build()));
		if(resultadoEliminarTareas.hayErrores()){
			return new ResultadoEliminarPiezas(resultadoEliminarTareas, null, ErrorEliminarPiezas.ERROR_AL_ELIMINAR_TAREAS);
		}

		return gestorTaller.eliminarPiezas(piezasAEliminar);
	}

	public Boolean tieneTareasNoTerminadasAsociadas(Pieza pieza) throws PersistenciaException {
		ArrayList<Tarea> tareasNoTerminadasAsociadas = gestorProceso.listarTareas(new FiltroTarea.Builder().noEstado(EstadoTareaStr.FINALIZADA).pieza(pieza).build());
		return tareasNoTerminadasAsociadas.size() > 0;
	}

	public ArrayList<Herramienta> listarHerramientas(Filtro<Herramienta> filtro) throws PersistenciaException {
		return gestorMaterial.listarHerramientas(filtro);
	}

	public ResultadoCrearHerramientas crearHerramientas(ArrayList<Herramienta> herramientas) throws PersistenciaException {
		return gestorMaterial.crearHerramientas(herramientas);
	}

	public ResultadoEliminarHerramienta eliminarHerramienta(Herramienta herramienta) throws PersistenciaException {
		//Se eliminan las tareas de la herramienta
		ResultadoEliminarTareas resultadoEliminarTareas = gestorProceso.eliminarTareas(gestorProceso.listarTareas(new FiltroTarea.Builder().noEstado(EstadoTareaStr.FINALIZADA).herramienta(herramienta).build()));
		if(resultadoEliminarTareas.hayErrores()){
			return new ResultadoEliminarHerramienta(resultadoEliminarTareas, ErrorEliminarHerramienta.ERROR_AL_ELIMINAR_TAREAS);
		}

		//Se continúa con la eliminación de la herramienta
		return gestorMaterial.eliminarHerramienta(herramienta);
	}

	public Boolean tieneTareasNoTerminadasAsociadas(Herramienta herramienta) throws PersistenciaException {
		ArrayList<Tarea> tareasNoTerminadasAsociadas = gestorProceso.listarTareas(new FiltroTarea.Builder().noEstado(EstadoTareaStr.FINALIZADA).herramienta(herramienta).build());
		return tareasNoTerminadasAsociadas.size() > 0;
	}

	public ArrayList<Material> listarMateriales(Filtro<Material> filtro) throws PersistenciaException {
		return gestorMaterial.listarMateriales(filtro);
	}

	public ResultadoCrearMateriales crearMateriales(ArrayList<Material> materiales) throws PersistenciaException {
		return gestorMaterial.crearMateriales(materiales);
	}

	public ResultadoEliminarMaterial eliminarMaterial(Material material) throws PersistenciaException {
		return gestorMaterial.eliminarMaterial(material);
	}

	public ArrayList<Proceso> listarProcesos(Filtro<Proceso> filtro) throws PersistenciaException {
		return gestorProceso.listarProcesos(filtro);
	}

	public ResultadoCrearProceso crearProceso(Proceso proceso) throws PersistenciaException {
		return gestorProceso.crearProceso(proceso);
	}

	public ResultadoModificarProceso modificarProceso(Proceso proceso) throws PersistenciaException {
		return gestorProceso.modificarProceso(proceso);
	}

	public ResultadoEliminarProceso eliminarProceso(Proceso proceso) throws PersistenciaException {
		return gestorProceso.eliminarProceso(proceso);
	}

	public ArrayList<Tarea> listarTareas(Filtro<Tarea> filtro) throws PersistenciaException {
		return gestorProceso.listarTareas(filtro);
	}

	public ResultadoCrearTarea crearTarea(Tarea tarea) throws PersistenciaException {
		return gestorProceso.crearTarea(tarea);
	}

	public ResultadoModificarTarea modificarTarea(Tarea tarea) throws PersistenciaException {
		return gestorProceso.modificarTarea(tarea);
	}

	public ResultadoEliminarTarea eliminarTarea(Tarea tarea) throws PersistenciaException {
		return gestorProceso.eliminarTarea(tarea);
	}

	public ResultadoModificarTarea comenzarTarea(Tarea tarea) throws PersistenciaException {
		return gestorProceso.comenzarTarea(tarea);
	}

	public ResultadoModificarTarea pausarTarea(Tarea tarea) throws PersistenciaException {
		return gestorProceso.pausarTarea(tarea);
	}

	public ResultadoModificarTarea reanudarTarea(Tarea tarea) throws PersistenciaException {
		return gestorProceso.reanudarTarea(tarea);
	}

	public ResultadoModificarTarea terminarTarea(Tarea tarea) throws PersistenciaException {
		return gestorProceso.terminarTarea(tarea);
	}

	public ResultadoModificarTarea cancelarTarea(Tarea tarea) throws PersistenciaException {
		return gestorProceso.cancelarTarea(tarea);
	}

}
