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
import javafx.stage.Window;
import proy.gui.componentes.StyleCSS;
import proy.gui.controladores.ControladorRomano;

public class VentanaPersonalizada extends Stage {

	public VentanaPersonalizada(String URLVista, String titulo, Window padre) throws IOException {
		//Crear el cargador de la pantalla
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ControladorRomano.class.getResource(URLVista));

		//Cargar vista
		Parent scenaSiguiente = (Parent) loader.load();

		//Setear estilo si no tiene
		if(scenaSiguiente.getStylesheets().isEmpty()){
			scenaSiguiente.getStylesheets().add(new StyleCSS().getDefaultStyle());
		}

		crearPantalla(scenaSiguiente, titulo, padre);
	}

	private void crearPantalla(Parent vista, String titulo, Window padre) {
		//Crear panel base
		this.initStyle(StageStyle.UNDECORATED);
		if(padre != null){
			this.initOwner(padre);
		}

		//Setear titulo de espera
		this.setTitle("Esperando");

		Scene scene = new Scene(vista);
		this.setScene(scene);
	}

	public VentanaPersonalizada(Parent vista, String titulo, Window padre) {
		crearPantalla(vista, titulo, padre);
	}
}
