/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import proy.datos.clases.DatosLogin;
import proy.excepciones.PersistenciaException;
import proy.gui.ControladorDialogo;
import proy.gui.componentes.SafePasswordField;
import proy.logica.gestores.resultados.ResultadoAutenticacion;
import proy.logica.gestores.resultados.ResultadoAutenticacion.ErrorAutenticacion;

public class LoguearAdminController extends ControladorDialogo {

	public static final String URL_VISTA = "/proy/gui/vistas/LoguearAdmin.fxml";

	@FXML
	private TextField nombre;

	@FXML
	private VBox passwordBox;

	private SafePasswordField contra;

	private Boolean loginExitoso = false;

	@Override
	protected void inicializar() {
		stage.setTitle("Loguearse como administrador");

		contra = new SafePasswordField();
		contra.setPromptText("Ingrese su contraseña...");
		passwordBox.getChildren().add(contra);
	}

	@FXML
	private void iniciarSesion() {
		ResultadoAutenticacion resultado = null;
		StringBuffer erroresBfr = new StringBuffer();

		//Toma de datos de la vista
		String user = nombre.getText().trim();
		char[] pass = contra.getPassword();
		if(user.isEmpty() || pass.length < 1){
			presentadorVentanas.presentarError("No se ha podido iniciar sesión", "Campos vacíos.", stage);
			return;
		}
		DatosLogin datos = new DatosLogin(user, pass);

		//Inicio transacción al gestor
		try{
			resultado = coordinador.autenticarAdministrador(datos);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}

		//Tratamiento de errores
		if(resultado.hayErrores()){
			for(ErrorAutenticacion r: resultado.getErrores()){
				switch(r) {
				case DATOS_INVALIDOS:
					erroresBfr.append("Datos inválidos al iniciar sesión.\n");
					break;
				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				presentadorVentanas.presentarError("No se ha podido iniciar sesión", errores, stage);
			}
		}
		else{
			//Operacion exitosa
			loginExitoso = true;
			salir();
		}
	}

	public Boolean fueExitosoLogin() {
		return loginExitoso;
	}

}
