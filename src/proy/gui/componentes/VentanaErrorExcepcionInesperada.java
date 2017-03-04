/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.componentes;

import javafx.stage.Window;

/**
 * Representa una ventana que muestra un mensaje de error inesperado
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
