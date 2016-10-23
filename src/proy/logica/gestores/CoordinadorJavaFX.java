/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import proy.datos.clases.DatosLogin;
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
import proy.logica.gestores.resultados.ResultadoEliminarParte;
import proy.logica.gestores.resultados.ResultadoEliminarPieza;
import proy.logica.gestores.resultados.ResultadoEliminarProceso;
import proy.logica.gestores.resultados.ResultadoEliminarTarea;
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

	public ResultadoEliminarParte eliminarParte(Parte parte) throws PersistenciaException {
		return gestorTaller.eliminarParte(parte);
	}

	public ArrayList<Pieza> listarPiezas(FiltroPieza filtro) throws PersistenciaException {
		return gestorTaller.listarPiezas(filtro);
	}

	public ResultadoCrearPieza crearPieza(Pieza pieza) throws PersistenciaException {
		return gestorTaller.crearPieza(pieza);
	}

	public ResultadoEliminarPieza eliminarPieza(Pieza pieza) throws PersistenciaException {
		return gestorTaller.eliminarPieza(pieza);
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
