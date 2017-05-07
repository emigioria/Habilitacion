/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import proy.datos.clases.EstadoStr;
import proy.datos.entidades.Maquina;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.datos.entidades.Proceso;
import proy.datos.entidades.Tarea;
import proy.datos.filtros.Filtro;
import proy.datos.filtros.implementacion.FiltroMaquina;
import proy.datos.filtros.implementacion.FiltroTarea;
import proy.datos.servicios.ProcesoService;
import proy.datos.servicios.TallerService;
import proy.excepciones.ObjNotFoundException;
import proy.excepciones.PersistenciaException;
import proy.logica.gestores.resultados.ResultadoCrearMaquina;
import proy.logica.gestores.resultados.ResultadoCrearModificarPartesAlModificarMaquina;
import proy.logica.gestores.resultados.ResultadoCrearPartesAlCrearMaquina;
import proy.logica.gestores.resultados.ResultadoCrearPiezasAlCrearMaquina;
import proy.logica.gestores.resultados.ResultadoCrearPiezasAlModificarMaquina;
import proy.logica.gestores.resultados.ResultadoEliminarMaquina;
import proy.logica.gestores.resultados.ResultadoEliminarParte;
import proy.logica.gestores.resultados.ResultadoEliminarPieza;
import proy.logica.gestores.resultados.ResultadoEliminarPiezasDeParte;
import proy.logica.gestores.resultados.ResultadoEliminarProcesos;
import proy.logica.gestores.resultados.ResultadoModificarMaquina;
import proy.logica.gestores.resultados.ResultadoCrearMaquina.ErrorCrearMaquina;
import proy.logica.gestores.resultados.ResultadoCrearModificarPartesAlModificarMaquina.ErrorCrearModificarPartesAlModificarMaquina;
import proy.logica.gestores.resultados.ResultadoCrearPartesAlCrearMaquina.ErrorCrearPartesAlCrearMaquina;
import proy.logica.gestores.resultados.ResultadoCrearPiezasAlCrearMaquina.ErrorCrearPiezasALCrearMaquina;
import proy.logica.gestores.resultados.ResultadoCrearPiezasAlModificarMaquina.ErrorCrearPiezasAlModificarMaquina;
import proy.logica.gestores.resultados.ResultadoEliminarParte.ErrorEliminarParte;
import proy.logica.gestores.resultados.ResultadoEliminarPieza.ErrorEliminarPieza;
import proy.logica.gestores.resultados.ResultadoModificarMaquina.ErrorModificarMaquina;

@Service
public class TallerGestor {

	@Resource
	private TallerService persistidorTaller;

	@Resource
	private ProcesoGestor gestorProceso;

	@Resource
	private ProcesoService persistidorProceso;

	public ArrayList<Maquina> listarMaquinas(Filtro<Maquina> filtro) throws PersistenciaException {
		return persistidorTaller.obtenerMaquinas(filtro);
	}

	public ResultadoCrearMaquina crearMaquina(Maquina maquina) throws PersistenciaException {
		ResultadoCrearMaquina resultado = validarCrearMaquina(maquina);

		if(!resultado.hayErrores()){
			persistidorTaller.guardarMaquina(maquina);
		}

		return resultado;
	}

	private ResultadoCrearMaquina validarCrearMaquina(Maquina maquina) throws PersistenciaException {
		ArrayList<ErrorCrearMaquina> errores = new ArrayList<>();
		if(maquina.getNombre() == null || maquina.getNombre().isEmpty()){
			errores.add(ErrorCrearMaquina.NOMBRE_INCOMPLETO);
		}
		else{
			ArrayList<Maquina> maquinasRepetidas = persistidorTaller.obtenerMaquinas(new FiltroMaquina.Builder().nombre(maquina.getNombre()).build());
			if(!maquinasRepetidas.isEmpty()){
				errores.add(ErrorCrearMaquina.NOMBRE_REPETIDO);
			}
		}

		//Valido la creación de las partes de la máquina
		ResultadoCrearPartesAlCrearMaquina resultadosCrearPartes = validarCrearPartesAlCrearMaquina(maquina);
		if(resultadosCrearPartes.hayErrores()){
			errores.add(ErrorCrearMaquina.ERROR_AL_CREAR_PARTES);
		}

		return new ResultadoCrearMaquina(resultadosCrearPartes, errores.toArray(new ErrorCrearMaquina[0]));
	}

