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
import javafx.util.Callback;
import proy.datos.entidades.Operario;
import proy.excepciones.PersistenciaException;
import proy.gui.ManejadorExcepciones;
import proy.gui.componentes.TableCellTextViewString;
import proy.gui.componentes.VentanaError;
import proy.logica.gestores.filtros.FiltroOperario;
import proy.logica.gestores.resultados.ResultadoCrearOperario;
import proy.logica.gestores.resultados.ResultadoCrearOperario.ErrorCrearOperario;

public class AOperariosController extends ControladorRomano {

	public static final String URLVista = "/proy/gui/vistas/AOperarios.fxml";

	@FXML
	private TableView<Operario> tablaOperarios;

	@FXML
	private TableColumn<Operario, String> columnaNombre;

	@FXML
	private TableColumn<Operario, String> columnaApellido;

	@FXML
	private TableColumn<Operario, String> columnaDNI;

	@FXML
	private ArrayList<Operario> operariosAGuardar = new ArrayList<>();;

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			columnaNombre.setCellValueFactory((CellDataFeatures<Operario, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getNombre());
				}
				else{
					return new SimpleStringProperty("<Sin Nombre>");
				}
			});
			columnaApellido.setCellValueFactory((CellDataFeatures<Operario, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getApellido());
				}
				else{
					return new SimpleStringProperty("<Sin Apellido>");
				}
			});
			columnaDNI.setCellValueFactory((CellDataFeatures<Operario, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getDNI());
				}
				else{
					return new SimpleStringProperty("<Sin DNI>");
				}
			});

			Callback<TableColumn<Operario, String>, TableCell<Operario, String>> call = col -> {
				return new TableCellTextViewString<Operario>() {

					@Override
					public void changed(ObservableValue<? extends Operario> observable, Operario oldValue, Operario newValue) {
						this.setEditable(false);
						if(this.getTableRow() != null && newValue != null){
							this.setEditable(operariosAGuardar.contains(newValue));
						}
					}
				};
			};
			tablaOperarios.setEditable(true);

			columnaNombre.setCellFactory(call);
			columnaNombre.setOnEditCommit((t) -> {
				t.getRowValue().setNombre(t.getNewValue().trim());
			});

			columnaApellido.setCellFactory(call);
			columnaApellido.setOnEditCommit((t) -> {
				t.getRowValue().setApellido(t.getNewValue().trim());
			});

			columnaDNI.setCellFactory(call);
			columnaDNI.setOnEditCommit((t) -> {
				t.getRowValue().setDNI(t.getNewValue().trim());
			});

			actualizar();
		});
	}

	@FXML
	public void nuevoOperario() {

		if(!tablaOperarios.isEditable()){
			tablaOperarios.setEditable(true);
		}
		Operario nuevoOperario = new Operario();
		operariosAGuardar.add(nuevoOperario);
		tablaOperarios.getItems().add(0, nuevoOperario);
	}

	@FXML
	public void eliminarOperario() {
		Operario oSelected = tablaOperarios.getSelectionModel().getSelectedItem();

		if(oSelected != null){
			try{
				coordinador.eliminarOperario(oSelected);
			} catch(PersistenciaException e){
				ManejadorExcepciones.presentarExcepcion(e, apilador.getStage());
				return;
			} catch(Exception e){
				ManejadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
				return;
			}
			actualizar();
		}
		else{
			return;
		}
	}

	@FXML
	public void guardarOperario() {
		ResultadoCrearOperario resultado = null;
		Boolean hayErrores;
		String errores = "";

		if(operariosAGuardar.size() == 0){
			return;
		}

		for(Operario operario: operariosAGuardar){
			try{
				resultado = coordinador.crearOperario(operario);
			} catch(PersistenciaException e){
				ManejadorExcepciones.presentarExcepcion(e, apilador.getStage());
				return;
			} catch(Exception e){
				ManejadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
				return;
			}
		}

		hayErrores = resultado.hayErrores();
		if(hayErrores){
			for(ErrorCrearOperario r: resultado.getErrores()){
				switch(r) {
				case NombreIncompleto:
					errores += "El nombre no es válido.\n";
					break;
				case ApellidoIncompleto:
					errores += "El apellido no es válido \n";
					break;
				case DNIIncompleto:
					errores += "El DNI no es válido \n";
					break;
				case DNIRepetido:
					errores += "Ya existe un operario con ese DNI \n";
					break;
				}
			}
			if(!errores.isEmpty()){
				new VentanaError("Error al crear herramienta", errores, apilador.getStage());
			}
		}
		else{
			tablaOperarios.setEditable(false);
		}

	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			try{
				tablaOperarios.getItems().clear();
				tablaOperarios.getItems().addAll(coordinador.listarOperarios(new FiltroOperario.Builder().build()));
			} catch(PersistenciaException e){
				ManejadorExcepciones.presentarExcepcion(e, apilador.getStage());
			}
		});
	}
}
