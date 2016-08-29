package proy.gui.controladores;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import proy.datos.entidades.Operario;

public class AOperariosController extends ControladorRomano {

	public static String URLVista = "/proy/gui/vistas/AOperarios.fxml";

	@FXML
	private TableView<Operario> tablaOperarios;

	@FXML
	private TableColumn<Operario, String> columnaNombre;

	@FXML
	private TableColumn<Operario, String> columnaApellido;

	@FXML
	private TableColumn<Operario, String> columnaDNI;

	@FXML
	private Button botonNuevo;

	@FXML
	private Button botonEliminar;

	@FXML
	private Button botonGuardar;

	@FXML
	private Button botonSalir;

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			columnaNombre.setCellValueFactory((CellDataFeatures<Operario, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getNombre());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
			columnaApellido.setCellValueFactory((CellDataFeatures<Operario, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getApellido());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
			columnaDNI.setCellValueFactory((CellDataFeatures<Operario, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getDNI());
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