	/**
	 * Valida que las partes que se están creando se pueden persistir.
	 * Incluye la validación de la creación de las piezas pertenecientes a esas partes
	 * Las partes pertenecen a una máquina nueva.
	 *
	 * @param maquinaDeLasPartesAValidar
	 *            maquina con las partes nuevas que se quieren validar.
	 * @return
	 */
	private ResultadoCrearPartesAlCrearMaquina validarCrearPartesAlCrearMaquina(Maquina maquinaDeLasPartesAValidar) {
		Map<String, ResultadoCrearPiezasAlCrearMaquina> resultadosCrearPiezas;
		Set<ErrorCrearPartesAlCrearMaquina> errores = new HashSet<>();
		List<Parte> partesAValidar = new ArrayList<>();

		//Busco partes sin cantidad
		for(Parte parteActual: maquinaDeLasPartesAValidar.getPartes()){
			if(parteActual.getCantidad() == null || parteActual.getCantidad() < 1){
				errores.add(ErrorCrearPartesAlCrearMaquina.CANTIDAD_INCOMPLETA);
			}
		}

		//Saco las partes que no tienen nombre
		for(Parte parteActual: maquinaDeLasPartesAValidar.getPartes()){
			if(parteActual.getNombre() == null || parteActual.getNombre().isEmpty()){
				errores.add(ErrorCrearPartesAlCrearMaquina.NOMBRE_INCOMPLETO);
			}
			else{
				partesAValidar.add(parteActual);
			}
		}

		//al resto las ordeno por nombre
		partesAValidar.sort((p1, p2) -> {
			return p1.getNombre().compareTo(p2.getNombre());
		});

		//busco coincidencias entre los nombres de las partes ingresadas
		Set<String> nombresRepetidos = new HashSet<>();
		if(!partesAValidar.isEmpty()){
			Iterator<Parte> it1 = partesAValidar.iterator();
			Parte anterior = it1.next();
			Parte actual;
			while(it1.hasNext()){
				actual = it1.next();
				if(actual.getNombre().equals(anterior.getNombre())){
					errores.add(ErrorCrearPartesAlCrearMaquina.NOMBRE_INGRESADO_REPETIDO);
					nombresRepetidos.add(actual.getNombre());
				}
				anterior = actual;
			}
		}

		//Valido la creación de las piezas de cada parte
		resultadosCrearPiezas = validarCrearPiezasAlCrearMaquina(maquinaDeLasPartesAValidar.getPartes());
		for(ResultadoCrearPiezasAlCrearMaquina res: resultadosCrearPiezas.values()){
			if(res.hayErrores()){
				errores.add(ErrorCrearPartesAlCrearMaquina.ERROR_AL_CREAR_PIEZAS);
			}
		}

		return new ResultadoCrearPartesAlCrearMaquina(resultadosCrearPiezas, nombresRepetidos, errores.toArray(new ErrorCrearPartesAlCrearMaquina[0]));
	}

