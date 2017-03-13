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
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import proy.datos.entidades.Material;
import proy.datos.filtros.implementacion.FiltroMaterial;
import proy.excepciones.PersistenciaException;
import proy.gui.ControladorRomano;
import proy.gui.componentes.tablecell.TableCellTextViewString;
import proy.gui.componentes.ventanas.VentanaConfirmacion;
import proy.logica.gestores.resultados.ResultadoCrearMateriales;
import proy.logica.gestores.resultados.ResultadoCrearMateriales.ErrorCrearMateriales;
import proy.logica.gestores.resultados.ResultadoEliminarMaterial;
import proy.logica.gestores.resultados.ResultadoEliminarMaterial.ErrorEliminarMaterial;

public class AMaterialesController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/AMateriales.fxml";

	@FXML
	private TextField nombreMaterial;

	@FXML
	private TextField medidaMaterial;

	@FXML
	private TableView<Material> tablaMateriales;

	@FXML
	private TableColumn<Material, String> columnaMaterial;

	@FXML
	private TableColumn<Material, String> columnaMedidas;

	private ArrayList<Material> materialesAGuardar = new ArrayList<>();

	@Override
	protected void inicializar() {
		columnaMaterial.setCellValueFactory(param -> {
			if(param.getValue() != null){
				if(param.getValue().getNombre() != null){
					return new SimpleStringProperty(formateadorString.primeraMayuscula(param.getValue().getNombre()));
				}
			}
			return new SimpleStringProperty("");
		});
		columnaMedidas.setCellValueFactory(param -> {
			if(param.getValue() != null){
				if(param.getValue().getMedidas() != null){
					return new SimpleStringProperty(param.getValue().getMedidas());
				}
			}
			return new SimpleStringProperty("");
		});

		columnaMaterial.setCellFactory(col -> {
			return new TableCellTextViewString<Material>() {

				@Override
				protected Boolean esEditable(Material newValue) {
					return materialesAGuardar.contains(newValue);
				}

				@Override
				public void onEdit(Material object, String newValue) {
					object.setNombre(newValue.toLowerCase().trim());
				}

				@Override
				protected String getEstilo(String item, boolean empty) {
					if(!empty){
						//Si la fila no es de relleno la pinto de rojo cuando está incorrecta
						if(item == null || item.isEmpty()){
							return "-fx-background-color: red";
						}
					}
					return super.getEstilo(item, empty);
				}
			};
		});
		columnaMedidas.setCellFactory(col -> {
			return new TableCellTextViewString<Material>() {

				@Override
				protected Boolean esEditable(Material newValue) {
					return materialesAGuardar.contains(newValue);
				}

				@Override
				public void onEdit(Material object, String newValue) {
					object.setMedidas(newValue.trim());
				}
			};
		});

		actualizar();
	}

	@FXML
	private void nuevoMaterial() {
		if(!tablaMateriales.isEditable()){
			tablaMateriales.setEditable(true);
		}

		Material nuevoMaterial = new Material();
		materialesAGuardar.add(nuevoMaterial);
		tablaMateriales.getItems().add(0, nuevoMaterial);
	}

	@FXML
	private void guardar() {
		crearMateriales();
		actualizar();
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
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
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
				presentadorVentanas.presentarError("Error al crear material", errores, stage);
			}
		}
		else{
			tablaMateriales.setEditable(false);
			materialesAGuardar.clear();
			presentadorVentanas.presentarInformacion("Operación exitosa", "Se han guardado correctamente los materiales", stage);
		}
	}

	@FXML
	private void eliminarMaterial() {
		ResultadoEliminarMaterial resultadoEliminarMaterial;
		StringBuffer erroresBfr = new StringBuffer();

		//Toma de datos de la vista
		Material materialAEliminar = tablaMateriales.getSelectionModel().getSelectedItem();
		if(materialAEliminar == null){
			return;
		}

		//Si no fue guardada previamente se elimina sin ir al gestor
		if(materialesAGuardar.contains(materialAEliminar)){
			materialesAGuardar.remove(materialAEliminar);
			tablaMateriales.getItems().remove(materialAEliminar);
			return;
		}

		//Se le pide al usuario que confirme la eliminación del material
		VentanaConfirmacion vc = presentadorVentanas.presentarConfirmacion("Confirmar eliminar material",
				"Se eliminará el material <" + materialAEliminar + "> de forma permanente.\n" +
						"¿Está seguro de que desea continuar?",
				stage);
		if(!vc.acepta()){
			return;
		}

		//Inicio transacciones al gestor
		try{
			resultadoEliminarMaterial = coordinador.eliminarMaterial(materialAEliminar);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}

		//Tratamiento de errores
		if(resultadoEliminarMaterial.hayErrores()){
			for(ErrorEliminarMaterial e: resultadoEliminarMaterial.getErrores()){
				switch(e) {
				case PIEZAS_ACTIVAS_ASOCIADAS:
					erroresBfr.append("El material no se puede eliminar porque está siendo utilizado en las siguientes piezas:\n");
					for(String pieza: resultadoEliminarMaterial.getPiezasAsociadasPorMaterial()){
						erroresBfr.append("\t<");
						erroresBfr.append(pieza);
						erroresBfr.append(">\n");
					}
					break;
				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				presentadorVentanas.presentarError("Error al eliminar material", errores, stage);
			}
		}
		else{
			tablaMateriales.getItems().remove(materialAEliminar);
			presentadorVentanas.presentarToast("Se ha eliminado correctamente el material", stage);
		}
	}

	@FXML
	private void buscar() {
		String nombreBuscado = nombreMaterial.getText().trim().toLowerCase();
		String medidaBuscada = medidaMaterial.getText().trim();
		tablaMateriales.getItems().clear();
		tablaMateriales.getItems().addAll(materialesAGuardar);
		try{
			tablaMateriales.getItems().addAll(coordinador.listarMateriales(new FiltroMaterial.Builder().nombreContiene(nombreBuscado).medida(medidaBuscada).build()));
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
		}
	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			stage.setTitle("Lista de materiales");

			tablaMateriales.getItems().clear();
			tablaMateriales.getItems().addAll(materialesAGuardar);
			try{
				tablaMateriales.getItems().addAll(coordinador.listarMateriales(new FiltroMaterial.Builder().build()));
			} catch(PersistenciaException e){
				presentadorVentanas.presentarExcepcion(e, stage);
			}
		});
	}

	@Override
	public Boolean sePuedeSalir() {
		if(materialesAGuardar.isEmpty()){
			return true;
		}
		else{
			VentanaConfirmacion confirmacion = presentadorVentanas.presentarConfirmacion("¿Quiere salir sin guardar?",
					"Hay materiales nuevos sin guardar, si sale ahora se perderán los cambios.", stage);
			if(confirmacion.acepta()){
				return true;
			}
		}
		return false;
	}
}
