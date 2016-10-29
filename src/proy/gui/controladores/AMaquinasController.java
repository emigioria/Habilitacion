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
import proy.comun.FormateadorString;
import proy.datos.entidades.Maquina;
import proy.excepciones.PersistenciaException;
import proy.gui.PresentadorExcepciones;
import proy.gui.componentes.VentanaConfirmacion;
import proy.gui.componentes.VentanaError;
import proy.gui.componentes.VentanaInformacion;
import proy.logica.gestores.filtros.FiltroMaquina;
import proy.logica.gestores.resultados.ResultadoEliminarMaquina;
import proy.logica.gestores.resultados.ResultadoEliminarMaquina.ErrorEliminarMaquina;

public class AMaquinasController extends ControladorRomano {

	public static final String URLVista = "/proy/gui/vistas/AMaquinas.fxml";

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
						return new SimpleStringProperty(FormateadorString.primeraMayuscula(param.getValue().getNombre()));
					}
				}
				return new SimpleStringProperty("<Sin nombre>");
			});

			actualizar();
		});
	}

	@FXML
	public void nuevaMaquina() {
		NMMaquinaController nuevaPantalla = (NMMaquinaController) ControladorRomano.nuevaScene(NMMaquinaController.URLVista, apilador, coordinador);
		nuevaPantalla.formatearNuevaMaquina();
	}

	@FXML
	public void modificarMaquina() {
		Maquina maquina = tablaMaquinas.getSelectionModel().getSelectedItem();
		if(maquina != null){
			NMMaquinaController nuevaPantalla = (NMMaquinaController) ControladorRomano.nuevaScene(NMMaquinaController.URLVista, apilador, coordinador);
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
		VentanaConfirmacion vc = new VentanaConfirmacion("Confirmación eliminar máquina",
				"Se eliminará la máquina <" + maquina.getNombre() + "> y sus componentes de forma permanente.\n" +
						"¿Está seguro de que desea continuar?",
				apilador.getStage());
		if(!vc.acepta()){
			return;
		}

		//Inicio transacciones al gestor
		try{
			resultado = coordinador.eliminarMaquina(maquina);
		} catch(PersistenciaException e){
			PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
			return;
		} catch(Exception e){
			PresentadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
			return;
		}

		//Tratamiento de errores
		if(resultado.hayErrores()){
			for(ErrorEliminarMaquina e: resultado.getErrores()){
				switch(e) {

				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				new VentanaError("Error al eliminar la máquina", errores, apilador.getStage());
			}
		}
		else{
			new VentanaInformacion("Operación exitosa", "Se ha eliminado la máquina con éxito");
		}
	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			try{
				tablaMaquinas.getItems().clear();
				tablaMaquinas.getItems().addAll(coordinador.listarMaquinas(new FiltroMaquina.Builder().build()));
			} catch(PersistenciaException e){
				PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
			}
		});
	}
}
