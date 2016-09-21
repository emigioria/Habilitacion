package proy.gui.controladores;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import proy.datos.entidades.Herramienta;

public class AHerramientasController extends ControladorRomano {

	public static String URLVista = "/proy/gui/vistas/AHerramientas.fxml";

	@FXML
	private TextField nombreHerramienta;

	@FXML
	private TableView<Herramienta> tablaHerramientas;

	@FXML
	private TableColumn<Herramienta, String> columnaNombre;

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			columnaNombre.setCellValueFactory((CellDataFeatures<Herramienta, String> param) -> {
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
	public void nuevaHerramienta() {

	}

	@FXML
	public void eliminarHerramienta() {

	}

	@Override
	public void actualizar() {

	}
}
