package proy.datos.servicios;

import java.util.ArrayList;

import proy.datos.entidades.Proceso;
import proy.datos.entidades.Tarea;
import proy.excepciones.PersistenciaException;

public interface ProcesoService {

	public ArrayList<Proceso> obtenerProcesos(Filtro filtro) throws PersistenciaException;

	public void guardarProceso(Proceso proceso) throws PersistenciaException;

	public void actualizarProceso(Proceso proceso) throws PersistenciaException;

	public void bajaProceso(Proceso proceso) throws PersistenciaException;

	public ArrayList<Tarea> obtenerTareas(Filtro filtro) throws PersistenciaException;

	public void guardarTarea(Tarea tarea) throws PersistenciaException;

	public void actualizarTarea(Tarea tarea) throws PersistenciaException;

	public void bajaTarea(Tarea tarea) throws PersistenciaException;

}
