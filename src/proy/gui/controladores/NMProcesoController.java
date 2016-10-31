/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Maquina;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.datos.entidades.Proceso;

public class NMProcesoController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/NMProceso.fxml";

	@FXML
	private TextField tiempoTeoricoPreparacion;

	@FXML
	private TextField tiempoTeoricoProceso;

	@FXML
	private TextArea observacionesProceso;

	@FXML
	private ComboBox<Maquina> cbMaquina;

	@FXML
	private ComboBox<Parte> cbParte;

	@FXML
	private ComboBox<String> cbDescripcion;

	@FXML
	private ComboBox<String> cbTipo;

	@FXML
	private ComboBox<Pieza> cbPieza;

	@FXML
	private ComboBox<Herramienta> cbHerramienta;

	@FXML
	private ListView<Pieza> listaPiezas;

	@FXML
	private ListView<Herramienta> listaHerramientas;

	@FXML
	public void agregarPieza() {

	}

	@FXML
	public void quitarPieza() {

	}

	@FXML
	public void agregarHerramienta() {

	}

	@FXML
	public void quitarHerramienta() {

	}

	@FXML
	public void guardar() {

	}

	public void formatearNuevoProceso() {

	}

	public void formatearModificarProceso(Proceso proceso) {

	}

	@Override
	public void actualizar() {

	}
}
