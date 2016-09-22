/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.componentes;

import javafx.scene.control.Dialog;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class VentanaEsperaBaseDeDatos extends Dialog<Void> {

	public VentanaEsperaBaseDeDatos() {
		this(null);
	}

	public VentanaEsperaBaseDeDatos(Window padre) {
		super();
		this.initStyle(StageStyle.UNDECORATED);
		if(padre != null){
			this.initOwner(padre);
		}
		this.setContentText("Esperando a la base de datos...");
		this.setHeaderText(null);
		//		this.setTitle("Esperando");
	}
}
