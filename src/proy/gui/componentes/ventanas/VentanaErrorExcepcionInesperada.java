/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.componentes.ventanas;

import javafx.stage.Window;

/**
 * Representa una ventana que muestra un mensaje de error inesperado
 */
public class VentanaErrorExcepcionInesperada extends VentanaErrorExcepcion {

	/**
	 * Constructor. Genera la ventana
	 *
	 * @param padre
	 *            ventana en la que se mostrará este diálogo
	 */
	protected VentanaErrorExcepcionInesperada(Window padre) {
		super(padre);
		this.setContentText("Ha surgido un error inesperado.");
		this.setHeaderText(null);
		this.setTitle("Error inesperado");
		this.showAndWait();
	}
}
