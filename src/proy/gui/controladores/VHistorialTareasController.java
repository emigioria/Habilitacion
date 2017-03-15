/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import proy.datos.entidades.Material;
import proy.datos.entidades.Pieza;
import proy.gui.ControladorRomano;

public class VHistorialTareasController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/VHistorialTareas.fxml";

	@FXML
	private Label nombreProceso;

	@FXML
	private Label nombreMaquina;

	@FXML
	private Label nombreParte;

	@FXML
	private Label nombreOperario;

	@FXML
	private TableView<Pieza> tablaPiezas;

	@FXML
	private TableColumn<Pieza, String> columnaPieza;

	@FXML
	private TableColumn<Pieza, String> columnaMaterial;

	@FXML
	private TitledPane panelTarea;

	@Override
	protected void inicializar() {
		columnaPieza.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().getNombre().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaMaterial.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().getMaterial().getNombre().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});

		panelTarea.setExpanded(true);
		Material material = new Material();
		material.setNombre("Chau");
		Pieza pieza = new Pieza();
		pieza.setMaterial(material);
		pieza.setNombre("Hola");
		tablaPiezas.getItems().add(pieza);

		(new Thread(() -> {
			try{
				Thread.sleep(100);
			} catch(Exception e){
			}
			Platform.runLater(() -> {
				TableRow<?> tableRow = (TableRow<?>) tablaPiezas.lookup("TableRow");
				tablaPiezas.minHeightProperty().bind(new SimpleIntegerProperty(1).multiply(tableRow.getHeight()).add(((Pane) tablaPiezas.lookup("TableHeaderRow")).getHeight() + 12));
				tablaPiezas.prefHeightProperty().bind(Bindings.size(tablaPiezas.getItems()).multiply(tableRow.getHeight()).add(((Pane) tablaPiezas.lookup("TableHeaderRow")).getHeight() + 12));
				tablaPiezas.maxHeightProperty().bind(Bindings.size(tablaPiezas.getItems()).multiply(tableRow.getHeight()).add(((Pane) tablaPiezas.lookup("TableHeaderRow")).getHeight() + 12));
				tablaPiezas.getItems().clear();
			});
		})).start();
	}

	@Override
	public void actualizar() {

	}

}
