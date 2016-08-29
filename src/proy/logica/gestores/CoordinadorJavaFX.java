package proy.logica.gestores;

import java.util.ArrayList;

import javax.annotation.Resource;

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
import proy.logica.gestores.filtros.FiltroAdministrador;
import proy.logica.gestores.filtros.FiltroComentario;
import proy.logica.gestores.filtros.FiltroHerramienta;
import proy.logica.gestores.filtros.FiltroMaquina;
import proy.logica.gestores.filtros.FiltroMaterial;
import proy.logica.gestores.filtros.FiltroOperario;
import proy.logica.gestores.filtros.FiltroParte;
import proy.logica.gestores.filtros.FiltroPieza;
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

	@Resource
	private UsuarioGestor gestorUsuario;

	@Resource
	private TallerGestor gestorTaller;

	@Resource
	private ProcesoGestor gestorProceso;

	public ResultadoAutenticacion autenticarAdministrador(FiltroAdministrador filtro) throws PersistenciaException {
		return gestorUsuario.autenticarAdministrador(filtro);
	}

	public ResultadoCrearComentario crearComentario(Comentario comentario) throws PersistenciaException {
		return gestorUsuario.crearComentario(comentario);
	}

	public ArrayList<Comentario> listarComentarios(FiltroComentario filtro) throws PersistenciaException {
		return gestorUsuario.listarComentarios(filtro);
	}

	public ArrayList<Operario> listarOperarios(FiltroOperario filtro) throws PersistenciaException {
		return gestorUsuario.listarOperarios(filtro);
	}

	public ResultadoCrearOperario crearOperario(Operario operario) throws PersistenciaException {
		return gestorUsuario.crearOperario(operario);
	}

	public ResultadoEliminarOperario eliminarOperario(Operario operario) throws PersistenciaException {
		return gestorUsuario.eliminarOperario(operario);
	}

	public ArrayList<Maquina> listarMaquinas(FiltroMaquina filtro) throws PersistenciaException {
		return gestorTaller.listarMaquinas(filtro);
	}

	public ResultadoCrearMaquina crearMaquina(Maquina maquina) throws PersistenciaException {
		return gestorTaller.crearMaquina(maquina);
	}

	public ResultadoModificarMaquina modificarMaquina(Maquina maquinaNueva, Maquina maquinaVieja) throws PersistenciaException {
		return gestorTaller.modificarMaquina(maquinaNueva, maquinaVieja);
	}

	public ResultadoEliminarMaquina eliminarMaquina(Maquina maquina) throws PersistenciaException {
		return gestorTaller.eliminarMaquina(maquina);
	}

	public ArrayList<Parte> listarPartes(FiltroParte filtro) throws PersistenciaException {
		return gestorTaller.listarPartes(filtro);
	}

	public ResultadoCrearParte crearParte(Parte parte) throws PersistenciaException {
		return gestorTaller.crearParte(parte);
	}

	public ResultadoModificarParte modificarParte(Parte parteNueva, Parte parteVieja) throws PersistenciaException {
		return gestorTaller.modificarParte(parteNueva, parteVieja);
	}

	public ResultadoEliminarParte eliminarParte(Parte parte) throws PersistenciaException {
		return gestorTaller.eliminarParte(parte);
	}

	public ArrayList<Pieza> listarPiezas(FiltroPieza filtro) throws PersistenciaException {
		return gestorTaller.listarPiezas(filtro);
	}

	public ResultadoCrearPieza crearPieza(Pieza pieza) throws PersistenciaException {
		return gestorTaller.crearPieza(pieza);
	}

	public ResultadoEliminarPieza eliminarPieza(Pieza pieza) throws PersistenciaException {
		return gestorTaller.eliminarPieza(pieza);
	}

	public ArrayList<Herramienta> listarHerramientas(FiltroHerramienta filtro) throws PersistenciaException {
		return gestorTaller.listarHerramientas(filtro);
	}

	public ResultadoCrearHerramienta crearHerramienta(Herramienta herramienta) throws PersistenciaException {
		return gestorTaller.crearHerramienta(herramienta);
	}

	public ResultadoEliminarHerramienta eliminarHerramienta(Herramienta herramienta) throws PersistenciaException {
		return gestorTaller.eliminarHerramienta(herramienta);
	}

	public ArrayList<Material> listarMateriales(FiltroMaterial filtro) throws PersistenciaException {
		return gestorTaller.listarMateriales(filtro);
	}

	public ResultadoCrearMaterial crearMaterial(Material material) throws PersistenciaException {
		return gestorTaller.crearMaterial(material);
	}

	public ResultadoEliminarMaterial eliminarMaterial(Material material) throws PersistenciaException {
		return gestorTaller.eliminarMaterial(material);
	}

	public ArrayList<Proceso> listarProcesos(FiltroProceso filtro) throws PersistenciaException {
		return gestorProceso.listarProcesos(filtro);
	}

	public ResultadoCrearProceso crearProceso(Proceso proceso) throws PersistenciaException {
		return gestorProceso.crearProceso(proceso);
	}

	public ResultadoModificarProceso modificarProceso(Proceso procesoNuevo, Proceso procesoViejo) throws PersistenciaException {
		return gestorProceso.modificarProceso(procesoNuevo, procesoViejo);
	}

	public ResultadoEliminarProceso eliminarProceso(Proceso proceso) throws PersistenciaException {
		return gestorProceso.eliminarProceso(proceso);
	}

	public ArrayList<Tarea> listarTareas(FiltroTarea filtro) throws PersistenciaException {
		return gestorProceso.listarTareas(filtro);
	}

	public ResultadoCrearTarea crearTarea(Tarea tarea) throws PersistenciaException {
		return gestorProceso.crearTarea(tarea);
	}

	public ResultadoModificarTarea modificarTarea(Tarea tareaNueva, Tarea tareaVieja) throws PersistenciaException {
		return gestorProceso.modificarTarea(tareaNueva, tareaVieja);
	}

	public ResultadoEliminarTarea eliminarTarea(Tarea tarea) throws PersistenciaException {
		return gestorProceso.eliminarTarea(tarea);
	}

	public ResultadoModificarTarea ComenzarTarea(Tarea tarea) throws PersistenciaException {
		return gestorProceso.ComenzarTarea(tarea);
	}

	public ResultadoModificarTarea PausarTarea(Tarea tarea) throws PersistenciaException {
		return gestorProceso.PausarTarea(tarea);
	}

	public ResultadoModificarTarea ReanudarTarea(Tarea tarea) throws PersistenciaException {
		return gestorProceso.ReanudarTarea(tarea);
	}

	public ResultadoModificarTarea TerminarTarea(Tarea tarea) throws PersistenciaException {
		return gestorProceso.TerminarTarea(tarea);
	}

	public ResultadoModificarTarea CancelarTarea(Tarea tarea) throws PersistenciaException {
		return gestorProceso.CancelarTarea(tarea);
	}
}
