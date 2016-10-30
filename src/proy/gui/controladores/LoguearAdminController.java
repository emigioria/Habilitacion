/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.awt.Frame;

import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import proy.datos.clases.DatosLogin;
import proy.excepciones.PersistenciaException;
import proy.gui.PresentadorExcepciones;
import proy.gui.componentes.VentanaError;
import proy.logica.gestores.resultados.ResultadoAutenticacion;
import proy.logica.gestores.resultados.ResultadoAutenticacion.ErrorAutenticacion;

public class LoguearAdminController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/LoguearAdmin.fxml";

	@FXML
	private TextField nombre;

	@FXML
	private SwingNode swingContra;

	private JPasswordField contra;

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			contra = new JPasswordField();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					swingContra.setContent(contra);
				}
			});
		});
	}

	@FXML
	public void iniciarSesion() {
		//TODO borrar para activar login
		ControladorRomano.cambiarScene(MenuAdministracionController.URL_VISTA, apilador, coordinador);
		matarSwing();
		if(true){
			return;
		}
		@SuppressWarnings("unused")
		//borrar

		ResultadoAutenticacion resultado = null;
		Boolean hayErrores;
		DatosLogin datos;
		String errores = "";

		//Toma de datos de la vista
		String user = nombre.getText().trim();
		char[] pass = contra.getPassword();
		if(user.isEmpty() || pass.length < 1){
			new VentanaError("No se ha podido iniciar sesión", "Campos vacíos.", apilador.getStage());
			return;
		}
		datos = new DatosLogin(user, pass);

		//Inicio transacción al gestor
		try{
			resultado = coordinador.autenticarAdministrador(datos);
		} catch(PersistenciaException e){
			PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
			return;
		} catch(Exception e){
			PresentadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
			return;
		}

		//Tratamiento de errores
		hayErrores = resultado.hayErrores();
		if(hayErrores){
			for(ErrorAutenticacion r: resultado.getErrores()){
				switch(r) {
				case DATOS_INVALIDOS:
					errores += "Datos inválidos al iniciar sesión.\n";
					break;
				}
			}
			if(!errores.isEmpty()){
				new VentanaError("No se ha podido iniciar sesión", errores, apilador.getStage());
			}
		}
		else{
			//Operacion exitosa
			ControladorRomano.cambiarScene(MenuAdministracionController.URL_VISTA, apilador, coordinador);
			matarSwing();
		}
	}

	@Override
	public void actualizar() {

	}

	@Override
	public void salir() {
		matarSwing();
		super.salir();
	}

	private void matarSwing() {
		Frame[] frames = Frame.getFrames();
		for(Frame f: frames){
			f.dispose();
		}
	}
}
