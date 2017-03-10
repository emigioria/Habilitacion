/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.componentes.ventanas;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import proy.gui.componentes.StyleCSS;

public abstract class CustomAlert extends Alert {

	public CustomAlert(AlertType alertType, String contentText, ButtonType[] buttons) {
		super(alertType, contentText, buttons);
		this.getDialogPane().getStylesheets().add(new StyleCSS().getDefaultStyle());
	}

	public CustomAlert(AlertType alertType) {
		super(alertType);
		this.getDialogPane().getStylesheets().add(new StyleCSS().getDefaultStyle());
	}

}
