/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import proy.gui.componentes.StyleCSS;
import proy.gui.componentes.ventanas.PresentadorVentanas;
import proy.logica.CoordinadorJavaFX;

public abstract class ControladorRomano extends ControladorJavaFX implements ControladorApilable {

	private PilaJavaFX apilador;

	private void setApilador(PilaJavaFX apilador) {
		this.apilador = apilador;
	}

	protected ControladorRomano nuevaScene(String URLVista) {
		return nuevaCambiarScene(URLVista, apilador, coordinador, false);
	}

	protected ControladorRomano cambiarScene(String URLVista) {
		return nuevaCambiarScene(URLVista, apilador, coordinador, true);
	}

	protected ControladorRomano nuevaCambiarScene(String URLVista, PilaJavaFX apilador, CoordinadorJavaFX coordinador, Boolean cambiar) {
		try{
			//Crear el cargador de la pantalla
			FXMLLoader loader = new FXMLLoader(ControladorRomano.class.getResource(URLVista));

			//Cargar vista
			Parent pantallaSiguiente = (Parent) loader.load();

			//Cargar controlador
			ControladorRomano controlador = loader.getController();

			//Setear estilo si no tiene
			if(pantallaSiguiente.getStylesheets().isEmpty()){
				pantallaSiguiente.getStylesheets().add(new StyleCSS().getDefaultStyle());
			}

			if(cambiar){
				apilador.cambiarPantalla(pantallaSiguiente, controlador);
			}
			else{
				apilador.apilarPantalla(pantallaSiguiente, controlador);
			}
			controlador.setStage(stage);
			controlador.setApilador(apilador);
			controlador.setCoordinador(coordinador);
			return controlador;
		} catch(IOException e){
			new PresentadorVentanas().presentarExcepcionInesperada(e, stage);
			return null;
		}
	}

	@Override
	@FXML
	public void salir() {
		apilador.desapilarPantalla();
	}

	@Override
	public void dejarDeMostrar() {

	}

	@Override
	public void actualizar() {

	}

	@Override
	public Boolean sePuedeSalir() {
		return true;
	}

	public static PilaScene crearYMostrarPrimeraVentana(CoordinadorJavaFX coordinador, Stage primaryStage, String URL_Vista) {
		Scene primaryScene = new Scene(new Pane());
		primaryStage.setScene(primaryScene);
		PilaScene apilador = new PilaScene(primaryScene);
		ControladorRomano pantallaMock = new ControladorRomano() {
			@Override
			public void actualizar() {
			}

			@Override
			public void inicializar() {
			}

			@Override
			public void dejarDeMostrar() {

			}
		};
		pantallaMock.setApilador(apilador);
		pantallaMock.setCoordinador(coordinador);
		pantallaMock.setStage(primaryStage);
		pantallaMock.nuevaScene(URL_Vista);
		primaryStage.show();
		return apilador;
	}
}
