/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Maquina;
import proy.datos.entidades.Material;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.datos.entidades.Proceso;
import proy.datos.servicios.TallerService;
import proy.excepciones.ObjNotFoundException;
import proy.excepciones.PersistenciaException;
import proy.logica.gestores.filtros.FiltroHerramienta;
import proy.logica.gestores.filtros.FiltroMaquina;
import proy.logica.gestores.filtros.FiltroMaterial;
import proy.logica.gestores.filtros.FiltroParte;
import proy.logica.gestores.filtros.FiltroPieza;
import proy.logica.gestores.resultados.ResultadoCrearHerramientas;
import proy.logica.gestores.resultados.ResultadoCrearHerramientas.ErrorCrearHerramientas;
import proy.logica.gestores.resultados.ResultadoCrearMaquina;
import proy.logica.gestores.resultados.ResultadoCrearMaquina.ErrorCrearMaquina;
import proy.logica.gestores.resultados.ResultadoCrearMateriales;
import proy.logica.gestores.resultados.ResultadoCrearMateriales.ErrorCrearMateriales;
import proy.logica.gestores.resultados.ResultadoCrearModificarPartes;
import proy.logica.gestores.resultados.ResultadoCrearModificarPartes.ErrorCrearModificarPartes;
import proy.logica.gestores.resultados.ResultadoCrearModificarPiezas;
import proy.logica.gestores.resultados.ResultadoCrearParte;
import proy.logica.gestores.resultados.ResultadoCrearPieza;
import proy.logica.gestores.resultados.ResultadoEliminarHerramienta;
import proy.logica.gestores.resultados.ResultadoEliminarHerramientas;
import proy.logica.gestores.resultados.ResultadoEliminarMaquina;
import proy.logica.gestores.resultados.ResultadoEliminarMateriales;
import proy.logica.gestores.resultados.ResultadoEliminarMateriales.ErrorEliminarMateriales;
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

	public ArrayList<Maquina> listarMaquinas(FiltroMaquina filtro) throws PersistenciaException {
		return persistidorTaller.obtenerMaquinas(filtro);
	}

	public ResultadoCrearMaquina crearMaquina(Maquina maquina) throws PersistenciaException {
		ResultadoCrearMaquina resultado = validarCrearMaquina(maquina);

		if(!resultado.hayErrores()){
			persistidorTaller.guardarMaquina(maquina);
		}

		return resultado;
	}

	public ResultadoCrearMaquina validarCrearMaquina(Maquina maquina) throws PersistenciaException {
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

		return new ResultadoCrearMaquina(errores.toArray(new ErrorCrearMaquina[0]));
	}

	public ResultadoModificarMaquina modificarMaquina(Maquina maquina) throws PersistenciaException {
		ResultadoModificarMaquina resultado = validarModificarMaquina(maquina);

		if(!resultado.hayErrores()){
			persistidorTaller.actualizarMaquina(maquina);
		}

		return resultado;
	}

	public ResultadoModificarMaquina validarModificarMaquina(Maquina maquina) throws PersistenciaException {
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

		ResultadoCrearModificarPartes resultado = validarPartes(maquina.getPartes());
		if(resultado.hayErrores()){
			//errores.add();
		}

		return new ResultadoModificarMaquina(errores.toArray(new ErrorModificarMaquina[0]));
	}

	public ResultadoCrearModificarPartes validarPartes(Collection<Parte> partesAGuardarOModificar) throws PersistenciaException {
		Set<ErrorCrearModificarPartes> errores = new HashSet<>();
		Map<String, ResultadoCrearModificarPiezas> resultadosCrearModificarPiezas = new HashMap<>();
		Map<Maquina, ArrayList<ErrorCrearModificarPartes>> erroresPorMaquina = new HashMap<>();
		Map<Maquina, HashSet<String>> nombresYaExistentesPorMaquina = new HashMap<>();

		//divido las partes por máquina. Solo me quedo con sus nombres
		Map<Maquina, ArrayList<String>> nombresPartesPorMaquina = new HashMap<>();
		for(Parte parte: partesAGuardarOModificar){
			if(!nombresPartesPorMaquina.containsKey(parte.getMaquina())){
				nombresPartesPorMaquina.put(parte.getMaquina(), new ArrayList<String>());
			}
			nombresPartesPorMaquina.get(parte.getMaquina()).add(parte.getNombre());
		}

		//Busco si hay nombres incompletos, y me quedo solo con los completos.
		boolean nombreIncompletoEncontrado = false;
		@SuppressWarnings("unused")
		boolean nombreIncompletoEncontradoPorMaquina;
		for(Map.Entry<Maquina, ArrayList<String>> maquinaYPartes: nombresPartesPorMaquina.entrySet()){
			nombreIncompletoEncontradoPorMaquina = false;
			Iterator<String> it = maquinaYPartes.getValue().iterator();
			while(it.hasNext()){
				String nombreParte = it.next();
				if(nombreParte == null || nombreParte.isEmpty()){
					nombreIncompletoEncontradoPorMaquina = true;
					it.remove();
				}
			}
			if(nombreIncompletoEncontradoPorMaquina = true){
				nombreIncompletoEncontrado = true;
				if(!erroresPorMaquina.containsKey(maquinaYPartes.getKey())){
					erroresPorMaquina.put(maquinaYPartes.getKey(), new ArrayList<>());
				}
				erroresPorMaquina.get(maquinaYPartes.getKey()).add(ErrorCrearModificarPartes.NOMBRE_INCOMPLETO);
			}
		}
		if(nombreIncompletoEncontrado){
			errores.add(ErrorCrearModificarPartes.NOMBRE_INCOMPLETO);
		}

		//Ordeno los nombres alfabéticamente para que nombres iguales queden uno al lado del otro
		for(ArrayList<String> nombresPartes: nombresPartesPorMaquina.values()){
			Collections.sort(nombresPartes);
		}

		//Veo si hay repeticiones entre los nombres que que el usuario quiere guardar, y las remuevo
		boolean nombreIngresadoRepetidoEncontrado = false;
		for(Map.Entry<Maquina, ArrayList<String>> maquinaYPartes: nombresPartesPorMaquina.entrySet()){
			Iterator<String> it = maquinaYPartes.getValue().iterator();
			String nombrePrevio = "";
			String nombreActual;
			boolean nombreIngresadoRepetidoEncontradoPorMaquina = false;
			while(it.hasNext()){
				nombreActual = it.next();
				if(nombreActual.equals(nombrePrevio)){
					nombreIngresadoRepetidoEncontradoPorMaquina = true;
					it.remove();
				}
				else{
					nombrePrevio = nombreActual;
				}
			}
			if(nombreIngresadoRepetidoEncontradoPorMaquina){
				if(!erroresPorMaquina.containsKey(maquinaYPartes.getKey())){
					erroresPorMaquina.put(maquinaYPartes.getKey(), new ArrayList<>());
				}
				erroresPorMaquina.get(maquinaYPartes.getKey()).add(ErrorCrearModificarPartes.NOMBRE_INGRESADO_REPETIDO);
			}
		}
		if(nombreIngresadoRepetidoEncontrado){
			errores.add(ErrorCrearModificarPartes.NOMBRE_INGRESADO_REPETIDO);
		}

		//Busco en la BD coincidencias en los nombres
		boolean nombresYaExistentesEncontrados = false;
		for(Map.Entry<Maquina, ArrayList<String>> maquinaYPartes: nombresPartesPorMaquina.entrySet()){
			ArrayList<Parte> partesConNombreCoincidentePorMaquina = persistidorTaller.obtenerPartes(new FiltroParte.Builder().nombres(maquinaYPartes.getValue()).build());
			if(!partesConNombreCoincidentePorMaquina.isEmpty()){
				nombresYaExistentesEncontrados = true;
				if(!erroresPorMaquina.containsKey(maquinaYPartes.getKey())){
					erroresPorMaquina.put(maquinaYPartes.getKey(), new ArrayList<>());
				}
				erroresPorMaquina.get(maquinaYPartes.getKey()).add(ErrorCrearModificarPartes.NOMBRE_YA_EXISTENTE);
			}
			for(Parte parte: partesConNombreCoincidentePorMaquina){
				if(!nombresYaExistentesPorMaquina.containsKey(maquinaYPartes.getKey())){
					nombresYaExistentesPorMaquina.put(maquinaYPartes.getKey(), new HashSet<>());
				}
				nombresYaExistentesPorMaquina.get(maquinaYPartes.getKey()).add(parte.getNombre());
			}
		}
		if(nombresYaExistentesEncontrados){
			errores.add(ErrorCrearModificarPartes.NOMBRE_YA_EXISTENTE);
		}

		//Valido la creación o modificación de las piezas de cada parte
		for(Parte parte: partesAGuardarOModificar){
			ResultadoCrearModificarPiezas resultado = validarPiezas(parte.getPiezas());
			if(resultado.hayErrores()){
				resultadosCrearModificarPiezas.put(parte.toString(), resultado);
				errores.add(ErrorCrearModificarPartes.ERROR_AL_CREAR_O_MODIFICAR_PIEZAS);
			}
		}

		return new ResultadoCrearModificarPartes(erroresPorMaquina, nombresYaExistentesPorMaquina, resultadosCrearModificarPiezas, errores.toArray(new ErrorCrearModificarPartes[0]));
	}

	public ResultadoCrearModificarPiezas validarPiezas(Collection<Pieza> piezas) {

		return new ResultadoCrearModificarPiezas();
	}

	public ResultadoEliminarMaquina eliminarMaquina(Maquina maquina) throws PersistenciaException {
		persistidorTaller.bajaMaquina(maquina);

		return new ResultadoEliminarMaquina();
	}

	public ArrayList<Parte> listarPartes(FiltroParte filtro) throws PersistenciaException {
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

	public ResultadoEliminarPartes eliminarPartes(ArrayList<Parte> partesAEliminar) throws PersistenciaException {
		ResultadoEliminarPartes resultado = validarEliminarPartes(partesAEliminar);

		if(!resultado.hayErrores()){
			ArrayList<Parte> partesABajaFisica = new ArrayList<>(partesAEliminar);
			ArrayList<Parte> partesABajaLogica;

			//si la parte tiene tareas asociadas, se le da baja lógica
			partesABajaLogica = persistidorTaller.obtenerPartes(new FiltroParte.Builder().partes(partesAEliminar).conTareas().build());

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

	private ResultadoEliminarPartes validarEliminarPartes(ArrayList<Parte> partesAEliminar) {
		Set<ErrorEliminarPartes> errores = new HashSet<>();

		//Verifico si se pueden borrar las piezas y procesos asociados
		Map<String, ResultadoEliminarPiezas> resultadosEliminarPiezas = new HashMap<>();
		Map<String, ResultadoEliminarProcesos> resultadosEliminarProcesos = new HashMap<>();
		for(Parte parte: partesAEliminar){
			resultadosEliminarPiezas.put(parte.toString(), this.validarEliminarPiezas(new ArrayList<>(parte.getPiezas())));
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

	public ArrayList<Pieza> listarPiezas(FiltroPieza filtro) throws PersistenciaException {
		return persistidorTaller.obtenerPiezas(filtro);
	}

	public ResultadoCrearPieza crearPieza(Pieza pieza) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

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

	public ArrayList<Herramienta> listarHerramientas(FiltroHerramienta filtro) throws PersistenciaException {
		return persistidorTaller.obtenerHerramientas(filtro);
	}

	public ResultadoCrearHerramientas crearHerramientas(ArrayList<Herramienta> herramientas) throws PersistenciaException {
		ResultadoCrearHerramientas resultado = validarCrearHerramientas(herramientas);
		if(!resultado.hayErrores()){
			persistidorTaller.guardarHerramientas(herramientas);
		}
		return resultado;
	}

	public ResultadoCrearHerramientas validarCrearHerramientas(ArrayList<Herramienta> herramientas) throws PersistenciaException {
		ArrayList<String> nombresHerramientasRepetidos = new ArrayList<>();
		ListIterator<Herramienta> itHerramientasAGuardar = null, itHerramientasGuardadas = null;
		Herramienta herramientaAGuardar = null, herramientaGuardada = null;

		//Creo la lista de errores
		ArrayList<ErrorCrearHerramientas> erroresHerramientas = new ArrayList<>();

		ArrayList<String> nombresHerramientasABuscarEnLaBD = new ArrayList<>();

		//Reviso que el nombre esté completo
		boolean nombreIncompletoEncontrado = false;
		for(Herramienta herramienta: herramientas){
			if(herramienta.getNombre() == null || herramienta.getNombre().isEmpty()){
				nombreIncompletoEncontrado = true;
			}
			else{
				//Si el nombre está completo lo busco en la BD
				nombresHerramientasABuscarEnLaBD.add(herramienta.getNombre());
			}
		}
		//Si encontré un nombre incompleto, agrego el error
		if(nombreIncompletoEncontrado){
			erroresHerramientas.add(ErrorCrearHerramientas.NOMBRE_INCOMPLETO);
		}

		if(!nombresHerramientasABuscarEnLaBD.isEmpty()){

			List<Herramienta> herramientas_coincidentes = persistidorTaller.obtenerHerramientas(new FiltroHerramienta.Builder().nombres(nombresHerramientasABuscarEnLaBD).build());
			if(!herramientas_coincidentes.isEmpty()){
				erroresHerramientas.add(ErrorCrearHerramientas.NOMBRE_YA_EXISTENTE);
				for(Herramienta herramienta: herramientas_coincidentes){
					nombresHerramientasRepetidos.add(herramienta.toString());
				}
			}

		}

		boolean nombreIngresadoRepetidoEncontrado = false;
		itHerramientasAGuardar = herramientas.listIterator();

		while(itHerramientasAGuardar.hasNext() && !nombreIngresadoRepetidoEncontrado){
			herramientaAGuardar = itHerramientasAGuardar.next();
			itHerramientasGuardadas = herramientas.listIterator(itHerramientasAGuardar.nextIndex());
			while(itHerramientasGuardadas.hasNext() && !nombreIngresadoRepetidoEncontrado){
				herramientaGuardada = itHerramientasGuardadas.next();
				if(herramientaAGuardar.getNombre() != null && herramientaGuardada.getNombre() != null &&
						herramientaAGuardar.getNombre().equals(herramientaGuardada.getNombre())){
					nombreIngresadoRepetidoEncontrado = true;
					erroresHerramientas.add(ErrorCrearHerramientas.NOMBRE_REPETIDO);
				}
			}
		}

		return new ResultadoCrearHerramientas(nombresHerramientasRepetidos, erroresHerramientas.toArray(new ErrorCrearHerramientas[0]));
	}

	public ResultadoEliminarHerramienta eliminarHerramienta(ArrayList<Herramienta> herramientas) throws PersistenciaException {
		persistidorTaller.bajaHerramientas(herramientas);
		return new ResultadoEliminarHerramienta();
	}

	public ResultadoEliminarHerramientas eliminarHerramientas(ArrayList<Herramienta> herramientas) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ArrayList<Material> listarMateriales(FiltroMaterial filtro) throws PersistenciaException {
		return persistidorTaller.obtenerMateriales(filtro);
	}

	public ResultadoCrearMateriales crearMateriales(ArrayList<Material> materiales) throws PersistenciaException {
		ResultadoCrearMateriales resultado = validarCrearMateriales(materiales);

		if(!resultado.hayErrores()){
			persistidorTaller.guardarMateriales(materiales);
		}
		return resultado;
	}

	private ResultadoCrearMateriales validarCrearMateriales(ArrayList<Material> materiales) throws PersistenciaException {
		ArrayList<String> nombresMaterialesRepetidos = new ArrayList<>();
		ListIterator<Material> itMaterialesAGuardar = null, itMaterialesGuardados = null;
		Material materialAGuardar = null, materialGuardado = null;

		//Creo la lista de errores
		ArrayList<ErrorCrearMateriales> erroresMateriales = new ArrayList<>();

		//Creo una lista de los nombres de materiales voy a buscar en la BD
		ArrayList<String> nombresMaterialesABuscarEnLaBD = new ArrayList<>();

		//Reviso que el nombre esté completo
		boolean nombreIncompletoEncontrado = false;
		for(Material material: materiales){
			if(material.getNombre() == null || material.getNombre().isEmpty()){
				nombreIncompletoEncontrado = true;
			}
			else{
				//Si el nombre está completo lo busco en la BD
				nombresMaterialesABuscarEnLaBD.add(material.getNombre());
			}
		}
		//Si encontré un nombre incompleto, agrego el error
		if(nombreIncompletoEncontrado){
			erroresMateriales.add(ErrorCrearMateriales.NOMBRE_INCOMPLETO);
		}

		//Si hay materiales a buscar
		if(!nombresMaterialesABuscarEnLaBD.isEmpty()){

			//busco en la BD materiales cuyo nombre coincida con el de alguno de los nuevos materiales
			List<Material> materiales_coincidentes = persistidorTaller.obtenerMateriales(new FiltroMaterial.Builder().nombres(nombresMaterialesABuscarEnLaBD).build());
			if(!materiales_coincidentes.isEmpty()){
				erroresMateriales.add(ErrorCrearMateriales.NOMBRE_YA_EXISTENTE);
				for(Material material: materiales_coincidentes){
					nombresMaterialesRepetidos.add(material.toString());
				}
			}
		}

		//veo si hay nombres que se repiten entre los nuevos materiales
		boolean nombreIngresadoRepetidoEncontrado = false;
		itMaterialesAGuardar = materiales.listIterator();

		while(itMaterialesAGuardar.hasNext() && !nombreIngresadoRepetidoEncontrado){
			materialAGuardar = itMaterialesAGuardar.next();
			itMaterialesGuardados = materiales.listIterator(itMaterialesAGuardar.nextIndex());
			while(itMaterialesGuardados.hasNext() && !nombreIngresadoRepetidoEncontrado){
				materialGuardado = itMaterialesGuardados.next();
				if(materialAGuardar.getNombre() != null && materialGuardado.getNombre() != null &&
						materialAGuardar.getNombre().equals(materialGuardado.getNombre())){
					nombreIngresadoRepetidoEncontrado = true;
					erroresMateriales.add(ErrorCrearMateriales.NOMBRE_INGRESADO_REPETIDO);
				}
			}
		}

		return new ResultadoCrearMateriales(nombresMaterialesRepetidos, erroresMateriales.toArray(new ErrorCrearMateriales[0]));
	}

	public ResultadoEliminarMateriales eliminarMateriales(ArrayList<Material> materiales) throws PersistenciaException {
		ResultadoEliminarMateriales resultado = validarEliminarMateriales(materiales);

		if(!resultado.hayErrores()){
			ArrayList<Material> materialesABajaLogica;
			ArrayList<Material> materialesABajaFisica = new ArrayList<>(materiales);

			//si el material tiene piezas asociadas, se le da baja lógica
			materialesABajaLogica = persistidorTaller.obtenerMateriales(new FiltroMaterial.Builder().materiales(materiales).conPiezas().build());

			//si el material no tiene piezas asociadas, se le da baja fisica
			materialesABajaFisica.removeAll(materialesABajaLogica);

			if(!materialesABajaFisica.isEmpty()){
				try{
					persistidorTaller.bajaMateriales(materialesABajaFisica);
				} catch(ObjNotFoundException e){ //TODO probar y ver si esto anda para extenderlo en todas las bajas
					persistidorTaller.bajaMateriales(persistidorTaller.obtenerMateriales(new FiltroMaterial.Builder().materiales(materialesABajaFisica).build()));
				}
			}

			if(!materialesABajaLogica.isEmpty()){
				for(Material material: materialesABajaLogica){
					material.darDeBaja();
				}
				persistidorTaller.actualizarMateriales(materialesABajaLogica);
			}

		}

		return resultado;
	}

	private ResultadoEliminarMateriales validarEliminarMateriales(ArrayList<Material> materiales) throws PersistenciaException {
		//Creo la lista de errores
		ArrayList<ErrorEliminarMateriales> erroresMateriales = new ArrayList<>();

		//Busco las piezas activas asociadas a los materiales
		ArrayList<Pieza> piezasAsociadas = persistidorTaller.obtenerPiezas(new FiltroPieza.Builder().materiales(materiales).build());

		//Separo las piezas por material
		Map<String, List<String>> piezasAsociadasPorMaterial = new HashMap<>();

		if(!piezasAsociadas.isEmpty()){
			for(Material material: materiales){
				ArrayList<String> piezasDelMaterial = new ArrayList<>();
				piezasAsociadasPorMaterial.put(material.toString(), piezasDelMaterial);

				for(Pieza pieza: piezasAsociadas){
					if(material.equals(pieza.getMaterial())){
						piezasDelMaterial.add(pieza.toString());
					}
				}
			}

			erroresMateriales.add(ErrorEliminarMateriales.PIEZAS_ACTIVAS_ASOCIADAS);
		}

		return new ResultadoEliminarMateriales(piezasAsociadasPorMaterial, erroresMateriales.toArray(new ErrorEliminarMateriales[0]));
	}
}
