/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import proy.datos.entidades.Herramienta;
import proy.excepciones.PersistenciaException;
import proy.gui.PresentadorExcepciones;
import proy.gui.componentes.TableCellTextViewString;
import proy.gui.componentes.VentanaError;
import proy.logica.gestores.filtros.FiltroHerramienta;
import proy.logica.gestores.resultados.ResultadoCrearHerramienta;
import proy.logica.gestores.resultados.ResultadoCrearHerramienta.ErrorCrearHerramienta;

public class AHerramientasController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/AHerramientas.fxml";

	@FXML
	private TextField nombreHerramienta;

	@FXML
	private TableView<Herramienta> tablaHerramientas;

	@FXML
	private TableColumn<Herramienta, String> columnaNombre;

	@FXML
	private ArrayList<Herramienta> herramientasAGuardar = new ArrayList<>();

	@FXML
	private void initialize() {
		Platform.runLater(() -> {

			columnaNombre.setCellValueFactory((CellDataFeatures<Herramienta, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getNombre());
				}
				else{
					return new SimpleStringProperty("<Sin Nombre>");
				}
			});

			Callback<TableColumn<Herramienta, String>, TableCell<Herramienta, String>> call = col -> {
				return new TableCellTextViewString<Herramienta>(Herramienta.class) {

					@Override
					public void changed(ObservableValue<? extends Herramienta> observable, Herramienta oldValue, Herramienta newValue) {
						this.setEditable(false);
						if(this.getTableRow() != null && newValue != null){
							this.setEditable(herramientasAGuardar.contains(newValue));
						}
					}
				};
			};
			tablaHerramientas.setEditable(true);

			columnaNombre.setCellFactory(call);
			columnaNombre.setOnEditCommit((t) -> {
				t.getRowValue().setNombre(t.getNewValue().trim());
			});

			actualizar();
		});

	}

	@FXML
	public void nuevaHerramienta() {

		if(!tablaHerramientas.isEditable()){
			tablaHerramientas.setEditable(true);
		}

		Herramienta nuevaHerramienta = new Herramienta();
		herramientasAGuardar.add(nuevaHerramienta);
		tablaHerramientas.getItems().add(0, nuevaHerramienta);

	}

	@FXML
	public void eliminarHerramienta() {
		Herramienta hSelected = tablaHerramientas.getSelectionModel().getSelectedItem();

		if(hSelected != null){
			try{
				coordinador.eliminarHerramienta(hSelected);
			} catch(PersistenciaException e){
				PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
				return;
			} catch(Exception e){
				PresentadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
				return;
			}
			actualizar();
		}
		else{
			return;
		}
	}

	public void guardarHerramienta() {
		ResultadoCrearHerramienta resultado = null;
		Boolean hayErrores;
		String errores = "";

		if(herramientasAGuardar.size() == 0){
			return;
		}

		for(Herramienta herramienta: herramientasAGuardar){
			try{
				resultado = coordinador.crearHerramienta(herramienta);
			} catch(PersistenciaException e){
				PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
				return;
			} catch(Exception e){
				PresentadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
				return;
			}
		}

		hayErrores = resultado.hayErrores();
		if(hayErrores){
			for(ErrorCrearHerramienta r: resultado.getErrores()){
				switch(r) {
				case NOMBRE_INCOMPLETO:
					errores += "El nombre no es válido.\n";
					break;
				case NOMBRE_REPETIDO:
					errores += "Ya existe una herramienta con ese nombre. \n";
					break;
				}
			}
			if(!errores.isEmpty()){
				new VentanaError("Error al crear herramienta", errores, apilador.getStage());
			}
		}
		else{
			tablaHerramientas.setEditable(false);
		}

	}

	public void buscar() {
		FiltroHerramienta filtro = new FiltroHerramienta.Builder().nombre(nombreHerramienta.getText()).build();
		ArrayList<Herramienta> result = null;
		try{
			result = coordinador.listarHerramientas(filtro);
		} catch(PersistenciaException e){
			PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
		}
		tablaHerramientas.getItems().clear();
		tablaHerramientas.getItems().addAll(result);
	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			try{
				tablaHerramientas.getItems().clear();
				tablaHerramientas.getItems().addAll(coordinador.listarHerramientas(new FiltroHerramienta.Builder().build()));
			} catch(PersistenciaException e){
				PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
			}
		});
	}
}
