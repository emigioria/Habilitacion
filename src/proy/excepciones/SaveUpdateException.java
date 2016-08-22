package proy.excepciones;

/**
 * Representa un error en el guardado o modificación de datos sobe la base de datos
 *
 * @author Acosta - Gioria - Moretti - Rebechi
 *
 */
public class SaveUpdateException extends PersistenciaException {

	private static final long serialVersionUID = 1L;

	public SaveUpdateException() {
		super("Error inesperado interactuando con la base de datos.\nNo se pudieron guardar los datos deseados.");
	}
}
