package proy.gui.controladores;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import proy.datos.entidades.Maquina;

public class AMaquinasController extends ControladorRomano {

	public static String URLVista = "/proy/gui/vistas/AMaquinas.fxml";

	@FXML
	private TextField nombreMaquina;

	@FXML
	private TableView<Maquina> tablaMaquinas;

	@FXML
	private TableColumn<Maquina, String> columnaNombre;

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			columnaNombre.setCellValueFactory((CellDataFeatures<Maquina, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getNombre());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
		});
	}

	@FXML
	public void nuevaMaquina() {

	}

	@FXML
	public void modificarMaquina() {

	}

	@FXML
	public void eliminarMaquina() {

	}

	@Override
	public void actualizar() {

	}
}
