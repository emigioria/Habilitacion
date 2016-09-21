package proy.logica.gestores;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Service;

import proy.datos.entidades.Proceso;
import proy.datos.entidades.Tarea;
import proy.datos.servicios.ProcesoService;
import proy.excepciones.PersistenciaException;
import proy.logica.gestores.filtros.FiltroProceso;
import proy.logica.gestores.filtros.FiltroTarea;
import proy.logica.gestores.resultados.ResultadoCrearProceso;
import proy.logica.gestores.resultados.ResultadoCrearTarea;
import proy.logica.gestores.resultados.ResultadoEliminarProceso;
import proy.logica.gestores.resultados.ResultadoEliminarTarea;
import proy.logica.gestores.resultados.ResultadoModificarProceso;
import proy.logica.gestores.resultados.ResultadoModificarTarea;

@Service
public class ProcesoGestor {

	@Resource
	private ProcesoService persistidorProceso;

	public ArrayList<Proceso> listarProcesos(FiltroProceso filtro) throws PersistenciaException {
		return persistidorProceso.obtenerProcesos(filtro);
	}

	public ResultadoCrearProceso crearProceso(Proceso proceso) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoModificarProceso modificarProceso(Proceso proceso) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoEliminarProceso eliminarProceso(Proceso proceso) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ArrayList<Tarea> listarTareas(FiltroTarea filtro) throws PersistenciaException {
		return persistidorProceso.obtenerTareas(filtro);
	}

	public ResultadoCrearTarea crearTarea(Tarea tarea) throws PersistenciaException {
		ResultadoCrearTarea resultado = validarCrearTarea(tarea);
		if(!resultado.hayErrores()){
			persistidorProceso.guardarTarea(tarea);
		}
		throw new NotYetImplementedException();
	}

	private ResultadoCrearTarea validarCrearTarea(Tarea tarea) {
		// TODO validar tarea para crearla
		return new ResultadoCrearTarea();
	}

	public ResultadoModificarTarea modificarTarea(Tarea tarea) throws PersistenciaException {
		ResultadoModificarTarea resultado = validarModificarTarea(tarea);
		if(!resultado.hayErrores()){
			persistidorProceso.actualizarTarea(tarea);
		}
		return resultado;
	}

	private ResultadoModificarTarea validarModificarTarea(Tarea tarea) {
		// TODO validar tarea para modificarla
		return new ResultadoModificarTarea();
	}

	public ResultadoEliminarTarea eliminarTarea(Tarea tarea) throws PersistenciaException {
		ResultadoEliminarTarea resultado = validarEliminarTarea(tarea);
		if(!resultado.hayErrores()){
			persistidorProceso.bajaTarea(tarea);
		}
		throw new NotYetImplementedException();
	}

	private ResultadoEliminarTarea validarEliminarTarea(Tarea tarea) {
		// TODO validar tarea para eliminarla
		return new ResultadoEliminarTarea();
	}

	public ResultadoModificarTarea comenzarTarea(Tarea tarea) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoModificarTarea pausarTarea(Tarea tarea) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoModificarTarea reanudarTarea(Tarea tarea) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoModificarTarea terminarTarea(Tarea tarea) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoModificarTarea cancelarTarea(Tarea tarea) throws PersistenciaException {
		throw new NotYetImplementedException();
	}
}
