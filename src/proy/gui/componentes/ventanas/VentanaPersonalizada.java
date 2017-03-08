/**
 * Copyright (C) 2016 Fernando Berti - Daniel Campodonico - Emiliano Gioria - Lucas Moretti - Esteban Rebechi - Andres Leonel Rico
 * This file is part of Olimpo.
 *
 * Olimpo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Olimpo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Olimpo. If not, see <http://www.gnu.org/licenses/>.
 */
package proy.gui.componentes.ventanas;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import proy.gui.ControladorDialogo;
import proy.gui.ControladorRomano;
import proy.gui.componentes.StyleCSS;
import proy.logica.CoordinadorJavaFX;

public class VentanaPersonalizada<T> extends Stage {

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
			this.initStyle(StageStyle.DECORATED);
			if(padre != null){
				this.initOwner(padre);
			}
			this.getIcons().addAll(padre.getIcons());

			controlador.setStage(this);
			controlador.setCoordinador(coordinador);

			Scene scene = new Scene(vista);
			this.setScene(scene);
		} catch(IOException e){
			new PresentadorVentanas().presentarExcepcionInesperada(e, padre);
		}
	}
}
