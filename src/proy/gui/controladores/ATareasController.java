/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import proy.datos.entidades.Tarea;
import proy.excepciones.PersistenciaException;
import proy.gui.ControladorRomano;
import proy.logica.gestores.resultados.ResultadoEliminarTarea;
import proy.logica.gestores.resultados.ResultadoEliminarTarea.ErrorEliminarTarea;

public class ATareasController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/ATareas.fxml";

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
	private TableColumn<Tarea, Number> columnaCantidad;

	@FXML
	private TableColumn<Tarea, String> columnaFecha;

	@Override
	protected void inicializar() {
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
		columnaCantidad.setCellValueFactory((CellDataFeatures<Tarea, Number> param) -> {
			if(param.getValue() != null){
				return new SimpleIntegerProperty(param.getValue().getCantidadTeorica());
			}
			else{
				return new SimpleIntegerProperty(-1);
			}
		});
		columnaFecha.setCellValueFactory((CellDataFeatures<Tarea, String> param) -> {
			if(param.getValue() != null){
				return new SimpleStringProperty(conversorFechas.diaMesYAnioToString(param.getValue().getFechaPlanificada()));
			}
			else{
				return new SimpleStringProperty("<no name>");
			}
		});
	}

	@FXML
	public void buscar() {
		/*
		 * FiltroTarea filtro = new FiltroTarea.Builder().(columnaMaquina.getText()).build();
		 * ArrayList<Herramienta> result = null;
		 * try{
		 * result = coordinador.listarHerramientas(filtro);
		 * } catch(PersistenciaException e){
		 * ManejadorExcepciones.presentarExcepcion(e, stage);
		 * }
		 * tablaHerramientas.getItems().clear();
		 * tablaHerramientas.getItems().addAll(result);
		 */

	}

	@FXML
	public void nuevaTarea() {
		NMTareaController nuevaPantalla = (NMTareaController) this.nuevaScene(NMTareaController.URL_VISTA);
		nuevaPantalla.formatearNuevaTarea();
	}

	@FXML
	public void modificarTarea() {
		Tarea tarea = tablaTareas.getSelectionModel().getSelectedItem();
		if(tarea != null){
			NMTareaController nuevaPantalla = (NMTareaController) this.nuevaScene(NMTareaController.URL_VISTA);
			nuevaPantalla.formatearModificarTarea(tarea);
		}
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
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
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
				presentadorVentanas.presentarError("No se ha podido eliminar la tarea", errores, stage);
			}
		}
		else{
			//Operacion exitosa
			tablaTareas.getItems().remove(tarea);
		}
	}

	@Override
	public void actualizar() {

	}
}
