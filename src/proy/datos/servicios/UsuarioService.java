package proy.datos.servicios;

import java.util.ArrayList;

import proy.datos.entidades.Administrador;
import proy.datos.entidades.Comentario;
import proy.datos.entidades.Operario;
import proy.excepciones.PersistenciaException;

public interface UsuarioService {

	public ArrayList<Administrador> obtenerAdministradores(Filtro filtro) throws PersistenciaException;

	public void guardarComentario(Comentario comentario) throws PersistenciaException;

	public ArrayList<Comentario> obtenerComentarios(Filtro filtro) throws PersistenciaException;

	public ArrayList<Operario> obtenerOperarios(Filtro filtro) throws PersistenciaException;

	public void guardarOperario(Operario operario) throws PersistenciaException;

	public void bajaOperario(Operario operario) throws PersistenciaException;

}
