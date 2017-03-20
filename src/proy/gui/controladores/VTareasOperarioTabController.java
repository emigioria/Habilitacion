/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.io.IOException;
import java.util.List;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import proy.datos.clases.EstadoTareaStr;
import proy.datos.entidades.Operario;
import proy.datos.entidades.Tarea;
import proy.gui.componentes.IconoCancelar;
import proy.gui.componentes.IconoDetener;
import proy.gui.componentes.IconoPausa;
import proy.gui.componentes.IconoPlay;

public class VTareasOperarioTabController {

	public static final String URL_VISTA = "/proy/gui/vistas/VTareasOperarioTab.fxml";

	@FXML
	private Tab operarioTab;

	@FXML
	private Button botonDetener;

	@FXML
	private Button botonPlay;

	@FXML
	private Button botonCancelar;

	@FXML
	private Button botonPausa;

	@FXML
	private Accordion tareasBox;

	@FXML
	private Label numeroTarea;

	@FXML
	private Label nombreParte;

	@FXML
	private Label nombreProceso;

	@FXML
	private Label tiempoEjecutado;

	@FXML
	private Label estadoTarea;

	@FXML
	private ProgressBar progresoTarea;

	private Operario operario;

	private List<Tarea> tareas;

	private Tarea tareaEjecutando;

	private Runnable refreshAction;

