/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import javax.swing.JPasswordField;

import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoguearAdminController extends ControladorRomano {

	public static String URLVista = "/proy/gui/vistas/LoguearAdmin.fxml";

	@FXML
	TextField nombre;

	@FXML
	SwingNode swingContra;

	JPasswordField contra;

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			contra = new JPasswordField();
			swingContra.setContent(contra);
		});
	}

	@FXML
	public void iniciarSesion() {
		//TODO hacer
		//		try{
		//String user = nombre.getText().trim();
		//String pass = Contra.encriptarMD5(contra.getPassword(), Contra.generarSal());
		//coordinador.autenticarAdministrador(new FiltroAdministrador());
		//		} catch(PersistenciaException e){
		//			ManejadorExcepciones.presentarExcepcion(e, apilador.getStage());
		//			return;
		//		}
		Boolean hayErrores = false;
		if(!hayErrores){
			ControladorRomano.cambiarScene(MenuAdministracionController.URLVista, apilador, coordinador);
		}
	}

	@Override
	public void actualizar() {

	}
}
