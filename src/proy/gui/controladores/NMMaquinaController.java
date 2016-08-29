package proy.gui.controladores;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;

public class NMMaquinaController extends ControladorRomano {

	public static String URLVista = "/proy/gui/vistas/NMMaquina.fxml";

	@FXML
	private TextField nombreMaquina;

	@FXML
	private TextField nombreParte;

	@FXML
	private TextField nombrePieza;

	@FXML
	private TableView<Parte> tablaPartes;

	@FXML
	private TableColumn<Parte, String> columnaNombreParte;

	@FXML
	private TableColumn<Parte, Number> columnaCantidadParte;

	@FXML
	private TableView<Pieza> tablaPiezas;

	@FXML
	private TableColumn<Pieza, String> columnaNombrePieza;

	@FXML
	private TableColumn<Pieza, Number> columnaCantidadPieza;

	@FXML
	private TableColumn<Pieza, String> columnaMaterialPieza;

	@FXML
	private TableColumn<Pieza, String> columnaCodigoPlanoPieza;

	@FXML
	private Button botonNuevaParte;

	@FXML
	private Button botonEliminarParte;

	@FXML
	private Button botonNuevaPieza;

	@FXML
	private Button botonEliminarPieza;

	@FXML
	private Button botonGuardar;

	@FXML
	private Button botonSalir;

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			columnaNombreParte.setCellValueFactory((CellDataFeatures<Parte, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getNombre());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
			columnaCantidadParte.setCellValueFactory((CellDataFeatures<Parte, Number> param) -> {
				if(param.getValue() != null){
					return new SimpleIntegerProperty(param.getValue().getCantidad());
				}
				else{
					return new SimpleIntegerProperty(-1);
				}
			});
			columnaNombrePieza.setCellValueFactory((CellDataFeatures<Pieza, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getNombre());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
			columnaCantidadPieza.setCellValueFactory((CellDataFeatures<Pieza, Number> param) -> {
				if(param.getValue() != null){
					return new SimpleIntegerProperty(param.getValue().getCantidad());
				}
				else{
					return new SimpleIntegerProperty(-1);
				}
			});
			columnaMaterialPieza.setCellValueFactory((CellDataFeatures<Pieza, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getMaterial().getNombre());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
			columnaCodigoPlanoPieza.setCellValueFactory((CellDataFeatures<Pieza, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getCodigoPlano());
				}
				else{
					return new SimpleStringProperty("<no name>");
				}
			});
		});
	}

	@FXML
	public void nuevaParte() {

	}

	@FXML
	public void eliminarParte() {

	}

	@FXML
	public void nuevaPieza() {

	}

	@FXML
	public void eliminarPieza() {

	}

	@FXML
	public void guardar() {

	}

	@Override
	public void actualizar() {

	}
}
