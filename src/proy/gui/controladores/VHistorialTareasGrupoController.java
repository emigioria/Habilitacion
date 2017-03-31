/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import proy.datos.entidades.Tarea;
import proy.gui.ControladorJavaFX;

public class VHistorialTareasGrupoController extends ControladorJavaFX {

	public static final String URL_VISTA = "/proy/gui/vistas/VHistorialTareasGrupo.fxml";

	private Parent root;

	@FXML
	private VBox renglonBox;

	@FXML
	private Label lbFecha;

	private Date fecha;

	public VHistorialTareasGrupoController(Date fecha, List<Tarea> tareas) throws IOException {
		this.fecha = fecha;

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource(URL_VISTA));
		loader.setControllerFactory(claseControlador -> {
			if(claseControlador != null && !claseControlador.isInstance(this)){
				throw new IllegalArgumentException("¡Instancia del controlador inválida, esperada una instancia de la clase '" + claseControlador.getName() + "'!");
			}

			return this;
		});

		root = loader.load();

		//Cargar renglones de tareas
		for(Tarea t: tareas){
			renglonBox.getChildren().add(new VHistorialTareasRenglonController(t).getRenglon());
		}
	}

	public Node getNode() {
		return root;
	}

	@Override
	protected void inicializar() {
		lbFecha.setText(conversorTiempos.diaMesYAnioToString(fecha));
	}

	@Override
	protected void salir() {

	}
}
