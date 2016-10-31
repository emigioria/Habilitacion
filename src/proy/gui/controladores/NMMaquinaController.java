/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.converter.IntegerStringConverter;
import proy.comun.FormateadorString;
import proy.datos.entidades.Maquina;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.excepciones.PersistenciaException;
import proy.gui.PresentadorExcepciones;
import proy.gui.componentes.TableCellTextView;
import proy.gui.componentes.TableCellTextViewString;
import proy.gui.componentes.VentanaConfirmacion;
import proy.gui.componentes.VentanaError;
import proy.gui.componentes.VentanaInformacion;
import proy.logica.gestores.filtros.FiltroParte;
import proy.logica.gestores.filtros.FiltroPieza;
import proy.logica.gestores.resultados.ResultadoCrearMaquina;
import proy.logica.gestores.resultados.ResultadoCrearMaquina.ErrorCrearMaquina;
import proy.logica.gestores.resultados.ResultadoEliminarPartes;
import proy.logica.gestores.resultados.ResultadoEliminarPartes.ErrorEliminarPartes;
import proy.logica.gestores.resultados.ResultadoEliminarTareas.ErrorEliminarTareas;
import proy.logica.gestores.resultados.ResultadoModificarMaquina;
import proy.logica.gestores.resultados.ResultadoModificarMaquina.ErrorModificarMaquina;

