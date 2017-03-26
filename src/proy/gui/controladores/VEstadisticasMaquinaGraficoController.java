/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import proy.gui.ControladorDialogo;

public class VEstadisticasMaquinaGraficoController extends ControladorDialogo {

	public static final String URL_VISTA = "/proy/gui/vistas/VEstadisticasMaquinaGrafico.fxml";

	@FXML
	private PieChart graficoProcesos;

	private ObservableList<Data> datos;

	private String tituloGrafico;

	private String tituloVentana;

	public void formatearDatos(ObservableList<Data> datos) {
		this.datos = datos;
	}

	public void formatearTituloGrafico(String tituloGrafico) {
		this.tituloGrafico = tituloGrafico;
	}

	public void formatearTituloVentana(String tituloVentana) {
		this.tituloVentana = tituloVentana;
	}

	@Override
	protected void inicializar() {
		stage.setTitle(tituloVentana);
		graficoProcesos.setTitle(tituloGrafico);
		graficoProcesos.setData(datos);
	}
}
