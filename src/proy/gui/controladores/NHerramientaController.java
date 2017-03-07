package proy.gui.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import proy.datos.entidades.Herramienta;
import proy.gui.ControladorDialogo;

public class NHerramientaController extends ControladorDialogo {

	public static final String URL_VISTA = "/proy/gui/vistas/NHerramienta.fxml";

	private Herramienta herramienta;

	@FXML
	private TextField tfNombre;

	@FXML
	private void guardar() {
		herramienta = new Herramienta();
		herramienta.setNombre(tfNombre.getText().trim());
		salir();
	}

	@FXML
	private void salir() {
		stage.hide();
	}

	@Override
	public void inicializar() {
		stage.setTitle("Nueva herramienta");
	}

}
