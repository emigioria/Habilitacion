/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.componentes;

import javafx.scene.control.Alert;
import javafx.stage.Window;

/**
 * Representa una ventana que muestra un mensaje de error
 *
 * @author Acosta - Gioria - Moretti - Rebechi
 *
 */
public class VentanaErrorExcepcion extends Alert {

	/**
	 * Constructor. Genera parte de la ventana
	 */
	protected VentanaErrorExcepcion(AlertType alertType) {
		super(alertType);
	}

	/**
	 * Constructor. Genera la ventana
	 *
	 * @param mensaje
	 *            mensaje a mostrar en la ventana
	 */
	public VentanaErrorExcepcion(String mensaje) {
		this(mensaje, null);
	}

	/**
	 * Constructor. Genera la ventana
	 *
	 * @param mensaje
	 *            mensaje a mostrar en la ventana
	 * @param padre
	 *            ventana en la que se mostrará este diálogo
	 */
	public VentanaErrorExcepcion(String mensaje, Window padre) {
		super(AlertType.ERROR);
		if(padre != null){
			this.initOwner(padre);
		}
		this.setContentText(mensaje);
		this.setHeaderText(null);
		this.setTitle("Error");
		this.showAndWait();
	}
}
