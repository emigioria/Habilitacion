/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.componentes.ventanas;

import javafx.stage.Window;

/**
 * Representa una ventana que muestra un mensaje de error
 */
public class VentanaInformacion extends CustomAlert {

	/**
	 * Constructor. Genera la ventana
	 *
	 * @param mensaje
	 *            mensaje a mostrar en la ventana
	 */
	protected VentanaInformacion(String titulo, String mensaje) {
		this(titulo, mensaje, null);
	}

	/**
	 * Constructor. Genera la ventana
	 *
	 * @param mensaje
	 *            mensaje a mostrar en la ventana
	 * @param padre
	 *            ventana en la que se mostrar� este di�logo
	 */
	protected VentanaInformacion(String titulo, String mensaje, Window padre) {
		super(AlertType.INFORMATION, padre);
		this.setContentText(mensaje);
		this.setHeaderText(null);
		this.setTitle(titulo);
		this.showAndWait();
	}
}
