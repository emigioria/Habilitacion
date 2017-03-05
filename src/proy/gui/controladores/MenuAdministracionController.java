/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import javafx.fxml.FXML;

public class MenuAdministracionController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/MenuAdministracion.fxml";

	@FXML
	public void administrarOperarios() {
		this.nuevaScene(AOperariosController.URL_VISTA);
	}

	@FXML
	public void administrarMaquinas() {
		this.nuevaScene(AMaquinasController.URL_VISTA);
	}

	@FXML
	public void administrarProcesos() {
		this.nuevaScene(AProcesosController.URL_VISTA);
	}

	@FXML
	public void administrarMateriales() {
		this.nuevaScene(AMaterialesController.URL_VISTA);
	}

	@FXML
	public void administrarHerramientas() {
		this.nuevaScene(AHerramientasController.URL_VISTA);
	}

	@FXML
	public void administrarTareas() {
		this.nuevaScene(ATareasController.URL_VISTA);
	}

	@Override
	public void actualizar() {

	}

	@Override
	protected void inicializar() {

	}
}
