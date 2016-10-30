/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import proy.datos.entidades.Proceso;

public class AProcesosController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/AProcesos.fxml";

	@FXML
	private TextField nombreProceso;

	@FXML
	private TableView<Proceso> tablaProcesos;

	@FXML
	private TableColumn<Proceso, String> columnaMaquina;

	@FXML
	private TableColumn<Proceso, String> columnaParte;

	@FXML
	private TableColumn<Proceso, String> columnaDescripcion;

	@FXML
	private TableColumn<Proceso, String> columnaTipo;

	@FXML
	private TableColumn<Proceso, String> columnaTiempoPreparacion;

	@FXML
	private TableColumn<Proceso, String> columnaTiempoProceso;

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			columnaMaquina.setCellValueFactory((CellDataFeatures<Proceso, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getParte().getMaquina().getNombre());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
			columnaParte.setCellValueFactory((CellDataFeatures<Proceso, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getParte().getNombre());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
			columnaDescripcion.setCellValueFactory((CellDataFeatures<Proceso, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getDescripcion());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
			columnaTipo.setCellValueFactory((CellDataFeatures<Proceso, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getTipo());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
			columnaTiempoPreparacion.setCellValueFactory((CellDataFeatures<Proceso, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getTiempoTeoricoPreparacion());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
			columnaTiempoProceso.setCellValueFactory((CellDataFeatures<Proceso, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getTiempoTeoricoProceso());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
		});
	}

	@FXML
	public void nuevoProceso() {
		NMProcesoController nuevaPantalla = (NMProcesoController) ControladorRomano.nuevaScene(NMProcesoController.URL_VISTA, apilador, coordinador);
		nuevaPantalla.formatearNuevoProceso();
	}

	@FXML
	public void modificarProceso() {
		Proceso proceso = tablaProcesos.getSelectionModel().getSelectedItem();
		if(proceso != null){
			NMProcesoController nuevaPantalla = (NMProcesoController) ControladorRomano.nuevaScene(NMProcesoController.URL_VISTA, apilador, coordinador);
			nuevaPantalla.formatearModificarProceso(proceso);
		}
	}

	@FXML
	public void eliminarProceso() {

	}

	@Override
	public void actualizar() {

	}
}
