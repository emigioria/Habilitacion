/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import proy.datos.entidades.Material;
import proy.gui.ControladorDialogo;

public class NMaterialController extends ControladorDialogo {

	public static final String URL_VISTA = "/proy/gui/vistas/NMaterial.fxml";

	private Material material;

	@FXML
	private TextField tfNombre;

	@FXML
	private TextField tfMedidas;

	@FXML
	private void guardar() {
		material = new Material();
		material.setNombre(tfNombre.getText().trim());
		material.setMedidas(tfMedidas.getText().trim());
		salir();
	}

	@FXML
	private void salir() {
		stage.hide();
	}

	@Override
	protected void inicializar() {
		stage.setTitle("Nuevo material");
	}

}
