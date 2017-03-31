/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.componentes.ventanas;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import proy.gui.ControladorDialogo;
import proy.gui.ControladorRomano;
import proy.gui.componentes.StyleCSS;
import proy.logica.CoordinadorJavaFX;

public class VentanaPersonalizada extends Stage {

	private ControladorDialogo controlador;

	public VentanaPersonalizada(String URLVista, CoordinadorJavaFX coordinador, Stage padre) {
		try{
			//Crear el cargador de la pantalla
			FXMLLoader loader = new FXMLLoader(ControladorRomano.class.getResource(URLVista));

			//Cargar vista
			Parent vista = (Parent) loader.load();

			//Cargar controlador
			controlador = loader.getController();

			//Setear estilo si no tiene
			if(vista.getStylesheets().isEmpty()){
				vista.getStylesheets().add(new StyleCSS().getDefaultStyle());
			}

			//Crear pantalla
			this.initModality(Modality.APPLICATION_MODAL);
			this.initStyle(StageStyle.DECORATED);
			if(padre != null){
				try{
					this.initOwner(padre);
				} catch(NullPointerException e){
					//Si no tiene scene tira esta excepcion
				}
				this.getIcons().addAll(padre.getIcons());
			}

			controlador.setStage(this);
			controlador.setCoordinador(coordinador);

			Scene scene = new Scene(vista);
			this.setScene(scene);
		} catch(IOException e){
			new PresentadorVentanas().presentarExcepcionInesperada(e, padre);
		}
	}

	public ControladorDialogo getControlador() {
		return controlador;
	}
}
