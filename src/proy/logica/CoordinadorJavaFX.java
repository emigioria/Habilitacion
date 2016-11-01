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
import proy.excepciones.PersistenciaException;
import proy.logica.gestores.ProcesoGestor;
import proy.logica.gestores.TallerGestor;
import proy.logica.gestores.UsuarioGestor;
import proy.logica.gestores.filtros.FiltroComentario;
import proy.logica.gestores.filtros.FiltroHerramienta;
import proy.logica.gestores.filtros.FiltroMaquina;
import proy.logica.gestores.filtros.FiltroMaterial;
import proy.logica.gestores.filtros.FiltroOperario;
import proy.logica.gestores.filtros.FiltroParte;
import proy.logica.gestores.filtros.FiltroPieza;
import proy.logica.gestores.filtros.FiltroProceso;
import proy.logica.gestores.filtros.FiltroTarea;
import proy.logica.gestores.resultados.ResultadoAutenticacion;
import proy.logica.gestores.resultados.ResultadoCrearComentario;
import proy.logica.gestores.resultados.ResultadoCrearHerramienta;
import proy.logica.gestores.resultados.ResultadoCrearMaquina;
import proy.logica.gestores.resultados.ResultadoCrearMateriales;
import proy.logica.gestores.resultados.ResultadoCrearOperario;
import proy.logica.gestores.resultados.ResultadoCrearParte;
import proy.logica.gestores.resultados.ResultadoCrearPieza;
import proy.logica.gestores.resultados.ResultadoCrearProceso;
import proy.logica.gestores.resultados.ResultadoCrearTarea;
import proy.logica.gestores.resultados.ResultadoEliminarHerramienta;
import proy.logica.gestores.resultados.ResultadoEliminarMaquina;
import proy.logica.gestores.resultados.ResultadoEliminarMateriales;
import proy.logica.gestores.resultados.ResultadoEliminarOperario;
import proy.logica.gestores.resultados.ResultadoEliminarPartes;
import proy.logica.gestores.resultados.ResultadoEliminarPartes.ErrorEliminarPartes;
import proy.logica.gestores.resultados.ResultadoEliminarPiezas;
import proy.logica.gestores.resultados.ResultadoEliminarPiezas.ErrorEliminarPiezas;
import proy.logica.gestores.resultados.ResultadoEliminarProceso;
import proy.logica.gestores.resultados.ResultadoEliminarTarea;
import proy.logica.gestores.resultados.ResultadoEliminarTareas;
import proy.logica.gestores.resultados.ResultadoModificarMaquina;
import proy.logica.gestores.resultados.ResultadoModificarParte;
import proy.logica.gestores.resultados.ResultadoModificarProceso;
import proy.logica.gestores.resultados.ResultadoModificarTarea;

@Service
public class CoordinadorJavaFX {

	@Resource
	private UsuarioGestor gestorUsuario;

	@Resource
	private TallerGestor gestorTaller;

	@Resource
	private ProcesoGestor gestorProceso;

	public ResultadoAutenticacion autenticarAdministrador(DatosLogin login) throws PersistenciaException {
		return gestorUsuario.autenticarAdministrador(login);
	}

	public ResultadoCrearComentario crearComentario(Comentario comentario) throws PersistenciaException {
		return gestorUsuario.crearComentario(comentario);
	}

	public ArrayList<Comentario> listarComentarios(FiltroComentario filtro) throws PersistenciaException {
		return gestorUsuario.listarComentarios(filtro);
	}

	public ArrayList<Operario> listarOperarios(FiltroOperario filtro) throws PersistenciaException {
		return gestorUsuario.listarOperarios(filtro);
	}

	public ResultadoCrearOperario crearOperario(Operario operario) throws PersistenciaException {
		return gestorUsuario.crearOperario(operario);
	}

	public ResultadoEliminarOperario eliminarOperario(Operario operario) throws PersistenciaException {
		return gestorUsuario.eliminarOperario(operario);
	}

