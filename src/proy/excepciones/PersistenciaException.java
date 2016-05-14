package proy.excepciones;

/**
 * Representa un error pertinente a la persistencia de datos
 *
 * @author Acosta - Gioria - Moretti - Rebechi
 *
 */
public class PersistenciaException extends Exception {

	private static final long serialVersionUID = 1L;

	public PersistenciaException(String msg) {
		super(msg);
	}
}
