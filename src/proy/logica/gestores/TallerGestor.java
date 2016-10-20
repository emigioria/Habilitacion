/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores;

import java.util.ArrayList;
import java.util.List;

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

	public ArrayList<ResultadoCrearMaterial> crearMateriales(ArrayList<Material> materiales) throws PersistenciaException {
		ArrayList<ResultadoCrearMaterial> resultados = validarCrearMateriales(materiales);

		if(resultados.stream().allMatch(r -> r.hayErrores() == false)){
			persistidorTaller.guardarMateriales(materiales);
		}
		return resultados;
	}

	private ArrayList<ResultadoCrearMaterial> validarCrearMateriales(ArrayList<Material> materiales) throws PersistenciaException {

		//Creo e inicializo la lista de errores para cada material
		ArrayList<ArrayList<ErrorCrearMaterial>> erroresMateriales = new ArrayList<>(materiales.size());
		for(int i = 0; i < materiales.size(); i++){
			erroresMateriales.add(new ArrayList<ErrorCrearMaterial>());
		}

		//Creo una lista de los nombres de materiales voy a buscar en la BD
		ArrayList<String> materiales_a_buscar_en_la_BD = new ArrayList<>();

		//Reviso que el nombre esté completo
		for(int i = 0; i < materiales.size(); i++){
			if(materiales.get(i).getNombre() == null || materiales.get(i).getNombre().isEmpty()){
				//Si el nombre está incompleto agrego el error
				erroresMateriales.get(i).add(ErrorCrearMaterial.NombreIncompleto);
			}
			else{
				//Si el nombre está completo lo busco en la BD
				materiales_a_buscar_en_la_BD.add(materiales.get(i).getNombre());
			}
		}

		if(!materiales_a_buscar_en_la_BD.isEmpty()){
			//busco en la BD materiales cuyo nombre coincida con el de alguno de los nuevos materiales
			List<Material> materiales_coincidentes = persistidorTaller.obtenerMateriales(
					new FiltroMaterial.Builder().nombres(materiales_a_buscar_en_la_BD).build());

			//veo qué materiales están repetidos
			for(int i = 0; i < materiales.size(); i++){
				for(Material c: materiales_coincidentes){
					if(materiales.get(i).getNombre().equals(c.getNombre())){
						erroresMateriales.get(i).add(ErrorCrearMaterial.NombreRepetido);
					}
				}
			}
		}

		//Creo e inicializo la lista de resultados para cada material
		ArrayList<ResultadoCrearMaterial> resultados = new ArrayList<>(materiales.size());
		for(ArrayList<ErrorCrearMaterial> errores: erroresMateriales){
			resultados.add(new ResultadoCrearMaterial(errores.toArray(new ErrorCrearMaterial[0])));
		}

		return resultados;
	}

	public ResultadoEliminarMaterial eliminarMaterial(Material material) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

}
