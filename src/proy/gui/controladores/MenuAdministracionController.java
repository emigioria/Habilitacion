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
		ControladorRomano.nuevaScene(AOperariosController.URL_VISTA, apilador, coordinador);
	}

	@FXML
	public void administrarMaquinas() {
		ControladorRomano.nuevaScene(AMaquinasController.URL_VISTA, apilador, coordinador);
	}

	@FXML
	public void administrarProcesos() {
		ControladorRomano.nuevaScene(AProcesosController.URL_VISTA, apilador, coordinador);
	}

	@FXML
	public void administrarMateriales() {
		ControladorRomano.nuevaScene(AMaterialesController.URL_VISTA, apilador, coordinador);
	}

	@FXML
	public void administrarHerramientas() {
		ControladorRomano.nuevaScene(AHerramientasController.URL_VISTA, apilador, coordinador);
	}

	@FXML
	public void administrarTareas() {
		ControladorRomano.nuevaScene(ATareasController.URL_VISTA, apilador, coordinador);
	}

	@Override
	public void actualizar() {

	}
}
