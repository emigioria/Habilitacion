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
import proy.datos.clases.EstadoTareaStr;
import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Proceso;
import proy.datos.entidades.Tarea;
import proy.excepciones.PersistenciaException;
import proy.gui.PresentadorExcepciones;
import proy.gui.componentes.TableCellTextViewString;
import proy.gui.componentes.VentanaConfirmacion;
import proy.gui.componentes.VentanaError;
import proy.logica.gestores.filtros.FiltroHerramienta;
import proy.logica.gestores.filtros.FiltroProceso;
import proy.logica.gestores.filtros.FiltroTarea;
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
				t.getRowValue().setNombre(t.getNewValue().toLowerCase().trim());
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
	public void eliminarHerramienta() { //TODO cambiar para que este homogeneo con ABM Material
		Herramienta herramientaAEliminar = tablaHerramientas.getSelectionModel().getSelectedItem();

		if(herramientaAEliminar != null){
			ArrayList<Proceso> procesosAsociados = new ArrayList<>();
			try{
				procesosAsociados = coordinador.listarProcesos(new FiltroProceso.Builder().herramienta(herramientaAEliminar).build());
			} catch(PersistenciaException e){
				PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
				return;
			}
			if(!procesosAsociados.isEmpty()){ //hay procesos usando esa herramienta, los elimino?
				VentanaConfirmacion confirmacionProcesos = new VentanaConfirmacion("¿Eliminar procesos asociados?",
						"Al eliminar la herramienta se eliminarán los siguientes procesos: " + procesosAsociados);
				if(confirmacionProcesos.acepta()){
					ArrayList<Tarea> tareasNoTerminadas = new ArrayList<>();
					try{
						tareasNoTerminadas = coordinador.listarTareas(new FiltroTarea.Builder().herramienta(herramientaAEliminar).noEstado(EstadoTareaStr.FINALIZADA).build());
					} catch(PersistenciaException e){
						PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
						return;
					}
					if(!tareasNoTerminadas.isEmpty()){ //hay tareas no terminadas, las elimino tambien?
						VentanaConfirmacion confirmacionTareas = new VentanaConfirmacion("¿Eliminar tareas asociados?",
								"Al eliminar la herramienta se eliminarán las siguientes tareas: " + tareasNoTerminadas);
						if(confirmacionTareas.acepta()){
							for(Tarea tarea: tareasNoTerminadas){
								try{
									coordinador.eliminarTarea(tarea);
								} catch(PersistenciaException e){
									PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
									return;
								}
							}
						}
						else{ //si no quiere eliminar las tareas, sale
							return;
						}
					}
					//elimina logicamente la herramienta y los procesos
					try{
						herramientaAEliminar.darDeBaja();
						coordinador.eliminarHerramienta(herramientaAEliminar); //TODO poner bien
						for(Proceso proceso: procesosAsociados){
							proceso.darDeBaja();
							coordinador.eliminarProceso(proceso); //TODO poner bien
						}
					} catch(PersistenciaException e){
						PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
						return;
					}
				}
			}
			else{ //si no tiene procesos asociados, elimina fisicamente
				try{
					coordinador.eliminarHerramienta(herramientaAEliminar);
				} catch(PersistenciaException e){
					PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
					return;
				} catch(Exception e){
					PresentadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
					return;
				}
				actualizar();
			}

		}
	}

	public void guardarHerramienta() { //TODO cambiar para que este homogeneo con ABM Material
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
			herramientasAGuardar.clear();
		}

	}

	public void buscar() {
		String nombreBuscado = nombreHerramienta.getText().trim();
		if(nombreBuscado.isEmpty()){
			actualizar();
		}
		else{
			FiltroHerramienta filtro = new FiltroHerramienta.Builder().nombre(nombreBuscado.toLowerCase()).build();
			ArrayList<Herramienta> resultado = null;
			try{
				resultado = coordinador.listarHerramientas(filtro);
			} catch(PersistenciaException e){
				PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
			}
			tablaHerramientas.getItems().clear();
			tablaHerramientas.getItems().addAll(herramientasAGuardar);
			tablaHerramientas.getItems().addAll(resultado);
		}
	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			try{
				tablaHerramientas.getItems().clear();
				tablaHerramientas.getItems().addAll(coordinador.listarHerramientas(new FiltroHerramienta.Builder().build()));
				tablaHerramientas.getItems().addAll(herramientasAGuardar);
			} catch(PersistenciaException e){
				PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
			}
		});
	}

	@Override
	public void salir() {
		if(herramientasAGuardar.isEmpty()){
			super.salir();
		}
		else{
			VentanaConfirmacion confirmacion = new VentanaConfirmacion("¿Quiere salir sin guardar?",
					"Hay herramientas nuevas sin guardar, si sale ahora se perderán los cambios.");
			if(confirmacion.acepta()){
				super.salir();
			}
		}
	}
}