	/**
	 * Valida que las piezas que se están creando se pueden persistir.
	 * Las piezas pueden pertenecer a distintas partes.
	 * Las piezas pertenecen a una máquina nueva.
	 *
	 * @param partesDeLasPiezasAValidar
	 *            son las piezas nuevas que se quieren validar.
	 * @return
	 */
	private Map<String, ResultadoCrearPiezasAlCrearMaquina> validarCrearPiezasAlCrearMaquina(Collection<Parte> partesDeLasPiezasAValidar) {
		Map<String, ResultadoCrearPiezasAlCrearMaquina> resultadosCrearPiezas = new HashMap<>();

		for(Parte parte: partesDeLasPiezasAValidar){
			Set<ErrorCrearPiezasALCrearMaquina> errores = new HashSet<>();
			List<Pieza> piezasAValidar = new ArrayList<>();

			//Busco piezas sin cantidad
			for(Pieza piezaActual: parte.getPiezas()){
				if(piezaActual.getCantidad() == null || piezaActual.getCantidad() < 1){
					errores.add(ErrorCrearPiezasALCrearMaquina.CANTIDAD_INCOMPLETA);
				}
			}

			//Busco piezas sin material
			for(Pieza piezaActual: parte.getPiezas()){
				if(piezaActual.getMaterial() == null){
					errores.add(ErrorCrearPiezasALCrearMaquina.MATERIAL_INCOMPLETO);
				}
			}

			//Saco las piezas que no tienen nombre
			for(Pieza piezaActual: parte.getPiezas()){
				if(piezaActual.getNombre() == null || piezaActual.getNombre().isEmpty()){
					errores.add(ErrorCrearPiezasALCrearMaquina.NOMBRE_INCOMPLETO);
				}
				else{
					piezasAValidar.add(piezaActual);
				}
			}

			//al resto las ordeno por nombre
			piezasAValidar.sort((p1, p2) -> {
				return p1.getNombre().compareTo(p2.getNombre());
			});

			//busco coincidencias entre los nombres de las piezas ingresadas
			Set<String> nombresRepetidos = new HashSet<>();
			if(!piezasAValidar.isEmpty()){
				Iterator<Pieza> it1 = piezasAValidar.iterator();
				Pieza anterior = it1.next();
				Pieza actual;
				while(it1.hasNext()){
					actual = it1.next();
					if(actual.getNombre().equals(anterior.getNombre())){
						errores.add(ErrorCrearPiezasALCrearMaquina.NOMBRE_INGRESADO_REPETIDO);
						nombresRepetidos.add(actual.getNombre());
					}
					anterior = actual;
				}
			}

			resultadosCrearPiezas.put(parte.toString(), new ResultadoCrearPiezasAlCrearMaquina(nombresRepetidos, errores.toArray(new ErrorCrearPiezasALCrearMaquina[0])));
		}

		return resultadosCrearPiezas;
	}

	public ResultadoModificarMaquina modificarMaquina(Maquina maquina) throws PersistenciaException {
		ResultadoModificarMaquina resultado = validarModificarMaquina(maquina);

		if(!resultado.hayErrores()){
			persistidorTaller.actualizarMaquina(maquina);
		}

		return resultado;
	}

	private ResultadoModificarMaquina validarModificarMaquina(Maquina maquina) throws PersistenciaException {
		ArrayList<ErrorModificarMaquina> errores = new ArrayList<>();
		if(maquina.getNombre() == null || maquina.getNombre().isEmpty()){
			errores.add(ErrorModificarMaquina.NOMBRE_INCOMPLETO);
		}
		else{
			ArrayList<Maquina> maquinasRepetidas = persistidorTaller.obtenerMaquinas(new FiltroMaquina.Builder().nombre(maquina.getNombre()).build());
			maquinasRepetidas.remove(maquina);
			if(!maquinasRepetidas.isEmpty()){
				errores.add(ErrorModificarMaquina.NOMBRE_REPETIDO);
			}
		}

		//Valido la creacion o modificación de las partes de la maquina
		ResultadoCrearModificarPartesAlModificarMaquina resultadoCrearModificarPartes = validarCrearModificarPartesAlModificarMaquina(maquina);
		if(resultadoCrearModificarPartes.hayErrores()){
			errores.add(ErrorModificarMaquina.ERROR_AL_CREAR_O_MODIFICAR_PARTES);
		}

		return new ResultadoModificarMaquina(resultadoCrearModificarPartes, errores.toArray(new ErrorModificarMaquina[0]));
	}

