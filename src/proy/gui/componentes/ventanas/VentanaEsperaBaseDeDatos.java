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

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import proy.gui.componentes.IconoAnimadoEspera;
import proy.gui.componentes.StyleCSS;

public class VentanaEsperaBaseDeDatos extends Stage {

	protected VentanaEsperaBaseDeDatos() {
		this(null);
	}

	protected VentanaEsperaBaseDeDatos(Window padre) {
		//Crear panel base
		this.initStyle(StageStyle.UNDECORATED);
		if(padre != null){
			this.initOwner(padre);
		}
		VBox panel = new VBox();
		panel.setAlignment(Pos.CENTER);

		//Setear estilo
		panel.getStylesheets().add(new StyleCSS().getDefaultStyle());

		//Setear imagen de espera
		ImageView imagen = new ImageView(new IconoAnimadoEspera());
		panel.getChildren().add(imagen);

		//Setear titulo de espera
		this.setTitle("Esperando");

		//Setear texto de espera
		Label esperando = new Label("Esperando a la base de datos...");
		esperando.setAlignment(Pos.CENTER);
		esperando.setFont(new Font(20));
		panel.getChildren().add(esperando);

		Scene scene = new Scene(panel);
		this.setScene(scene);
	}
}
