package proy.gui.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoguearAdminController extends ControladorRomano {

	public static String URLVista = "/proy/gui/vistas/LoguearAdmin.fxml";

	@FXML
	TextField nombre;

	@FXML
	PasswordField contra;

	@FXML
	public void iniciarSesion() {
		//TODO hacer
		//		try{
		//coordinador.autenticarAdministrador(new FiltroAdministrador());
		//Contra.encriptarMD5(contra.getText().toCharArray(), Contra.generarSal());
		//		} catch(PersistenciaException e){
		//			ManejadorExcepciones.presentarExcepcion(e, apilador.getStage());
		//			return;
		//		}
		Boolean hayErrores = false;
		if(!hayErrores){
			ControladorRomano.nuevaScene(MenuAdministracionController.URLVista, apilador, coordinador);
		}
	}

	@Override
	public void actualizar() {

	}
}
