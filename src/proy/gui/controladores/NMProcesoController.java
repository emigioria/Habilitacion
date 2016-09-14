package proy.gui.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Maquina;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;

public class NMProcesoController extends ControladorRomano {

	public static String URLVista = "/proy/gui/vistas/NMProceso.fxml";

	@FXML
	private TextField tiempoTeoricoPreparacion;

	@FXML
	private TextField tiempoTeoricoProceso;

	@FXML
	private TextArea observacionesProceso;

	@FXML
	private ComboBox<Maquina> cbMaquina;

	@FXML
	private ComboBox<Parte> cbParte;

	@FXML
	private ComboBox<String> cbDescripcion;

	@FXML
	private ComboBox<String> cbTipo;

	@FXML
	private ComboBox<Pieza> cbPieza;

	@FXML
	private ComboBox<Herramienta> cbHerramienta;

	@FXML
	private ListView<Pieza> listaPiezas;

	@FXML
	private ListView<Herramienta> listaHerramientas;

	@FXML
	private Button botonAgregarPieza;

	@FXML
	private Button botonQuitarPieza;

	@FXML
	private Button botonAgregarHerramienta;

	@FXML
	private Button botonQuitarHerramienta;

	@FXML
	private Button botonGuardar;

	@FXML
	private Button botonSalir;

	@FXML
	public void agregarPieza() {

	}

	@FXML
	public void quitarPieza() {

	}

	@FXML
	public void agregarHerramienta() {

	}

	@FXML
	public void quitarHerramienta() {

	}

	@FXML
	public void guardar() {

	}

	@Override
	public void actualizar() {

	}
}