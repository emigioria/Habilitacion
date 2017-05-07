/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import proy.datos.entidades.Pausa;
import proy.gui.ControladorDialogo;

public class NMPausaController extends ControladorDialogo {

	public static final String URL_VISTA = "vistas/NMPausa.fxml";

	@FXML
	private TextArea taCausaStr;

	@FXML
	private Button botonGuardar;

	private Pausa pausa;

	@FXML
	private void guardar() {
		pausa.setCausa(taCausaStr.getText().trim());
		salir();
	}

	@Override
	protected void inicializar() {
		stage.setTitle("Nueva pausa");

		if(pausa == null){
			pausa = new Pausa();
			pausa.setFechaHoraInicio(new Date());
		}

		taCausaStr.textProperty().addListener((obs, oldO, newO) -> {
			cambiarGuardar();
		});
		cambiarGuardar();
	}

	private void cambiarGuardar() {
		botonGuardar.setDisable(taCausaStr.getText().trim().isEmpty());
	}

	public void formatearModificarPausa(Pausa pausa) {
		this.pausa = pausa;
		taCausaStr.setText(pausa.getCausa());
	}

	public Pausa getResultado() {
		return pausa;
	}

	@FXML
	private void cancelar() {
		pausa = null;
		salir();
	}
}
