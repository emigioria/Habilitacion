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
import proy.logica.gestores.filtros.FiltroMaquina;

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
		Maquina maquina = tablaMaquinas.getSelectionModel().getSelectedItem();
		if(maquina != null){
			//se pregunta al usuario si desea confirmar la elininación de la máquina
			VentanaConfirmacion vc = new VentanaConfirmacion("Confirmación eliminar máquina",
					"Se eliminará la máquina <"+maquina.getNombre()+"> y sus componentes de forma permanente. "
							+ "¿Está seguro de que desea continuar?",
							apilador.getStage());

			if(vc.acepta()){
				//Inicio transacciones al gestor
				try{
					coordinador.eliminarMaquina(maquina);
				} catch(PersistenciaException e){
					PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
				} catch(Exception e){
					PresentadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
				}
			}
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