	/**
	 * Valida que las partes que se están creando o los cambios que se le hicieron a viejas partes se pueden persistir.
	 * Incluye la validación de la creación o modificación de las piezas pertenecientes a esas partes
	 *
	 * @param maquina
	 *            es la máquina con las partes nuevas y/o las partes viejas que se quieren modificar.
	 * @return
	 * @throws PersistenciaException
	 */
	private ResultadoCrearModificarPartesAlModificarMaquina validarCrearModificarPartesAlModificarMaquina(Maquina maquina) throws PersistenciaException {
		Set<ErrorCrearModificarPartesAlModificarMaquina> errores = new HashSet<>();

		//Quito las partes dadas de baja
		List<Parte> partes = new ArrayList<>(maquina.getPartes());
		partes.removeIf(t -> {
			return EstadoStr.BAJA.equals(t.getEstado().getNombre());
		});

		//Busco partes sin cantidad
		for(Parte parteActual: partes){
			if(parteActual.getCantidad() == null || parteActual.getCantidad() < 1){
				errores.add(ErrorCrearModificarPartesAlModificarMaquina.CANTIDAD_INCOMPLETA);
			}
		}

		//Creo una lista de los nombres de materiales que voy a buscar en la BD
		ArrayList<Parte> partesAValidar = new ArrayList<>();

		//Reviso que el nombre esté completo
		for(Parte parteActual: partes){
			if(parteActual.getNombre() == null || parteActual.getNombre().isEmpty()){
				//Si encontré un nombre incompleto, agrego el error
				errores.add(ErrorCrearModificarPartesAlModificarMaquina.NOMBRE_INCOMPLETO);
			}
			else{
				//Si el nombre está completo lo busco en la BD
				partesAValidar.add(parteActual);
			}
		}

		//veo si hay nombres que se repiten entre las nuevas partes con nombres
		//primero las ordeno por nombre
		partesAValidar.sort((p1, p2) -> {
			int comparacion = p1.getNombre().compareTo(p2.getNombre());
			if(comparacion == 0){
				//Pongo las partes previamente persistidas en un extremo para simplificar el saber si ya existe
				if(p1.getId() != null){
					return 1;
				}
				if(p2.getId() != null){
					return -1;
				}
			}
			return comparacion;
		});

		//luego busco coincidencias entre los nombres de las partes ingresadas
		Set<String> nombresPartesRepetidasBD = new HashSet<>();
		if(!partesAValidar.isEmpty()){
			Iterator<Parte> it1 = partesAValidar.iterator();
			Parte anterior = it1.next();
			Parte actual;
			while(it1.hasNext()){
				actual = it1.next();
				if(actual.getNombre().equals(anterior.getNombre())){
					if(actual.getId() != null || anterior.getId() != null){
						errores.add(ErrorCrearModificarPartesAlModificarMaquina.NOMBRE_YA_EXISTENTE);
						nombresPartesRepetidasBD.add(actual.getNombre());
					}
					else{
						errores.add(ErrorCrearModificarPartesAlModificarMaquina.NOMBRE_INGRESADO_REPETIDO);
					}
				}
				anterior = actual;
			}
		}

		//Valido la creación de las piezas de cada parte
		Map<String, ResultadoCrearPiezasAlModificarMaquina> resultadosCrearPiezas = validarCrearPiezasAlModificarMaquina(partesAValidar);
		for(ResultadoCrearPiezasAlModificarMaquina res: resultadosCrearPiezas.values()){
			if(res.hayErrores()){
				errores.add(ErrorCrearModificarPartesAlModificarMaquina.ERROR_AL_CREAR_PIEZAS);
			}
		}

		return new ResultadoCrearModificarPartesAlModificarMaquina(resultadosCrearPiezas, nombresPartesRepetidasBD, errores.toArray(new ErrorCrearModificarPartesAlModificarMaquina[0]));
	}

