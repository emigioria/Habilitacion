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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import proy.datos.entidades.Proceso;
import proy.datos.filtros.implementacion.FiltroProceso;
import proy.excepciones.PersistenciaException;
import proy.gui.ControladorRomano;
import proy.gui.componentes.ventanas.VentanaConfirmacion;
import proy.logica.gestores.resultados.ResultadoEliminarProceso;
import proy.logica.gestores.resultados.ResultadoEliminarProceso.ErrorEliminarProceso;

public class AProcesosController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/AProcesos.fxml";

	@FXML
	private TextField nombreProceso;

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
				return new SimpleStringProperty(param.getValue().getParte().getMaquina().getNombre());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaParte.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().getParte().getNombre());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaDescripcion.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().getDescripcion());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaTipo.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().getTipo());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaTiempoPreparacion.setCellValueFactory(param -> {
			try{
				long milisTTP = param.getValue().getTiempoTeoricoPreparacion().longValue();
				long segsTTP = (milisTTP / 1000) % 60;
				long minsTTP = (milisTTP / 60000) % 60;
				long horasTTP = milisTTP / 3600000;
				return new SimpleStringProperty(horasTTP + "hs " + minsTTP + "ms " + segsTTP + "ss");
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaTiempoProceso.setCellValueFactory(param -> {
			try{
				long milisTTP = param.getValue().getTiempoTeoricoProceso().longValue();
				long segsTTP = (milisTTP / 1000) % 60;
				long minsTTP = (milisTTP / 60000) % 60;
				long horasTTP = milisTTP / 3600000;
				return new SimpleStringProperty(horasTTP + "hs " + minsTTP + "ms " + segsTTP + "ss");
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
	}

	@FXML
	public void nuevoProceso() {
		NMProcesoController nuevaPantalla = (NMProcesoController) this.nuevaScene(NMProcesoController.URL_VISTA);
		nuevaPantalla.formatearNuevoProceso();
	}

	@FXML
	public void modificarProceso() {
		Proceso proceso = tablaProcesos.getSelectionModel().getSelectedItem();
		if(proceso != null){
			NMProcesoController nuevaPantalla = (NMProcesoController) this.nuevaScene(NMProcesoController.URL_VISTA);
			nuevaPantalla.formatearModificarProceso(proceso);
		}
	}

	@FXML
	public void eliminarProceso() {
		ResultadoEliminarProceso resultado;
		StringBuffer erroresBfr = new StringBuffer();
		Proceso proceso = tablaProcesos.getSelectionModel().getSelectedItem();

		if(proceso == null){
			return;
		}

		//se pregunta al usuario si desea confirmar la eliminación del proceso
		VentanaConfirmacion vc = presentadorVentanas.presentarConfirmacion("Confirmar eliminar proceso",
				"Se eliminará el proceso <" + proceso + "> y sus componentes de forma permanente.\n" +
						"¿Está seguro de que desea continuar?",
				stage);
		if(!vc.acepta()){
			return;
		}

		//Inicio transacciones al gestor
		try{
			resultado = coordinador.eliminarProceso(proceso);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}

		//Tratamiento de errores
		if(resultado.hayErrores()){
			for(ErrorEliminarProceso e: resultado.getErrores()){
				switch(e) {
				//no hay errores de eliminar maquina por ahora
				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				presentadorVentanas.presentarError("Error al eliminar el proceso", errores, stage);
			}
		}
		else{
			tablaProcesos.getItems().remove(proceso);
			presentadorVentanas.presentarInformacion("Operación exitosa", "Se ha eliminado el proceso con éxito", stage);
		}
	}

	public void buscar() {
		String nombreBuscado = nombreProceso.getText().trim().toLowerCase();
		tablaProcesos.getItems().clear();
		try{
			tablaProcesos.getItems().addAll(coordinador.listarProcesos(new FiltroProceso.Builder().build()));
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
		}
	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			stage.setTitle("Lista de procesos");

			tablaProcesos.getItems().clear();
			try{
				tablaProcesos.getItems().addAll(coordinador.listarProcesos(new FiltroProceso.Builder().build()));
			} catch(PersistenciaException e){
				presentadorVentanas.presentarExcepcion(e, stage);
			}
		});
	}
}
