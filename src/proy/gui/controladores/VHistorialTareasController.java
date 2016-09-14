package proy.gui.controladores;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import proy.datos.entidades.Pieza;

public class VHistorialTareasController extends ControladorRomano {

	public static String URLVista = "/proy/gui/vistas/VHistorialTareas.fxml";

	@FXML
	private Label nombreProceso;

	@FXML
	private Label nombreMaquina;

	@FXML
	private Label nombreParte;

	@FXML
	private Label nombreOperario;

	@FXML
	private TableView<Pieza> tablaPiezas;

	@FXML
	private TableColumn<Pieza, String> columnaPieza;

	@FXML
	private TableColumn<Pieza, String> columnaMaterial;

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			columnaPieza.setCellValueFactory((CellDataFeatures<Pieza, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getNombre());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
			columnaMaterial.setCellValueFactory((CellDataFeatures<Pieza, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getMaterial().getNombre());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
		});
	}

	@FXML
	public void nuevoOperario() {

	}

	@FXML
	public void eliminarOperario() {

	}

	@FXML
	public void guardarOperario() {

	}

	@Override
	public void actualizar() {

	}
}
