/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
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
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import proy.comun.ConversorFechas;
import proy.datos.entidades.Tarea;

public class VHistorialTareasGrupoController {

	public static final String URL_VISTA = "/proy/gui/vistas/VHistorialTareasGrupo.fxml";

	private ConversorFechas conversorFechas = new ConversorFechas();

	private Parent root;

	@FXML
	private Accordion renglonBox;

	@FXML
	private Label lbFecha;

	private Date fecha;

	private List<Tarea> tareas;

	public VHistorialTareasGrupoController(Date fecha, List<Tarea> tareas) throws IOException {
		this.fecha = fecha;
		this.tareas = tareas;

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

	public Node getNode() {
		return root;
	}

	@FXML
	private void initialize() throws IOException {
		for(Tarea t: tareas){
			renglonBox.getPanes().add(new VHistorialTareasRenglonController(t).getRenglon());
		}

		lbFecha.setText(conversorFechas.diaMesYAnioToString(fecha));
	}
}
