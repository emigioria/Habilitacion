/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import proy.comun.ConversorFechas;
import proy.comun.FormateadorString;
import proy.gui.componentes.ventanas.PresentadorVentanas;
import proy.logica.CoordinadorJavaFX;

public abstract class ControladorJavaFX {

	protected Stage stage;

	protected CoordinadorJavaFX coordinador;

	protected ConversorFechas conversorFechas = new ConversorFechas();

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

}
