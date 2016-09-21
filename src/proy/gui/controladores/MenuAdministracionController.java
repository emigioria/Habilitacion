package proy.gui.controladores;

import javafx.fxml.FXML;

public class MenuAdministracionController extends ControladorRomano {

	public static String URLVista = "/proy/gui/vistas/MenuAdministracion.fxml";

	@FXML
	public void administrarOperarios() {
		ControladorRomano.nuevaScene(AOperariosController.URLVista, apilador, coordinador);
	}

	@FXML
	public void administrarMaquinas() {
		ControladorRomano.nuevaScene(AMaquinasController.URLVista, apilador, coordinador);
	}

	@FXML
	public void administrarProcesos() {
		ControladorRomano.nuevaScene(AProcesosController.URLVista, apilador, coordinador);
	}

	@FXML
	public void administrarMateriales() {
		ControladorRomano.nuevaScene(AMaterialesController.URLVista, apilador, coordinador);
	}

	@FXML
	public void administrarHerramientas() {
		ControladorRomano.nuevaScene(AHerramientasController.URLVista, apilador, coordinador);
	}

	@FXML
	public void administrarTareas() {
		ControladorRomano.nuevaScene(ATareasController.URLVista, apilador, coordinador);
	}

	@Override
	public void actualizar() {

	}
}
