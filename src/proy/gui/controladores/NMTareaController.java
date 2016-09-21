package proy.gui.controladores;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;
import proy.datos.clases.EstadoTarea;
import proy.datos.entidades.Operario;
import proy.datos.entidades.Proceso;
import proy.datos.entidades.Tarea;
import proy.excepciones.ManejadorExcepciones;
import proy.excepciones.PersistenciaException;
import proy.gui.ConversorFechas;
import proy.gui.componentes.VentanaError;
import proy.logica.gestores.filtros.FiltroOperario;
import proy.logica.gestores.filtros.FiltroProceso;
import proy.logica.gestores.resultados.ResultadoCrearTarea;
import proy.logica.gestores.resultados.ResultadoCrearTarea.ErrorCrearTarea;
import proy.logica.gestores.resultados.ResultadoModificarTarea;
import proy.logica.gestores.resultados.ResultadoModificarTarea.ErrorModificarTarea;

public class NMTareaController extends ControladorRomano {

	public static String URLVista = "/proy/gui/vistas/NMTarea.fxml";

	@FXML
	private Label lbTitulo;

	@FXML
	private TextField proceso;

	@FXML
	private TextField maquina;

	@FXML
	private Spinner<Integer> cantidad;

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

	private Tarea tarea;

	@FXML
	public void buscar() {
		//TODO buscar bien procesos
		tablaProcesos.getItems().clear();
		try{
			tablaProcesos.getItems().addAll(coordinador.listarProcesos(new FiltroProceso()));
		} catch(PersistenciaException e){
			ManejadorExcepciones.presentarExcepcion(e, apilador.getStage());
		}
	}

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
			cantidad.getEditor().setTextFormatter(new TextFormatter<Integer>(
					new IntegerStringConverter(), 0,
					c -> {
						if(c.isContentChange()){
							Integer numeroIngresado = null;
							try{
								numeroIngresado = new Integer(c.getControlNewText());
							} catch(Exception e){
								//No ingreso un entero;
							}
							if(numeroIngresado == null){
								return null;
							}
						}
						return c;
					}));
			cantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 0));
			actualizar();
		});
	}

	@FXML
	public void guardarTarea() {
		Boolean hayErrores = true;
		if(tarea == null){
			hayErrores = crearTarea();
		}
		else{
			hayErrores = modificarTarea();
		}
		if(!hayErrores){
			salir();
		}
	}

	public Boolean crearTarea() {
		ResultadoCrearTarea resultado = null;
		Boolean hayErrores;
		Tarea tar = null;
		String errores = "";

		//Toma de datos de la vista
		tar = new Tarea();
		tar.setCantidadTeorica(cantidad.getValue());
		tar.setEstado(EstadoTarea.PLANIFICADA);
		tar.setFechaPlanificada(ConversorFechas.getDate(fechaTarea.getValue()));
		tar.setObservaciones(observaciones.getText());
		tar.setOperario(cbOperario.getValue());
		tar.setProceso(tablaProcesos.getSelectionModel().getSelectedItem());

		//Inicio transacción al gestor
		try{
			resultado = coordinador.crearTarea(tar);
		} catch(PersistenciaException e){
			ManejadorExcepciones.presentarExcepcion(e, apilador.getStage());
			return true;
		} catch(Exception e){
			ManejadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
			return true;
		}

		//Tratamiento de errores
		hayErrores = resultado.hayErrores();
		if(hayErrores){
			for(ErrorCrearTarea r: resultado.getErrores()){
				switch(r) {
				//TODO hacer validador primero
				default: //no usar default
					//ejemplo de error
					errores += "El nombre no es válido.\n";
				}
			}
			if(!errores.isEmpty()){
				new VentanaError("Error al crear tarea", errores, apilador.getStage());
			}
		}

		return hayErrores;
	}

	public Boolean modificarTarea() {
		ResultadoModificarTarea resultado = null;
		Boolean hayErrores;
		Tarea tar = null;
		String errores = "";

		//Toma de datos de la vista
		tar = this.tarea;
		tar.setCantidadTeorica(cantidad.getValue());
		tar.setEstado(EstadoTarea.PLANIFICADA);
		tar.setFechaPlanificada(ConversorFechas.getDate(fechaTarea.getValue()));
		tar.setObservaciones(observaciones.getText());
		tar.setOperario(cbOperario.getValue());
		tar.setProceso(tablaProcesos.getSelectionModel().getSelectedItem());

		//Inicio transacción al gestor
		try{
			resultado = coordinador.modificarTarea(tar);
		} catch(PersistenciaException e){
			ManejadorExcepciones.presentarExcepcion(e, apilador.getStage());
			return true;
		} catch(Exception e){
			ManejadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
			return true;
		}

		//Tratamiento de errores
		hayErrores = resultado.hayErrores();
		if(hayErrores){
			for(ErrorModificarTarea r: resultado.getErrores()){
				switch(r) {
				default: //ejemplo, no usar default
					errores += "El nombre no es válido.\n";
					break;
				}
			}
			if(!errores.isEmpty()){
				new VentanaError("Error al modificar tarea", errores, apilador.getStage());
			}
		}

		return hayErrores;
	}

	public void formatearNuevaTarea() {
		lbTitulo.setText("Nueva tarea");
	}

	public void formatearModificarTarea(Tarea tarea) {
		lbTitulo.setText("Modificar tarea");
		this.tarea = tarea;
		cargarDatos(tarea);
	}

	public void cargarDatos(Tarea tarea) {
		tablaProcesos.getSelectionModel().select(tarea.getProceso());
		cantidad.getValueFactory().setValue(tarea.getCantidadTeorica());
		cbOperario.getSelectionModel().select(tarea.getOperario());
		fechaTarea.setValue(ConversorFechas.getLocalDate(tarea.getFechaPlanificada()));
		observaciones.setText(tarea.getObservaciones());
	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			try{
				cbOperario.getItems().clear();
				cbOperario.getItems().addAll(coordinador.listarOperarios(new FiltroOperario()));
				tablaProcesos.getItems().clear();
				tablaProcesos.getItems().addAll(coordinador.listarProcesos(new FiltroProceso()));
			} catch(PersistenciaException e){

			}
		});
	}
}
