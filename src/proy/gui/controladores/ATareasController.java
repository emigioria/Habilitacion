package proy.gui.controladores;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import proy.datos.entidades.Tarea;
import proy.gui.ConversorFechas;

public class ATareasController extends ControladorRomano {

	public static String URLVista = "/proy/gui/vistas/ATareas.fxml";

	@FXML
	private TextField nombreOperario;

	@FXML
	private TextField fechaTarea;

	@FXML
	private TableView<Tarea> tablaTareas;

	@FXML
	private TableColumn<Tarea, String> columnaProceso;

	@FXML
	private TableColumn<Tarea, String> columnaMaquina;

	@FXML
	private TableColumn<Tarea, String> columnaParte;

	@FXML
	private TableColumn<Tarea, String> columnaOperario;

	@FXML
	private TableColumn<Tarea, String> columnaFecha;

	@FXML
	private Button botonBuscar;

	@FXML
	private Button botonNueva;

	@FXML
	private Button botonEliminar;

	@FXML
	private Button botonGuardar;

	@FXML
	private Button botonSalir;

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			columnaProceso.setCellValueFactory((CellDataFeatures<Tarea, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getProceso().toString());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
			columnaMaquina.setCellValueFactory((CellDataFeatures<Tarea, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getProceso().getParte().getMaquina().getNombre());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
			columnaParte.setCellValueFactory((CellDataFeatures<Tarea, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getProceso().getParte().getNombre());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
			columnaOperario.setCellValueFactory((CellDataFeatures<Tarea, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getOperario().toString());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
			columnaFecha.setCellValueFactory((CellDataFeatures<Tarea, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(ConversorFechas.diaMesYAnioToString(param.getValue().getFechaPlanificada()));
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
		});
	}

	@FXML
	public void buscar() {

	}

	@FXML
	public void nuevaTarea() {

	}

	@FXML
	public void eliminarTarea() {

	}

	@FXML
	public void guardarTarea() {

	}

	@Override
	public void actualizar() {

	}
}
