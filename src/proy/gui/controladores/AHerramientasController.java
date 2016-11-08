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
import proy.comun.FormateadorString;
import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Proceso;
import proy.excepciones.PersistenciaException;
import proy.gui.PresentadorExcepciones;
import proy.gui.componentes.TableCellTextViewString;
import proy.gui.componentes.VentanaConfirmacion;
import proy.gui.componentes.VentanaError;
import proy.gui.componentes.VentanaInformacion;
import proy.logica.gestores.filtros.FiltroHerramienta;
import proy.logica.gestores.filtros.FiltroProceso;
import proy.logica.gestores.resultados.ResultadoCrearHerramientas;
import proy.logica.gestores.resultados.ResultadoCrearHerramientas.ErrorCrearHerramientas;
import proy.logica.gestores.resultados.ResultadoEliminarHerramientas;
import proy.logica.gestores.resultados.ResultadoEliminarHerramientas.ErrorEliminarHerramientas;
import proy.logica.gestores.resultados.ResultadoEliminarTareas;
import proy.logica.gestores.resultados.ResultadoEliminarTareas.ErrorEliminarTareas;

public class AHerramientasController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/AHerramientas.fxml";

	@FXML
	private TextField nombreHerramienta;

	@FXML
	private TableView<Herramienta> tablaHerramientas;

	@FXML
	private TableColumn<Herramienta, String> columnaNombre;

	private ArrayList<Herramienta> herramientasAGuardar = new ArrayList<>();

	private ArrayList<Herramienta> herramientasAEliminar = new ArrayList<>();

	@FXML
	private void initialize() {
		Platform.runLater(() -> {

			columnaNombre.setCellValueFactory((CellDataFeatures<Herramienta, String> param) -> {
				if(param.getValue() != null){
					if(param.getValue().getNombre() != null){
						return new SimpleStringProperty(FormateadorString.primeraMayuscula(param.getValue().getNombre()));
					}
				}
				return new SimpleStringProperty("<Sin Nombre>");
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
				String nombre = t.getNewValue().toLowerCase().trim();
				if(!nombre.isEmpty()){
					t.getRowValue().setNombre(nombre);
				}
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

	private void crearHerramientas() {
		ResultadoCrearHerramientas resultadoCrearHerramientas;
		StringBuffer erroresBfr = new StringBuffer();

		if(herramientasAGuardar.isEmpty()){
			return;
		}

		try{
			resultadoCrearHerramientas = coordinador.crearHerramientas(herramientasAGuardar);
		} catch(PersistenciaException e){
			PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
			return;
		} catch(Exception e){
			PresentadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
			return;
		}

		if(resultadoCrearHerramientas.hayErrores()){
			for(ErrorCrearHerramientas r: resultadoCrearHerramientas.getErrores()){
				switch(r) {
				case NOMBRE_INCOMPLETO:
					erroresBfr.append("Hay nombres vacíos.\n");
					break;
				case NOMBRE_YA_EXISTENTE:
					erroresBfr.append("Estas herramientas ya existen en el sistema:\n");
					for(String nombreHerramienta: resultadoCrearHerramientas.getRepetidos()){
						erroresBfr.append("\t<");
						erroresBfr.append(nombreHerramienta);
						erroresBfr.append(">\n");
					}
					break;
				case NOMBRE_REPETIDO:
					erroresBfr.append("Se intenta añadir dos herramientas con el mismo nombre.\n");
					break;
				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				new VentanaError("Error al crear herramienta", errores, apilador.getStage());
			}
		}
		else{
			tablaHerramientas.setEditable(false);
			herramientasAGuardar.clear();
		}

	}

	@FXML
	public void guardar() {
		eliminarHerramientas();
		crearHerramientas();
	}

	@FXML
	public void eliminarHerramienta() {
		Herramienta herramientaAEliminar = tablaHerramientas.getSelectionModel().getSelectedItem();

		if(herramientaAEliminar == null){
			return;
		}

		//Se pregunta si quiere dar de baja
		ArrayList<Proceso> procesosAsociados = new ArrayList<>();
		try{
			procesosAsociados = coordinador.listarProcesos(new FiltroProceso.Builder().herramienta(herramientaAEliminar).build());
		} catch(PersistenciaException e){
			PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
			return;
		} catch(Exception e){
			PresentadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
			return;
		}

		VentanaConfirmacion vc;
		if(!procesosAsociados.isEmpty()){ //hay procesos usando esa herramienta, los elimino?
			vc = new VentanaConfirmacion("Confirmar eliminar herramienta",
					"Al eliminar la herramienta se eliminarán los siguientes procesos: <" + procesosAsociados + ">\n¿Está seguro que desea eliminar la herramienta <" + herramientaAEliminar + ">?", apilador.getStage()); //TODO hacer lindo el cartelito
			if(!vc.acepta()){
				return;
			}
		}

		//Si acepta dar de baja se verifica que la herramienta a eliminar no tiene tareas no terminadas asociadas
		Boolean tieneTareasNoTerminadasAsociadas = false;
		try{
			if(!herramientasAGuardar.contains(herramientaAEliminar)){
				tieneTareasNoTerminadasAsociadas = coordinador.tieneTareasNoTerminadasAsociadas(herramientaAEliminar);
			}
		} catch(PersistenciaException e){
			PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
			return;
		} catch(Exception e){
			PresentadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
			return;
		}

		//Se pregunta si quiere dar de baja estas tareas asociadas
		if(tieneTareasNoTerminadasAsociadas){
			vc = new VentanaConfirmacion("Confirmar eliminar herramienta",
					"La herramienta  <" + herramientaAEliminar + "> tiene tareas no terminadas asociadas\nSi continúa, esas tareas se eliminarán\n¿Está seguro que desea eliminarla?",
					apilador.getStage());
			if(!vc.acepta()){
				return;
			}
		}

		if(herramientasAGuardar.contains(herramientaAEliminar)){
			herramientasAGuardar.remove(herramientaAEliminar);
		}
		else{
			herramientasAEliminar.add(herramientaAEliminar);
		}
		tablaHerramientas.getItems().remove(herramientaAEliminar);
	}

	private void eliminarHerramientas() {
		ResultadoEliminarHerramientas resultadoEliminarHerramientas;
		StringBuffer erroresBfr = new StringBuffer();

		//Toma de datos de la vista
		if(herramientasAEliminar.isEmpty()){
			return;
		}

		//Inicio transacciones al gestor
		try{
			resultadoEliminarHerramientas = coordinador.eliminarHerramientas(herramientasAEliminar);
		} catch(PersistenciaException e){
			PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
			return;
		} catch(Exception e){
			PresentadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
			return;
		}

		//Tratamiento de errores
		if(resultadoEliminarHerramientas.hayErrores()){
			for(ErrorEliminarHerramientas e: resultadoEliminarHerramientas.getErrores()){
				switch(e) {
				case ERROR_AL_ELIMINAR_TAREAS:
					ResultadoEliminarTareas resultadoTareas = resultadoEliminarHerramientas.getResultadoTareas();
					if(resultadoTareas.hayErrores()){
						for(ErrorEliminarTareas ep: resultadoTareas.getErrores()){
							switch(ep) {
							//Todavia no hay errores en eliminar tarea
							}
						}
					}
					break;
				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				new VentanaError("Error al eliminar herramientas", errores, apilador.getStage());
			}
		}
		else{
			herramientasAEliminar.clear();
			new VentanaInformacion("Operación exitosa", "Se han eliminado correctamente las herramientas.");
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
