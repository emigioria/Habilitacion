/**
 * Copyright (C) 2016 Fernando Berti - Daniel Campodonico - Emiliano Gioria - Lucas Moretti - Esteban Rebechi - Andres Leonel Rico
 * This file is part of Olimpo.
 *
 * Olimpo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Olimpo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Olimpo. If not, see <http://www.gnu.org/licenses/>.
 */
package proy.gui.componentes.ventanas;

import javafx.stage.Window;

/**
 * Representa una ventana que muestra un mensaje de error inesperado
 */
public class VentanaErrorExcepcionInesperada extends VentanaErrorExcepcion {

	/**
	 * Constructor. Genera la ventana
	 */
	protected VentanaErrorExcepcionInesperada() {
		this(null);
	}

	/**
	 * Constructor. Genera la ventana
	 *
	 * @param padre
	 *            ventana en la que se mostrar� este di�logo
	 */
	protected VentanaErrorExcepcionInesperada(Window padre) {
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
