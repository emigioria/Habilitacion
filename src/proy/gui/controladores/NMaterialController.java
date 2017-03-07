package proy.gui.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import proy.datos.entidades.Material;
import proy.gui.ControladorDialogo;

public class NMaterialController extends ControladorDialogo {

	public static final String URL_VISTA = "/proy/gui/vistas/NMaterial.fxml";

	private Material material;

	@FXML
	private TextField tfNombre;

	@FXML
	private TextField tfMedidas;

	@FXML
	private void guardar() {
		material = new Material();
		material.setNombre(tfNombre.getText().trim());
		material.setMedidas(tfMedidas.getText().trim());
		salir();
	}

	@FXML
	private void salir() {
		stage.hide();
	}

	@Override
	protected void inicializar() {
		stage.setTitle("Nuevo material");
	}

}
