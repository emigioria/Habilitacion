/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import proy.datos.entidades.Operario;
import proy.datos.filtros.implementacion.FiltroOperario;
import proy.excepciones.PersistenciaException;
import proy.gui.ControladorRomano;
import proy.gui.componentes.tablecell.TableCellTextViewString;
import proy.gui.componentes.ventanas.VentanaConfirmacion;
import proy.logica.gestores.resultados.ResultadoCrearOperarios;
import proy.logica.gestores.resultados.ResultadoCrearOperarios.ErrorCrearOperarios;
import proy.logica.gestores.resultados.ResultadoEliminarOperario;
import proy.logica.gestores.resultados.ResultadoEliminarOperario.ErrorEliminarOperario;
import proy.logica.gestores.resultados.ResultadoEliminarTareas;
import proy.logica.gestores.resultados.ResultadoEliminarTareas.ErrorEliminarTareas;

public class AOperariosController extends ControladorRomano {

	public static final String URL_VISTA = "vistas/AOperarios.fxml";

	@FXML
	private TextField nombreOperario;

	@FXML
	private TextField apellidoOperario;

	@FXML
	private TextField dniOperario;

	@FXML
	private TableView<Operario> tablaOperarios;

	@FXML
	private TableColumn<Operario, String> columnaNombre;

	@FXML
	private TableColumn<Operario, String> columnaApellido;

	@FXML
	private TableColumn<Operario, String> columnaDNI;

	@FXML
	private Button botonEliminar;

	private ArrayList<Operario> operariosAGuardar = new ArrayList<>();;

