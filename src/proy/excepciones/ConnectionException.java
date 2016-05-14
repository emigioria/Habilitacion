package proy.excepciones;

/**
 * Representa un error de conexión con la base de datos
 *
 * @author Acosta - Gioria - Moretti - Rebechi
 *
 */
public class ConnectionException extends PersistenciaException {

	private static final long serialVersionUID = 1L;

	public ConnectionException() {
		super("No se pudo conectar con la base de datos.");
	}
}