	/**
	 * Valida que las piezas que se están creando se pueden persistir.
	 * Las piezas pueden pertenecer a distintas partes.
	 * Las piezas pueden contener piezas ya guardadas previamente
	 *
	 * @param partesDeLasPiezasAValidar
	 *            son las piezas nuevas y viejas que se quieren validar.
	 * @return
	 */
	private Map<String, ResultadoCrearPiezasAlModificarMaquina> validarCrearPiezasAlModificarMaquina(Collection<Parte> partesDeLasPiezasAValidar) throws PersistenciaException {
		Map<String, ResultadoCrearPiezasAlModificarMaquina> resultadosCrearPiezas = new HashMap<>();

		for(Parte parte: partesDeLasPiezasAValidar){
			Set<ErrorCrearPiezasAlModificarMaquina> errores = new HashSet<>();

			//Quito las piezas dadas de baja
			List<Pieza> piezasNuevas = new ArrayList<>(parte.getPiezas());
			piezasNuevas.removeIf(t -> {
				return EstadoStr.BAJA.equals(t.getEstado().getNombre());
			});

			//Busco piezas sin cantidad
			for(Pieza piezaActual: piezasNuevas){
				if(piezaActual.getCantidad() == null || piezaActual.getCantidad() < 1){
					errores.add(ErrorCrearPiezasAlModificarMaquina.CANTIDAD_INCOMPLETA);
				}
			}

			//Busco piezas sin material
			for(Pieza piezaActual: piezasNuevas){
				if(piezaActual.getMaterial() == null){
					errores.add(ErrorCrearPiezasAlModificarMaquina.MATERIAL_INCOMPLETO);
				}
			}

			//Valido los nombres de las piezas
			List<Pieza> piezasAValidarNombre = new ArrayList<>();
			//Reviso que el nombre esté completo
			for(Pieza piezaActual: piezasNuevas){
				if(piezaActual.getNombre() == null || piezaActual.getNombre().isEmpty()){
					//Si encontré un nombre incompleto, agrego el error
					errores.add(ErrorCrearPiezasAlModificarMaquina.NOMBRE_INCOMPLETO);
				}
				else{
					//Si el nombre está completo lo busco en la BD
					piezasAValidarNombre.add(piezaActual);
				}
			}

			//veo si hay nombres que se repiten entre las nuevas piezas con nombres
			//primero las ordeno por nombre
			piezasAValidarNombre.sort((p1, p2) -> {
				int comparacion = p1.getNombre().compareTo(p2.getNombre());
				if(comparacion == 0){
					//Pongo las partes previamente persistidas en un extremo para simplificar el saber si ya existe
					if(p1.getId() != null){
						return 1;
					}
					if(p2.getId() != null){
						return -1;
					}
				}
				return comparacion;
			});

			//luego busco coincidencias entre los nombres de las piezas ingresadas
			Set<String> nombresPiezasRepetidasBD = new HashSet<>();
			if(!piezasAValidarNombre.isEmpty()){
				Iterator<Pieza> it1 = piezasAValidarNombre.iterator();
				Pieza anterior = it1.next();
				Pieza actual;
				while(it1.hasNext()){
					actual = it1.next();
					if(actual.getNombre().equals(anterior.getNombre())){
						if(actual.getId() != null || anterior.getId() != null){
							errores.add(ErrorCrearPiezasAlModificarMaquina.NOMBRE_YA_EXISTENTE);
							nombresPiezasRepetidasBD.add(actual.getNombre());
						}
						else{
							errores.add(ErrorCrearPiezasAlModificarMaquina.NOMBRE_INGRESADO_REPETIDO);
						}
					}
					anterior = actual;
				}
			}

			resultadosCrearPiezas.put(parte.toString(), new ResultadoCrearPiezasAlModificarMaquina(nombresPiezasRepetidasBD, errores.toArray(new ErrorCrearPiezasAlModificarMaquina[0])));
		}

		return resultadosCrearPiezas;
	}

