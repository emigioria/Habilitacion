package proy.gui.controladores;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import proy.gui.componentes.IconoDetener;
import proy.gui.componentes.IconoPlay;

public class VTareasController extends ControladorRomano {

	public static String URLVista = "/proy/gui/vistas/VTareas.fxml";

	@FXML
	Button botonDetener;

	@FXML
	Button botonPlay;

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			ImageView imagen = new ImageView(new IconoDetener());
			imagen.setFitWidth(botonDetener.getWidth());
			imagen.setFitHeight(botonDetener.getHeight());
			botonDetener.setGraphic(imagen);
			imagen = new ImageView(new IconoPlay());
			imagen.setFitWidth(botonPlay.getWidth());
			imagen.setFitHeight(botonPlay.getHeight());
			botonPlay.setGraphic(imagen);

			final String STYLE_NORMAL = "-fx-background-color: transparent; -fx-padding: 5, 5, 5, 5;";
			final String STYLE_PRESSED = "-fx-background-color: transparent; -fx-padding: 6 4 4 6;";
			botonDetener.setStyle(STYLE_NORMAL);
			botonPlay.setStyle(STYLE_NORMAL);
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
		});
	}

	@FXML
	public void loguearse() {
		ControladorRomano.nuevaScene(LoguearAdminController.URLVista, apilador, coordinador);
	}

	@Override
	public void actualizar() {

	}
}
