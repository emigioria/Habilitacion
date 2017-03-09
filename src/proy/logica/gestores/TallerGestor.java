/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
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
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Service;

import proy.datos.clases.EstadoStr;
import proy.datos.entidades.Maquina;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.datos.entidades.Proceso;
import proy.datos.filtros.Filtro;
import proy.datos.filtros.implementacion.FiltroMaquina;
import proy.datos.filtros.implementacion.FiltroParte;
import proy.datos.filtros.implementacion.FiltroPieza;
import proy.datos.servicios.TallerService;
import proy.excepciones.PersistenciaException;
import proy.logica.gestores.resultados.ResultadoCrearMaquina;
import proy.logica.gestores.resultados.ResultadoCrearMaquina.ErrorCrearMaquina;
import proy.logica.gestores.resultados.ResultadoCrearModificarPartes;
import proy.logica.gestores.resultados.ResultadoCrearModificarPartes.ErrorCrearModificarPartes;
import proy.logica.gestores.resultados.ResultadoCrearParte;
import proy.logica.gestores.resultados.ResultadoCrearPartesMaquinaNueva;
import proy.logica.gestores.resultados.ResultadoCrearPartesMaquinaNueva.ErrorCrearPartesMaquinaNueva;
import proy.logica.gestores.resultados.ResultadoCrearPieza;
import proy.logica.gestores.resultados.ResultadoCrearPiezas;
import proy.logica.gestores.resultados.ResultadoCrearPiezas.ErrorCrearPiezas;
import proy.logica.gestores.resultados.ResultadoCrearPiezasMaquinaNueva;
import proy.logica.gestores.resultados.ResultadoCrearPiezasMaquinaNueva.ErrorCrearPiezasMaquinaNueva;
import proy.logica.gestores.resultados.ResultadoEliminarMaquina;
import proy.logica.gestores.resultados.ResultadoEliminarPartes;
import proy.logica.gestores.resultados.ResultadoEliminarPartes.ErrorEliminarPartes;
import proy.logica.gestores.resultados.ResultadoEliminarPiezas;
import proy.logica.gestores.resultados.ResultadoEliminarPiezas.ErrorEliminarPiezas;
import proy.logica.gestores.resultados.ResultadoEliminarProcesos;
import proy.logica.gestores.resultados.ResultadoModificarMaquina;
import proy.logica.gestores.resultados.ResultadoModificarMaquina.ErrorModificarMaquina;
import proy.logica.gestores.resultados.ResultadoModificarPartes;
import proy.logica.gestores.resultados.ResultadoModificarPartes.ErrorModificarPartes;

@Service
public class TallerGestor {

	@Resource
	private TallerService persistidorTaller;

	@Resource
	private ProcesoGestor gestorProceso;

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
		ResultadoCrearPartesMaquinaNueva resultadosCrearPartes = validarPartesNuevas(maquina);
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
	private ResultadoCrearPartesMaquinaNueva validarPartesNuevas(Maquina maquinaDeLasPartesAValidar) {
		Map<String, ResultadoCrearPiezasMaquinaNueva> resultadosCrearPiezas;
		Set<ErrorCrearPartesMaquinaNueva> errores = new HashSet<>();
		List<Parte> partesAValidar = new ArrayList<>();

		//Busco partes sin cantidad
		for(Parte parteActual: maquinaDeLasPartesAValidar.getPartes()){
			if(parteActual.getCantidad() == null || parteActual.getCantidad() < 1){
				errores.add(ErrorCrearPartesMaquinaNueva.CANTIDAD_INCOMPLETA);
			}
		}

		//Saco las partes que no tienen nombre
		for(Parte parteActual: maquinaDeLasPartesAValidar.getPartes()){
			if(parteActual.getNombre() == null || parteActual.getNombre().isEmpty()){
				errores.add(ErrorCrearPartesMaquinaNueva.NOMBRE_INCOMPLETO);
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
					errores.add(ErrorCrearPartesMaquinaNueva.NOMBRE_INGRESADO_REPETIDO);
					nombresRepetidos.add(actual.getNombre());
				}
				anterior = actual;
			}
		}

		//Valido la creación de las piezas de cada parte
		resultadosCrearPiezas = validarPiezasNuevas(maquinaDeLasPartesAValidar.getPartes());
		for(ResultadoCrearPiezasMaquinaNueva res: resultadosCrearPiezas.values()){
			if(res.hayErrores()){
				errores.add(ErrorCrearPartesMaquinaNueva.ERROR_AL_CREAR_PIEZAS);
			}
		}

