package proy.gui.controladores;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import proy.datos.entidades.Material;

public class AMaterialesController extends ControladorRomano {

	public static String URLVista = "/proy/gui/vistas/AMateriales.fxml";

	@FXML
	private TextField nombreMaterial;

	@FXML
	private TableView<Material> tablaMateriales;

	@FXML
	private TableColumn<Material, String> columnaMaterial;

	@FXML
	private TableColumn<Material, String> columnaMedidas;

	@FXML
	private Button botonNuevo;

	@FXML
	private Button botonEliminar;

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			columnaMaterial.setCellValueFactory((CellDataFeatures<Material, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getNombre());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
			columnaMedidas.setCellValueFactory((CellDataFeatures<Material, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getMedidas());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
		});
	}

	@FXML
	public void nuevoMaterial() {

	}

	@FXML
	public void eliminarMaterial() {

	}

	@Override
	public void actualizar() {

	}
}
