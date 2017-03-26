/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import proy.datos.entidades.Maquina;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Proceso;
import proy.datos.filtros.implementacion.FiltroMaquina;
import proy.datos.filtros.implementacion.FiltroParte;
import proy.datos.filtros.implementacion.FiltroProceso;
import proy.excepciones.PersistenciaException;
import proy.gui.ControladorRomano;
import proy.gui.componentes.ventanas.VentanaConfirmacion;
import proy.logica.gestores.resultados.ResultadoEliminarProceso;
import proy.logica.gestores.resultados.ResultadoEliminarProceso.ErrorEliminarProceso;
import proy.logica.gestores.resultados.ResultadoEliminarTareas;
import proy.logica.gestores.resultados.ResultadoEliminarTareas.ErrorEliminarTareas;

public class AProcesosController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/AProcesos.fxml";

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
	private TextField descripcionProceso;

	@FXML
	private TextField tipoProceso;

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

	@Override
	protected void inicializar() {
		columnaMaquina.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().getParte().getMaquina().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaParte.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().getParte().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaDescripcion.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(formateadorString.primeraMayuscula(param.getValue().getDescripcion().toString()));
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaTipo.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(formateadorString.primeraMayuscula(param.getValue().getTipo().toString()));
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaTiempoPreparacion.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(conversorTiempos.milisAHsMsSsConTexto(param.getValue().getTiempoTeoricoPreparacion().longValue()));
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaTiempoProceso.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(conversorTiempos.milisAHsMsSsConTexto(param.getValue().getTiempoTeoricoProceso().longValue()));
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

		actualizar();
	}

	@FXML
	private void nuevoProceso() {
		NMProcesoController nuevaPantalla = (NMProcesoController) this.nuevaScene(NMProcesoController.URL_VISTA);
		nuevaPantalla.formatearNuevoProceso();
	}

	@FXML
	private void modificarProceso() {
		Proceso proceso = tablaProcesos.getSelectionModel().getSelectedItem();
		if(proceso != null){
			NMProcesoController nuevaPantalla = (NMProcesoController) this.nuevaScene(NMProcesoController.URL_VISTA);
			nuevaPantalla.formatearModificarProceso(proceso);
		}
	}

	@FXML
	private void eliminarProceso() {
		ResultadoEliminarProceso resultadoEliminarProceso;
		StringBuffer erroresBfr = new StringBuffer();
		Proceso procesoAEliminar = tablaProcesos.getSelectionModel().getSelectedItem();

		if(procesoAEliminar == null){
			return;
		}

		//Se le pide al usuario que confirme la eliminación del proceso
		VentanaConfirmacion vc = presentadorVentanas.presentarConfirmacion("Confirmar eliminar proceso",
				"Se eliminará el proceso <" + procesoAEliminar + "> y sus componentes de forma permanente.\n" +
						"¿Está seguro de que desea continuar?",
				stage);
		if(!vc.acepta()){
			return;
		}

		//Si acepta dar de baja se verifica que el proceso a eliminar no tiene tareas no terminadas asociadas
		Boolean tieneTareasNoTerminadasAsociadas = false;
		try{
			tieneTareasNoTerminadasAsociadas = coordinador.tieneTareasNoTerminadasAsociadas(procesoAEliminar);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}

		//Se pregunta si quiere dar de baja estas tareas asociadas
		if(tieneTareasNoTerminadasAsociadas){
			vc = presentadorVentanas.presentarConfirmacion("Confirmar eliminar proceso",
					"El proceso <" + procesoAEliminar + "> tiene tareas no terminadas asociadas.\nSi continúa, esas tareas se eliminarán\n¿Está seguro que desea eliminarlo?",
					stage);
			if(!vc.acepta()){
				return;
			}
		}

		//Inicio transacciones al gestor
		try{
			resultadoEliminarProceso = coordinador.eliminarProceso(procesoAEliminar);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}

		//Tratamiento de errores
		if(resultadoEliminarProceso.hayErrores()){
			for(ErrorEliminarProceso e: resultadoEliminarProceso.getErrores()){
				switch(e) {
				case ERROR_AL_ELIMINAR_TAREAS:
					ResultadoEliminarTareas resultadoTareas = resultadoEliminarProceso.getResultadoEliminarTareas();
					if(resultadoTareas.hayErrores()){
						for(ErrorEliminarTareas eet: resultadoTareas.getErrores()){
							switch(eet) {
							case HAY_TAREA_FINALIZADA:
								throw new RuntimeException();
							}
						}
					}
					break;
				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				presentadorVentanas.presentarError("Error al eliminar el proceso", errores, stage);
			}
		}
		else{
			tablaProcesos.getItems().remove(procesoAEliminar);
			presentadorVentanas.presentarToast("Se ha eliminado el proceso <" + procesoAEliminar + ">con éxito", stage);
		}
	}

	@FXML
	private void buscar() {
		Maquina maquinaBuscada = cbMaquina.getValue();
		if(maquinaBuscada == nullMaquina){
			maquinaBuscada = null;
		}
		Parte parteBuscada = cbParte.getValue();
		if(parteBuscada == nullParte){
			parteBuscada = null;
		}
		String descripcionBuscada = descripcionProceso.getText().trim().toLowerCase();
		String tipoBuscado = tipoProceso.getText().trim().toLowerCase();
		tablaProcesos.getItems().clear();
		try{
			tablaProcesos.getItems().addAll(coordinador.listarProcesos(new FiltroProceso.Builder()
					.parte(parteBuscada)
					.descripcionContiene(descripcionBuscada)
					.tipoContiene(tipoBuscado)
					.maquina(maquinaBuscada)
					.build()));
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
		}
	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			stage.setTitle("Lista de procesos");

			tablaProcesos.getItems().clear();

			cbMaquina.getItems().clear();
			cbMaquina.getItems().add(nullMaquina);

			cbParte.getItems().clear();
			cbParte.getItems().add(nullParte);

			try{
				tablaProcesos.getItems().addAll(coordinador.listarProcesos(new FiltroProceso.Builder().build()));

				cbMaquina.getItems().addAll(coordinador.listarMaquinas(new FiltroMaquina.Builder().build()));
			} catch(PersistenciaException e){
				presentadorVentanas.presentarExcepcion(e, stage);
			}
		});
	}
}
