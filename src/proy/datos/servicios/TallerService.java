package proy.datos.servicios;

import java.util.ArrayList;

import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Maquina;
import proy.datos.entidades.Material;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.excepciones.PersistenciaException;

public interface TallerService {

	public ArrayList<Maquina> obtenerMaquinas(Filtro filtro) throws PersistenciaException;

	public void guardarMaquina(Maquina maquina) throws PersistenciaException;

	public void actualizarMaquina(Maquina maquina) throws PersistenciaException;

	public void bajaMaquina(Maquina maquina) throws PersistenciaException;

	public ArrayList<Parte> obtenerPartes(Filtro filtro) throws PersistenciaException;

	public void guardarParte(Parte parte) throws PersistenciaException;

	public void actualizarParte(Parte parte) throws PersistenciaException;

	public void bajaParte(Parte parte) throws PersistenciaException;

	public ArrayList<Pieza> obtenerPiezas(Filtro filtro) throws PersistenciaException;

	public void guardarPieza(Pieza pieza) throws PersistenciaException;

	public void bajaPieza(Pieza pieza) throws PersistenciaException;

	public ArrayList<Herramienta> obtenerHerramientas(Filtro filtro) throws PersistenciaException;

	public void guardarHerramienta(Herramienta herramienta) throws PersistenciaException;

	public void bajaHerramienta(Herramienta herramienta) throws PersistenciaException;

	public ArrayList<Material> obtenerMateriales(Filtro filtro) throws PersistenciaException;

	public void guardarMaterial(Material material) throws PersistenciaException;

	public void bajaMaterial(Material material) throws PersistenciaException;

}
