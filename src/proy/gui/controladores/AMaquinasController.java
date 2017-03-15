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
import proy.gui.ControladorRomano;
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

	@Override
	protected void inicializar() {
		columnaNombre.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});

		actualizar();
	}

	@FXML
	public void nuevaMaquina() {
		NMMaquinaController nuevaPantalla = (NMMaquinaController) this.nuevaScene(NMMaquinaController.URL_VISTA);
		nuevaPantalla.formatearNuevaMaquina();
	}

	@FXML
	public void modificarMaquina() {
		Maquina maquina = tablaMaquinas.getSelectionModel().getSelectedItem();
		if(maquina != null){
			NMMaquinaController nuevaPantalla = (NMMaquinaController) this.nuevaScene(NMMaquinaController.URL_VISTA);
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
		//se pregunta 2 veces al usuario si desea confirmar la eliminación de la máquina
		VentanaConfirmacion vc = presentadorVentanas.presentarConfirmacion("Confirmar eliminar máquina",
				"Se eliminará la máquina <" + maquina + "> y sus componentes de forma permanente.\n" +
						"¿Está seguro de que desea continuar?",
				stage);
		if(!vc.acepta()){
			return;
		}
		if(!vc.acepta()){
			return;
		}
		vc = presentadorVentanas.presentarConfirmacion("Confirmar eliminar máquina",
				"Se eliminará la máquina <" + maquina + "> y no se podrá recuperar.\n" +
						"¿Está seguro de que desea continuar?",
				stage);
		if(!vc.acepta()){
			return;
		}

		//Inicio transacciones al gestor
		try{
			resultado = coordinador.eliminarMaquina(maquina);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
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
				presentadorVentanas.presentarError("Error al eliminar la máquina", errores, stage);
			}
		}
		else{
			tablaMaquinas.getItems().remove(maquina);
			presentadorVentanas.presentarInformacion("Operación exitosa", "Se ha eliminado la máquina con éxito", stage);
		}
	}

	public void buscar() {
		String nombreBuscado = nombreMaquina.getText().trim().toLowerCase();
		tablaMaquinas.getItems().clear();
		try{
			tablaMaquinas.getItems().addAll(coordinador.listarMaquinas(new FiltroMaquina.Builder().nombre(nombreBuscado).build()));
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
		}
	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			stage.setTitle("Lista de máquinas");
			tablaMaquinas.getItems().clear();
			try{
				tablaMaquinas.getItems().addAll(coordinador.listarMaquinas(new FiltroMaquina.Builder().build()));
			} catch(PersistenciaException e){
				presentadorVentanas.presentarExcepcion(e, stage);
			}
		});
	}
}
