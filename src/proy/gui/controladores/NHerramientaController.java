/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import proy.datos.entidades.Herramienta;
import proy.gui.ControladorDialogo;

public class NHerramientaController extends ControladorDialogo {

	public static final String URL_VISTA = "/proy/gui/vistas/NHerramienta.fxml";

	private Herramienta herramienta;

	@FXML
	private TextField tfNombre;

	@FXML
	private void guardar() {
		herramienta = new Herramienta();
		herramienta.setNombre(tfNombre.getText().trim());
		salir();
	}

	@FXML
	private void salir() {
		stage.hide();
	}

	@Override
	protected void inicializar() {
		stage.setTitle("Nueva herramienta");
	}

}
