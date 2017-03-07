/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import proy.comun.ConversorFechas;
import proy.comun.FormateadorString;
import proy.gui.ControladorApilable;
import proy.gui.PilaScene;
import proy.gui.componentes.ventanas.PresentadorVentanas;
import proy.logica.CoordinadorJavaFX;

public abstract class ControladorRomano implements ControladorApilable {

	protected PilaScene apilador;

	protected CoordinadorJavaFX coordinador;

	protected ConversorFechas conversorFechas = new ConversorFechas();

	protected FormateadorString formateadorString = new FormateadorString();

	protected PresentadorVentanas presentadorVentanas = new PresentadorVentanas();

	public void setApilador(PilaScene apilador) {
		this.apilador = apilador;
	}

	public void setCoordinador(CoordinadorJavaFX coordinador) {
		this.coordinador = coordinador;
	}

	public ControladorRomano nuevaScene(String URLVista) {
		return nuevaCambiarScene(URLVista, apilador, coordinador, false);
	}

	public ControladorRomano cambiarScene(String URLVista) {
		return nuevaCambiarScene(URLVista, apilador, coordinador, true);
	}

	private ControladorRomano nuevaCambiarScene(String URLVista, PilaScene apilador, CoordinadorJavaFX coordinador, Boolean cambiar) {
		try{
			//Crear el cargador de la pantalla
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ControladorRomano.class.getResource(URLVista));

			//Cargar vista
			Parent scenaSiguiente = (Parent) loader.load();

			//Cargar controlador
			ControladorRomano controlador = loader.getController();

			//Setear estilo si no tiene
			if(scenaSiguiente.getStylesheets().isEmpty()){
				//TODO descomentar
				//				scenaSiguiente.getStylesheets().add(new StyleCSS().getDefaultStyle());
			}

			Scene scene = new Scene(scenaSiguiente);
			if(cambiar){
				apilador.cambiarScene(scene, controlador);
			}
			else{
				apilador.apilarScene(scene, controlador);
			}
			controlador.setApilador(apilador);
			controlador.setCoordinador(coordinador);
			return controlador;
		} catch(IOException e){
			new PresentadorVentanas().presentarExcepcion(e, apilador.getStage());
			return null;
		}
	}

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			inicializar();
		});
	}

	protected abstract void inicializar();

	@FXML
	public void salir() {
		//Stage stage = apilador.getStage();
		//stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
		apilador.desapilarScene();
	}
}
