/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import proy.comun.FormateadorString;
import proy.datos.entidades.Maquina;
import proy.excepciones.PersistenciaException;
import proy.gui.PresentadorExcepciones;
import proy.gui.componentes.TableCellTextViewString;
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
		});

		Callback<TableColumn<Maquina, String>, TableCell<Maquina, String>> call = col -> {
			return new TableCellTextViewString<Maquina>() {

				@Override
				public void changed(ObservableValue<? extends Maquina> observable, Maquina oldValue, Maquina newValue) {
					this.setEditable(false);
				}
			};
		};
		columnaNombre.setCellFactory(call);

		actualizar();
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
