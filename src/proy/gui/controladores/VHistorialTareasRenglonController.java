/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.TitledPane;
import proy.datos.entidades.Tarea;
import proy.gui.ControladorJavaFX;

public class VHistorialTareasRenglonController extends ControladorJavaFX {

	public static final String URL_VISTA = "/proy/gui/vistas/VHistorialTareasRenglon.fxml";

	private TitledPane root;

	private Tarea tarea;

	public VHistorialTareasRenglonController(Tarea tarea) throws IOException {
		this.tarea = tarea;

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource(URL_VISTA));
		loader.setControllerFactory(claseControlador -> {
			if(claseControlador != null && !claseControlador.isInstance(this)){
				throw new IllegalArgumentException("¡Instancia del controlador inválida, esperada una instancia de la clase '" + claseControlador.getName() + "'!");
			}

			return this;
		});

		root = loader.load();
	}

	public TitledPane getRenglon() {
		return root;
	}

	@Override
	protected void inicializar() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void salir() {

	}
}
