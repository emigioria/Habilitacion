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
	private ProcesoService persistidorDocumento;

	public ArrayList<Proceso> listarProcesos(FiltroProceso filtro) throws PersistenciaException {
		throw new NotYetImplementedException();
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
		throw new NotYetImplementedException();
	}

	public ResultadoCrearTarea crearTarea(Tarea tarea) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoModificarTarea modificarTarea(Tarea tarea) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoEliminarTarea eliminarTarea(Tarea tarea) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoModificarTarea ComenzarTarea(Tarea tarea) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoModificarTarea PausarTarea(Tarea tarea) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoModificarTarea ReanudarTarea(Tarea tarea) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoModificarTarea TerminarTarea(Tarea tarea) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoModificarTarea CancelarTarea(Tarea tarea) throws PersistenciaException {
		throw new NotYetImplementedException();
	}
}