	public ResultadoEliminarMaquina eliminarMaquina(Maquina maquina) throws PersistenciaException {
		ResultadoEliminarMaquina resultadoEliminarMaquina = validarEliminarMaquina(maquina);
		if(!resultadoEliminarMaquina.hayErrores()){
			persistidorTaller.bajaMaquina(maquina);
		}
		return resultadoEliminarMaquina;
	}

	private ResultadoEliminarMaquina validarEliminarMaquina(Maquina maquina) throws PersistenciaException {
		return new ResultadoEliminarMaquina();
	}

	public ArrayList<Parte> listarPartes(Filtro<Parte> filtro) throws PersistenciaException {
		return persistidorTaller.obtenerPartes(filtro);
	}

	/**
	 * Se encarga de dar baja física o lógica a una parte
	 *
	 * @param parte
	 *            es la parte que se quiere eliminar
	 * @return el resultado de la operación
	 * @throws PersistenciaException
	 */
	public ResultadoEliminarParte eliminarParte(Parte parte) throws PersistenciaException {
		ResultadoEliminarParte resultado = validarEliminarParte(parte);

		if(!resultado.hayErrores()){
			//si la parte tiene tareas asociadas, se le da baja lógica
			ArrayList<Tarea> tareasDeLaParte = persistidorProceso.obtenerTareas(new FiltroTarea.Builder().parte(parte).build());

			if(!tareasDeLaParte.isEmpty()){
				//si la parte tiene tareas asociadas, se le da de baja lógica junto a sus procesos y piezas

				//dar de baja piezas
				for(Pieza pieza: parte.getPiezas()){
					this.eliminarPieza(pieza);
				}

				//dar de baja procesos
				for(Proceso proceso: parte.getProcesos()){
					gestorProceso.eliminarProceso(proceso);
				}

				//dar de baja logica parte
				parte.darDeBaja();
				persistidorTaller.actualizarParte(parte);
			}
			else{
				//sino de baja física junto a sus procesos y piezas
				try{
					Maquina maquinaDeLaParte = parte.getMaquina(); //Al ir al persistidor se va a cambiar por una que tiene la información de la base de datos
					persistidorTaller.bajaParte(parte);
					maquinaDeLaParte.getPartes().remove(parte); //Si todo salió bien, quito la pieza de la parte
				} catch(ObjNotFoundException e){
					//Si no se encontró ya fue eliminado previamente.
				}
			}
		}

		return resultado;
	}

	/**
	 * Valida que la parte se pueda eliminar. Incluye la validación de la eliminación de piezas y procesos asociados
	 *
	 * @param partesAEliminar
	 *            son las partes cuya eliminación se va a validar
	 * @return
	 */
	private ResultadoEliminarParte validarEliminarParte(Parte parteAEliminar) {
		Set<ErrorEliminarParte> errores = new HashSet<>();

		//Verifico si se pueden borrar las piezas y procesos asociados de las piezas que están dadas de alta
		ResultadoEliminarPiezasDeParte resultadoEliminarPiezas = this.validarEliminarPiezasDeParte(parteAEliminar);
		if(resultadoEliminarPiezas.hayErrores()){
			errores.add(ErrorEliminarParte.ERROR_AL_ELIMINAR_PIEZAS);
		}

		ArrayList<Proceso> procesosAEliminar = new ArrayList<>(parteAEliminar.getProcesos());
		procesosAEliminar.removeIf(t -> EstadoStr.BAJA.equals(t.getEstado().getNombre()));
		ResultadoEliminarProcesos resultadoEliminarProcesos = gestorProceso.validarEliminarProcesos(procesosAEliminar);
		if(resultadoEliminarProcesos.hayErrores()){
			errores.add(ErrorEliminarParte.ERROR_AL_ELIMINAR_PROCESOS);
		}

		return new ResultadoEliminarParte(resultadoEliminarPiezas, resultadoEliminarProcesos, errores.toArray(new ErrorEliminarParte[0]));
	}