	public ArrayList<Maquina> listarMaquinas(FiltroMaquina filtro) throws PersistenciaException {
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

	public ArrayList<Parte> listarPartes(FiltroParte filtro) throws PersistenciaException {
		return gestorTaller.listarPartes(filtro);
	}

	public ResultadoCrearParte crearParte(Parte parte) throws PersistenciaException {
		return gestorTaller.crearParte(parte);
	}

	public ResultadoModificarParte modificarParte(Parte parte) throws PersistenciaException {
		return gestorTaller.modificarParte(parte);
	}

	public ResultadoEliminarPartes eliminarPartes(ArrayList<Parte> partes) throws PersistenciaException {
		ResultadoEliminarTareas resultadoEliminarTareas = gestorProceso.eliminarTareas(gestorProceso.listarTareas(new FiltroTarea.Builder().noEstado(EstadoTareaStr.FINALIZADA).partes(partes).build()));
		if(resultadoEliminarTareas.hayErrores()){
			return new ResultadoEliminarPartes(resultadoEliminarTareas, null, ErrorEliminarPartes.ERROR_AL_ELIMINAR_TAREAS);
		}
		
		ResultadoEliminarPartes resultadoEliminarPartes = gestorTaller.eliminarPartes(partes);
		ArrayList<Parte> partesDadasBajaLogica = resultadoEliminarPartes.getPartesDadasBajaLogica();
		if(!partesDadasBajaLogica.isEmpty()){
			//dar de baja logica procesos
			ArrayList<Proceso> procesosABajaLogica = new ArrayList<>();
			for(Parte parte: partesDadasBajaLogica){
				procesosABajaLogica.addAll(gestorProceso.listarProcesos(new FiltroProceso.Builder().parte(parte).build()));
			}
			gestorProceso.bajaLogicaProcesos(procesosABajaLogica);
		}
		return resultadoEliminarPartes;
	}

	public Boolean tieneTareasNoTerminadasAsociadas(Parte parte) throws PersistenciaException {
		ArrayList<Tarea> tareasNoTerminadasAsociadas = gestorProceso.listarTareas(new FiltroTarea.Builder().noEstado(EstadoTareaStr.FINALIZADA).parte(parte).build());
		return tareasNoTerminadasAsociadas.size() > 0;
	}

	public ArrayList<Pieza> listarPiezas(FiltroPieza filtro) throws PersistenciaException {
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
		
		ResultadoEliminarPiezas resultadoEliminarPiezas = gestorTaller.eliminarPiezas(piezasAEliminar);
		ArrayList<Pieza> piezasDadasBajaLogica = resultadoEliminarPiezas.getPiezasDadasBajaLogica();
		if(!piezasDadasBajaLogica.isEmpty()){
			//dar de baja logica procesos
			ArrayList<Proceso> procesosABajaLogica = gestorProceso.listarProcesos(new FiltroProceso.Builder().piezas(piezasDadasBajaLogica).build());
			gestorProceso.bajaLogicaProcesos(procesosABajaLogica);
		}
		return resultadoEliminarPiezas;
	}

	public Boolean tieneTareasNoTerminadasAsociadas(Pieza pieza) throws PersistenciaException {
		ArrayList<Tarea> tareasNoTerminadasAsociadas = gestorProceso.listarTareas(new FiltroTarea.Builder().noEstado(EstadoTareaStr.FINALIZADA).pieza(pieza).build());
		return tareasNoTerminadasAsociadas.size() > 0;
	}

	public ArrayList<Herramienta> listarHerramientas(FiltroHerramienta filtro) throws PersistenciaException {
		return gestorTaller.listarHerramientas(filtro);
	}

	public ResultadoCrearHerramienta crearHerramienta(Herramienta herramienta) throws PersistenciaException {
		return gestorTaller.crearHerramienta(herramienta);
	}

	public ResultadoEliminarHerramienta eliminarHerramienta(Herramienta herramienta) throws PersistenciaException {
		return gestorTaller.eliminarHerramienta(herramienta);
	}

	public Boolean tieneTareasNoTerminadasAsociadas(Herramienta herramienta) throws PersistenciaException {
		ArrayList<Tarea> tareasNoTerminadasAsociadas = gestorProceso.listarTareas(new FiltroTarea.Builder().noEstado(EstadoTareaStr.FINALIZADA).herramienta(herramienta).build());
		return tareasNoTerminadasAsociadas.size() > 0;
	}

	public ArrayList<Material> listarMateriales(FiltroMaterial filtro) throws PersistenciaException {
		return gestorTaller.listarMateriales(filtro);
	}

	public ResultadoCrearMateriales crearMateriales(ArrayList<Material> materiales) throws PersistenciaException {
		return gestorTaller.crearMateriales(materiales);
	}

	public ResultadoEliminarMateriales eliminarMateriales(ArrayList<Material> materiales) throws PersistenciaException {
		return gestorTaller.eliminarMateriales(materiales);
	}

	public ArrayList<Proceso> listarProcesos(FiltroProceso filtro) throws PersistenciaException {
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

	public ArrayList<Tarea> listarTareas(FiltroTarea filtro) throws PersistenciaException {
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
