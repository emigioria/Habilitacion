/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.util.ArrayList;
import java.util.List;

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
import proy.excepciones.PersistenciaException;
import proy.gui.ManejadorExcepciones;
import proy.gui.componentes.TableCellTextViewString;
import proy.gui.componentes.VentanaError;
import proy.gui.componentes.VentanaInformacion;
import proy.logica.gestores.filtros.FiltroMaterial;
import proy.logica.gestores.resultados.ResultadoCrearMaterial;
import proy.logica.gestores.resultados.ResultadoCrearMaterial.ErrorCrearMaterial;

public class AMaterialesController extends ControladorRomano {

	public static final String URLVista = "/proy/gui/vistas/AMateriales.fxml";

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
	private void initialize() {
		Platform.runLater(() -> {
			columnaMaterial.setCellValueFactory(param -> {
				if(param.getValue() != null){
					if(param.getValue().getNombre() != null){
						return new SimpleStringProperty(param.getValue().getNombre());
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
				return new TableCellTextViewString<Material>() {

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
	public void nuevoMaterial() {
		if(!tablaMateriales.isEditable()){
			tablaMateriales.setEditable(true);
		}

		Material nuevoMaterial = new Material();
		materialesAGuardar.add(nuevoMaterial);
		tablaMateriales.getItems().add(0, nuevoMaterial);
	}

	@FXML
	public void eliminarMaterial() {

	}

	@FXML
	public void guardar() {
		List<ResultadoCrearMaterial> resultados;
		StringBuffer erroresBfr = new StringBuffer();

		//Toma de datos de la vista
		if(materialesAGuardar.size() == 0){
			return;
		}

		//Inicio transacción al gestor
		try{
			resultados = coordinador.crearMateriales(materialesAGuardar);
		} catch(PersistenciaException e){
			ManejadorExcepciones.presentarExcepcion(e, apilador.getStage());
			return;
		} catch(Exception e){
			ManejadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
			return;
		}

		//Tratamiento de errores
		boolean nombreIncompletoEncontrado = false;
		boolean medidasIncompletasEncontradas = false;

		for(int i = 0; i < resultados.size(); i++){
			if(resultados.get(i).hayErrores()){
				for(ErrorCrearMaterial e: resultados.get(i).getErrores()){
					switch(e) {
					case NombreIncompleto:
						nombreIncompletoEncontrado = true;
						break;
					case NombreRepetidoEnBD:
						erroresBfr.append("Ya existe un material con el nombre \"");
						erroresBfr.append(materialesAGuardar.get(i).getNombre());
						erroresBfr.append("\".\n");
						break;
					case NombreRepetidoEnVista:
						erroresBfr.append("Se intenta añadir dos materiales con el mismo nombre.\n");
					}

				}
			}
		}
		if(nombreIncompletoEncontrado){
			erroresBfr.append("Hay nombres vacíos.\n");
		}
		if(medidasIncompletasEncontradas){
			erroresBfr.append("Hay medidas vacías.\n");
		}

		String errores = erroresBfr.toString();
		if(!errores.isEmpty()){
			new VentanaError("Error al crear herramienta", errores, apilador.getStage());
		}
		else{
			materialesAGuardar.clear();
			new VentanaInformacion("Operación exitosa", "Se han guardado correctamente los materiales");
		}
	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			try{
				tablaMateriales.getItems().clear();
				tablaMateriales.getItems().addAll(coordinador.listarMateriales(new FiltroMaterial.Builder().build()));
			} catch(PersistenciaException e){
				ManejadorExcepciones.presentarExcepcion(e, apilador.getStage());
			}
		});
	}
}
