/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import proy.datos.clases.EstadoStr;
import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Pausa;
import proy.datos.entidades.Pieza;
import proy.datos.entidades.Tarea;
import proy.gui.ControladorJavaFX;

public class VHistorialTareasRenglonController extends ControladorJavaFX {

	public static final String URL_VISTA = "/proy/gui/vistas/VHistorialTareasRenglon.fxml";

	private Tarea tarea;

	private TitledPane root;

	@FXML
	private Label lbFechaPlanificacion;

	@FXML
	private Label lbMaquina;

	@FXML
	private Label lbParte;

	@FXML
	private Label lbProceso;

	@FXML
	private Label lbOperario;

	@FXML
	private Label lbCantidadSolicitada;

	@FXML
	private Label lbCantidadRealizada;

	@FXML
	private Label lbTTPreparacion;

	@FXML
	private Label lbTTProceso;

	@FXML
	private Label lbTTTareaTeorico;

	@FXML
	private Label lbFechaHoraInicio;

	@FXML
	private Label lbFechaHoraFin;

	@FXML
	private Label lbTTTarea;

	@FXML
	private TextArea taObservacionesProceso;

	@FXML
	private TextArea taObservacionesTarea;

	@FXML
	private TextArea taObservacionesOperario;

	@FXML
	private Accordion pausasBox;

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

	@FXML
	private GridPane tablaTitulos;

	public VHistorialTareasRenglonController(Tarea tarea) throws IOException {
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
		//Hack para que se acomode el título del renglón al ancho del padre
		try{
			ChangeListener<Number> widthListener = (obs, oldV, newV) -> {
				tablaTitulos.setPrefWidth(newV.intValue() - 33);
				tablaTitulos.autosize();
			};
			((Region) root.getParent().getParent().getParent().getParent()).widthProperty().addListener(widthListener);
			root.parentProperty().addListener((obs, oldV, newV) -> {
				if(newV == null){
					try{
						((Region) oldV.getParent().getParent().getParent()).widthProperty().removeListener(widthListener);
					} catch(NullPointerException e){
						//Si los getParent fallan
					}
				}
			});
		} catch(NullPointerException e){
			//Si los getParent fallan
		}
		tablaTitulos.setPrefWidth(root.getWidth() - 33);
		tablaTitulos.autosize();

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
		lbFechaPlanificacion.setText(conversorTiempos.diaMesYAnioToString(tarea.getFechaPlanificada()));
		lbMaquina.setText(tarea.getProceso().getParte().getMaquina().toString());
		lbParte.setText(tarea.getProceso().getParte().toString());
		lbProceso.setText(tarea.getProceso().toString());
		lbOperario.setText(tarea.getOperario().toString());
		lbCantidadSolicitada.setText("Cantidad solicitada: " + tarea.getCantidadTeorica().toString());
		lbCantidadRealizada.setText("Cantidad realizada: " + tarea.getCantidadReal().toString());
		lbTTPreparacion.setText("Tiempo teórico de preparación: " + conversorTiempos.milisAHsMsSsConTexto(tarea.getProceso().getTiempoTeoricoPreparacion()));
		lbTTProceso.setText("Tiempo teórico de proceso: " + conversorTiempos.milisAHsMsSsConTexto(tarea.getProceso().getTiempoTeoricoProceso()));
		lbTTTareaTeorico.setText("Tiempo total de tarea teórico: " + conversorTiempos.milisAHsMsSsConTexto(tarea.getTiempoTotalTarea()));
		lbTTTarea.setText("Tiempo total de tarea: " + conversorTiempos.milisAHsMsSsConTexto(tarea.getTiempoEjecutando()));
		lbFechaHoraInicio.setText("Fecha y hora iniciada: " + conversorTiempos.diaMesAnioHoraYMinutosToString(tarea.getFechaHoraInicio()));
		lbFechaHoraFin.setText("Fecha y hora finalizada: " + conversorTiempos.diaMesAnioHoraYMinutosToString(tarea.getFechaHoraFin()));
		taObservacionesProceso.setText("");
		if(tarea.getProceso().getObservaciones() != null){
			taObservacionesProceso.setText(tarea.getProceso().getObservaciones());
		}
		taObservacionesTarea.setText("");
		if(tarea.getObservacionesTarea() != null){
			taObservacionesTarea.setText(tarea.getObservacionesTarea());
		}

		taObservacionesOperario.setText("");
		if(tarea.getObservacionesOperario() != null){
			taObservacionesOperario.setText(tarea.getObservacionesOperario());
		}

		pausasBox.getPanes().clear();
		for(Pausa pausa: tarea.getPausas()){
			TextArea pausaStr = new TextArea(pausa.getCausa());
			pausaStr.setEditable(false);
			pausaStr.setMaxHeight(100.0);
			pausaStr.setMinHeight(100.0);
			pausaStr.setWrapText(true);
			TitledPane pausaBox = new TitledPane("Inicio: " + conversorTiempos.diaMesAnioHoraYMinutosToString(pausa.getFechaHoraInicio()) + " Fin: " + conversorTiempos.diaMesAnioHoraYMinutosToString(pausa.getFechaHoraFin()), pausaStr);
			pausaBox.setExpanded(false);
			pausasBox.getPanes().add(pausaBox);
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
