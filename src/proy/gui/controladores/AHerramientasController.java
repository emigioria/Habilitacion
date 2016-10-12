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
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import proy.datos.entidades.Herramienta;
import proy.excepciones.PersistenciaException;
import proy.gui.ManejadorExcepciones;
import proy.gui.componentes.VentanaError;
import proy.logica.gestores.filtros.FiltroHerramienta;
import proy.logica.gestores.resultados.ResultadoCrearHerramienta;
import proy.logica.gestores.resultados.ResultadoCrearHerramienta.ErrorCrearHerramienta;

public class AHerramientasController extends ControladorRomano {

	public static final String URLVista = "/proy/gui/vistas/AHerramientas.fxml";

	@FXML
	private TextField nombreHerramienta;

	@FXML
	private TableView<Herramienta> tablaHerramientas;

	@FXML
	private TableColumn<Herramienta, String> columnaNombre;

	@FXML
	private void initialize() {
		Platform.runLater(() -> {

			columnaNombre.setCellValueFactory((CellDataFeatures<Herramienta, String> param) -> {
				if(param.getValue() != null){
					return new SimpleStringProperty(param.getValue().getNombre());
				}
				else{
					return new SimpleStringProperty("<Nombre>");
				}
			});
			actualizar();
		});
	}

	@FXML
	public void nuevaHerramienta() {
		//hace editable la tabla y crea herramienta vacia
		//..ver como hacer editable solo una fila
		tablaHerramientas.setEditable(true);
		columnaNombre.setCellFactory(TextFieldTableCell.forTableColumn());

		Herramienta nuevaHerramienta = new Herramienta();
		tablaHerramientas.getItems().add(0, nuevaHerramienta);
		tablaHerramientas.getSelectionModel().select(0);

		//cuando apreta enter guarda
		columnaNombre.setOnEditCommit(seleccion -> {
			ResultadoCrearHerramienta resultado = null;
			Boolean hayErrores;
			String errores = "";

			nuevaHerramienta.setNombre(seleccion.getNewValue());

			try{
				resultado = coordinador.crearHerramienta(nuevaHerramienta);
			} catch(PersistenciaException e){
				ManejadorExcepciones.presentarExcepcion(e, apilador.getStage());
				return;
			} catch(Exception e){
				ManejadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
				return;
			}

			hayErrores = resultado.hayErrores();
			if(hayErrores){
				for(ErrorCrearHerramienta r: resultado.getErrores()){
					switch(r) {
					//TODO hacer validador primero
					case NombreIncompleto:
						errores += "El nombre no es válido.\n";
					case NombreRepetido:
						errores += "Ya existe una herramienta con ese nombre. \n";
					}
				}
				if(!errores.isEmpty()){
					new VentanaError("Error al crear herramienta", errores, apilador.getStage());
				}
			}
		});
	}

	@FXML
	public void eliminarHerramienta() {
		int selectedIndex = tablaHerramientas.getSelectionModel().getSelectedIndex();
		if(selectedIndex >= 0){
			Herramienta hSelected = tablaHerramientas.getSelectionModel().getSelectedItem();
			try{
				coordinador.eliminarHerramienta(hSelected);
			} catch(PersistenciaException e){
				ManejadorExcepciones.presentarExcepcion(e, apilador.getStage());
				return;
			} catch(Exception e){
				ManejadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
				return;
			}
			tablaHerramientas.getItems().remove(selectedIndex);
		}
		else{
			return;
		}
	}

	public void guardarHerramienta() {

	}

	public void buscar() {
		FiltroHerramienta filtroH = new FiltroHerramienta.Builder().build();
		System.out.println(filtroH.getNamedQueryName());
		try{
			coordinador.listarHerramientas(filtroH);
		} catch(PersistenciaException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			try{
				tablaHerramientas.getItems().clear();
				tablaHerramientas.getItems().addAll(coordinador.listarHerramientas(new FiltroHerramienta.Builder().build()));
			} catch(PersistenciaException e){
				ManejadorExcepciones.presentarExcepcion(e, apilador.getStage());
			}
		});
	}
}
