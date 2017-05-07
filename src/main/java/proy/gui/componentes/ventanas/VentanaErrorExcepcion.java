/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.componentes.ventanas;

import javafx.stage.Window;

/**
 * Representa una ventana que muestra un mensaje de error
 */
public class VentanaErrorExcepcion extends VentanaError {

	/**
	 * Constructor. Genera parte de la ventana
	 */
	protected VentanaErrorExcepcion(Window padre) {
		super(padre);
	}

	/**
	 * Constructor. Genera la ventana
	 *
	 * @param mensaje
	 *            mensaje a mostrar en la ventana
	 * @param padre
	 *            ventana en la que se mostrará este diálogo
	 */
	protected VentanaErrorExcepcion(String mensaje, Window padre) {
		this(padre);
		this.setContentText(mensaje);
		this.setHeaderText(null);
		this.setTitle("Error");
		this.showAndWait();
	}
}