	public VTareasOperarioTabController(Operario operario, List<Tarea> tareas, Runnable refreshAction) throws IOException {
		this.operario = operario;
		this.tareas = tareas;
		this.refreshAction = refreshAction;

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource(URL_VISTA));
		loader.setControllerFactory(claseControlador -> {
			if(claseControlador != null && !claseControlador.isInstance(this)){
				throw new IllegalArgumentException("¡Instancia del controlador inválida, esperada una instancia de la clase '" + claseControlador.getName() + "'!");
			}

			return this;
		});
		loader.load();
	}

	@FXML
	private void initialize() throws IOException {
		//Configurar botones
		configurarBotones();

		//Setear operario
		operarioTab.setText(operario.toString());

		//Cargar tareas
		for(final Tarea tarea: tareas){
			TitledPane renglon = new VTareasTareaRenglonController(tarea).getRenglon();
			renglon.focusedProperty().addListener((obs, oldV, newV) -> {
				if(newV){
					cargarTarea(tarea);
				}
			});
			renglon.setText(getNombreTarea(tarea));
			tareasBox.getPanes().add(renglon);
		}

		//Mostrar tarea
		if(tareas.size() > 0){
			cargarTarea(tareas.get(0));
		}
		for(Tarea tarea: tareas){
			if(EstadoTareaStr.EJECUTANDO.equals(tarea.getEstado().getNombre())){
				tareaEjecutando = tarea;
				cargarTarea(tareaEjecutando);
			}
		}
	}

	private String getNombreTarea(Tarea tarea) {
		return "Tarea " + (tareas.indexOf(tarea) + 1);
	}

	private void configurarBotones() {
		Platform.runLater(() -> {
			ImageView imagen = new ImageView(new IconoDetener());
			imagen.setFitWidth(botonDetener.getWidth());
			imagen.setFitHeight(botonDetener.getHeight());
			botonDetener.setGraphic(imagen);

			imagen = new ImageView(new IconoPlay());
			imagen.setFitWidth(botonPlay.getWidth());
			imagen.setFitHeight(botonPlay.getHeight());
			botonPlay.setGraphic(imagen);

			imagen = new ImageView(new IconoCancelar());
			imagen.setFitWidth(botonCancelar.getWidth());
			imagen.setFitHeight(botonCancelar.getHeight());
			botonCancelar.setGraphic(imagen);

			imagen = new ImageView(new IconoPausa());
			imagen.setFitWidth(botonPausa.getWidth());
			imagen.setFitHeight(botonPausa.getHeight());
			botonPausa.setGraphic(imagen);

			final String STYLE_NORMAL = "-fx-background-color: transparent; -fx-padding: 5 5 5 5;";
			final String STYLE_PRESSED = "-fx-background-color: transparent; -fx-padding: 6 4 4 6;";
			botonDetener.setStyle(STYLE_NORMAL);
			botonPlay.setStyle(STYLE_NORMAL);
			botonPausa.setStyle(STYLE_NORMAL);
			botonCancelar.setStyle(STYLE_NORMAL);

			EventHandler<MouseEvent> eventoClickPressed = new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					((Button) event.getSource()).setStyle(STYLE_PRESSED);
				}
			};
			EventHandler<MouseEvent> eventoClickReleased = new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					((Button) event.getSource()).setStyle(STYLE_NORMAL);
				}
			};
			botonPlay.setOnMousePressed(eventoClickPressed);
			botonPlay.setOnMouseReleased(eventoClickReleased);
			botonDetener.setOnMousePressed(eventoClickPressed);
			botonDetener.setOnMouseReleased(eventoClickReleased);
			botonCancelar.setOnMousePressed(eventoClickPressed);
			botonCancelar.setOnMouseReleased(eventoClickReleased);
			botonPausa.setOnMousePressed(eventoClickPressed);
			botonPausa.setOnMouseReleased(eventoClickReleased);

			botonPlay.setTooltip(new Tooltip("Comenzar tarea"));
			botonDetener.setTooltip(new Tooltip("Detener tarea"));
			botonCancelar.setTooltip(new Tooltip("Cancelar tarea"));
			botonPausa.setTooltip(new Tooltip("Pausar tarea"));
		});
	}

	private void cargarTarea(Tarea tarea) {
		if(tareaEjecutando != null && tarea != tareaEjecutando){
			return;
		}
		numeroTarea.setText(getNombreTarea(tarea));
		estadoTarea.setText(tarea.getEstado().toString());
		nombreParte.setText(tarea.getProceso().getParte().toString());
		nombreProceso.setText(tarea.getProceso().toString());

		//Setear tiempo de proceso y progreso
		Long tEMilis = tarea.getTiempoEjecutando();
		Long tESs = tEMilis % 60000;
		Long tEMs = tEMilis % 3600000;
		Long tEHs = tEMilis / 3600000;
		tiempoEjecutado.setText((tEHs < 10 ? "0" + tEHs : tEHs) + ":" + (tEMs < 10 ? "0" + tEMs : tEMs) + ":" + (tESs < 10 ? "0" + tESs : tESs));
		Long tTPMilis = tarea.getProceso().getTiempoTeoricoProceso();
		progresoTarea.setVisible(true);
		progresoTarea.setProgress(tEMilis / tTPMilis);
		if(progresoTarea.getProgress() >= 1){
			progresoTarea.setStyle("-fx-accent: red;");
		}
		else{
			progresoTarea.setStyle("");
		}

		//Quitar botones que no van
		switch(tarea.getEstado().getNombre()) {
		case PLANIFICADA:
			botonPlay.setVisible(true);
			botonPausa.setVisible(false);
			botonDetener.setVisible(false);
			botonCancelar.setVisible(false);
			break;
		case EJECUTANDO:
			botonPlay.setVisible(false);
			botonPausa.setVisible(true);
			botonDetener.setVisible(true);
			botonCancelar.setVisible(true);
			break;
		case PAUSADA:
			botonPlay.setVisible(true);
			botonPausa.setVisible(false);
			botonDetener.setVisible(false);
			botonCancelar.setVisible(true);
			break;
		case FINALIZADA:
			botonPlay.setVisible(false);
			botonPausa.setVisible(false);
			botonDetener.setVisible(false);
			botonCancelar.setVisible(false);
			break;
		}
	}

	@FXML
	private void comenzarReanudarTarea() {

		refreshAction.run();
	}

	@FXML
	private void pausarTarea() {

		refreshAction.run();
	}

	@FXML
	private void detenerTarea() {

		refreshAction.run();
	}

	@FXML
	private void cancelarTarea() {

		refreshAction.run();
	}

	public Tab getTab() {
		return operarioTab;
	}
}
