/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.util.Date;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import proy.datos.clases.EstadoTareaStr;
import proy.datos.entidades.Maquina;
import proy.datos.entidades.Operario;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Tarea;
import proy.datos.filtros.implementacion.FiltroMaquina;
import proy.datos.filtros.implementacion.FiltroOperario;
import proy.datos.filtros.implementacion.FiltroParte;
import proy.datos.filtros.implementacion.FiltroTarea;
import proy.excepciones.PersistenciaException;
import proy.gui.ControladorRomano;
import proy.gui.componentes.ventanas.VentanaConfirmacion;
import proy.logica.gestores.resultados.ResultadoEliminarTarea;
import proy.logica.gestores.resultados.ResultadoEliminarTarea.ErrorEliminarTarea;

public class ATareasController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/ATareas.fxml";

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

	@FXML
	private TableColumn<Tarea, String> columnaEstado;

	@FXML
	private ComboBox<Operario> cbOperario;

	@FXML
	private Button botonModificar;

	@FXML
	private Button botonEliminar;

	private Operario nullOperario = new Operario() {
		@Override
		public String toString() {
			return "Operario";
		}
	};

	@FXML
	private ComboBox<Maquina> cbMaquina;

	private Maquina nullMaquina = new Maquina() {
		@Override
		public String toString() {
			return "Máquina";
		}
	};

	@FXML
	private ComboBox<Parte> cbParte;

	private Parte nullParte = new Parte() {
		@Override
		public String toString() {
			return "Parte";
		}
	};

	@FXML
	private DatePicker dpDespuesDe;

	@FXML
	private DatePicker dpAntesDe;

	@Override
	protected void inicializar() {
		columnaProceso.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().getProceso().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaMaquina.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().getProceso().getParte().getMaquina().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaParte.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().getProceso().getParte().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaOperario.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().getOperario().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaCantidad.setCellValueFactory(param -> {
			try{
				return new SimpleIntegerProperty(param.getValue().getCantidadTeorica().intValue());
			} catch(NullPointerException e){
				return new SimpleIntegerProperty(-1);
			}
		});
		columnaFecha.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(conversorFechas.diaMesYAnioToString(param.getValue().getFechaPlanificada()));
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaEstado.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().getEstado().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		cbMaquina.getSelectionModel().selectedItemProperty().addListener((obs, olvV, newV) -> {
			cbParte.getItems().clear();
			cbParte.getItems().add(nullParte);
			if(newV != null && newV != nullMaquina){
				try{
					cbParte.getItems().addAll(coordinador.listarPartes(new FiltroParte.Builder().maquina(newV).build()));
				} catch(PersistenciaException e){
					presentadorVentanas.presentarExcepcion(e, stage);
				}
			}
		});

		tablaTareas.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
			if(newV != null){
				botonEliminar.setDisable(!EstadoTareaStr.PLANIFICADA.equals(newV.getEstado().getNombre()));
				botonModificar.setDisable(!EstadoTareaStr.PLANIFICADA.equals(newV.getEstado().getNombre()));
			}
		});
		actualizar();
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
		ResultadoEliminarTarea resultado;
		StringBuffer erroresBfr = new StringBuffer();

		//Toma de datos de la vista
		Tarea tarea = tablaTareas.getSelectionModel().getSelectedItem();
		if(tarea == null){
			return;
		}

		//Se verifica que sea una tarea planificada
		if(!EstadoTareaStr.PLANIFICADA.equals(tarea.getEstado().getNombre())){
			presentadorVentanas.presentarError("Error al eliminar la tarea", "La tarea seleccionada ya fue empezada por un operario, por lo que no se puede eliminar.\n", stage);
			return;
		}

		//se pregunta al usuario si desea confirmar la eliminación de la tarea
		VentanaConfirmacion vc = presentadorVentanas.presentarConfirmacion("Confirmar eliminar tarea",
				"Se eliminará la tarea seleccionada de forma permanente.\n" +
						"¿Está seguro de que desea continuar?",
				stage);
		if(!vc.acepta()){
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
		if(resultado.hayErrores()){
			for(ErrorEliminarTarea e: resultado.getErrores()){
				switch(e) {
				case TAREA_NO_PLANIFICADA:
					erroresBfr.append("La tarea seleccionada ya fue empezada por un operario, por lo que no se puede eliminar.\n");
					break;
				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				presentadorVentanas.presentarError("Error al eliminar la tarea", errores, stage);
			}
		}
		else{
			tablaTareas.getItems().remove(tarea);
			presentadorVentanas.presentarToast("Se ha eliminado la tarea con éxito", stage);
		}
	}

	@FXML
	public void verHistorialDeTareas() {
		this.nuevaScene(VHistorialTareasController.URL_VISTA);
	}

	@FXML
	public void buscar() {
		Operario operarioBuscado = cbOperario.getValue();
		if(operarioBuscado == nullOperario){
			operarioBuscado = null;
		}
		Maquina maquinaBuscada = cbMaquina.getValue();
		if(maquinaBuscada == nullMaquina){
			maquinaBuscada = null;
		}
		Parte parteBuscada = cbParte.getValue();
		if(parteBuscada == nullParte){
			parteBuscada = null;
		}
		Date fechaInicio = conversorFechas.getDate(dpDespuesDe.getValue());
		Date fechaFin = conversorFechas.getDate(dpAntesDe.getValue());

		tablaTareas.getItems().clear();
		try{
			tablaTareas.getItems().addAll(coordinador.listarTareas(new FiltroTarea.Builder()
					.operario(operarioBuscado)
					.maquina(maquinaBuscada)
					.parte(parteBuscada)
					.fechaPlanificadaInicio(fechaInicio)
					.fechaPlanificadaFin(fechaFin)
					.build()));
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
		}
	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			stage.setTitle("Lista de tareas");

			tablaTareas.getItems().clear();

			cbMaquina.getItems().clear();
			cbMaquina.getItems().add(nullMaquina);

			cbParte.getItems().clear();
			cbParte.getItems().add(nullParte);

			cbOperario.getItems().clear();
			cbOperario.getItems().add(nullOperario);

			try{
				tablaTareas.getItems().addAll(coordinador.listarTareas(new FiltroTarea.Builder().noEstado(EstadoTareaStr.FINALIZADA).build()));

				cbMaquina.getItems().addAll(coordinador.listarMaquinas(new FiltroMaquina.Builder().build()));
				cbOperario.getItems().addAll(coordinador.listarOperarios(new FiltroOperario.Builder().build()));
			} catch(PersistenciaException e){
				presentadorVentanas.presentarExcepcion(e, stage);
			}
		});
	}
}
