package proy.logica.gestores;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Material;
import proy.datos.entidades.Pieza;
import proy.datos.entidades.Proceso;
import proy.datos.entidades.Tarea;
import proy.datos.filtros.Filtro;
import proy.datos.filtros.implementacion.FiltroHerramienta;
import proy.datos.filtros.implementacion.FiltroMaterial;
import proy.datos.filtros.implementacion.FiltroPieza;
import proy.datos.filtros.implementacion.FiltroTarea;
import proy.datos.servicios.MaterialService;
import proy.datos.servicios.ProcesoService;
import proy.datos.servicios.TallerService;
import proy.excepciones.ObjNotFoundException;
import proy.excepciones.PersistenciaException;
import proy.logica.gestores.resultados.ResultadoCrearHerramientas;
import proy.logica.gestores.resultados.ResultadoCrearHerramientas.ErrorCrearHerramientas;
import proy.logica.gestores.resultados.ResultadoCrearMateriales;
import proy.logica.gestores.resultados.ResultadoCrearMateriales.ErrorCrearMateriales;
import proy.logica.gestores.resultados.ResultadoEliminarHerramienta;
import proy.logica.gestores.resultados.ResultadoEliminarMaterial;
import proy.logica.gestores.resultados.ResultadoEliminarMaterial.ErrorEliminarMaterial;

@Service
public class MaterialGestor {

	@Resource
	private MaterialService persistidorMaterial;

	@Resource
	private TallerService persistidorTaller;

	@Resource
	private ProcesoService persistidorProceso;

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
		//Creo la lista de errores
		Set<ErrorCrearHerramientas> erroresHerramientas = new HashSet<>();

		//Creo una lista de los nombres de herramientas que voy a buscar en la BD
		ArrayList<String> nombresHerramientasABuscarEnLaBD = new ArrayList<>();
		ArrayList<Herramienta> herramientasAValidar = new ArrayList<>();

		//Reviso que el nombre esté completo
		for(Herramienta herramienta: herramientas){
			if(herramienta.getNombre() == null || herramienta.getNombre().isEmpty()){
				//Si encontré un nombre incompleto, agrego el error
				erroresHerramientas.add(ErrorCrearHerramientas.NOMBRE_INCOMPLETO);
			}
			else{
				//Si el nombre está completo lo busco en la BD
				nombresHerramientasABuscarEnLaBD.add(herramienta.getNombre());
				herramientasAValidar.add(herramienta);
			}
		}

		//Si hay materiales a buscar
		ArrayList<String> nombresHerramientasRepetidosBD = new ArrayList<>();
		if(!nombresHerramientasABuscarEnLaBD.isEmpty()){
			//busco en la BD materiales cuyo nombre coincida con el de alguno de los nuevos materiales
			List<Herramienta> herramientas_coincidentes = persistidorMaterial.obtenerHerramientas(new FiltroHerramienta.Builder().nombres(nombresHerramientasABuscarEnLaBD).build());
			if(!herramientas_coincidentes.isEmpty()){
				erroresHerramientas.add(ErrorCrearHerramientas.NOMBRE_YA_EXISTENTE);
				for(Herramienta herramienta: herramientas_coincidentes){
					nombresHerramientasRepetidosBD.add(herramienta.toString());
				}
			}

		}

		//veo si hay nombres que se repiten entre los nuevos materiales con nombres
		//primero los ordeno por nombre
		herramientasAValidar.sort((p1, p2) -> {
			return p1.getNombre().compareTo(p2.getNombre());
		});

		//luego busco coincidencias entre los nombres de los materiales ingresados
		if(!herramientasAValidar.isEmpty()){
			Iterator<Herramienta> it1 = herramientasAValidar.iterator();
			Herramienta anterior = it1.next();
			Herramienta actual;
			while(it1.hasNext()){
				actual = it1.next();
				if(actual.getNombre().equals(anterior.getNombre())){
					erroresHerramientas.add(ErrorCrearHerramientas.NOMBRE_INGRESADO_REPETIDO);
				}
				anterior = actual;
			}
		}

		return new ResultadoCrearHerramientas(nombresHerramientasRepetidosBD, erroresHerramientas.toArray(new ErrorCrearHerramientas[0]));
	}

	public ResultadoEliminarHerramienta eliminarHerramienta(Herramienta herramienta) throws PersistenciaException {
		ResultadoEliminarHerramienta resultado = validarEliminarHerramienta(herramienta);

		if(!resultado.hayErrores()){
			//si la herramienta tiene tareas asociadas, se le da baja lógica
			ArrayList<Tarea> tareasDeLaHerramienta = persistidorProceso.obtenerTareas(new FiltroTarea.Builder().herramienta(herramienta).build());

			if(tareasDeLaHerramienta.isEmpty()){
				//si el material tiene tareas asociadas, se le da de baja lógica junto a sus procesos
				herramienta.darDeBaja();
				for(Proceso p: herramienta.getProcesos()){
					p.darDeBaja();
				}
				persistidorMaterial.actualizarHerramienta(herramienta);
			}
			else{
				//sino de baja física junto a sus procesos
				try{
					persistidorMaterial.bajaHerramienta(herramienta);
				} catch(ObjNotFoundException e){
					//Si no se encontró ya fue eliminado previamente.
				}
			}
		}

		return resultado;
	}

	private ResultadoEliminarHerramienta validarEliminarHerramienta(Herramienta herramienta) throws PersistenciaException {
		//No hay validaciones hasta el momento
		return new ResultadoEliminarHerramienta();
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

		//Creo una lista de los nombres de materiales que voy a buscar en la BD
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
			//si el material tiene piezas asociadas, se le da baja lógica
			ArrayList<Material> materialABajaLogica = persistidorMaterial.obtenerMateriales(new FiltroMaterial.Builder().material(material).conPiezas().build());

			if(!materialABajaLogica.isEmpty()){
				material.darDeBaja();
				persistidorMaterial.actualizarMaterial(material);
			}
			else{
				//sino, se le da baja fisica
				try{
					persistidorMaterial.bajaMaterial(material);
				} catch(ObjNotFoundException e){
					//Si no se encontró ya fue eliminado previamente.
				}
			}

		}

		return resultado;
	}

	private ResultadoEliminarMaterial validarEliminarMaterial(Material material) throws PersistenciaException {
		//Creo la lista de errores
		ArrayList<ErrorEliminarMaterial> erroresMateriales = new ArrayList<>();

		//Busco las piezas activas asociadas al material
		ArrayList<Pieza> piezasAsociadas = persistidorTaller.obtenerPiezas(new FiltroPieza.Builder().material(material).build());
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
