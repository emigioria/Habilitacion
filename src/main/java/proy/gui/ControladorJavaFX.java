/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import proy.comun.ConversorTiempos;
import proy.comun.FormateadorString;
import proy.gui.componentes.ventanas.PresentadorVentanas;
import proy.logica.CoordinadorJavaFX;

public abstract class ControladorJavaFX {

	protected Stage stage;

	protected CoordinadorJavaFX coordinador;

	protected ConversorTiempos conversorTiempos = new ConversorTiempos();

	protected FormateadorString formateadorString = new FormateadorString();

	protected PresentadorVentanas presentadorVentanas = new PresentadorVentanas();

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setCoordinador(CoordinadorJavaFX coordinador) {
		this.coordinador = coordinador;
	}

	@FXML
	private final void initialize() {
		Platform.runLater(() -> {
			inicializar();
		});
	}

	protected abstract void inicializar();

	@FXML
	protected abstract void salir();

}
