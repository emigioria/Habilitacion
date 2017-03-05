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
import proy.datos.filtros.implementacion.FiltroOperario;
import proy.excepciones.PersistenciaException;
import proy.gui.PresentadorExcepciones;
import proy.gui.componentes.TableCellTextViewString;
import proy.gui.componentes.VentanaConfirmacion;
import proy.gui.componentes.VentanaError;
import proy.gui.componentes.VentanaInformacion;
import proy.logica.gestores.resultados.ResultadoCrearOperario;
import proy.logica.gestores.resultados.ResultadoCrearOperario.ErrorCrearOperario;

public class AOperariosController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/AOperarios.fxml";

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
				return new TableCellTextViewString<Operario>(Operario.class) {

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
				PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
				return;
			} catch(Exception e){
				PresentadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
				return;
			}
			actualizar();
			String mensaje = "Los datos del operario " + oSelected.getNombre() +
					" " + oSelected.getApellido() + " han sido eliminados del sistema.";
			new VentanaInformacion("Operario Guardado", mensaje);
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
				PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
				return;
			} catch(Exception e){
				PresentadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
				return;
			}
		}

		hayErrores = resultado.hayErrores();
		if(hayErrores){
			for(ErrorCrearOperario r: resultado.getErrores()){
				switch(r) {
				case NOMBRE_INCOMPLETO:
					errores += "El nombre no es válido.\n";
					break;
				case APELLIDO_INCOMPLETO:
					errores += "El apellido no es válido \n";
					break;
				case DNI_INCOMPLETO:
					errores += "El DNI no es válido \n";
					break;
				case DNI_REPETIDO:
					errores += "Ya existe un operario con ese DNI \n";
					break;
				}
			}
			if(!errores.isEmpty()){
				new VentanaError("Error al crear operario", errores, apilador.getStage());
			}
		}
		else{
			tablaOperarios.setEditable(false);
			operariosAGuardar.clear();
		}

	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			try{
				tablaOperarios.getItems().clear();
				tablaOperarios.getItems().addAll(coordinador.listarOperarios(new FiltroOperario.Builder().build()));
			} catch(PersistenciaException e){
				PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
			}
		});
	}

	@Override
	public void salir() {
		if(operariosAGuardar.isEmpty()){
			super.salir();
		}
		else{
			VentanaConfirmacion confirmacion = new VentanaConfirmacion("¿Quiere salir sin guardar?",
					"Hay operarios nuevos sin guardar, si sale ahora se perderán los cambios.");
			if(confirmacion.acepta()){
				super.salir();
			}
		}
	}
}
