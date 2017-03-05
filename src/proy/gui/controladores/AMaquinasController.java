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
import proy.datos.entidades.Maquina;
import proy.datos.filtros.implementacion.FiltroMaquina;
import proy.excepciones.PersistenciaException;
import proy.gui.componentes.ventanas.VentanaConfirmacion;
import proy.logica.gestores.resultados.ResultadoEliminarMaquina;
import proy.logica.gestores.resultados.ResultadoEliminarMaquina.ErrorEliminarMaquina;

public class AMaquinasController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/AMaquinas.fxml";

	@FXML
	private TextField nombreMaquina;

	@FXML
	private TableView<Maquina> tablaMaquinas;

	@FXML
	private TableColumn<Maquina, String> columnaNombre;

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			columnaNombre.setCellValueFactory(param -> {
				if(param.getValue() != null){
					if(param.getValue().getNombre() != null){
						return new SimpleStringProperty(formateadorString.primeraMayuscula(param.getValue().getNombre()));
					}
				}
				return new SimpleStringProperty("<Sin nombre>");
			});

			actualizar();
		});
	}

	@FXML
	public void nuevaMaquina() {
		NMMaquinaController nuevaPantalla = (NMMaquinaController) ControladorRomano.nuevaScene(NMMaquinaController.URL_VISTA, apilador, coordinador);
		nuevaPantalla.formatearNuevaMaquina();
	}

	@FXML
	public void modificarMaquina() {
		Maquina maquina = tablaMaquinas.getSelectionModel().getSelectedItem();
		if(maquina != null){
			NMMaquinaController nuevaPantalla = (NMMaquinaController) ControladorRomano.nuevaScene(NMMaquinaController.URL_VISTA, apilador, coordinador);
			nuevaPantalla.formatearModificarMaquina(maquina);
		}
	}

	@FXML
	public void eliminarMaquina() {
		ResultadoEliminarMaquina resultado;
		StringBuffer erroresBfr = new StringBuffer();
		Maquina maquina = tablaMaquinas.getSelectionModel().getSelectedItem();

		if(maquina == null){
			return;
		}
		//se pregunta al usuario si desea confirmar la elininación de la máquina
		VentanaConfirmacion vc = presentadorVentanas.presentarConfirmacion("Confirmar eliminar máquina",
				"Se eliminará la máquina <" + maquina.getNombre() + "> y sus componentes de forma permanente.\n" +
						"¿Está seguro de que desea continuar?",
				apilador.getStage());
		if(!vc.acepta()){
			return;
		}
		vc = presentadorVentanas.presentarConfirmacion("Confirmar eliminar máquina",
				"Se eliminará la máquina <" + maquina.getNombre() + "> y sus procesos y tareas de forma permanente.\n" +
						"¿Está seguro de que desea continuar?",
				apilador.getStage());
		if(!vc.acepta()){
			return;
		}
		vc = presentadorVentanas.presentarConfirmacion("Confirmar eliminar máquina",
				"Se eliminará la máquina <" + maquina.getNombre() + "> y no se podrá recuperar.\n" +
						"¿Está seguro de que desea continuar?",
				apilador.getStage());
		if(!vc.acepta()){
			return;
		}

		//Inicio transacciones al gestor
		try{
			resultado = coordinador.eliminarMaquina(maquina);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, apilador.getStage());
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, apilador.getStage());
			return;
		}

		//Tratamiento de errores
		if(resultado.hayErrores()){
			for(ErrorEliminarMaquina e: resultado.getErrores()){
				switch(e) {
				//no hay errores de eliminar maquina por ahora
				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				presentadorVentanas.presentarError("Error al eliminar la máquina", errores, apilador.getStage());
			}
		}
		else{
			tablaMaquinas.getItems().remove(maquina);
			presentadorVentanas.presentarInformacion("Operación exitosa", "Se ha eliminado la máquina con éxito", apilador.getStage());
		}
	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			try{
				tablaMaquinas.getItems().clear();
				tablaMaquinas.getItems().addAll(coordinador.listarMaquinas(new FiltroMaquina.Builder().build()));
			} catch(PersistenciaException e){
				presentadorVentanas.presentarExcepcion(e, apilador.getStage());
			}
		});
	}
}
