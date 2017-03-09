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
import proy.datos.entidades.Operario;
import proy.datos.filtros.implementacion.FiltroOperario;
import proy.excepciones.PersistenciaException;
import proy.gui.ControladorRomano;
import proy.gui.componentes.TableCellTextViewString;
import proy.gui.componentes.ventanas.VentanaConfirmacion;
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

	@Override
	protected void inicializar() {
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

		tablaOperarios.setEditable(true);

		columnaNombre.setCellFactory(col -> {
			return new TableCellTextViewString<Operario>(Operario.class) {

				@Override
				public void changed(ObservableValue<? extends Operario> observable, Operario oldValue, Operario newValue) {
					esEditable(this, newValue);
				}

				@Override
				public void onEdit(Operario object, String newValue) {
					object.setNombre(newValue.trim());
				}
			};
		});

		columnaApellido.setCellFactory(col -> {
			return new TableCellTextViewString<Operario>(Operario.class) {

				@Override
				public void changed(ObservableValue<? extends Operario> observable, Operario oldValue, Operario newValue) {
					esEditable(this, newValue);
				}

				@Override
				public void onEdit(Operario object, String newValue) {
					object.setApellido(newValue.trim());
				}
			};
		});

		columnaDNI.setCellFactory(col -> {
			return new TableCellTextViewString<Operario>(Operario.class) {

				@Override
				public void changed(ObservableValue<? extends Operario> observable, Operario oldValue, Operario newValue) {
					esEditable(this, newValue);
				}

				@Override
				public void onEdit(Operario object, String newValue) {
					object.setDNI(newValue.trim());
				}
			};
		});

		actualizar();
	}

	private void esEditable(TableCell<?, ?> tableCell, Operario newValue) {
		tableCell.setEditable(false);
		if(tableCell.getTableRow() != null && newValue != null){
			tableCell.setEditable(operariosAGuardar.contains(newValue));
		}
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
				presentadorVentanas.presentarExcepcion(e, stage);
				return;
			} catch(Exception e){
				presentadorVentanas.presentarExcepcionInesperada(e, stage);
				return;
			}
			actualizar();
			String mensaje = "Los datos del operario " + oSelected.getNombre() +
					" " + oSelected.getApellido() + " han sido eliminados del sistema.";
			presentadorVentanas.presentarInformacion("Operario Guardado", mensaje, stage);
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
				presentadorVentanas.presentarExcepcion(e, stage);
				return;
			} catch(Exception e){
				presentadorVentanas.presentarExcepcionInesperada(e, stage);
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
				presentadorVentanas.presentarError("Error al crear operario", errores, stage);
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
				presentadorVentanas.presentarExcepcion(e, stage);
			}
		});
	}

	@Override
	public void salir() {
		if(operariosAGuardar.isEmpty()){
			super.salir();
		}
		else{
			VentanaConfirmacion confirmacion = presentadorVentanas.presentarConfirmacion("¿Quiere salir sin guardar?",
					"Hay operarios nuevos sin guardar, si sale ahora se perderán los cambios.", stage);
			if(confirmacion.acepta()){
				super.salir();
			}
		}
	}
}
