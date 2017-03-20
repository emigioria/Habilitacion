/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import proy.datos.entidades.Pausa;
import proy.datos.entidades.Tarea;
import proy.datos.filtros.implementacion.FiltroTarea;
import proy.excepciones.PersistenciaException;
import proy.gui.ControladorJavaFX;
import proy.gui.componentes.IconoCancelar;
import proy.gui.componentes.IconoDetener;
import proy.gui.componentes.IconoPausa;
import proy.gui.componentes.IconoPlay;
import proy.gui.componentes.ventanas.VentanaConfirmacion;
import proy.gui.componentes.ventanas.VentanaPersonalizada;
import proy.logica.gestores.resultados.ResultadoModificarEstadoTarea;
import proy.logica.gestores.resultados.ResultadoModificarEstadoTarea.ErrorModificarEstadoTarea;

public class VTareasOperarioTabController extends ControladorJavaFX {

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

	private Tarea tareaMostrada;

	public VTareasOperarioTabController(Operario operario) throws IOException {
		this.operario = operario;

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

	@Override
	protected void inicializar() {
		//Configurar botones
		configurarBotones();

		//Setear operario
		operarioTab.setText(operario.toString());

		actualizar();
	}

	private String getNombreTarea(Tarea tarea) {
		return "Tarea " + (tareas.indexOf(tarea) + 1);
	}

	private void configurarBotones() {
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
		botonDetener.setTooltip(new Tooltip("Terminar tarea"));
		botonCancelar.setTooltip(new Tooltip("Cancelar tarea"));
		botonPausa.setTooltip(new Tooltip("Pausar tarea"));
	}

	@FXML
	private void comenzarReanudarTarea() {
		switch(tareaMostrada.getEstado().getNombre()) {
		case PLANIFICADA:
			comenzarTarea();
			break;
		case PAUSADA:
			reanudarTarea();
			break;
		default:
			throw new RuntimeException();
		}
	}

	private void comenzarTarea() {
		ResultadoModificarEstadoTarea resultadoModificarEstadoTarea;
		StringBuffer erroresBfr = new StringBuffer();

		tareaMostrada.setFechaHoraInicio(new Date());

		//Inicio transacción al gestor
		try{
			resultadoModificarEstadoTarea = coordinador.comenzarTarea(tareaMostrada);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}

		//Tratamiento de errores
		if(resultadoModificarEstadoTarea.hayErrores()){
			for(ErrorModificarEstadoTarea r: resultadoModificarEstadoTarea.getErrores()){
				switch(r) {
				//TODO hacer validador primero
				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				presentadorVentanas.presentarError("Error al modificar la tarea", errores, stage);
			}
			return;
		}
		else{
			actualizar();
			presentadorVentanas.presentarToast("Tarea comenzada con éxito.", stage);
			return;
		}
	}

	private void reanudarTarea() {
		ResultadoModificarEstadoTarea resultadoModificarEstadoTarea;
		StringBuffer erroresBfr = new StringBuffer();

		//Toma de datos de la vista
		final Tarea tareaADespausar = tareaMostrada;

		Pausa ultimaPausa = null;
		for(Pausa p: tareaADespausar.getPausas()){
			if(p.getFechaHoraFin() == null){
				ultimaPausa = p;
			}
		}

		//Permitir modificar la causa de la pausa
		VentanaPersonalizada ventanaPausa = presentadorVentanas.presentarVentanaPersonalizada(NMPausaController.URL_VISTA, coordinador, stage);
		((NMPausaController) ventanaPausa.getControlador()).formatearModificarPausa(ultimaPausa);
		ventanaPausa.showAndWait();

		ultimaPausa = ((NMPausaController) ventanaPausa.getControlador()).getResultado();
		if(ultimaPausa == null){
			return;
		}
		ultimaPausa.setFechaHoraFin(new Date());

		//Inicio transacción al gestor
		try{
			resultadoModificarEstadoTarea = coordinador.reanudarTarea(tareaADespausar);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}

		//Tratamiento de errores
		if(resultadoModificarEstadoTarea.hayErrores()){
			for(ErrorModificarEstadoTarea r: resultadoModificarEstadoTarea.getErrores()){
				switch(r) {
				//TODO hacer validador primero
				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				presentadorVentanas.presentarError("Error al modificar la tarea", errores, stage);
			}
			return;
		}
		else{
			actualizar();
			presentadorVentanas.presentarToast("Tarea reanudada con éxito.", stage);
			return;
		}
	}

	@FXML
	private void pausarTarea() {
		ResultadoModificarEstadoTarea resultadoModificarEstadoTarea;
		StringBuffer erroresBfr = new StringBuffer();

		//Toma de datos de la vista
		final Tarea tareaAPausar = tareaMostrada;

		//Pedir causa de la pausa
		VentanaPersonalizada ventanaPausa = presentadorVentanas.presentarVentanaPersonalizada(NMPausaController.URL_VISTA, coordinador, stage);
		ventanaPausa.showAndWait();

		Pausa pausa = ((NMPausaController) ventanaPausa.getControlador()).getResultado();
		if(pausa == null){
			return;
		}
		tareaAPausar.getPausas().add(pausa);

		//Inicio transacción al gestor
		try{
			resultadoModificarEstadoTarea = coordinador.pausarTarea(tareaAPausar);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}

		//Tratamiento de errores
		if(resultadoModificarEstadoTarea.hayErrores()){
			for(ErrorModificarEstadoTarea r: resultadoModificarEstadoTarea.getErrores()){
				switch(r) {
				//TODO hacer validador primero
				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				presentadorVentanas.presentarError("Error al modificar la tarea", errores, stage);
			}
			return;
		}
		else{
			actualizar();
			presentadorVentanas.presentarToast("Tarea pausada con éxito.", stage);
			return;
		}
	}

	@FXML
	private void detenerTarea() {
		ResultadoModificarEstadoTarea resultadoModificarEstadoTarea;
		StringBuffer erroresBfr = new StringBuffer();
		//Toma de datos de la vista
		final Tarea tareaADetener = tareaMostrada;

		//Pedir cantidad realizada y observaciones
		VentanaPersonalizada ventanaPausa = presentadorVentanas.presentarVentanaPersonalizada(NFinTareaController.URL_VISTA, coordinador, stage);
		((NFinTareaController) ventanaPausa.getControlador()).formatearConTarea(tareaADetener);
		ventanaPausa.showAndWait();

		Tarea tareaFinal = ((NFinTareaController) ventanaPausa.getControlador()).getResultado();
		if(tareaFinal == null || tareaADetener != tareaFinal){
			return;
		}
		tareaADetener.setFechaHoraFin(new Date());

		//Inicio transacción al gestor
		try{
			resultadoModificarEstadoTarea = coordinador.terminarTarea(tareaADetener);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}

		//Tratamiento de errores
		if(resultadoModificarEstadoTarea.hayErrores()){
			for(ErrorModificarEstadoTarea r: resultadoModificarEstadoTarea.getErrores()){
				switch(r) {
				//TODO hacer validador primero
				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				presentadorVentanas.presentarError("Error al modificar la tarea", errores, stage);
			}
			return;
		}
		else{
			actualizar();
			presentadorVentanas.presentarToast("Tarea comenzada con éxito.", stage);
			return;
		}
	}

	@FXML
	private void cancelarTarea() {
		ResultadoModificarEstadoTarea resultadoModificarEstadoTarea;
		StringBuffer erroresBfr = new StringBuffer();

		final Tarea tareaACancelar = tareaMostrada;
		//se pregunta al usuario si desea cancelar la tarea
		VentanaConfirmacion vc = presentadorVentanas.presentarConfirmacion("Confirmar cancelar tarea",
				"Se cancelará la tarea seleccionada.\n" +
						"¿Está seguro de que desea continuar?",
				stage);
		if(!vc.acepta()){
			return;
		}

		tareaACancelar.setFechaHoraInicio(null);
		tareaACancelar.setFechaHoraFin(null);
		tareaACancelar.setCantidadReal(null);
		tareaACancelar.setObservacionesOperario(null);
		tareaACancelar.getPausas().clear();

		//Inicio transacción al gestor
		try{
			resultadoModificarEstadoTarea = coordinador.cancelarTarea(tareaACancelar);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}

		//Tratamiento de errores
		if(resultadoModificarEstadoTarea.hayErrores()){
			for(ErrorModificarEstadoTarea r: resultadoModificarEstadoTarea.getErrores()){
				switch(r) {
				//TODO hacer validador primero
				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				presentadorVentanas.presentarError("Error al modificar la tarea", errores, stage);
			}
			return;
		}
		else{
			actualizar();
			presentadorVentanas.presentarToast("Tarea comenzada con éxito.", stage);
			return;
		}
	}

	public Tab getTab() {
		return operarioTab;
	}

	public void actualizar() {
		tareasBox.getPanes().clear();

		//Cargar tareas
		ArrayList<EstadoTareaStr> estados = new ArrayList<>();
		estados.add(EstadoTareaStr.EJECUTANDO);
		estados.add(EstadoTareaStr.PAUSADA);
		estados.add(EstadoTareaStr.PLANIFICADA);

		try{
			tareas = coordinador.listarTareas(new FiltroTarea.Builder().estados(estados).operario(operario).ordenFechaFinalizada().build());

			for(final Tarea tarea: tareas){
				TitledPane renglon = new VTareasTareaRenglonController(tarea).getRenglon();
				renglon.focusedProperty().addListener((obs, oldV, newV) -> {
					if(newV){
						mostrarTarea(tarea);
					}
				});
				renglon.setText(getNombreTarea(tarea));
				tareasBox.getPanes().add(renglon);
			}

			//Mostrar la tarea que se está ejecutando
			tareaEjecutando = null;
			for(Tarea tarea: tareas){
				if(EstadoTareaStr.EJECUTANDO.equals(tarea.getEstado().getNombre())){
					tareaEjecutando = tarea;
					mostrarTarea(tareaEjecutando);
				}
			}

			//Si no se mostró ninguna tarea ejecutando
			if(tareaEjecutando == null){
				//Actualizar tarea que ya estaba o mostrar la primera en la lista
				if(tareaMostrada != null){
					mostrarTarea(tareaMostrada);
				}
				else{
					if(tareas.size() > 0){
						mostrarTarea(tareas.get(0));
					}
				}
			}
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
		}
	}

	private void mostrarTarea(Tarea tarea) {
		if(tareaEjecutando != null && tarea != tareaEjecutando){
			return;
		}
		tareaMostrada = tarea;
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

	@Override
	protected void salir() {

	}
}
