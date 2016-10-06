/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import proy.gui.ControladorApilable;
import proy.gui.ManejadorExcepciones;
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
		return nuevaCambiarScene(URLVista, apilador, coordinador, false);
	}

	public static ControladorRomano cambiarScene(String URLVista, PilaScene apilador, CoordinadorJavaFX coordinador) {
		return nuevaCambiarScene(URLVista, apilador, coordinador, true);
	}

	private static ControladorRomano nuevaCambiarScene(String URLVista, PilaScene apilador, CoordinadorJavaFX coordinador, Boolean cambiar) {
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ControladorRomano.class.getResource(URLVista));
			Parent scenaSiguiente = (Parent) loader.load();
			ControladorRomano controlador = loader.getController();

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
			ManejadorExcepciones.presentarExcepcion(e, apilador.getStage());
		}
		return null;
	}

	@FXML
	public void salir() {
		//Stage stage = apilador.getStage();
		//stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
		apilador.desapilarScene();
	}
}
