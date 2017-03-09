package proy.logica.gestores;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Service;

import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Material;
import proy.datos.entidades.Pieza;
import proy.datos.filtros.Filtro;
import proy.datos.filtros.implementacion.FiltroHerramienta;
import proy.datos.filtros.implementacion.FiltroMaterial;
import proy.datos.filtros.implementacion.FiltroPieza;
import proy.datos.servicios.MaterialService;
import proy.datos.servicios.TallerService;
import proy.excepciones.ObjNotFoundException;
import proy.excepciones.PersistenciaException;
import proy.logica.gestores.resultados.ResultadoCrearHerramientas;
import proy.logica.gestores.resultados.ResultadoCrearHerramientas.ErrorCrearHerramientas;
import proy.logica.gestores.resultados.ResultadoCrearMateriales;
import proy.logica.gestores.resultados.ResultadoCrearMateriales.ErrorCrearMateriales;
import proy.logica.gestores.resultados.ResultadoEliminarHerramienta;
import proy.logica.gestores.resultados.ResultadoEliminarHerramientas;
import proy.logica.gestores.resultados.ResultadoEliminarMaterial;
import proy.logica.gestores.resultados.ResultadoEliminarMaterial.ErrorEliminarMaterial;

@Service
public class MaterialGestor {

	@Resource
	private MaterialService persistidorMaterial;

	@Resource
	private TallerService persistidorTaller;

	public ArrayList<Herramienta> listarHerramientas(Filtro<Herramienta> filtro) throws PersistenciaException {
		return persistidorMaterial.obtenerHerramientas(filtro);
	}

	public ResultadoCrearHerramientas crearHerramientas(ArrayList<Herramienta> herramientas) throws PersistenciaException {
		ResultadoCrearHerramientas resultado = validarCrearHerramientas(herramientas);
		if(!resultado.hayErrores()){
			persistidorMaterial.guardarHerramientas(herramientas);
		}
		return resultado;
	}

	private ResultadoCrearHerramientas validarCrearHerramientas(ArrayList<Herramienta> herramientas) throws PersistenciaException {
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

			List<Herramienta> herramientas_coincidentes = persistidorMaterial.obtenerHerramientas(new FiltroHerramienta.Builder().nombres(nombresHerramientasABuscarEnLaBD).build());
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
		persistidorMaterial.bajaHerramientas(herramientas);
		return new ResultadoEliminarHerramienta();
	}

	public ResultadoEliminarHerramientas eliminarHerramientas(ArrayList<Herramienta> herramientas) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ArrayList<Material> listarMateriales(Filtro<Material> filtro) throws PersistenciaException {
		return persistidorMaterial.obtenerMateriales(filtro);
	}

	public ResultadoCrearMateriales crearMateriales(ArrayList<Material> materiales) throws PersistenciaException {
		ResultadoCrearMateriales resultado = validarCrearMateriales(materiales);

		if(!resultado.hayErrores()){
			persistidorMaterial.guardarMateriales(materiales);
		}
		return resultado;
	}