	@Override
	protected void inicializar() {
		//Inicialización de la columna Nombre
		{
			//Seteamos el Cell Value Factory
			columnaNombre.setCellValueFactory(param -> {
				try{
					return new SimpleStringProperty(formateadorString.nombrePropio(param.getValue().getNombre().toString()));
				} catch(NullPointerException e){
					return new SimpleStringProperty("");
				}
			});
			//Seteamos el Cell Factory para permitir edición
			columnaNombre.setCellFactory(col -> {
				return new TableCellTextViewString<Operario>() {

					@Override
					protected Boolean esEditable(Operario newValue) {
						return operariosAGuardar.contains(newValue);
					}

					@Override
					public void onEdit(Operario object, String newValue) {
						object.setNombre(newValue.trim());
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
		}

		//Inicialización de la columna Apellido
		{
			//Seteamos el Cell Value Factory
			columnaApellido.setCellValueFactory(param -> {
				try{
					return new SimpleStringProperty(formateadorString.nombrePropio(param.getValue().getApellido().toString()));
				} catch(NullPointerException e){
					return new SimpleStringProperty("");
				}
			});
			//Seteamos el Cell Factory para permitir edición
			columnaApellido.setCellFactory(col -> {
				return new TableCellTextViewString<Operario>() {

					@Override
					protected Boolean esEditable(Operario newValue) {
						return operariosAGuardar.contains(newValue);
					}

					@Override
					public void onEdit(Operario object, String newValue) {
						object.setApellido(newValue.trim());
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
		}

		//Inicialización de la columna DNI
		{
			//Seteamos el Cell Value Factory
			columnaDNI.setCellValueFactory(param -> {
				try{
					return new SimpleStringProperty(param.getValue().getDNI().toString());
				} catch(NullPointerException e){
					return new SimpleStringProperty("");
				}
			});
			//Seteamos el Cell Factory para permitir edición
			columnaDNI.setCellFactory(col -> {
				return new TableCellTextViewString<Operario>() {

					@Override
					protected Boolean esEditable(Operario newValue) {
						return operariosAGuardar.contains(newValue);
					}

					@Override
					public void onEdit(Operario object, String newValue) {
						object.setDNI(newValue.trim());
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
		}

		tablaOperarios.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
			botonEliminar.setDisable(newV == null);
		});

		actualizar();
	}

	@FXML
	private void nuevoOperario() {
		if(!tablaOperarios.isEditable()){
			tablaOperarios.setEditable(true);
		}
		Operario nuevoOperario = new Operario();
		operariosAGuardar.add(nuevoOperario);
		tablaOperarios.getItems().add(0, nuevoOperario);
	}

	@FXML
	private void guardar() {
		crearOperarios();
		actualizar();
	}

	private void crearOperarios() {
		ResultadoCrearOperarios resultadoCrearOperarios;
		StringBuffer erroresBfr = new StringBuffer();

		//Toma de datos de la vista
		if(operariosAGuardar.isEmpty()){
			return;
		}

		//Inicio transacciones al gestor
		try{
			resultadoCrearOperarios = coordinador.crearOperarios(operariosAGuardar);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}

		//Tratamiento de errores
		if(resultadoCrearOperarios.hayErrores()){
			for(ErrorCrearOperarios e: resultadoCrearOperarios.getErrores()){
				switch(e) {
				case NOMBRE_INCOMPLETO:
					erroresBfr.append("Hay nombres vacíos.\n");
					break;
				case APELLIDO_INCOMPLETO:
					erroresBfr.append("Hay apellidos vacíos.\n");
					break;
				case DNI_INCOMPLETO:
					erroresBfr.append("Hay DNIs vacíos.\n");
					break;
				case DNI_YA_EXISTENTE:
					erroresBfr.append("Estos DNIs ya existen en el sistema:\n");
					for(String dniRepetido: resultadoCrearOperarios.getDNIRepetidos()){
						erroresBfr.append("\t<");
						erroresBfr.append(dniRepetido);
						erroresBfr.append(">\n");
					}
					break;
				case DNI_INGRESADO_REPETIDO:
					erroresBfr.append("Se intenta añadir dos operarios con el mismo DNI.\n");
					break;
				}
			}
			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				presentadorVentanas.presentarError("Error al crear operarios", errores, stage);
			}
		}
		else{
			tablaOperarios.setEditable(false);
			operariosAGuardar.clear();
			presentadorVentanas.presentarInformacion("Operación exitosa", "Se han guardado correctamente los operarios", stage);
		}
	}

	@FXML
	private void eliminarOperario() {
		ResultadoEliminarOperario resultadoEliminarOperario;
		StringBuffer erroresBfr = new StringBuffer();

		//Toma de datos de la vista
		Operario operarioAEliminar = tablaOperarios.getSelectionModel().getSelectedItem();
		if(operarioAEliminar == null){
			return;
		}

		//Si no fue guardada previamente se elimina sin ir al gestor
		if(operariosAGuardar.contains(operarioAEliminar)){
			operariosAGuardar.remove(operarioAEliminar);
			tablaOperarios.getItems().remove(operarioAEliminar);
			return;
		}

		//Se le pide al usuario que confirme la eliminación del operario
		VentanaConfirmacion vc = presentadorVentanas.presentarConfirmacion("Confirmar eliminar operario",
				"Se eliminará el operario <" + operarioAEliminar + "> de forma permanente.\n" +
						"¿Está seguro de que desea continuar?",
				stage);
		if(!vc.acepta()){
			return;
		}

		//Si acepta dar de baja se verifica que el operario a eliminar no tiene tareas no terminadas asociadas
		Boolean tieneTareasNoTerminadasAsociadas = false;
		try{
			tieneTareasNoTerminadasAsociadas = coordinador.tieneTareasNoTerminadasAsociadas(operarioAEliminar);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}

		//Se pregunta si quiere dar de baja estas tareas asociadas
		if(tieneTareasNoTerminadasAsociadas){
			vc = presentadorVentanas.presentarConfirmacion("Confirmar eliminar operario",
					"El operario <" + operarioAEliminar + "> tiene tareas no terminadas asociadas.\nSi continúa, esas tareas se eliminarán\n¿Está seguro que desea eliminarlo?",
					stage);
			if(!vc.acepta()){
				return;
			}
		}

		//Inicio transacciones al gestor
		try{
			resultadoEliminarOperario = coordinador.eliminarOperario(operarioAEliminar);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}

		//Tratamiento de errores
		if(resultadoEliminarOperario.hayErrores()){
			for(ErrorEliminarOperario e: resultadoEliminarOperario.getErrores()){
				switch(e) {
				case ERROR_AL_ELIMINAR_TAREAS:
					ResultadoEliminarTareas resultadoTareas = resultadoEliminarOperario.getResultadoEliminarTareas();
					if(resultadoTareas.hayErrores()){
						for(ErrorEliminarTareas eet: resultadoTareas.getErrores()){
							switch(eet) {
							case HAY_TAREA_FINALIZADA:
								throw new RuntimeException();
							}
						}
					}
					break;
				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				presentadorVentanas.presentarError("Error al eliminar operario", errores, stage);
			}
		}
		else{
			tablaOperarios.getItems().remove(operarioAEliminar);
			presentadorVentanas.presentarToast("Se ha eliminado correctamente el operario", stage);
		}
	}

	@FXML
	private void buscar() {
		String nombreBuscado = nombreOperario.getText().trim().toLowerCase();
		String apellidoBuscado = apellidoOperario.getText().trim().toLowerCase();
		String dniBuscado = dniOperario.getText().trim().toLowerCase();
		tablaOperarios.getItems().clear();
		tablaOperarios.getItems().addAll(operariosAGuardar);
		try{
			tablaOperarios.getItems().addAll(coordinador.listarOperarios(new FiltroOperario.Builder().nombre(nombreBuscado).apellido(apellidoBuscado).dni(dniBuscado).build()));
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
		}
	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			stage.setTitle("Lista de operarios");

			tablaOperarios.getItems().clear();
			tablaOperarios.getItems().addAll(operariosAGuardar);
			try{
				tablaOperarios.getItems().addAll(coordinador.listarOperarios(new FiltroOperario.Builder().build()));
			} catch(PersistenciaException e){
				presentadorVentanas.presentarExcepcion(e, stage);
			}
		});
	}

	@Override
	public Boolean sePuedeSalir() {
		if(operariosAGuardar.isEmpty()){
			return true;
		}
		else{
			VentanaConfirmacion confirmacion = presentadorVentanas.presentarConfirmacion("¿Quiere salir sin guardar?",
					"Hay operarios nuevos sin guardar, si sale ahora se perderán los cambios.", stage);
			if(confirmacion.acepta()){
				return true;
			}
		}
		return false;
	}
}
