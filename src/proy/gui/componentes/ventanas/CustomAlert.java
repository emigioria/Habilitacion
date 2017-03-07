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
