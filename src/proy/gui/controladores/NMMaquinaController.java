/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import proy.datos.entidades.Maquina;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.excepciones.PersistenciaException;
import proy.gui.PresentadorExcepciones;
import proy.gui.componentes.VentanaError;
import proy.gui.componentes.VentanaInformacion;
import proy.logica.gestores.filtros.FiltroParte;
import proy.logica.gestores.filtros.FiltroPieza;
import proy.logica.gestores.resultados.ResultadoCrearMaquina;
import proy.logica.gestores.resultados.ResultadoCrearMaquina.ErrorCrearMaquina;

public class NMMaquinaController extends ControladorRomano {

	public static final String URLVista = "/proy/gui/vistas/NMMaquina.fxml";

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

	private Maquina maquina;

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

			tablaPartes.getSelectionModel().selectedItemProperty().addListener((ovs, viejo, nuevo) -> {
				Platform.runLater(() -> {
					tablaPiezas.getItems().clear();
					try{
						if(nuevo != null){
							tablaPiezas.getItems().addAll(coordinador.listarPiezas(new FiltroPieza.Builder().parte(nuevo).build()));
						}
					} catch(PersistenciaException e){
						PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
					}
				});
			});

			actualizar();
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
		Boolean hayErrores;
		if(maquina == null){
			hayErrores = crearMaquina();
		}
		else{
			hayErrores = modificarMaquina();
		}
		if(hayErrores != null && !hayErrores){
			salir();
		}
	}

	private Boolean crearMaquina() {
		ResultadoCrearMaquina resultado;
		StringBuffer erroresBfr = new StringBuffer();
		Maquina maquina = new Maquina();

		//Toma de datos de la vista
		maquina.setNombre(nombreMaquina.getText().toLowerCase().trim());

		//Inicio transacciones al gestor
		try{
			resultado = coordinador.crearMaquina(maquina);
		} catch(PersistenciaException e){
			PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
			return true;
		} catch(Exception e){
			PresentadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
			return true;
		}

		//Tratamiento de errores
		if(resultado.hayErrores()){
			for(ErrorCrearMaquina e: resultado.getErrores()){
				switch(e) {
				case NombreIncompleto:
					erroresBfr.append("El nombre de la máquina está vacío.\n");
					break;
				case NombreRepetido:
					erroresBfr.append("Ya existe una máquina con ese nombre en la Base de Datos.\n");
					break;
				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				new VentanaError("Error al crear la máquina", errores, apilador.getStage());
			}
			return true;
		}
		else{
			new VentanaInformacion("Operación exitosa", "Se ha creado la máquina con éxito");
			return false;
		}
	}

	private Boolean modificarMaquina() {
		// TODO Auto-generated method stub
		return true;
	}

	public void formatearNuevaMaquina() {

	}

	public void formatearModificarMaquina(Maquina maquina) {
		this.maquina = maquina;
		nombreMaquina.setText(maquina.getNombre());
		actualizar();
	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			try{
				if(maquina != null){
					tablaPartes.getItems().clear();
					tablaPartes.getItems().addAll(coordinador.listarPartes(new FiltroParte.Builder().maquina(maquina).build()));
					tablaPiezas.getItems().clear();
				}
			} catch(PersistenciaException e){
				PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
			}
		});
	}
}
