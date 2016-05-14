package proy.gui.componentes;

import javafx.stage.Window;

/**
 * Representa una ventana que muestra un mensaje de error inesperado
 *
 * @author Acosta - Gioria - Moretti - Rebechi
 *
 */
public class VentanaErrorExcepcionInesperada extends VentanaErrorExcepcion {

	/**
	 * Constructor. Genera la ventana
	 */
	public VentanaErrorExcepcionInesperada() {
		this(null);
	}

	/**
	 * Constructor. Genera la ventana
	 *
	 * @param padre
	 *            ventana en la que se mostrará este diálogo
	 */
	public VentanaErrorExcepcionInesperada(Window padre) {
		super(AlertType.ERROR);
		if(padre != null){
			this.initOwner(padre);
		}
		this.setContentText("Ha surgido un error inesperado.");
		this.setHeaderText(null);
		this.setTitle("Error inesperado");
		this.showAndWait();
	}
}
