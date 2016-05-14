package proy.excepciones;

/**
 * Representa un error al borrar datos de la base de datos
 *
 * @author Acosta - Gioria - Moretti - Rebechi
 *
 */
public class DeleteException extends PersistenciaException {

	private static final long serialVersionUID = 1L;

	public DeleteException() {
		super("Error inesperado interactuando con la base de datos.\nNo se pudo eliminar los datos deseados.");
	}
}
