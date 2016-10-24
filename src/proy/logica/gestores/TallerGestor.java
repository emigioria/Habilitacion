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

import proy.datos.clases.EstadoStr;
import proy.datos.entidades.Estado;
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
import proy.logica.gestores.resultados.ResultadoCrearMateriales;
import proy.logica.gestores.resultados.ResultadoCrearMateriales.ErrorCrearMateriales;
import proy.logica.gestores.resultados.ResultadoCrearParte;
import proy.logica.gestores.resultados.ResultadoCrearPieza;
import proy.logica.gestores.resultados.ResultadoEliminarHerramienta;
import proy.logica.gestores.resultados.ResultadoEliminarMaquina;
import proy.logica.gestores.resultados.ResultadoEliminarMateriales;
import proy.logica.gestores.resultados.ResultadoEliminarMateriales.ErrorEliminarMateriales;
import proy.logica.gestores.resultados.ResultadoEliminarParte;
import proy.logica.gestores.resultados.ResultadoEliminarPieza;
import proy.logica.gestores.resultados.ResultadoModificarMaquina;
import proy.logica.gestores.resultados.ResultadoModificarParte;

@Service
public class TallerGestor {

	@Resource
	private TallerService persistidorTaller;

	public ArrayList<Maquina> listarMaquinas(FiltroMaquina filtro) throws PersistenciaException {
		return persistidorTaller.obtenerMaquinas(filtro);
	}

	public ResultadoCrearMaquina crearMaquina(Maquina maquina) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoModificarMaquina modificarMaquina(Maquina maquina) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoEliminarMaquina eliminarMaquina(Maquina maquina) throws PersistenciaException {
		throw new NotYetImplementedException();
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

	public ResultadoEliminarParte eliminarParte(Parte parte) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ArrayList<Pieza> listarPiezas(FiltroPieza filtro) throws PersistenciaException {
		return listarPiezas(filtro);
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
			return new ResultadoCrearHerramienta(ErrorCrearHerramienta.NombreIncompleto);
		}
		else{
			ArrayList<Herramienta> lista = persistidorTaller.obtenerHerramientas(new FiltroHerramienta.Builder().nombre(herramienta.getNombre()).build());
			if(lista.size() != 0){
				return new ResultadoCrearHerramienta(ErrorCrearHerramienta.NombreRepetido);
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
		ArrayList<String> materiales_a_buscar_en_la_BD = new ArrayList<>();

		//Reviso que el nombre esté completo
		boolean nombreIncompletoEncontrado = false;
		for(Material m: materiales){
			if(m.getNombre() == null || m.getNombre().isEmpty()){
				nombreIncompletoEncontrado = true;
			}
			else{
				//Si el nombre está completo lo busco en la BD
				materiales_a_buscar_en_la_BD.add(m.getNombre());
			}
		}
		//Si encontré un nombre incompleto, agrego el error
		if(nombreIncompletoEncontrado){
			erroresMateriales.add(ErrorCrearMateriales.NombreIncompleto);
		}

		//Si hay materiales a buscar
		if(!materiales_a_buscar_en_la_BD.isEmpty()){

			//busco en la BD materiales cuyo nombre coincida con el de alguno de los nuevos materiales
			List<Material> materiales_coincidentes = persistidorTaller.obtenerMateriales(new FiltroMaterial.Builder().nombres(materiales_a_buscar_en_la_BD).build());
			if(!materiales_coincidentes.isEmpty()){
				erroresMateriales.add(ErrorCrearMateriales.NombreYaExistente);
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
					erroresMateriales.add(ErrorCrearMateriales.NombreIngresadoRepetido);
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

			ArrayList<String> nombresMateriales = new ArrayList<>();
			for(Material material: materiales){
				nombresMateriales.add(material.getNombre());
			}

			//si el material tiene piezas asociadas, se le da baja lógica
			materialesABajaLogica = persistidorTaller.obtenerMateriales(new FiltroMaterial.Builder().nombres(nombresMateriales).conPiezas().build());

			//si el material no tiene piezas asociadas, se le da baja fisica
			materialesABajaFisica.removeAll(materialesABajaLogica);

			if(!materialesABajaFisica.isEmpty()){
				persistidorTaller.bajaMateriales(materialesABajaFisica);
			}

			if(!materialesABajaLogica.isEmpty()){
				for(Material material: materialesABajaLogica){
					material.setEstado(new Estado(EstadoStr.BAJA));
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

			erroresMateriales.add(ErrorEliminarMateriales.PiezasActivasAsociadas);
		}

		return new ResultadoEliminarMateriales(piezasAsociadasPorMaterial, erroresMateriales.toArray(new ErrorEliminarMateriales[0]));
	}
}