public class NMMaquinaController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/NMMaquina.fxml";

	private String titulo;

	@FXML
	private TextField nombreMaquina;

	@FXML
	private TextField nombreParte;

	@FXML
	private TextField nombrePieza;

	@FXML
	private TableView<Parte> tablaPartes;

	@FXML
	private TableColumn<Parte, String> columnaNombreParte;

	@FXML
	private TableColumn<Parte, Number> columnaCantidadParte;

	@FXML
	private TableView<Pieza> tablaPiezas;

	@FXML
	private TableColumn<Pieza, String> columnaNombrePieza;

	@FXML
	private TableColumn<Pieza, Number> columnaCantidadPieza;

	@FXML
	private TableColumn<Pieza, String> columnaMaterialPieza;

	@FXML
	private TableColumn<Pieza, String> columnaCodigoPlanoPieza;

	@FXML
	private Label lbNMMaquina;

	private Maquina maquina;

	private ArrayList<Parte> partesAGuardar = new ArrayList<>();
	private ArrayList<Parte> partesAEliminar = new ArrayList<>();

	private HashMap<Parte, ArrayList<Pieza>> piezasAGuardar = new HashMap<>();
	private HashMap<Parte, ArrayList<Pieza>> piezasAEliminar = new HashMap<>();

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			tablaPartes.setEditable(true);

			//Inicialización de la columna PARTE->NOMBRE

			//Seteamos el Cell Value Factory
			columnaNombreParte.setCellValueFactory(param -> {
				if(param.getValue() != null){
					if(param.getValue().getNombre() != null){
						return new SimpleStringProperty(FormateadorString.primeraMayuscula(param.getValue().getNombre()));
					}
				}
				return new SimpleStringProperty("<Sin nombre>");
			});
			//Seteamos el Cell Factory
			columnaNombreParte.setCellFactory(col -> {
				return new TableCellTextViewString<Parte>(Parte.class) {

					@Override
					public void changed(ObservableValue<? extends Parte> observable, Parte oldValue, Parte newValue) {

					}
				};
			});
			//Al terminar de editar, se guarda el valor
			columnaNombreParte.setOnEditCommit(t -> {
				t.getRowValue().setNombre(t.getNewValue().toLowerCase().trim());
				//Truco para que se llame a Cell.updateItem() para que formatee el valor ingresado.
				t.getTableColumn().setVisible(false);
				t.getTableColumn().setVisible(true);
			});

			//Inicialización de la columna PARTE->CANTIDAD

			//Seteamos el Cell Value Factory
			columnaCantidadParte.setCellValueFactory(param -> {
				if(param.getValue() != null){
					if(param.getValue().getCantidad() != null){
						return new SimpleIntegerProperty(param.getValue().getCantidad());
					}
				}
				return new SimpleIntegerProperty(-1);
			});
			//Seteamos el Cell Factory
			columnaCantidadParte.setCellFactory(col -> {
				return new TableCellTextView<Parte, Number>(Parte.class, new IntegerStringConverter()) {

					@Override
					public void changed(ObservableValue<? extends Parte> observable, Parte oldValue, Parte newValue) {

					}
				};
			});
			//Al terminar de editar, se guarda el valor.
			columnaCantidadParte.setOnEditCommit(t -> {
				if(t.getNewValue() != null){
					t.getRowValue().setCantidad((Integer) t.getNewValue());
				}
				//Truco para que se llame a Cell.updateItem() para que formatee el valor ingresado.
				t.getTableColumn().setVisible(false);
				t.getTableColumn().setVisible(true);
			});

			//Inicialización de la columna PIEZA->NOMBRE

			//Seteamos el Cell Value Factory
			columnaNombrePieza.setCellValueFactory(param -> {
				if(param.getValue() != null){
					if(param.getValue().getNombre() != null){
						return new SimpleStringProperty(FormateadorString.primeraMayuscula(param.getValue().getNombre()));
					}
				}
				return new SimpleStringProperty("<Sin nombre>");
			});

			//Inicialización de la columna PIEZA->CANTIDAD

			//Seteamos el Cell Value Factory
			columnaCantidadPieza.setCellValueFactory(param -> {
				if(param.getValue() != null){
					if(param.getValue().getCantidad() != null){
						return new SimpleIntegerProperty(param.getValue().getCantidad());
					}
				}
				return new SimpleIntegerProperty(-1);
			});

			//Inicialización de la columna PIEZA->MATERIAL

			//Seteamos el Cell Value Factory
			columnaMaterialPieza.setCellValueFactory(param -> {
				if(param.getValue() != null){
					if(param.getValue().getMaterial() != null){
						if(param.getValue().getMaterial().getNombre() != null){
							return new SimpleStringProperty(FormateadorString.primeraMayuscula(param.getValue().getMaterial().getNombre()));
						}
					}
				}
				return new SimpleStringProperty("<Sin nombre>");
			});

			//Inicialización de la columna PIEZA->CODIGO->PLANO

			//Seteamos el Cell Value Factory
			columnaCodigoPlanoPieza.setCellValueFactory(param -> {
				if(param.getValue() != null){
					if(param.getValue().getCodigoPlano() != null){
						return new SimpleStringProperty(FormateadorString.primeraMayuscula(param.getValue().getCodigoPlano()));
					}
				}
				return new SimpleStringProperty("<Sin nombre>");
			});

			//Cuando cambia la parte seleccionada, cargamos sus piezas
			tablaPartes.getSelectionModel().selectedItemProperty().addListener((ovs, viejo, nuevo) -> {
				Platform.runLater(() -> {
					tablaPiezas.getItems().clear();
					try{
						if(nuevo != null){
							if(!partesAGuardar.contains(nuevo)){
								tablaPiezas.getItems().addAll(coordinador.listarPiezas(new FiltroPieza.Builder().parte(nuevo).build()));
							}
							else{
								tablaPiezas.getItems().addAll(piezasAGuardar.get(nuevo));
							}
						}
					} catch(PersistenciaException e){
						PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
					}
				});
			});

			actualizar();
		});
	}

	@FXML
	public void nuevaParte() {
		Parte nuevaParte = new Parte();
		partesAGuardar.add(nuevaParte);
		piezasAGuardar.put(nuevaParte, new ArrayList<>());
		tablaPartes.getItems().add(0, nuevaParte);
	}

	@FXML
	public void eliminarParte() {
		Parte parteAEliminar = tablaPartes.getSelectionModel().getSelectedItem();
		if(parteAEliminar != null){

			//Se pregunta si quiere dar de baja
			VentanaConfirmacion vc = new VentanaConfirmacion("Confirmar eliminar parte",
					"¿Está seguro que desea eliminar la parte <" + parteAEliminar + ">?",
					apilador.getStage());

			if(!vc.acepta()){
				return;
			}

			//Si acepta dar de baja se verifica que la parte a eliminar no tiene tareas no terminadas asociadas
			Boolean tieneTareasNoTerminadasAsociadas;
			try{
				tieneTareasNoTerminadasAsociadas = coordinador.tieneTareasNoTerminadasAsociadas(parteAEliminar);
			} catch(PersistenciaException e){
				PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
				return;
			} catch(Exception e){
				PresentadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
				return;
			}

			//Se pregunta si quiere dar de baja estas tareas asociadas
			if(tieneTareasNoTerminadasAsociadas){
				vc = new VentanaConfirmacion("Confirmar eliminar parte",
						"La parte a eliminar tiene tareas no terminadas asociadas\n¿Está seguro que desea eliminar la parte <" + parteAEliminar + ">?",
						apilador.getStage());
				if(!vc.acepta()){
					return;
				}
			}

			if(partesAGuardar.contains(parteAEliminar)){
				partesAGuardar.remove(parteAEliminar);
				piezasAGuardar.remove(parteAEliminar);
			}
			else{
				partesAEliminar.add(parteAEliminar);
			}
			tablaPartes.getItems().remove(parteAEliminar);
		}
	}

	public Boolean eliminarPartes() {
		ResultadoEliminarPartes resultadoEliminarPartes;
		StringBuffer erroresBfr = new StringBuffer();

		if(partesAEliminar.isEmpty()){
			return false;
		}

		//Inicio transacciones al gestor
		try{
			resultadoEliminarPartes = coordinador.eliminarPartes(partesAEliminar);
		} catch(PersistenciaException e){
			PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
			return true;
		} catch(Exception e){
			PresentadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
			return true;
		}
		
		//Tratamiento de errores
		if(resultadoEliminarPartes.hayErrores()){
			for(ErrorEliminarPartes ep: resultadoEliminarPartes.getErrores()){
				switch(ep) {
				case ERROR_AL_ELIMINAR_TAREAS:
					for(ErrorEliminarTareas et: resultadoEliminarPartes.getResultadoTareas().getErrores()){
						switch(et){
						//no hay errores de eliminar tareas aún
						}
					}
					break;
				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				new VentanaError("Error al eliminar las partes", errores, apilador.getStage());
			}
			
			return true;
		}
		else{
			partesAEliminar.clear();
			new VentanaInformacion("Operación exitosa", "Se han eliminado correctamente las partes");
		}

		return false;
	}

	@FXML
	public void nuevaPieza() {
		Parte parteDePiezaAGuardar = tablaPartes.getSelectionModel().getSelectedItem();
		if(parteDePiezaAGuardar == null){
			return;
		}
		Pieza nuevaPieza = new Pieza();
		if(!piezasAGuardar.containsKey(parteDePiezaAGuardar)){
			piezasAGuardar.put(parteDePiezaAGuardar, new ArrayList<>());
		}
		piezasAGuardar.get(parteDePiezaAGuardar).add(nuevaPieza);
		tablaPiezas.getItems().add(0, nuevaPieza);
	}

	@FXML
	public void eliminarPieza() {
		Parte parteDePiezaAEliminar = tablaPartes.getSelectionModel().getSelectedItem();
		Pieza piezaAEliminar = tablaPiezas.getSelectionModel().getSelectedItem();
		if(piezaAEliminar != null){

			//Se pregunta si quiere dar de baja
			VentanaConfirmacion vc = new VentanaConfirmacion("Confirmar eliminar pieza",
					"¿Está seguro que desea eliminar la pieza <" + piezaAEliminar + ">?",
					apilador.getStage());

			if(!vc.acepta()){
				return;
			}

			//Si acepta dar de baja se verifica que la pieza a eliminar no tiene tareas no terminadas asociadas
			Boolean tieneTareasNoTerminadasAsociadas;
			try{
				tieneTareasNoTerminadasAsociadas = coordinador.tieneTareasNoTerminadasAsociadas(piezaAEliminar);
			} catch(PersistenciaException e){
				PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
				return;
			} catch(Exception e){
				PresentadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
				return;
			}

			//Se pregunta si quiere dar de baja estas tareas asociadas
			if(tieneTareasNoTerminadasAsociadas){
				vc = new VentanaConfirmacion("Confirmar eliminar pieza",
						"La pieza a eliminar corresponde a una parte que tiene tareas no terminadas asociadas\n¿Está seguro que desea eliminar la pieza <" + piezaAEliminar + ">?",
						apilador.getStage());
				if(!vc.acepta()){
					return;
				}
			}

			if(piezasAGuardar.get(parteDePiezaAEliminar).contains(piezaAEliminar)){
				piezasAGuardar.get(parteDePiezaAEliminar).remove(piezaAEliminar);
			}
			else{
				if(!piezasAEliminar.containsKey(parteDePiezaAEliminar)){
					piezasAEliminar.put(parteDePiezaAEliminar, new ArrayList<>());
				}
				piezasAEliminar.get(parteDePiezaAEliminar).add(piezaAEliminar);
			}
			tablaPiezas.getItems().remove(piezaAEliminar);
		}
	}

	@FXML
	public void guardar() {
		Boolean hayErrores = null;
		if(maquina == null){
			hayErrores = crearMaquina();
		}
		else{
			hayErrores = modificarMaquina();
		}
		if(hayErrores != null && !hayErrores){
			salir();
		}
	}

	private Boolean crearMaquina() {
		ResultadoCrearMaquina resultado;
		StringBuffer erroresBfr = new StringBuffer();
		Maquina maquina = new Maquina();

		//Toma de datos de la vista
		maquina.setNombre(nombreMaquina.getText().toLowerCase().trim());

		//Inicio transacciones al gestor
		try{
			resultado = coordinador.crearMaquina(maquina);
		} catch(PersistenciaException e){
			PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
			return true;
		} catch(Exception e){
			PresentadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
			return true;
		}

		//Tratamiento de errores
		if(resultado.hayErrores()){
			for(ErrorCrearMaquina e: resultado.getErrores()){
				switch(e) {
				case NOMBRE_INCOMPLETO:
					erroresBfr.append("El nombre de la máquina está vacío.\n");
					break;
				case NOMBRE_REPETIDO:
					erroresBfr.append("Ya existe una máquina con ese nombre en la Base de Datos.\n");
					break;
				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				new VentanaError("Error al crear la máquina", errores, apilador.getStage());
			}
			return true;
		}
		else{
			new VentanaInformacion("Operación exitosa", "Se ha creado la máquina con éxito");
			return false;
		}
	}

	private Boolean modificarMaquina() {

		ResultadoModificarMaquina resultado;
		StringBuffer erroresBfr = new StringBuffer();

		//Toma de datos de la vista
		maquina.setNombre(nombreMaquina.getText().toLowerCase().trim());

		//Inicio transacciones al gestor
		try{
			resultado = coordinador.modificarMaquina(maquina);
		} catch(PersistenciaException e){
			PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
			return true;
		} catch(Exception e){
			PresentadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
			return true;
		}

		//Tratamiento de errores
		if(resultado.hayErrores()){
			for(ErrorModificarMaquina e: resultado.getErrores()){
				switch(e) {
				case NOMBRE_INCOMPLETO:
					erroresBfr.append("El nombre de la máquina está vacío.\n");
					break;
				case NOMBRE_REPETIDO:
					erroresBfr.append("Ya existe una máquina con ese nombre en la Base de Datos.\n");
					break;
				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				new VentanaError("Error al crear la máquina", errores, apilador.getStage());
			}
			return true;
		}
		else{
			new VentanaInformacion("Operación exitosa", "Se ha creado la máquina con éxito");
			return false;
		}
	}

	public void formatearNuevaMaquina() {
		titulo = "Nueva máquina - Romano";
		lbNMMaquina.setText(titulo);
	}

	public void formatearModificarMaquina(Maquina maquina) {
		titulo = "Modificar máquina - Romano";
		lbNMMaquina.setText(titulo);
		this.maquina = maquina;
		nombreMaquina.setText(maquina.getNombre());
		actualizar();
	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			try{
				if(maquina != null){
					tablaPartes.getItems().clear();
					tablaPartes.getItems().addAll(coordinador.listarPartes(new FiltroParte.Builder().maquina(maquina).build()));
					tablaPiezas.getItems().clear();
				}
			} catch(PersistenciaException e){
				PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
			}
			apilador.getStage().setTitle(titulo);
		});
	}
}
