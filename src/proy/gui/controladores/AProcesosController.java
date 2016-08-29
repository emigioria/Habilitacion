package proy.gui.controladores;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import proy.datos.entidades.Proceso;

public class AProcesosController extends ControladorRomano {

	public static String URLVista = "/proy/gui/vistas/AProcesos.fxml";

	@FXML
	private TextField nombreProceso;

	@FXML
	private TableView<Proceso> tablaProcesos;

	@FXML
	private TableColumn<Proceso, String> columnaMaquina;

	@FXML
	private TableColumn<Proceso, String> columnaParte;

	@FXML
	private TableColumn<Proceso, String> columnaDescripcion;

	@FXML
	private TableColumn<Proceso, String> columnaTipo;

	@FXML
	private TableColumn<Proceso, String> columnaTiempoPreparacion;

	@FXML
	private TableColumn<Proceso, String> columnaTiempoProceso;

	@FXML
	private Button botonNueva;

	@FXML
	private Button botonEliminar;

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
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
			columnaDescripcion.setCellValueFactory((CellDataFeatures<Proceso, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getDescripcion());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
			columnaTipo.setCellValueFactory((CellDataFeatures<Proceso, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getTipo());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
			columnaTiempoPreparacion.setCellValueFactory((CellDataFeatures<Proceso, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getTiempoTeoricoPreparacion());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
			columnaTiempoProceso.setCellValueFactory((CellDataFeatures<Proceso, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getTiempoTeoricoProceso());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
		});
	}

	@FXML
	public void nuevoProceso() {

	}

	@FXML
	public void eliminarProceso() {

	}

	@Override
	public void actualizar() {

	}
}
