package proy.gui.controladores;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import proy.datos.entidades.Tarea;
import proy.excepciones.ManejadorExcepciones;
import proy.excepciones.PersistenciaException;
import proy.gui.ConversorFechas;
import proy.gui.componentes.VentanaError;
import proy.logica.gestores.resultados.ResultadoEliminarTarea;
import proy.logica.gestores.resultados.ResultadoEliminarTarea.ErrorEliminarTarea;

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
		ResultadoEliminarTarea resultado = null;
		Boolean hayErrores;
		Tarea tarea;
		String errores = "";

		//Toma de datos de la vista
		tarea = tablaTareas.getSelectionModel().getSelectedItem();
		if(tarea == null){
			return;
		}

		//Inicio transacción al gestor
		try{
			resultado = coordinador.eliminarTarea(tarea);
		} catch(PersistenciaException e){
			ManejadorExcepciones.presentarExcepcion(e, apilador.getStage());
			return;
		} catch(Exception e){
			ManejadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
			return;
		}

		//Tratamiento de errores
		hayErrores = resultado.hayErrores();
		if(hayErrores){
			for(ErrorEliminarTarea r: resultado.getErrores()){
				switch(r) {
				default: //ejemplo, no usar default
					errores += "No se ha encontrado la tarea buscada.\n";
					break;
				}
			}
			if(!errores.isEmpty()){
				new VentanaError("No se ha podido eliminar la tarea", errores, apilador.getStage());
			}
		}
		else{
			//Operacion exitosa
			tablaTareas.getItems().remove(tarea);
		}
	}

	@FXML
	public void guardarTarea() {

	}

	@Override
	public void actualizar() {

	}
}
