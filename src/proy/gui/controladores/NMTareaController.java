package proy.gui.controladores;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import proy.datos.entidades.Operario;
import proy.datos.entidades.Proceso;

public class NMTareaController extends ControladorRomano {

	public static String URLVista = "/proy/gui/vistas/NMTarea.fxml";

	@FXML
	private TextField proceso;

	@FXML
	private TextField maquina;

	@FXML
	private TextField cantidad;

	@FXML
	private TextArea observaciones;

	@FXML
	private ComboBox<Operario> cbOperario;

	@FXML
	private DatePicker fechaTarea;

	@FXML
	private TableView<Proceso> tablaProcesos;

	@FXML
	private TableColumn<Proceso, String> columnaProceso;

	@FXML
	private TableColumn<Proceso, String> columnaMaquina;

	@FXML
	private TableColumn<Proceso, String> columnaParte;

	@FXML
	private Button botonBuscar;

	@FXML
	private Button botonAceptar;

	@FXML
	private Button botonCancelar;

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			columnaProceso.setCellValueFactory((CellDataFeatures<Proceso, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().toString());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
			columnaMaquina.setCellValueFactory((CellDataFeatures<Proceso, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getParte().getMaquina().getNombre());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
			columnaParte.setCellValueFactory((CellDataFeatures<Proceso, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getParte().getNombre());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
		});
	}

	@FXML
	public void guardarTarea() {

	}

	@Override
	public void actualizar() {

	}
}