		return new ResultadoCrearPartesMaquinaNueva(resultadosCrearPiezas, nombresRepetidos, errores.toArray(new ErrorCrearPartesMaquinaNueva[0]));
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
	private Map<String, ResultadoCrearPiezasMaquinaNueva> validarPiezasNuevas(Collection<Parte> partesDeLasPiezasAValidar) {
		Map<String, ResultadoCrearPiezasMaquinaNueva> resultadosCrearPiezas = new HashMap<>();
		Set<ErrorCrearPiezasMaquinaNueva> errores = new HashSet<>();
		List<Pieza> piezasAValidar;
		for(Parte parte: partesDeLasPiezasAValidar){
			piezasAValidar = new ArrayList<>();

			//Busco piezas sin cantidad
			for(Pieza piezaActual: parte.getPiezas()){
				if(piezaActual.getCantidad() == null || piezaActual.getCantidad() < 1){
					errores.add(ErrorCrearPiezasMaquinaNueva.CANTIDAD_INCOMPLETA);
				}
			}

			//Busco piezas sin material
			for(Pieza piezaActual: parte.getPiezas()){
				if(piezaActual.getCantidad() == null || piezaActual.getCantidad() < 1){
					errores.add(ErrorCrearPiezasMaquinaNueva.MATERIAL_INCOMPLETO);
				}
			}

			//Saco las piezas que no tienen nombre
			for(Pieza piezaActual: parte.getPiezas()){
				if(piezaActual.getNombre() == null || piezaActual.getNombre().isEmpty()){
					errores.add(ErrorCrearPiezasMaquinaNueva.NOMBRE_INCOMPLETO);
				}
				else{
					piezasAValidar.add(piezaActual);
				}
			}

			//al resto las ordeno por nombre
			piezasAValidar.sort((p1, p2) -> {
				return p1.getNombre().compareTo(p2.getNombre());
			});

			//busco coincidencias entre los nombres de las partes ingresadas
			Set<String> nombresRepetidos = new HashSet<>();
			if(!piezasAValidar.isEmpty()){
				Iterator<Pieza> it1 = piezasAValidar.iterator();
				Pieza anterior = it1.next();
				Pieza actual;
				while(it1.hasNext()){
					actual = it1.next();
					if(actual.getNombre().equals(anterior.getNombre())){
						errores.add(ErrorCrearPiezasMaquinaNueva.NOMBRE_INGRESADO_REPETIDO);
						nombresRepetidos.add(actual.getNombre());
					}
					anterior = actual;
				}
			}

			resultadosCrearPiezas.put(parte.toString(), new ResultadoCrearPiezasMaquinaNueva(nombresRepetidos, errores.toArray(new ErrorCrearPiezasMaquinaNueva[0])));
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
		ResultadoCrearModificarPartes resultadoCrearModificarPartes;
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
		ArrayList<Maquina> listaConEstaMaquina = new ArrayList<>();
		listaConEstaMaquina.add(maquina);
		resultadoCrearModificarPartes = validarPartes(listaConEstaMaquina).get(maquina);
		if(resultadoCrearModificarPartes.hayErrores()){
			errores.add(ErrorModificarMaquina.ERROR_AL_CREAR_O_MODIFICAR_PARTES);
		}

		return new ResultadoModificarMaquina(resultadoCrearModificarPartes, errores.toArray(new ErrorModificarMaquina[0]));
	}

	/**
	 * Valida que las partes que se están creando o los cambios que se le hicieron a viejas partes se pueden persistir.
	 * Las partes pueden pertenecer a distintas máquinas.
	 * Incluye la validación de la creación o modificación de las piezas pertenecientes a esas partes
	 *
	 * @param maquinasDeLasPartesAValidar
	 *            son las partes nuevas y/o las partes viejas que se quieren modificar.
	 * @return
	 * @throws PersistenciaException
	 */
	public Map<Maquina, ResultadoCrearModificarPartes> validarPartes(Collection<Maquina> maquinasDeLasPartesAValidar) throws PersistenciaException {
		Map<Maquina, ResultadoCrearModificarPartes> resultadosCrearModificarPartes = new HashMap<>();
		Map<String, ResultadoCrearPiezas> resultadosCrearPiezas;
		ArrayList<ErrorCrearModificarPartes> errores = new ArrayList<>();
		List<Parte> partesAValidar;
		for(Maquina maquina: maquinasDeLasPartesAValidar){
			partesAValidar = new ArrayList<>(maquina.getPartes());
			partesAValidar.removeIf(t -> {
				return EstadoStr.BAJA.equals(t.getEstado().getNombre());
			});
			Iterator<Parte> it = partesAValidar.iterator();
			Parte parteActual;
			boolean nombreIncompletoEncontrado = false;
			while(it.hasNext()){
				parteActual = it.next();
				if(parteActual.getNombre() == null || parteActual.getNombre().isEmpty()){
					nombreIncompletoEncontrado = true;
					it.remove();
				}
			}
			if(nombreIncompletoEncontrado){
				errores.add(ErrorCrearModificarPartes.NOMBRE_INCOMPLETO);
			}

			//las ordeno por nombre
			partesAValidar.sort((p1, p2) -> {
				return p1.getNombre().compareTo(p2.getNombre());
			});

			//busco coincidencias entre los nombres de las piezas ingresadas
			if(!partesAValidar.isEmpty()){
				Iterator<Parte> it2 = partesAValidar.iterator();
				Parte anterior = it2.next();
				Parte actual;
				boolean nombreIngresadoRepetidoEncontrado = false;
				while(it2.hasNext()){
					actual = it2.next();
					if(actual.getNombre().equals(anterior.getNombre())){
						nombreIngresadoRepetidoEncontrado = true;
						it2.remove();
					}
				}
				if(nombreIngresadoRepetidoEncontrado){
					errores.add(ErrorCrearModificarPartes.NOMBRE_INGRESADO_REPETIDO);
				}
			}

			//busco coincidencias con los nombres de las piezas de la BD
			ArrayList<String> nombresDePartes = new ArrayList<>();
			for(Parte parte: partesAValidar){
				nombresDePartes.add(parte.getNombre());
			}
			ArrayList<Parte> partesConNombreCoincidente = persistidorTaller.obtenerPartes(new FiltroParte.Builder().nombres(nombresDePartes).maquina(maquina).sinUnir().build());
			Set<String> nombresPartesRepetidas = new HashSet<>();
			if(!partesConNombreCoincidente.isEmpty()){
				errores.add(ErrorCrearModificarPartes.NOMBRE_YA_EXISTENTE);
				for(Parte parte: partesConNombreCoincidente){
					nombresPartesRepetidas.add(parte.toString());
				}
			}

			//Valido la creación de las piezas de cada parte
			resultadosCrearPiezas = validarPiezas(partesAValidar);
			Iterator<ResultadoCrearPiezas> it2 = resultadosCrearPiezas.values().iterator();
			boolean errorAlCrearPiezasEncontrado = false;
			while(!errorAlCrearPiezasEncontrado && it2.hasNext()){
				if(it2.next().hayErrores()){
					errorAlCrearPiezasEncontrado = true;
					errores.add(ErrorCrearModificarPartes.ERROR_AL_CREAR_PIEZAS);
				}
			}

			resultadosCrearModificarPartes.put(maquina, new ResultadoCrearModificarPartes(resultadosCrearPiezas, nombresPartesRepetidas, errores.toArray(new ErrorCrearModificarPartes[0])));

		}

		return resultadosCrearModificarPartes;
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
	public Map<String, ResultadoCrearPiezas> validarPiezas(Collection<Parte> partesDeLasPiezasAValidar) throws PersistenciaException {
		Map<String, ResultadoCrearPiezas> resultadosCrearPiezas = new HashMap<>();
		ArrayList<ErrorCrearPiezas> errores = new ArrayList<>();
		List<Pieza> piezasAValidar;
		for(Parte parte: partesDeLasPiezasAValidar){
			//Quito las piezas dadas de baja
			piezasAValidar = new ArrayList<>(parte.getPiezas());
			piezasAValidar.removeIf(t -> {
				return EstadoStr.BAJA.equals(t.getEstado().getNombre());
			});

			//Saco las partes que no tienen nombre
			Iterator<Pieza> it = piezasAValidar.iterator();
			Pieza piezaActual;
			boolean nombreIncompletoEncontrado = false;
			while(it.hasNext()){
				piezaActual = it.next();
				if(piezaActual.getNombre() == null || piezaActual.getNombre().isEmpty()){
					nombreIncompletoEncontrado = true;
					it.remove();
				}
			}
			if(nombreIncompletoEncontrado){
				errores.add(ErrorCrearPiezas.NOMBRE_INCOMPLETO);
			}

			//las ordeno por nombre
			piezasAValidar.sort((p1, p2) -> {
				return p1.getNombre().compareTo(p2.getNombre());
			});

			//Quito las piezas guardadas previamente
			piezasAValidar = new ArrayList<>(parte.getPiezas());
			piezasAValidar.removeIf(t -> {
				return t.getId() != null;
			});

			//busco coincidencias entre los nombres de las piezas ingresadas
			Set<String> nombresRepetidos = new HashSet<>();
			if(!piezasAValidar.isEmpty()){
				Iterator<Pieza> it2 = piezasAValidar.iterator();
				Pieza anterior = it2.next();
				Pieza actual;
				boolean nombreIngresadoRepetidoEncontrado = false;
				while(it2.hasNext()){
					actual = it2.next();
					if(actual.getNombre().equals(anterior.getNombre())){
						nombreIngresadoRepetidoEncontrado = true;
						nombresRepetidos.add(actual.getNombre());
						it2.remove();
					}
					anterior = actual;
				}
				if(nombreIngresadoRepetidoEncontrado){
					errores.add(ErrorCrearPiezas.NOMBRE_INGRESADO_REPETIDO);
				}
			}

			//busco coincidencias con los nombres de las piezas de la BD
			ArrayList<String> nombresDePiezas = new ArrayList<>();
			for(Pieza pieza: piezasAValidar){
				nombresDePiezas.add(pieza.getNombre());
			}
			ArrayList<Pieza> piezasConNombreCoincidente = persistidorTaller.obtenerPiezas(new FiltroPieza.Builder().nombres(nombresDePiezas).parte(parte).sinUnir().build());
			Set<String> nombresYaExistentes = new HashSet<>();
			if(!piezasConNombreCoincidente.isEmpty()){
				errores.add(ErrorCrearPiezas.NOMBRE_YA_EXISTENTE);
				for(Pieza pieza: piezasConNombreCoincidente){
					nombresYaExistentes.add(pieza.toString());
				}
			}

			resultadosCrearPiezas.put(parte.toString(), new ResultadoCrearPiezas(nombresYaExistentes, nombresRepetidos, errores.toArray(new ErrorCrearPiezas[0])));
		}

		return resultadosCrearPiezas;
	}

	public ResultadoEliminarMaquina eliminarMaquina(Maquina maquina) throws PersistenciaException {
		persistidorTaller.bajaMaquina(maquina);
		return new ResultadoEliminarMaquina();
	}

	public ArrayList<Parte> listarPartes(Filtro<Parte> filtro) throws PersistenciaException {
		return persistidorTaller.obtenerPartes(filtro);
	}

	public ResultadoCrearParte crearParte(Parte parte) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoModificarPartes modificarPartes(ArrayList<Parte> partes) throws PersistenciaException {
		ResultadoModificarPartes resultado = validarModificarPartes(partes);
		if(!resultado.hayErrores()){
			persistidorTaller.actualizarPartes(partes);
		}
		return resultado;
	}

	private ResultadoModificarPartes validarModificarPartes(ArrayList<Parte> partes) throws PersistenciaException {
		Maquina maquina = partes.get(0).getMaquina();
		ArrayList<String> nombresPartesRepetidos = new ArrayList<>();
		ListIterator<Parte> itPartesAModificar = null, itPartesModificadas = null;
		Parte parteAModificar = null, parteModificada = null;

		//Creo la lista de errores
		ArrayList<ErrorModificarPartes> erroresModificarPartes = new ArrayList<>();

		//Creo una lista de los nombres de partes que voy a buscar en la BD
		ArrayList<String> nombresPartesABuscarEnLaBD = new ArrayList<>();

		//Reviso que el nombre esté completo
		boolean nombreIncompletoEncontrado = false;
		for(Parte parte: partes){
			if(parte.getNombre() == null || parte.getNombre().isEmpty()){
				nombreIncompletoEncontrado = true;
			}
			else{
				//Si el nombre está completo lo busco en la BD
				nombresPartesABuscarEnLaBD.add(parte.getNombre());
			}
		}
		//Si encontré un nombre incompleto, agrego el error
		if(nombreIncompletoEncontrado){
			erroresModificarPartes.add(ErrorModificarPartes.NOMBRE_INCOMPLETO);
		}

		//Si hay partes a buscar
		if(!nombresPartesABuscarEnLaBD.isEmpty()){

			//busco en la BD partes cuyo nombre coincida con el de alguna de las nuevas partes
			ArrayList<Parte> partes_coincidentes = persistidorTaller.obtenerPartes(new FiltroParte.Builder().maquina(maquina).nombres(nombresPartesABuscarEnLaBD).build());
			if(!partes_coincidentes.isEmpty()){
				erroresModificarPartes.add(ErrorModificarPartes.NOMBRE_YA_EXISTENTE);
				for(Parte parte: partes_coincidentes){
					nombresPartesRepetidos.add(parte.toString());
				}
			}
		}

		//veo si hay nombres que se repiten entre los nuevos materiales
		boolean nombreIngresadoRepetidoEncontrado = false;
		itPartesAModificar = partes.listIterator();

		while(itPartesAModificar.hasNext() && !nombreIngresadoRepetidoEncontrado){
			parteAModificar = itPartesAModificar.next();
			itPartesModificadas = partes.listIterator(itPartesAModificar.nextIndex());
			while(itPartesModificadas.hasNext() && !nombreIngresadoRepetidoEncontrado){
				parteModificada = itPartesModificadas.next();
				if(parteAModificar.getNombre() != null && parteModificada.getNombre() != null &&
						parteAModificar.getNombre().equals(parteModificada.getNombre())){
					nombreIngresadoRepetidoEncontrado = true;
				}
			}
		}
		if(nombreIngresadoRepetidoEncontrado){
			erroresModificarPartes.add(ErrorModificarPartes.NOMBRE_INGRESADO_REPETIDO);
		}

		return new ResultadoModificarPartes(nombresPartesRepetidos, erroresModificarPartes.toArray(new ErrorModificarPartes[0]));
	}

	/**
	 * eliminarPartes()
	 *
	 * se encarga de dar baja física o lógica a partes que pueden pertenecer a distintas máquinas
	 *
	 * @param partesAEliminar
	 *            son las partes que se quieren eliminar
	 * @return
	 * @throws PersistenciaException
	 */
	public ResultadoEliminarPartes eliminarPartes(ArrayList<Parte> partesAEliminar) throws PersistenciaException {
		ResultadoEliminarPartes resultado = validarEliminarPartes(partesAEliminar);

		if(!resultado.hayErrores()){
			ArrayList<Parte> partesABajaFisica = new ArrayList<>(partesAEliminar);
			ArrayList<Parte> partesABajaLogica;

			//si la parte tiene tareas asociadas, se le da baja lógica
			partesABajaLogica = persistidorTaller.obtenerPartes(new FiltroParte.Builder().partes(partesAEliminar).sinUnir().conTareas().build());

			//si la parte no tiene tareas asociadas, se le da baja fisica
			partesABajaFisica.removeAll(partesABajaLogica);

			if(!partesABajaFisica.isEmpty()){
				persistidorTaller.bajaPartes(partesABajaFisica);
			}

			if(!partesABajaLogica.isEmpty()){

				for(Parte parte: partesABajaLogica){

					//dar de baja logica piezas
					for(Pieza pieza: parte.getPiezas()){
						pieza.darDeBaja();
					}

					//dar de baja logica procesos
					for(Proceso proceso: parte.getProcesos()){
						proceso.darDeBaja();
					}

					//dar de baja logica parte
					parte.darDeBaja();
				}

				persistidorTaller.actualizarPartes(partesABajaLogica); //Al actualizar la parte se guardan los cambios de las piezas y los procesos por el tipo de cascada
			}
		}

		return resultado;
	}

	/**
	 * validarEliminarPartes
	 *
	 * hace la validación de que las partes se pueden eliminar. Incluye la validación de la eliminación de piezas y
	 * procesos asociados
	 *
	 * @param partesAEliminar
	 *            son las partes cuya eliminación se va a validar
	 * @return
	 */
	private ResultadoEliminarPartes validarEliminarPartes(ArrayList<Parte> partesAEliminar) {
		Set<ErrorEliminarPartes> errores = new HashSet<>();

		//Verifico si se pueden borrar las piezas y procesos asociados
		Map<String, ResultadoEliminarPiezas> resultadosEliminarPiezas = new HashMap<>();
		Map<String, ResultadoEliminarProcesos> resultadosEliminarProcesos = new HashMap<>();
		for(Parte parte: partesAEliminar){
			ArrayList<Pieza> piezasAValidar = new ArrayList<>(parte.getPiezas());
			piezasAValidar.removeIf(t -> {
				return EstadoStr.BAJA.equals(t.getEstado().getNombre());
			});
			resultadosEliminarPiezas.put(parte.toString(), this.validarEliminarPiezas(piezasAValidar));
			resultadosEliminarProcesos.put(parte.toString(), gestorProceso.validarEliminarProcesos(new ArrayList<>(parte.getProcesos())));
		}
		for(ResultadoEliminarPiezas resultado: resultadosEliminarPiezas.values()){
			if(resultado.hayErrores()){
				errores.add(ErrorEliminarPartes.ERROR_AL_ELIMINAR_PIEZAS);
			}
		}
		for(ResultadoEliminarProcesos resultado: resultadosEliminarProcesos.values()){
			if(resultado.hayErrores()){
				errores.add(ErrorEliminarPartes.ERROR_AL_ELIMINAR_PROCESOS);
			}
		}

		return new ResultadoEliminarPartes(resultadosEliminarPiezas, resultadosEliminarProcesos, errores.toArray(new ErrorEliminarPartes[0]));
	}

	public ArrayList<Pieza> listarPiezas(Filtro<Pieza> filtro) throws PersistenciaException {
		return persistidorTaller.obtenerPiezas(filtro);
	}

	public ResultadoCrearPieza crearPieza(Pieza pieza) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	/**
	 * eliminarPiezas()
	 *
	 * se encarga de dar baja física o lógica a piezas que pueden pertenecer a distintas partes
	 *
	 * @param piezasAEliminar
	 *            son las piezas que se quieren eliminar
	 * @return
	 * @throws PersistenciaException
	 */
	public ResultadoEliminarPiezas eliminarPiezas(ArrayList<Pieza> piezasAEliminar) throws PersistenciaException {
		ResultadoEliminarPiezas resultado = validarEliminarPiezas(piezasAEliminar);

		ArrayList<Pieza> piezasABajaLogica;
		ArrayList<Pieza> piezasABajaFisica;

		if(!resultado.hayErrores()){
			piezasABajaFisica = new ArrayList<>(piezasAEliminar);

			//si la parte tiene tareas asociadas, se le da baja lógica
			piezasABajaLogica = persistidorTaller.obtenerPiezas(new FiltroPieza.Builder().piezas(piezasAEliminar).conTareas().build());

			//si la parte no tiene tareas asociadas, se le da baja fisica
			piezasABajaFisica.removeAll(piezasABajaLogica);

			if(!piezasABajaFisica.isEmpty()){
				for(Pieza pieza: piezasABajaFisica){
					pieza.getParte().getPiezas().remove(pieza);
				}
				persistidorTaller.bajaPiezas(piezasABajaFisica);
			}

			if(!piezasABajaLogica.isEmpty()){
				for(Pieza pieza: piezasABajaLogica){
					//dar de baja logica procesos
					for(Proceso proceso: pieza.getProcesos()){
						proceso.darDeBaja();
					}

					//dar de baja logica pieza
					pieza.darDeBaja();
				}

				persistidorTaller.actualizarPiezas(piezasABajaLogica);
			}
		}

		return resultado;
	}

	/**
	 * validarEliminarPiezas
	 *
	 * hace la validación de que las piezas se pueden eliminar. Incluye la validación de la eliminación de
	 * procesos asociados
	 *
	 * @param piezasAEliminar
	 *            son las piezas cuya eliminación se va a validar
	 * @return
	 */
	private ResultadoEliminarPiezas validarEliminarPiezas(ArrayList<Pieza> piezasAEliminar) {
		Set<ErrorEliminarPartes> errores = new HashSet<>();

		//Verifico si se pueden borrar las piezas y procesos asociados
		Map<String, ResultadoEliminarProcesos> resultadosEliminarProcesos = new HashMap<>();
		for(Pieza pieza: piezasAEliminar){
			resultadosEliminarProcesos.put(pieza.toString(), gestorProceso.validarEliminarProcesos(new ArrayList<>(pieza.getProcesos())));
		}
		for(ResultadoEliminarProcesos resultado: resultadosEliminarProcesos.values()){
			if(resultado.hayErrores()){
				errores.add(ErrorEliminarPartes.ERROR_AL_ELIMINAR_PROCESOS);
			}
		}

		return new ResultadoEliminarPiezas(resultadosEliminarProcesos, errores.toArray(new ErrorEliminarPiezas[0]));
	}
}
