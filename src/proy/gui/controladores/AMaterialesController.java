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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import proy.datos.entidades.Material;
import proy.datos.filtros.implementacion.FiltroMaterial;
import proy.excepciones.PersistenciaException;
import proy.gui.PresentadorExcepciones;
import proy.gui.componentes.TableCellTextViewString;
import proy.gui.componentes.VentanaError;
import proy.gui.componentes.VentanaInformacion;
import proy.logica.gestores.resultados.ResultadoCrearMateriales;
import proy.logica.gestores.resultados.ResultadoCrearMateriales.ErrorCrearMateriales;
import proy.logica.gestores.resultados.ResultadoEliminarMateriales;
import proy.logica.gestores.resultados.ResultadoEliminarMateriales.ErrorEliminarMateriales;

public class AMaterialesController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/AMateriales.fxml";

	@FXML
	private TextField nombreMaterial;

	@FXML
	private TableView<Material> tablaMateriales;

	@FXML
	private TableColumn<Material, String> columnaMaterial;

	@FXML
	private TableColumn<Material, String> columnaMedidas;

	@FXML
	private ArrayList<Material> materialesAGuardar = new ArrayList<>();

	@FXML
	private ArrayList<Material> materialesAEliminar = new ArrayList<>();

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			columnaMaterial.setCellValueFactory(param -> {
				if(param.getValue() != null){
					if(param.getValue().getNombre() != null){
						return new SimpleStringProperty(formateadorString.primeraMayuscula(param.getValue().getNombre()));
					}
				}
				return new SimpleStringProperty("<Sin nombre>");
			});
			columnaMedidas.setCellValueFactory(param -> {
				if(param.getValue() != null){
					if(param.getValue().getMedidas() != null){
						return new SimpleStringProperty(param.getValue().getMedidas());
					}
				}
				return new SimpleStringProperty("<Sin medidas>");
			});

			Callback<TableColumn<Material, String>, TableCell<Material, String>> call = col -> {
				return new TableCellTextViewString<Material>(Material.class) {

					@Override
					public void changed(ObservableValue<? extends Material> observable, Material oldValue, Material newValue) {
						this.setEditable(false);
						if(this.getTableRow() != null && newValue != null){
							this.setEditable(materialesAGuardar.contains(newValue));
						}
					}
				};
			};
			columnaMaterial.setCellFactory(call);
			columnaMedidas.setCellFactory(call);

			columnaMaterial.setOnEditCommit((t) -> {
				t.getRowValue().setNombre(t.getNewValue().toLowerCase().trim());
				//Truco para que se llame a Cell.updateItem() para que formatee el valor ingresado.
				t.getTableColumn().setVisible(false);
				t.getTableColumn().setVisible(true);
			});
			columnaMedidas.setOnEditCommit((t) -> {
				t.getRowValue().setMedidas(t.getNewValue().toLowerCase().trim());
				//Truco para que se llame a Cell.updateItem() para que formatee el valor ingresado.
				t.getTableColumn().setVisible(false);
				t.getTableColumn().setVisible(true);
			});

			actualizar();
		});

	}

	@FXML
	public void guardar() {
		eliminarMateriales();
		crearMateriales();
	}

	@FXML
	public void nuevoMaterial() {
		if(!tablaMateriales.isEditable()){
			tablaMateriales.setEditable(true);
		}

		Material nuevoMaterial = new Material();
		materialesAGuardar.add(nuevoMaterial);
		tablaMateriales.getItems().add(0, nuevoMaterial);
	}

	private void crearMateriales() {
		ResultadoCrearMateriales resultadoCrearMateriales;
		StringBuffer erroresBfr = new StringBuffer();

		//Toma de datos de la vista
		if(materialesAGuardar.isEmpty()){
			return;
		}

		//Inicio transacciones al gestor
		try{
			resultadoCrearMateriales = coordinador.crearMateriales(materialesAGuardar);
		} catch(PersistenciaException e){
			PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
			return;
		} catch(Exception e){
			PresentadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
			return;
		}

		//Tratamiento de errores
		if(resultadoCrearMateriales.hayErrores()){
			for(ErrorCrearMateriales e: resultadoCrearMateriales.getErrores()){
				switch(e) {
				case NOMBRE_INCOMPLETO:
					erroresBfr.append("Hay nombres vacíos.\n");
					break;
				case NOMBRE_YA_EXISTENTE:
					erroresBfr.append("Estos materiales ya existen en el sistema:\n");
					for(String nombreMaterial: resultadoCrearMateriales.getRepetidos()){
						erroresBfr.append("\t<");
						erroresBfr.append(nombreMaterial);
						erroresBfr.append(">\n");
					}
					break;
				case NOMBRE_INGRESADO_REPETIDO:
					erroresBfr.append("Se intenta añadir dos materiales con el mismo nombre.\n");
					break;
				}
			}
			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				new VentanaError("Error al crear materiales", errores, apilador.getStage());
			}
		}
		else{
			materialesAGuardar.clear();
			new VentanaInformacion("Operación exitosa", "Se han guardado correctamente los materiales");
		}
	}

	@FXML
	public void eliminarMaterial() {
		Material materialAEliminar = tablaMateriales.getSelectionModel().getSelectedItem();
		if(materialAEliminar == null){
			return;
		}
		if(materialesAGuardar.contains(materialAEliminar)){
			materialesAGuardar.remove(materialAEliminar);
		}
		else{
			materialesAEliminar.add(materialAEliminar);
		}
		tablaMateriales.getItems().remove(materialAEliminar);
	}

	private void eliminarMateriales() {
		ResultadoEliminarMateriales resultadoEliminarMateriales;
		StringBuffer erroresBfr = new StringBuffer();

		//Toma de datos de la vista
		if(materialesAEliminar.isEmpty()){
			return;
		}

		//Inicio transacciones al gestor
		try{
			resultadoEliminarMateriales = coordinador.eliminarMateriales(materialesAEliminar);
		} catch(PersistenciaException e){
			PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
			return;
		} catch(Exception e){
			PresentadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
			return;
		}

		//Tratamiento de errores
		if(resultadoEliminarMateriales.hayErrores()){
			for(ErrorEliminarMateriales e: resultadoEliminarMateriales.getErrores()){
				switch(e) {
				case PIEZAS_ACTIVAS_ASOCIADAS:
					for(String material: resultadoEliminarMateriales.getPiezasAsociadasPorMaterial().keySet()){
						erroresBfr.append("El material <" + material + "> no se puede eliminar porque está siendo utilizado en las siguientes piezas:\n");
						for(String pieza: resultadoEliminarMateriales.getPiezasAsociadasPorMaterial().get(material)){
							erroresBfr.append("\t<");
							erroresBfr.append(pieza);
							erroresBfr.append(">\n");
						}
					}
					break;
				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				new VentanaError("Error al eliminar materiales", errores, apilador.getStage());
			}
		}
		else{
			materialesAEliminar.clear();
			new VentanaInformacion("Operación exitosa", "Se han eliminado correctamente los materiales");
		}
	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			try{
				tablaMateriales.getItems().clear();
				tablaMateriales.getItems().addAll(coordinador.listarMateriales(new FiltroMaterial.Builder().build()));
			} catch(PersistenciaException e){
				PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
			}
		});
	}
}