	/**
	 * Valida que las piezas de una parte a eliminar se pueden eliminar.
	 * No incluye la validación de la eliminación de procesos asociados a las piezas ya que son los mismos que los de la parte a eliminar.
	 *
	 * @param parteDeLasPiezasAEliminar
	 *            parte de las piezas cuya eliminación se va a validar
	 * @return resultado de la validación
	 */
	private ResultadoEliminarPiezasDeParte validarEliminarPiezasDeParte(Parte parteDeLasPiezasAEliminar) {
		//No hay errores al eliminar piezas de una parte
		return new ResultadoEliminarPiezasDeParte();
	}

	public ArrayList<Pieza> listarPiezas(Filtro<Pieza> filtro) throws PersistenciaException {
		return persistidorTaller.obtenerPiezas(filtro);
	}

	/**
	 * Se encarga de dar baja física o lógica una pieza
	 *
	 * @param piezaAEliminar
	 *            es la pieza que se quiere eliminar
	 * @return
	 * @throws PersistenciaException
	 */
	public ResultadoEliminarPieza eliminarPieza(Pieza pieza) throws PersistenciaException {
		ResultadoEliminarPieza resultado = validarEliminarPieza(pieza);

		if(!resultado.hayErrores()){
			//si la parte tiene tareas asociadas, se le da baja lógica
			ArrayList<Tarea> tareasDeLaPieza = persistidorProceso.obtenerTareas(new FiltroTarea.Builder().pieza(pieza).build());

			if(!tareasDeLaPieza.isEmpty()){
				//si la parte tiene tareas asociadas, se le da de baja lógica junto a sus procesos y piezas
				//dar de baja logica procesos
				for(Proceso proceso: pieza.getProcesos()){
					gestorProceso.eliminarProceso(proceso);
				}

				//dar de baja logica pieza
				pieza.darDeBaja();
				persistidorTaller.actualizarPieza(pieza);
			}
			else{
				//sino de baja física junto a sus procesos y piezas
				try{
					Parte parteDeLaPieza = pieza.getParte(); //Al ir al persistidor se va a cambiar por una que tiene la información de la base de datos
					persistidorTaller.bajaPieza(pieza);
					parteDeLaPieza.getPiezas().remove(pieza); //Si todo salió bien, quito la pieza de la parte
				} catch(ObjNotFoundException e){
					//Si no se encontró ya fue eliminado previamente.
				}
			}
		}

		return resultado;
	}

	/**
	 * Valida que la piezas se puede eliminar.
	 * Incluye la validación de la eliminación de procesos asociados
	 *
	 * @param piezaAEliminar
	 *            son las piezas cuya eliminación se va a validar
	 * @return resultado de la validación
	 */
	private ResultadoEliminarPieza validarEliminarPieza(Pieza piezaAEliminar) {
		Set<ErrorEliminarPieza> errores = new HashSet<>();

		//Verifico si se pueden los procesos asociados
		ArrayList<Proceso> procesosAEliminar = new ArrayList<>(piezaAEliminar.getProcesos());
		procesosAEliminar.removeIf(t -> EstadoStr.BAJA.equals(t.getEstado().getNombre()));
		ResultadoEliminarProcesos resultadoEliminarProcesos = gestorProceso.validarEliminarProcesos(procesosAEliminar);
		if(resultadoEliminarProcesos.hayErrores()){
			errores.add(ErrorEliminarPieza.ERROR_AL_ELIMINAR_PROCESOS);
		}

		return new ResultadoEliminarPieza(resultadoEliminarProcesos, errores.toArray(new ErrorEliminarPieza[0]));
	}
}
