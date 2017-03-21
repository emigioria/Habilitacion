/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;
import proy.datos.entidades.Tarea;
import proy.gui.ControladorDialogo;

public class NFinTareaController extends ControladorDialogo {

	public static final String URL_VISTA = "/proy/gui/vistas/NFinTarea.fxml";

	@FXML
	private Spinner<Integer> spCantidad;

	@FXML
	private TextArea taObservacionesStr;

	@FXML
	private Button botonGuardar;

	private Tarea tarea;

	@FXML
	private void guardar() {
		tarea.setObservacionesOperario(taObservacionesStr.getText().trim());
		tarea.setCantidadReal(spCantidad.getValue());
		salir();
	}

	@Override
	protected void inicializar() {
		stage.setTitle("Finalizar tarea");

		spCantidad.getEditor().setTextFormatter(new TextFormatter<>(
				new IntegerStringConverter(), 0,
				c -> {
					if(c.isContentChange()){
						Integer numeroIngresado = null;
						try{
							numeroIngresado = new Integer(c.getControlNewText());
						} catch(Exception e){
							//No ingreso un entero;
						}
						if(numeroIngresado == null){
							return null;
						}
					}
					return c;
				}));
		spCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 0));
		spCantidad.focusedProperty().addListener((obs, oldV, newV) -> {
			spCantidad.increment(0);
		});

		spCantidad.valueProperty().addListener((obs, oldO, newO) -> {
			cambiarGuardar();
		});
		cambiarGuardar();
	}

	private void cambiarGuardar() {
		botonGuardar.setDisable(spCantidad.getValue() < 1);
	}

	public void formatearConTarea(Tarea tarea) {
		this.tarea = tarea;
	}

	public Tarea getResultado() {
		return tarea;
	}

	@FXML
	private void cancelar() {
		tarea = null;
		salir();
	}
}