	private ResultadoCrearMateriales validarCrearMateriales(ArrayList<Material> materiales) throws PersistenciaException {
		//Creo la lista de errores
		Set<ErrorCrearMateriales> erroresMateriales = new HashSet<>();

		//Creo una lista de los nombres de materiales voy a buscar en la BD
		ArrayList<String> nombresMaterialesABuscarEnLaBD = new ArrayList<>();
		ArrayList<Material> materialesAValidar = new ArrayList<>();

		//Reviso que el nombre esté completo
		for(Material material: materiales){
			if(material.getNombre() == null || material.getNombre().isEmpty()){
				//Si encontré un nombre incompleto, agrego el error
				erroresMateriales.add(ErrorCrearMateriales.NOMBRE_INCOMPLETO);
			}
			else{
				//Si el nombre está completo lo busco en la BD
				nombresMaterialesABuscarEnLaBD.add(material.getNombre());
				materialesAValidar.add(material);
			}
		}

		//Si hay materiales a buscar
		ArrayList<String> nombresMaterialesRepetidosBD = new ArrayList<>();
		if(!nombresMaterialesABuscarEnLaBD.isEmpty()){
			//busco en la BD materiales cuyo nombre coincida con el de alguno de los nuevos materiales
			List<Material> materiales_coincidentes = persistidorMaterial.obtenerMateriales(new FiltroMaterial.Builder().nombres(nombresMaterialesABuscarEnLaBD).build());
			if(!materiales_coincidentes.isEmpty()){
				erroresMateriales.add(ErrorCrearMateriales.NOMBRE_YA_EXISTENTE);
				for(Material material: materiales_coincidentes){
					nombresMaterialesRepetidosBD.add(material.toString());
				}
			}
		}

		//veo si hay nombres que se repiten entre los nuevos materiales con nombres
		//primero los ordeno por nombre
		materialesAValidar.sort((p1, p2) -> {
			return p1.getNombre().compareTo(p2.getNombre());
		});

		//luego busco coincidencias entre los nombres de los materiales ingresados
		if(!materialesAValidar.isEmpty()){
			Iterator<Material> it1 = materialesAValidar.iterator();
			Material anterior = it1.next();
			Material actual;
			while(it1.hasNext()){
				actual = it1.next();
				if(actual.getNombre().equals(anterior.getNombre())){
					erroresMateriales.add(ErrorCrearMateriales.NOMBRE_INGRESADO_REPETIDO);
				}
				anterior = actual;
			}
		}

		return new ResultadoCrearMateriales(nombresMaterialesRepetidosBD, erroresMateriales.toArray(new ErrorCrearMateriales[0]));
	}

	public ResultadoEliminarMaterial eliminarMaterial(Material material) throws PersistenciaException {
		ResultadoEliminarMaterial resultado = validarEliminarMaterial(material);

		if(!resultado.hayErrores()){
			ArrayList<Material> materialesABajaLogica;
			ArrayList<Material> materialesABajaFisica = new ArrayList<>();
			materialesABajaFisica.add(material);

			//si el material tiene piezas asociadas, se le da baja lógica
			materialesABajaLogica = persistidorMaterial.obtenerMateriales(new FiltroMaterial.Builder().materiales(materialesABajaFisica).conPiezas().build());

			//si el material no tiene piezas asociadas, se le da baja fisica
			materialesABajaFisica.removeAll(materialesABajaLogica);

			if(!materialesABajaFisica.isEmpty()){
				try{
					persistidorMaterial.bajaMateriales(materialesABajaFisica);
				} catch(ObjNotFoundException e){
					//Si no se encontró ya fue eliminado previamente.
				}
			}

			if(!materialesABajaLogica.isEmpty()){
				for(Material m: materialesABajaLogica){
					m.darDeBaja();
				}
				persistidorMaterial.actualizarMateriales(materialesABajaLogica);
			}

		}

		return resultado;
	}

	private ResultadoEliminarMaterial validarEliminarMaterial(Material material) throws PersistenciaException {
		//Creo la lista de errores
		ArrayList<ErrorEliminarMaterial> erroresMateriales = new ArrayList<>();

		ArrayList<Material> materiales = new ArrayList<>();
		materiales.add(material);

		//Busco las piezas activas asociadas al material
		ArrayList<Pieza> piezasAsociadas = persistidorTaller.obtenerPiezas(new FiltroPieza.Builder().materiales(materiales).build());
		if(!piezasAsociadas.isEmpty()){
			erroresMateriales.add(ErrorEliminarMaterial.PIEZAS_ACTIVAS_ASOCIADAS);
		}

		//Agrego los nombres de las piezas a la lista que se asociará al resultado
		ArrayList<String> piezasAsociadasAlMaterial = new ArrayList<>();
		for(Pieza pieza: piezasAsociadas){
			piezasAsociadasAlMaterial.add(pieza.toString());
		}

		return new ResultadoEliminarMaterial(piezasAsociadasAlMaterial, erroresMateriales.toArray(new ErrorEliminarMaterial[0]));
	}
}
