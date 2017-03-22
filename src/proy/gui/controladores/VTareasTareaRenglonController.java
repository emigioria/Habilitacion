/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import proy.datos.clases.EstadoStr;
import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Pieza;
import proy.datos.entidades.Tarea;
import proy.gui.ControladorJavaFX;

public class VTareasTareaRenglonController extends ControladorJavaFX {

	public static final String URL_VISTA = "/proy/gui/vistas/VTareasTareaRenglon.fxml";

	private Tarea tarea;

	private TitledPane root;

	@FXML
	private Label lbMaquina;

	@FXML
	private Label lbParte;

	@FXML
	private Label lbTipoProceso;

	@FXML
	private Label lbDescripciónProceso;

	@FXML
	private Label lbCantidad;

	@FXML
	private Label lbTTPreparacion;

	@FXML
	private Label lbTTProceso;

	@FXML
	private Label lbTTTarea;

	@FXML
	private TextArea taObservacionesProceso;

	@FXML
	private TextArea taObservacionesTarea;

	@FXML
	private TableView<Pieza> tablaPiezas;

	@FXML
	private TableColumn<Pieza, String> columnaNombrePieza;

	@FXML
	private TableColumn<Pieza, String> columnaCodigoPlano;

	@FXML
	private TableColumn<Pieza, String> columnaNombreMaterial;

	@FXML
	private TableColumn<Pieza, String> columnaMedidasMaterial;

	@FXML
	private TableView<Herramienta> tablaHerramientas;

	@FXML
	private TableColumn<Herramienta, String> columnaNombreHerramienta;

	public VTareasTareaRenglonController(Tarea tarea) throws IOException {
		this.tarea = tarea;

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

	public TitledPane getRenglon() {
		return root;
	}

	@Override
	protected void inicializar() {
		columnaNombrePieza.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaCodigoPlano.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().getCodigoPlano().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaNombreMaterial.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().getMaterial().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaMedidasMaterial.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().getMaterial().getMedidas().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaNombreHerramienta.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		lbMaquina.setText("Maquina: " + tarea.getProceso().getParte().getMaquina().toString());
		lbParte.setText("Parte: " + tarea.getProceso().getParte().toString());
		lbTipoProceso.setText("Tipo de proceso: " + formateadorString.primeraMayuscula(tarea.getProceso().getTipo().toString()));
		lbDescripciónProceso.setText("Descipción de proceso: " + formateadorString.primeraMayuscula(tarea.getProceso().getDescripcion().toString()));
		lbCantidad.setText("Cantidad: " + tarea.getCantidadTeorica().toString());
		lbTTPreparacion.setText("Tiempo teórico de preparación: " + conversorTiempos.milisAHsMsSsConTexto(tarea.getProceso().getTiempoTeoricoPreparacion()));
		lbTTProceso.setText("Tiempo teórico de proceso: " + conversorTiempos.milisAHsMsSsConTexto(tarea.getProceso().getTiempoTeoricoProceso()));
		lbTTTarea.setText("Tiempo total de la tarea: " + conversorTiempos.milisAHsMsSsConTexto(tarea.getTiempoTotalTarea()));
		taObservacionesProceso.setText("");
		if(tarea.getProceso().getObservaciones() != null){
			taObservacionesProceso.setText(tarea.getProceso().getObservaciones());
		}
		taObservacionesTarea.setText("");
		if(tarea.getObservacionesTarea() != null){
			taObservacionesTarea.setText(tarea.getObservacionesTarea());
		}

		for(Pieza pieza: tarea.getProceso().getPiezas()){
			if(EstadoStr.ALTA.equals(pieza.getEstado().getNombre())){
				tablaPiezas.getItems().add(pieza);
			}
		}
		for(Herramienta herramienta: tarea.getProceso().getHerramientas()){
			if(EstadoStr.ALTA.equals(herramienta.getEstado().getNombre())){
				tablaHerramientas.getItems().add(herramienta);
			}
		}
	}

	@Override
	protected void salir() {

	}
}
