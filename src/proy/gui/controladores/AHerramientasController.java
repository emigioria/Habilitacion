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
import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Proceso;
import proy.datos.filtros.implementacion.FiltroHerramienta;
import proy.datos.filtros.implementacion.FiltroProceso;
import proy.excepciones.PersistenciaException;
import proy.gui.ControladorRomano;
import proy.gui.componentes.tablecell.TableCellTextViewString;
import proy.gui.componentes.ventanas.VentanaConfirmacion;
import proy.logica.gestores.resultados.ResultadoCrearHerramientas;
import proy.logica.gestores.resultados.ResultadoCrearHerramientas.ErrorCrearHerramientas;
import proy.logica.gestores.resultados.ResultadoEliminarHerramienta;
import proy.logica.gestores.resultados.ResultadoEliminarHerramienta.ErrorEliminarHerramienta;
import proy.logica.gestores.resultados.ResultadoEliminarProcesos;
import proy.logica.gestores.resultados.ResultadoEliminarProcesos.ErrorEliminarProcesos;
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

	@Override
	protected void inicializar() {
		columnaNombre.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(formateadorString.primeraMayuscula(param.getValue().getNombre().toString()));
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});

		tablaHerramientas.setEditable(true);

		columnaNombre.setCellFactory(col -> {
			return new TableCellTextViewString<Herramienta>() {

				@Override
				protected Boolean esEditable(Herramienta newValue) {
					return herramientasAGuardar.contains(newValue);
				}

				@Override
				public void onEdit(Herramienta object, String newValue) {
					String nombre = newValue.toLowerCase().trim();
					if(!nombre.isEmpty()){
						object.setNombre(nombre);
					}
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

		actualizar();
	}

	@FXML
	private void nuevaHerramienta() {
		if(!tablaHerramientas.isEditable()){
			tablaHerramientas.setEditable(true);
		}

		Herramienta nuevaHerramienta = new Herramienta();
		herramientasAGuardar.add(nuevaHerramienta);
		tablaHerramientas.getItems().add(0, nuevaHerramienta);

	}

	@FXML
	private void guardar() {
		crearHerramientas();
		actualizar();
	}

	private void crearHerramientas() {
		ResultadoCrearHerramientas resultadoCrearHerramientas;
		StringBuffer erroresBfr = new StringBuffer();

		//Toma de datos de la vista
		if(herramientasAGuardar.isEmpty()){
			return;
		}

		//Inicio transacciones al gestor
		try{
			resultadoCrearHerramientas = coordinador.crearHerramientas(herramientasAGuardar);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}

		//Tratamiento de errores
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
				case NOMBRE_INGRESADO_REPETIDO:
					erroresBfr.append("Se intenta añadir dos herramientas con el mismo nombre.\n");
					break;
				}
			}
			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				presentadorVentanas.presentarError("Error al crear herramienta", errores, stage);
			}
		}
		else{
			tablaHerramientas.setEditable(false);
			herramientasAGuardar.clear();
			presentadorVentanas.presentarInformacion("Operación exitosa", "Se han guardado correctamente las herramientas", stage);
		}

	}

	@FXML
	private void eliminarHerramienta() {
		ResultadoEliminarHerramienta resultadoEliminarHerramienta;
		StringBuffer erroresBfr = new StringBuffer();

		//Toma de datos de la vista
		Herramienta herramientaAEliminar = tablaHerramientas.getSelectionModel().getSelectedItem();
		if(herramientaAEliminar == null){
			return;
		}

		//Si no fue guardada previamente se elimina sin ir al gestor
		if(herramientasAGuardar.contains(herramientaAEliminar)){
			herramientasAGuardar.remove(herramientaAEliminar);
			tablaHerramientas.getItems().remove(herramientaAEliminar);
			return;
		}

		//Se buscan procesos asociados
		ArrayList<Proceso> procesosAsociados = new ArrayList<>();
		try{
			procesosAsociados = coordinador.listarProcesos(new FiltroProceso.Builder().herramienta(herramientaAEliminar).build());
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}

		//Se pregunta si quiere dar de baja
		VentanaConfirmacion vc;
		if(!procesosAsociados.isEmpty()){ //hay procesos usando esa herramienta, los elimino?
			vc = presentadorVentanas.presentarConfirmacion("Confirmar eliminar herramienta",
					"Al eliminar la herramienta <" + herramientaAEliminar + "> se eliminarán los siguientes procesos: " + procesosAsociados + "\n¿Está seguro que desea eliminarla?", stage);
		}
		else{
			vc = presentadorVentanas.presentarConfirmacion("Confirmar eliminar herramienta",
					"Se eliminará la herramienta <" + herramientaAEliminar + "> de forma permanente.\n" +
							"¿Está seguro de que desea continuar?",
					stage);
		}

		if(!vc.acepta()){
			return;
		}

		//Si acepta dar de baja se verifica que la herramienta a eliminar no tiene tareas no terminadas asociadas
		Boolean tieneTareasNoTerminadasAsociadas = false;
		try{
			tieneTareasNoTerminadasAsociadas = coordinador.tieneTareasNoTerminadasAsociadas(herramientaAEliminar);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}

		//Se pregunta si quiere dar de baja estas tareas asociadas
		if(tieneTareasNoTerminadasAsociadas){
			vc = presentadorVentanas.presentarConfirmacion("Confirmar eliminar herramienta",
					"La herramienta <" + herramientaAEliminar + "> tiene tareas no terminadas asociadas.\nSi continúa, esas tareas se eliminarán\n¿Está seguro que desea eliminarla?",
					stage);
			if(!vc.acepta()){
				return;
			}
		}

		//Inicio transacciones al gestor
		try{
			resultadoEliminarHerramienta = coordinador.eliminarHerramienta(herramientaAEliminar);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}

		//Tratamiento de errores
		if(resultadoEliminarHerramienta.hayErrores()){
			for(ErrorEliminarHerramienta e: resultadoEliminarHerramienta.getErrores()){
				switch(e) {
				case ERROR_AL_ELIMINAR_TAREAS:
					ResultadoEliminarTareas resultadoTareas = resultadoEliminarHerramienta.getResultadoEliminarTareas();
					if(resultadoTareas.hayErrores()){
						for(ErrorEliminarTareas eet: resultadoTareas.getErrores()){
							switch(eet) {
							case HAY_TAREA_FINALIZADA:
								throw new RuntimeException();
							}
						}
					}
					break;
				case ERROR_AL_ELIMINAR_PROCESOS:
					ResultadoEliminarProcesos resultadoProcesos = resultadoEliminarHerramienta.getResultadoEliminarProcesos();
					if(resultadoProcesos.hayErrores()){
						for(ErrorEliminarProcesos ep: resultadoProcesos.getErrores()){
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
				presentadorVentanas.presentarError("Error al eliminar herramientas", errores, stage);
			}
		}
		else{
			tablaHerramientas.getItems().remove(herramientaAEliminar);
			presentadorVentanas.presentarToast("Se ha eliminado correctamente la herramienta.", stage);
		}
	}

	public void buscar() {
		String nombreBuscado = nombreHerramienta.getText().trim().toLowerCase();
		tablaHerramientas.getItems().clear();
		tablaHerramientas.getItems().addAll(herramientasAGuardar);
		try{
			tablaHerramientas.getItems().addAll(coordinador.listarHerramientas(new FiltroHerramienta.Builder().nombreContiene(nombreBuscado).build()));
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
		}
	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			stage.setTitle("Lista de herramientas");
			tablaHerramientas.getItems().clear();
			tablaHerramientas.getItems().addAll(herramientasAGuardar);
			try{
				tablaHerramientas.getItems().addAll(coordinador.listarHerramientas(new FiltroHerramienta.Builder().build()));
			} catch(PersistenciaException e){
				presentadorVentanas.presentarExcepcion(e, stage);
			}
		});
	}

	@Override
	public Boolean sePuedeSalir() {
		if(herramientasAGuardar.isEmpty()){
			return true;
		}
		else{
			VentanaConfirmacion confirmacion = presentadorVentanas.presentarConfirmacion("¿Quiere salir sin guardar?",
					"Hay herramientas nuevas sin guardar, si sale ahora se perderán los cambios.", stage);
			if(confirmacion.acepta()){
				return true;
			}
		}
		return false;
	}
}
