/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.componentes.ventanas;

import javafx.scene.control.Alert;
import javafx.stage.Window;
import proy.gui.componentes.StyleCSS;

public abstract class CustomAlert extends Alert {

	public CustomAlert(AlertType alertType, Window padre) {
		super(alertType);
		try{
			this.initOwner(padre);
		} catch(NullPointerException e){
			//Si no tiene scene tira esta excepcion
		}
		this.getDialogPane().getStylesheets().add(new StyleCSS().getDefaultStyle());
	}

}
