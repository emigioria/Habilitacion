package proy.gui.controladores;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import proy.datos.entidades.Material;
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
	private TitledPane panelTarea;

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

			panelTarea.setExpanded(true);
			tablaPiezas.getItems().add(new Pieza("Hola", null, null, null, null, new Material("Chau", null)));

			(new Thread(() -> {
				try{
					Thread.sleep(100);
				} catch(Exception e){
				}
				Platform.runLater(() -> {
					TableRow<?> tableRow = (TableRow<?>) tablaPiezas.lookup("TableRow");
					tablaPiezas.minHeightProperty().bind(new SimpleIntegerProperty(1).multiply(tableRow.getHeight()).add(((Pane) tablaPiezas.lookup("TableHeaderRow")).getHeight() + 12));
					tablaPiezas.prefHeightProperty().bind(Bindings.size(tablaPiezas.getItems()).multiply(tableRow.getHeight()).add(((Pane) tablaPiezas.lookup("TableHeaderRow")).getHeight() + 12));
					tablaPiezas.maxHeightProperty().bind(Bindings.size(tablaPiezas.getItems()).multiply(tableRow.getHeight()).add(((Pane) tablaPiezas.lookup("TableHeaderRow")).getHeight() + 12));
					tablaPiezas.getItems().clear();
				});
			})).start();
		});
	}

	@Override
	public void actualizar() {

	}

}
