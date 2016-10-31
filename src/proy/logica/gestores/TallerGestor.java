/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Service;

import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Maquina;
import proy.datos.entidades.Material;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.datos.servicios.TallerService;
import proy.excepciones.PersistenciaException;
import proy.logica.gestores.filtros.FiltroHerramienta;
import proy.logica.gestores.filtros.FiltroMaquina;
import proy.logica.gestores.filtros.FiltroMaterial;
import proy.logica.gestores.filtros.FiltroParte;
import proy.logica.gestores.filtros.FiltroPieza;
import proy.logica.gestores.resultados.ResultadoCrearHerramienta;
import proy.logica.gestores.resultados.ResultadoCrearHerramienta.ErrorCrearHerramienta;
import proy.logica.gestores.resultados.ResultadoCrearMaquina;
import proy.logica.gestores.resultados.ResultadoCrearMaquina.ErrorCrearMaquina;
import proy.logica.gestores.resultados.ResultadoCrearMateriales;
import proy.logica.gestores.resultados.ResultadoCrearMateriales.ErrorCrearMateriales;
import proy.logica.gestores.resultados.ResultadoCrearParte;
import proy.logica.gestores.resultados.ResultadoCrearPieza;
import proy.logica.gestores.resultados.ResultadoEliminarHerramienta;
import proy.logica.gestores.resultados.ResultadoEliminarMaquina;
import proy.logica.gestores.resultados.ResultadoEliminarMateriales;
import proy.logica.gestores.resultados.ResultadoEliminarMateriales.ErrorEliminarMateriales;
import proy.logica.gestores.resultados.ResultadoEliminarPartes;
import proy.logica.gestores.resultados.ResultadoEliminarPieza;
import proy.logica.gestores.resultados.ResultadoModificarMaquina;
import proy.logica.gestores.resultados.ResultadoModificarMaquina.ErrorModificarMaquina;
import proy.logica.gestores.resultados.ResultadoModificarParte;

@Service
public class TallerGestor {

	@Resource
	private TallerService persistidorTaller;

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

		return new ResultadoModificarMaquina(errores.toArray(new ErrorModificarMaquina[0]));
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

	public ResultadoModificarParte modificarParte(Parte parte) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoEliminarPartes eliminarPartes(ArrayList<Parte> partesAEliminar) throws PersistenciaException {
		ResultadoEliminarPartes resultado = validarEliminarPartes(partesAEliminar);
		
		ArrayList<Parte> partesABajaLogica;
		ArrayList<Parte> partesABajaFisica;
		ArrayList<Pieza> piezasABajaLogica;
		
		if(!resultado.hayErrores()){
			partesABajaFisica = new ArrayList<>(partesAEliminar);

			//si la parte tiene tareas asociadas, se le da baja lógica
			partesABajaLogica = persistidorTaller.obtenerPartes(new FiltroParte.Builder().partes(partesAEliminar).conTareas().build());

			//si la parte no tiene tareas asociadas, se le da baja fisica
			partesABajaFisica.removeAll(partesABajaLogica);

			if(!partesABajaFisica.isEmpty()){
				persistidorTaller.bajaPartes(partesABajaFisica);
			}

			if(!partesABajaLogica.isEmpty()){
				
				//dar de baja logica piezas
				piezasABajaLogica = new ArrayList<>();
				for(Parte parte: partesABajaLogica){
					piezasABajaLogica.addAll(persistidorTaller.obtenerPiezas(new FiltroPieza.Builder().parte(parte).build()));
				}
				for(Pieza pieza: piezasABajaLogica){
					pieza.darDeBaja();
				}
				persistidorTaller.actualizarPiezas(piezasABajaLogica);
				
				
				//dar de baja logica partes
				for(Parte parte: partesABajaLogica){
					parte.darDeBaja();
				}
				persistidorTaller.actualizarPartes(partesABajaLogica);
				
				return new ResultadoEliminarPartes(null,partesABajaLogica);
			}
		}
		
		return resultado;
	}

	private ResultadoEliminarPartes validarEliminarPartes(ArrayList<Parte> partesAEliminar) {
		return new ResultadoEliminarPartes();
	}

	public ArrayList<Pieza> listarPiezas(FiltroPieza filtro) throws PersistenciaException {
		return persistidorTaller.obtenerPiezas(filtro);
	}

	public ResultadoCrearPieza crearPieza(Pieza pieza) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoEliminarPieza eliminarPieza(Pieza pieza) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ArrayList<Herramienta> listarHerramientas(FiltroHerramienta filtro) throws PersistenciaException {
		return persistidorTaller.obtenerHerramientas(filtro);
	}

	public ResultadoCrearHerramienta crearHerramienta(Herramienta herramienta) throws PersistenciaException {
		ResultadoCrearHerramienta resultado = validarCrearHerramienta(herramienta);

		if(!resultado.hayErrores()){
			persistidorTaller.guardarHerramienta(herramienta);
		}
		return resultado;
	}

	public ResultadoCrearHerramienta validarCrearHerramienta(Herramienta herramienta) throws PersistenciaException {
		if(herramienta.getNombre() == null || herramienta.getNombre().isEmpty()){
			return new ResultadoCrearHerramienta(ErrorCrearHerramienta.NOMBRE_INCOMPLETO);
		}
		else{
			ArrayList<Herramienta> herramientasRepetidas = persistidorTaller.obtenerHerramientas(new FiltroHerramienta.Builder().nombre(herramienta.getNombre()).build());
			if(!herramientasRepetidas.isEmpty()){
				return new ResultadoCrearHerramienta(ErrorCrearHerramienta.NOMBRE_REPETIDO);
			}
		}
		return new ResultadoCrearHerramienta();
	}

	public ResultadoEliminarHerramienta eliminarHerramienta(Herramienta herramienta) throws PersistenciaException {
		persistidorTaller.bajaHerramienta(herramienta);
		return new ResultadoEliminarHerramienta();
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
		ArrayList<Material> materiales_a_buscar_en_la_BD = new ArrayList<>();

		//Reviso que el nombre esté completo
		boolean nombreIncompletoEncontrado = false;
		for(Material m: materiales){
			if(m.getNombre() == null || m.getNombre().isEmpty()){
				nombreIncompletoEncontrado = true;
			}
			else{
				//Si el nombre está completo lo busco en la BD
				materiales_a_buscar_en_la_BD.add(m);
			}
		}
		//Si encontré un nombre incompleto, agrego el error
		if(nombreIncompletoEncontrado){
			erroresMateriales.add(ErrorCrearMateriales.NOMBRE_INCOMPLETO);
		}

		//Si hay materiales a buscar
		if(!materiales_a_buscar_en_la_BD.isEmpty()){

			//busco en la BD materiales cuyo nombre coincida con el de alguno de los nuevos materiales
			List<Material> materiales_coincidentes = persistidorTaller.obtenerMateriales(new FiltroMaterial.Builder().materiales(materiales_a_buscar_en_la_BD).build());
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
				persistidorTaller.bajaMateriales(materialesABajaFisica);
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
