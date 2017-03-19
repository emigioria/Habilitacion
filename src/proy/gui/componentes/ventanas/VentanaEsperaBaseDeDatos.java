/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
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
import proy.gui.componentes.IconoAplicacion;
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

		//Setear icono y titulo de espera
		this.setTitle("Esperando");
		this.getIcons().add(new IconoAplicacion());

		//Setear texto de espera
		Label esperando = new Label("Esperando a la base de datos...");
		esperando.setAlignment(Pos.CENTER);
		esperando.setFont(new Font(20));
		panel.getChildren().add(esperando);

		Scene scene = new Scene(panel);
		this.setScene(scene);
	}
}
