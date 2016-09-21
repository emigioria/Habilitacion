package proy.gui.controladores;

import javafx.fxml.FXML;

public class VTareasController extends ControladorRomano {

	public static String URLVista = "/proy/gui/vistas/VTareas.fxml";

	@FXML
	public void loguearse() {
		ControladorRomano.nuevaScene(LoguearAdminController.URLVista, apilador, coordinador);
	}

	@Override
	public void actualizar() {

	}
}
