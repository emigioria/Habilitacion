/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

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
import proy.logica.gestores.resultados.ResultadoCrearMaterial;
import proy.logica.gestores.resultados.ResultadoCrearMaterial.ErrorCrearMaterial;
import proy.logica.gestores.resultados.ResultadoCrearParte;
import proy.logica.gestores.resultados.ResultadoCrearPieza;
import proy.logica.gestores.resultados.ResultadoEliminarHerramienta;
import proy.logica.gestores.resultados.ResultadoEliminarMaquina;
import proy.logica.gestores.resultados.ResultadoEliminarMaterial;
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

	public ResultadoCrearMaterial crearMateriales(ArrayList<Material> materiales, ArrayList<Integer> repetidos) throws PersistenciaException {
		ResultadoCrearMaterial resultado = validarCrearMateriales(materiales, repetidos);

		if(resultado.hayErrores() == false){
			persistidorTaller.guardarMateriales(materiales);
		}
		return resultado;
	}

	private ResultadoCrearMaterial validarCrearMateriales(ArrayList<Material> materiales, ArrayList<Integer> repetidos) throws PersistenciaException {
		ListIterator<Material> itMaterial1 = null, itMaterial2 = null;
		Material material1 = null, material2 = null;

		//Creo la lista de errores
		ArrayList<ErrorCrearMaterial> erroresMateriales = new ArrayList<>();

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
			erroresMateriales.add(ErrorCrearMaterial.NombreIncompleto);
		}

		if(!materiales_a_buscar_en_la_BD.isEmpty()){

			//busco en la BD materiales cuyo nombre coincida con el de alguno de los nuevos materiales
			List<Material> materiales_coincidentes = persistidorTaller.obtenerMateriales(
					new FiltroMaterial.Builder().nombres(materiales_a_buscar_en_la_BD).build());

			//veo qué materiales están repetidos
			boolean nombreRepetidoEnBDEncontrado;
			itMaterial1 = materiales.listIterator();
			while(itMaterial1.hasNext()){
				nombreRepetidoEnBDEncontrado = false;
				material1 = itMaterial1.next();
				itMaterial2 = materiales_coincidentes.listIterator();
				while(itMaterial2.hasNext() && !nombreRepetidoEnBDEncontrado){
					material2 = itMaterial2.next();
					if(material1.getNombre() != null && material2.getNombre() != null &&
							material1.getNombre().equals(material2.getNombre())){

						nombreRepetidoEnBDEncontrado = true;
						//agrego el índice del material repetido a la lista de repetidos
						repetidos.add(itMaterial1.previousIndex());
					}
				}
			}
			itMaterial1 = null;
			itMaterial2 = null;
			material1 = null;
			if(!repetidos.isEmpty()){
				erroresMateriales.add(ErrorCrearMaterial.NombreRepetidoEnBD);
			}
		}

		//veo si hay nombres que se repiten entre los nuevos materiales
		boolean nombreRepetidoEnVistaEncontrado = false;
		itMaterial1 = materiales.listIterator();
		while(itMaterial1.hasNext() && !nombreRepetidoEnVistaEncontrado){
			material1 = itMaterial1.next();
			itMaterial2 = materiales.listIterator(itMaterial1.nextIndex());
			while(itMaterial2.hasNext() && !nombreRepetidoEnVistaEncontrado){
				material2 = itMaterial2.next();
				if(material1.getNombre() != null && material2.getNombre() != null &&
						material1.getNombre().equals(material2.getNombre())){
					nombreRepetidoEnVistaEncontrado = true;
					erroresMateriales.add(ErrorCrearMaterial.NombreRepetidoEnVista);
				}
			}
		}
		itMaterial1 = null;
		itMaterial2 = null;
		material1 = null;

		return new ResultadoCrearMaterial(erroresMateriales.toArray(new ErrorCrearMaterial[0]));
	}

	public ResultadoEliminarMaterial eliminarMaterial(Material material) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

}
