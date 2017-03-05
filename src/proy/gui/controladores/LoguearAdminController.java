/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import proy.datos.clases.DatosLogin;
import proy.excepciones.PersistenciaException;
import proy.gui.componentes.SafePasswordField;
import proy.logica.gestores.resultados.ResultadoAutenticacion;
import proy.logica.gestores.resultados.ResultadoAutenticacion.ErrorAutenticacion;

public class LoguearAdminController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/LoguearAdmin.fxml";

	@FXML
	private TextField nombre;

	@FXML
	private GridPane contenedor;

	private SafePasswordField contra;

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			contra = new SafePasswordField();
			contenedor.getChildren().add(contra);
			GridPane.setMargin(contra, new Insets(10.0));
			GridPane.setColumnIndex(contra, 1);
			GridPane.setRowIndex(contra, 1);
		});
	}

	@FXML
	public void iniciarSesion() {
		//TODO borrar para activar login
		ControladorRomano.cambiarScene(MenuAdministracionController.URL_VISTA, apilador, coordinador);
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
			presentadorVentanas.presentarError("No se ha podido iniciar sesión", "Campos vacíos.", apilador.getStage());
			return;
		}
		datos = new DatosLogin(user, pass);

		//Inicio transacción al gestor
		try{
			resultado = coordinador.autenticarAdministrador(datos);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, apilador.getStage());
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, apilador.getStage());
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
				presentadorVentanas.presentarError("No se ha podido iniciar sesión", errores, apilador.getStage());
			}
		}
		else{
			//Operacion exitosa
			ControladorRomano.cambiarScene(MenuAdministracionController.URL_VISTA, apilador, coordinador);
		}
	}

	@Override
	public void actualizar() {

	}
}
