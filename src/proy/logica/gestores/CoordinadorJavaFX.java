package proy.logica.gestores;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

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
import proy.gui.modelos.DatosLogin;
import proy.logica.gestores.filtros.FiltroComentario;
import proy.logica.gestores.filtros.FiltroHerramienta;
import proy.logica.gestores.filtros.FiltroMaquina;
import proy.logica.gestores.filtros.FiltroMaterial;
import proy.logica.gestores.filtros.FiltroOperario;
import proy.logica.gestores.filtros.FiltroProceso;
import proy.logica.gestores.filtros.FiltroTarea;
import proy.logica.gestores.resultados.ResultadoAutenticacion;
import proy.logica.gestores.resultados.ResultadoCrearComentario;
import proy.logica.gestores.resultados.ResultadoCrearHerramienta;
import proy.logica.gestores.resultados.ResultadoCrearMaquina;
import proy.logica.gestores.resultados.ResultadoCrearMaterial;
import proy.logica.gestores.resultados.ResultadoCrearOperario;
import proy.logica.gestores.resultados.ResultadoCrearParte;
import proy.logica.gestores.resultados.ResultadoCrearPieza;
import proy.logica.gestores.resultados.ResultadoCrearProceso;
import proy.logica.gestores.resultados.ResultadoCrearTarea;
import proy.logica.gestores.resultados.ResultadoEliminarHerramienta;
import proy.logica.gestores.resultados.ResultadoEliminarMaquina;
import proy.logica.gestores.resultados.ResultadoEliminarMaterial;
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

	public ResultadoAutenticacion autenticarAdministrador(DatosLogin login) throws PersistenciaException {
		return null;
	}

	public ResultadoCrearComentario crearComentario(Comentario comentario) throws PersistenciaException {
		return null;
	}

	public ArrayList<Comentario> listarComentarios(FiltroComentario filtro) throws PersistenciaException {
		return null;
	}

	public ArrayList<Maquina> listarMaquinas(FiltroMaquina filtro) throws PersistenciaException {
		return null;
	}

	public ResultadoCrearMaquina crearMaquina(Maquina maquina) throws PersistenciaException {
		return null;
	}

	public ResultadoModificarMaquina modificarMaquina(Maquina maquinaNueva, Maquina maquinaVieja) throws PersistenciaException {
		return null;
	}

	public ResultadoEliminarMaquina eliminarMaquina(Maquina maquina) throws PersistenciaException {
		return null;
	}

	public ResultadoCrearParte crearParte(Parte parte) throws PersistenciaException {
		return null;
	}

	public ResultadoModificarParte modificarParte(Parte parteNueva, Parte parteVieja) throws PersistenciaException {
		return null;
	}

	public ResultadoEliminarParte eliminarParte(Parte parte) throws PersistenciaException {
		return null;
	}

	public ResultadoCrearPieza crearPieza(Pieza pieza) throws PersistenciaException {
		return null;
	}

	public ResultadoEliminarPieza eliminarPieza(Pieza pieza) throws PersistenciaException {
		return null;
	}

	public ArrayList<Operario> listarOperarios(FiltroOperario filtro) throws PersistenciaException {
		return null;
	}

	public ResultadoCrearOperario crearOperario(Operario operario) throws PersistenciaException {
		return null;
	}

	public ResultadoEliminarOperario eliminarOperario(Operario operario) throws PersistenciaException {
		return null;
	}

	public ArrayList<Herramienta> listarHerramientas(FiltroHerramienta filtro) throws PersistenciaException {
		return null;
	}

	public ResultadoCrearHerramienta crearHerramienta(Herramienta herramienta) throws PersistenciaException {
		return null;
	}

	public ResultadoEliminarHerramienta eliminarHerramienta(Herramienta herramienta) throws PersistenciaException {
		return null;
	}

	public ArrayList<Material> listarMateriales(FiltroMaterial filtro) throws PersistenciaException {
		return null;
	}

	public ResultadoCrearMaterial crearMaterial(Material material) throws PersistenciaException {
		return null;
	}

	public ResultadoEliminarMaterial eliminarMaterial(Material material) throws PersistenciaException {
		return null;
	}

	public ArrayList<Proceso> listarProcesos(FiltroProceso filtro) throws PersistenciaException {
		return null;
	}

	public ResultadoCrearProceso crearProceso(Proceso proceso) throws PersistenciaException {
		return null;
	}

	public ResultadoModificarProceso modificarProceso(Proceso procesoNuevo, Proceso procesoViejo) throws PersistenciaException {
		return null;
	}

	public ResultadoEliminarProceso eliminarProceso(Proceso proceso) throws PersistenciaException {
		return null;
	}

	public ArrayList<Tarea> listarTareas(FiltroTarea filtro) throws PersistenciaException {
		return null;
	}

	public ResultadoCrearTarea crearTarea(Tarea tarea) throws PersistenciaException {
		return null;
	}

	public ResultadoModificarTarea modificarTarea(Tarea tareaNueva, Tarea tareaVieja) throws PersistenciaException {
		return null;
	}

	public ResultadoEliminarTarea eliminarTarea(Tarea tarea) throws PersistenciaException {
		return null;
	}

	public ResultadoModificarTarea ComenzarTarea(Tarea tarea) throws PersistenciaException {
		return null;
	}

	public ResultadoModificarTarea PausarTarea(Tarea tarea) throws PersistenciaException {
		return null;
	}

	public ResultadoModificarTarea ReanudarTarea(Tarea tarea) throws PersistenciaException {
		return null;
	}

	public ResultadoModificarTarea TerminarTarea(Tarea tarea) throws PersistenciaException {
		return null;
	}

	public ResultadoModificarTarea CancelarTarea(Tarea tarea) throws PersistenciaException {
		return null;
	}
}
