/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.util.Date;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
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
import proy.datos.clases.EstadoTareaStr;
import proy.datos.entidades.EstadoTarea;
import proy.datos.entidades.Operario;
import proy.datos.entidades.Proceso;
import proy.datos.entidades.Tarea;
import proy.datos.filtros.implementacion.FiltroOperario;
import proy.datos.filtros.implementacion.FiltroProceso;
import proy.excepciones.PersistenciaException;
import proy.gui.ControladorRomano;
import proy.logica.gestores.resultados.ResultadoCrearTarea;
import proy.logica.gestores.resultados.ResultadoCrearTarea.ErrorCrearTarea;
import proy.logica.gestores.resultados.ResultadoModificarTarea;
import proy.logica.gestores.resultados.ResultadoModificarTarea.ErrorModificarTarea;

public class NMTareaController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/NMTarea.fxml";

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

	private Tarea tarea;

	@FXML
	public void buscar() {
		//TODO buscar bien procesos
		tablaProcesos.getItems().clear();
		try{
			tablaProcesos.getItems().addAll(coordinador.listarProcesos(new FiltroProceso.Builder().build()));
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
		}
	}

	@Override
	protected void inicializar() {
		columnaProceso.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaMaquina.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().getParte().getMaquina().getNombre().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaParte.setCellValueFactory((CellDataFeatures<Proceso, String> param) -> {
			try{
				return new SimpleStringProperty(param.getValue().getParte().getNombre().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		cantidad.getEditor().setTextFormatter(new TextFormatter<>(
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
		fechaTarea.setValue(conversorFechas.getLocalDate(new Date()));
		actualizar();
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
		if(fechaTarea.getValue() == null){
			presentadorVentanas.presentarError("Error al crear tarea", "La fecha no debe estar vacía", stage);
			return true;
		}

		tar = new Tarea();
		tar.setCantidadTeorica(cantidad.getValue());
		tar.setEstado(new EstadoTarea(EstadoTareaStr.PLANIFICADA));
		tar.setFechaPlanificada(conversorFechas.getDate(fechaTarea.getValue()));
		tar.setObservaciones(observaciones.getText().trim());
		tar.setOperario(cbOperario.getValue());
		tar.setProceso(tablaProcesos.getSelectionModel().getSelectedItem());

		//Inicio transacción al gestor
		try{
			resultado = coordinador.crearTarea(tar);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return true;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
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
				presentadorVentanas.presentarError("Error al crear tarea", errores, stage);
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
		if(fechaTarea.getValue() == null){
			presentadorVentanas.presentarError("Error al crear tarea", "La fecha no debe estar vacía", stage);
			return true;
		}

		tar = this.tarea;
		tar.setCantidadTeorica(cantidad.getValue());
		tar.setEstado(new EstadoTarea(EstadoTareaStr.PLANIFICADA));
		tar.setFechaPlanificada(conversorFechas.getDate(fechaTarea.getValue()));
		tar.setObservaciones(observaciones.getText());
		tar.setOperario(cbOperario.getValue());
		tar.setProceso(tablaProcesos.getSelectionModel().getSelectedItem());

		//Inicio transacción al gestor
		try{
			resultado = coordinador.modificarTarea(tar);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return true;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
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
				presentadorVentanas.presentarError("Error al modificar tarea", errores, stage);
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
		fechaTarea.setValue(conversorFechas.getLocalDate(tarea.getFechaPlanificada()));
		observaciones.setText(tarea.getObservaciones());
	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			try{
				cbOperario.getItems().clear();
				cbOperario.getItems().addAll(coordinador.listarOperarios(new FiltroOperario.Builder().build()));
				tablaProcesos.getItems().clear();
				tablaProcesos.getItems().addAll(coordinador.listarProcesos(new FiltroProceso.Builder().build()));
			} catch(PersistenciaException e){

			}
		});
	}
}
