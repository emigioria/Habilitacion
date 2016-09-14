package proy.gui.controladores;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import proy.excepciones.ManejadorExcepciones;
import proy.gui.ControladorApilable;
import proy.gui.PilaScene;
import proy.logica.gestores.CoordinadorJavaFX;

public abstract class ControladorRomano implements ControladorApilable {

	protected PilaScene apilador;

	protected CoordinadorJavaFX coordinador;

	@Override
	public void setApilador(PilaScene apilador) {
		this.apilador = apilador;
	}

	public void setCoordinador(CoordinadorJavaFX coordinador) {
		this.coordinador = coordinador;
	}

	public static ControladorRomano nuevaScene(String URLVista, PilaScene apilador, CoordinadorJavaFX coordinador) {
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ControladorRomano.class.getResource(URLVista));
			Parent scenaSiguiente = (Parent) loader.load();
			ControladorRomano controlador = loader.getController();

			Scene scene = new Scene(scenaSiguiente);
			apilador.apilarScene(scene, controlador);
			controlador.setApilador(apilador);
			controlador.setCoordinador(coordinador);
			return controlador;
		} catch(IOException e){
			ManejadorExcepciones.presentarExcepcion(e, apilador.getStage());
		}
		return null;
	}

	@Override
	public abstract void actualizar();

	@FXML
	public void salir() {
		Stage stage = apilador.getStage();
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}
}
