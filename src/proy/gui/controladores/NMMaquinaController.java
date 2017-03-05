/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.IntegerStringConverter;
import proy.datos.entidades.Maquina;
import proy.datos.entidades.Material;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.excepciones.PersistenciaException;
import proy.gui.PresentadorExcepciones;
import proy.gui.componentes.TableCellTextView;
import proy.gui.componentes.TableCellTextViewString;
import proy.gui.componentes.VentanaConfirmacion;
import proy.gui.componentes.VentanaError;
import proy.gui.componentes.VentanaInformacion;
import proy.logica.gestores.filtros.FiltroMaterial;
import proy.logica.gestores.filtros.FiltroParte;
import proy.logica.gestores.filtros.FiltroPieza;
import proy.logica.gestores.resultados.ResultadoCrearMaquina;
import proy.logica.gestores.resultados.ResultadoCrearMaquina.ErrorCrearMaquina;
import proy.logica.gestores.resultados.ResultadoCrearModificarPartes;
import proy.logica.gestores.resultados.ResultadoCrearModificarPartes.ErrorCrearModificarPartes;
import proy.logica.gestores.resultados.ResultadoCrearPiezas;
import proy.logica.gestores.resultados.ResultadoCrearPiezas.ErrorCrearPiezas;
import proy.logica.gestores.resultados.ResultadoEliminarPartes;
import proy.logica.gestores.resultados.ResultadoEliminarPartes.ErrorEliminarPartes;
import proy.logica.gestores.resultados.ResultadoEliminarPiezas;
import proy.logica.gestores.resultados.ResultadoEliminarPiezas.ErrorEliminarPiezas;
import proy.logica.gestores.resultados.ResultadoEliminarProcesos;
import proy.logica.gestores.resultados.ResultadoEliminarProcesos.ErrorEliminarProcesos;
import proy.logica.gestores.resultados.ResultadoEliminarTareas;
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
	private TableColumn<Pieza, Material> columnaMaterialPieza;

	@FXML
	private TableColumn<Pieza, String> columnaCodigoPlanoPieza;

	@FXML
	private Label lbNMMaquina;

	private Maquina maquina;

	private ArrayList<Parte> partesAGuardar = new ArrayList<>(); //Partes nuevas no persistidas
	private ArrayList<Parte> partesAEliminar = new ArrayList<>(); //Partes persistidas a eliminar

	private Map<Parte, ArrayList<Pieza>> piezasAGuardar = new IdentityHashMap<>(); //Piezas nuevas no persistidas
	private Map<Parte, ArrayList<Pieza>> piezasAEliminar = new IdentityHashMap<>(); //Piezas persistidas a eliminar

	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			tablaPartes.setEditable(true);

			//Inicialización de la columna PARTE->NOMBRE

			//Seteamos el Cell Value Factory
			columnaNombreParte.setCellValueFactory(param -> {
				if(param.getValue() != null){
					if(param.getValue().getNombre() != null){
						return new SimpleStringProperty(formateadorString.primeraMayuscula(param.getValue().getNombre()));
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
						return new SimpleStringProperty(formateadorString.primeraMayuscula(param.getValue().getNombre()));
					}
				}
				return new SimpleStringProperty("<Sin nombre>");
			});
			//Seteamos el Cell Factory
			columnaNombrePieza.setCellFactory(col -> {
				return new TableCellTextViewString<Pieza>(Pieza.class) {

					@Override
					public void changed(ObservableValue<? extends Pieza> observable, Pieza oldValue, Pieza newValue) {
						this.setEditable(false);
						if(this.getTableRow() == null || newValue == null){
							return;
						}
						Parte parteSeleccionada = tablaPartes.getSelectionModel().getSelectedItem();
						if(parteSeleccionada == null){
							return;
						}
						if(piezasAGuardar.get(parteSeleccionada) != null && !piezasAGuardar.get(parteSeleccionada).isEmpty()){
							this.setEditable(piezasAGuardar.get(parteSeleccionada).contains(newValue));
						}
					}
				};
			});
			//Al terminar de editar, se guarda el valor
			columnaNombrePieza.setOnEditCommit(t -> {
				t.getRowValue().setNombre(t.getNewValue().toLowerCase().trim());
				//Truco para que se llame a Cell.updateItem() para que formatee el valor ingresado.
				t.getTableColumn().setVisible(false);
				t.getTableColumn().setVisible(true);
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
			//Seteamos el Cell Factory
			columnaCantidadPieza.setCellFactory(col -> {
				return new TableCellTextView<Pieza, Number>(Pieza.class, new IntegerStringConverter()) {

					@Override
					public void changed(ObservableValue<? extends Pieza> observable, Pieza oldValue, Pieza newValue) {
						this.setEditable(false);
						if(this.getTableRow() == null || newValue == null){
							return;
						}
						Parte parteSeleccionada = tablaPartes.getSelectionModel().getSelectedItem();
						if(parteSeleccionada == null){
							return;
						}
						if(piezasAGuardar.get(parteSeleccionada) != null && !piezasAGuardar.get(parteSeleccionada).isEmpty()){
							this.setEditable(piezasAGuardar.get(parteSeleccionada).contains(newValue));
						}
					}
				};
			});
			//Al terminar de editar, se guarda el valor.
			columnaCantidadPieza.setOnEditCommit(t -> {
				if(t.getNewValue() != null){
					t.getRowValue().setCantidad((Integer) t.getNewValue());
				}
				//Truco para que se llame a Cell.updateItem() para que formatee el valor ingresado.
				t.getTableColumn().setVisible(false);
				t.getTableColumn().setVisible(true);
			});

			//Inicialización de la columna PIEZA->MATERIAL

			columnaMaterialPieza.setCellValueFactory(new PropertyValueFactory<Pieza, Material>("material"));
			try{
				columnaMaterialPieza.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(coordinador.listarMateriales(new FiltroMaterial.Builder().build()))));
			} catch(PersistenciaException e){
				PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
			}
			columnaMaterialPieza.setOnEditCommit(new EventHandler<CellEditEvent<Pieza, Material>>() {

				@Override
				public void handle(CellEditEvent<Pieza, Material> t) {
					if(t.getNewValue() != null){
						Pieza pieza = t.getRowValue();
						Parte parteSeleccionada = tablaPartes.getSelectionModel().getSelectedItem();
						if(parteSeleccionada == null){
							return;
						}
						if(piezasAGuardar.get(parteSeleccionada) != null && !piezasAGuardar.get(parteSeleccionada).isEmpty()){
							if(piezasAGuardar.get(parteSeleccionada).contains(pieza)){
								pieza.setMaterial(t.getNewValue());
							}
						}
					}
					//Truco para que se llame a Cell.updateItem() para que formatee el valor ingresado.
					t.getTableColumn().setVisible(false);
					t.getTableColumn().setVisible(true);
				};
			});

			//Inicialización de la columna PIEZA->CODIGO_PLANO

			//Seteamos el Cell Value Factory
			columnaCodigoPlanoPieza.setCellValueFactory(param -> {
				if(param.getValue() != null){
					if(param.getValue().getCodigoPlano() != null){
						return new SimpleStringProperty(param.getValue().getCodigoPlano());
					}
				}
				return new SimpleStringProperty("<Sin nombre>");
			});
			//Seteamos el Cell Factory
			columnaCodigoPlanoPieza.setCellFactory(col -> {
				return new TableCellTextViewString<Pieza>(Pieza.class) {

					@Override
					public void changed(ObservableValue<? extends Pieza> observable, Pieza oldValue, Pieza newValue) {
						this.setEditable(false);
						if(this.getTableRow() == null || newValue == null){
							return;
						}
						Parte parteSeleccionada = tablaPartes.getSelectionModel().getSelectedItem();
						if(parteSeleccionada == null){
							return;
						}
						if(piezasAGuardar.get(parteSeleccionada) != null && !piezasAGuardar.get(parteSeleccionada).isEmpty()){
							this.setEditable(piezasAGuardar.get(parteSeleccionada).contains(newValue));
						}
					}
				};
			});
			//Al terminar de editar, se guarda el valor
			columnaCodigoPlanoPieza.setOnEditCommit(t ->

			{
				t.getRowValue().setCodigoPlano(t.getNewValue().trim());
				//Truco para que se llame a Cell.updateItem() para que formatee el valor ingresado.
				t.getTableColumn().setVisible(false);
				t.getTableColumn().setVisible(true);
			});

			//Cuando cambia la parte seleccionada, cargamos sus piezas
			tablaPartes.getSelectionModel().selectedIndexProperty().addListener((ovs, viejo, nuevo) -> {
				Platform.runLater(() -> {
					tablaPiezas.getItems().clear();
					try{
						Parte nueva = tablaPartes.getSelectionModel().getSelectedItem();
						if(nueva != null){
							if(!partesAGuardar.contains(nueva)){
								tablaPiezas.getItems().addAll(coordinador.listarPiezas(new FiltroPieza.Builder().parte(nueva).build()));
							}
							if(piezasAGuardar.get(nueva) != null && !piezasAGuardar.get(nueva).isEmpty()){
								tablaPiezas.getItems().addAll(piezasAGuardar.get(nueva));
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
		tablaPartes.getItems().add(0, nuevaParte);
		tablaPartes.getSelectionModel().select(null);
	}

	@FXML
	public void eliminarParte() {
		Parte parteAEliminar = tablaPartes.getSelectionModel().getSelectedItem();
		if(parteAEliminar == null){
			return;
		}

		//Se pregunta si quiere dar de baja
		VentanaConfirmacion vc = new VentanaConfirmacion("Confirmar eliminar parte",
				"¿Está seguro que desea eliminar la parte <" + parteAEliminar + ">?",
				apilador.getStage());

		if(!vc.acepta()){
			return;
		}

		//Si acepta dar de baja se verifica que la parte a eliminar no tiene tareas no terminadas asociadas
		Boolean tieneTareasNoTerminadasAsociadas = false;
		try{
			if(!partesAGuardar.contains(parteAEliminar)){
				tieneTareasNoTerminadasAsociadas = coordinador.tieneTareasNoTerminadasAsociadas(parteAEliminar);
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
			vc = new VentanaConfirmacion("Confirmar eliminar parte",
					"La parte <" + parteAEliminar + "> tiene tareas no terminadas asociadas\nSi continúa, esas tareas se eliminarán\n¿Está seguro que desea eliminarla?",
					apilador.getStage());
			if(!vc.acepta()){
				return;
			}
		}

		if(partesAGuardar.contains(parteAEliminar)){
			partesAGuardar.remove(parteAEliminar);
		}
		else{
			partesAEliminar.add(parteAEliminar);
		}
		piezasAGuardar.remove(parteAEliminar);
		piezasAEliminar.remove(parteAEliminar);
		tablaPartes.getItems().remove(parteAEliminar);
		tablaPartes.getSelectionModel().select(null);
	}

	private Boolean eliminarPartes() {
		ResultadoEliminarPartes resultadoEliminarPartes;
		StringBuffer erroresBfr = new StringBuffer();

		//Tomar datos de la vista
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
					erroresBfr.append(tratarErroresEliminarTarea(resultadoEliminarPartes.getResultadoTareas()));
					break;
				case ERROR_AL_ELIMINAR_PIEZAS:
					erroresBfr.append(tratarErroresEliminarPiezas(resultadoEliminarPartes.getResultadosEliminarPiezas()));
					break;
				case ERROR_AL_ELIMINAR_PROCESOS:
					erroresBfr.append(tratarErroresEliminarProcesos(resultadoEliminarPartes.getResultadosEliminarProcesos()));
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
			return false;
		}
	}

	private String tratarErroresEliminarProcesos(Map<String, ResultadoEliminarProcesos> resultadosEliminarProcesos) {
		String errores = "";
		for(String proceso: resultadosEliminarProcesos.keySet()){
			ResultadoEliminarProcesos resultado = resultadosEliminarProcesos.get(proceso);
			if(resultado.hayErrores()){
				for(ErrorEliminarProcesos ep: resultado.getErrores()){
					switch(ep) {
					//Todavia no hay errores en eliminar procesos
					}
				}
			}
		}
		return errores;
	}

	private String tratarErroresEliminarPiezas(Map<String, ResultadoEliminarPiezas> resultadosEliminarPiezas) {
		StringBuffer erroresBfr = new StringBuffer();
		for(String pieza: resultadosEliminarPiezas.keySet()){
			ResultadoEliminarPiezas resultado = resultadosEliminarPiezas.get(pieza);
			if(resultado.hayErrores()){
				erroresBfr.append(tratarErroresEliminarPiezas(resultado));
			}
		}

		return erroresBfr.toString();
	}

	private String tratarErroresEliminarPiezas(ResultadoEliminarPiezas resultadoEliminarPiezas) {
		StringBuffer erroresBfr = new StringBuffer();
		for(ErrorEliminarPiezas ep: resultadoEliminarPiezas.getErrores()){
			switch(ep) {
			case ERROR_AL_ELIMINAR_TAREAS:
				erroresBfr.append(tratarErroresEliminarTarea(resultadoEliminarPiezas.getResultadoTareas()));
				break;
			case ERROR_AL_ELIMINAR_PROCESOS:
				erroresBfr.append(tratarErroresEliminarProcesos(resultadoEliminarPiezas.getResultadosEliminarProcesos()));
			}
		}
		return erroresBfr.toString();
	}

	private String tratarErroresEliminarTarea(ResultadoEliminarTareas resultadoTareas) {
		String errores = "";
		if(resultadoTareas.hayErrores()){
			for(ErrorEliminarTareas ep: resultadoTareas.getErrores()){
				switch(ep) {
				//Todavia no hay errores en eliminar tarea
				}
			}
		}
		return errores;
	}

	@FXML
	public void nuevaPieza() {
		Parte parteDePiezaAGuardar = tablaPartes.getSelectionModel().getSelectedItem();
		if(parteDePiezaAGuardar == null){
			return;
		}
		Pieza nuevaPieza = new Pieza();
		if(piezasAGuardar.get(parteDePiezaAGuardar) == null || piezasAGuardar.get(parteDePiezaAGuardar).isEmpty()){
			piezasAGuardar.put(parteDePiezaAGuardar, new ArrayList<>());
		}
		piezasAGuardar.get(parteDePiezaAGuardar).add(nuevaPieza);
		tablaPiezas.getItems().add(0, nuevaPieza);
		if(!tablaPiezas.isEditable()){
			tablaPiezas.setEditable(true);
		}
	}

	@FXML
	public void eliminarPieza() {
		Parte parteDePiezaAEliminar = tablaPartes.getSelectionModel().getSelectedItem();
		Pieza piezaAEliminar = tablaPiezas.getSelectionModel().getSelectedItem();
		if(parteDePiezaAEliminar == null || piezaAEliminar == null){
			return;
		}

		//Se pregunta si quiere dar de baja
		VentanaConfirmacion vc = new VentanaConfirmacion("Confirmar eliminar pieza",
				"¿Está seguro que desea eliminar la pieza <" + piezaAEliminar + ">?",
				apilador.getStage());

		if(!vc.acepta()){
			return;
		}

		//Si acepta dar de baja se verifica que la pieza a eliminar no tiene tareas no terminadas asociadas
		Boolean tieneTareasNoTerminadasAsociadas = false;
		try{
			if(piezasAGuardar.get(parteDePiezaAEliminar) == null || !piezasAGuardar.get(parteDePiezaAEliminar).contains(piezaAEliminar)){
				tieneTareasNoTerminadasAsociadas = coordinador.tieneTareasNoTerminadasAsociadas(piezaAEliminar);
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
			vc = new VentanaConfirmacion("Confirmar eliminar pieza",
					"La pieza <" + piezaAEliminar + "> corresponde a una parte que tiene tareas no terminadas asociadas\nSi continúa, esas tareas se eliminarán\n¿Está seguro que desea eliminar la pieza?",
					apilador.getStage());
			if(!vc.acepta()){
				return;
			}
		}

		//Si la pieza a eliminar era nueva
		if(piezasAGuardar.get(parteDePiezaAEliminar).contains(piezaAEliminar)){
			piezasAGuardar.get(parteDePiezaAEliminar).remove(piezaAEliminar);
			if(piezasAGuardar.get(parteDePiezaAEliminar).isEmpty()){
				piezasAGuardar.remove(parteDePiezaAEliminar);
			}
		}
		//Si la pieza a eliminar existe en la BD
		else{
			if(piezasAGuardar.get(parteDePiezaAEliminar) == null || piezasAGuardar.get(parteDePiezaAEliminar).isEmpty()){
				piezasAEliminar.put(parteDePiezaAEliminar, new ArrayList<>());
			}
			piezasAEliminar.get(parteDePiezaAEliminar).add(piezaAEliminar);
		}

		//La quitamos de la tabla
		tablaPiezas.getItems().remove(piezaAEliminar);
	}

	private Boolean eliminarPiezas() {
		ResultadoEliminarPiezas resultadoEliminarPiezas;
		StringBuffer erroresBfr = new StringBuffer();
		ArrayList<Pieza> piezasAEliminarCompilado = new ArrayList<>();

		//comprueba si hay piezas que eliminar y las agrega a una misma lista
		for(ArrayList<Pieza> piezas: piezasAEliminar.values()){
			piezasAEliminarCompilado.addAll(piezas);
		}
		if(piezasAEliminarCompilado.isEmpty()){
			return false;
		}

		//Inicio transacciones al gestor
		try{
			resultadoEliminarPiezas = coordinador.eliminarPiezas(piezasAEliminarCompilado);
		} catch(PersistenciaException e){
			PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
			return true;
		} catch(Exception e){
			PresentadorExcepciones.presentarExcepcionInesperada(e, apilador.getStage());
			return true;
		}

		//Tratamiento de errores
		if(resultadoEliminarPiezas.hayErrores()){
			tratarErroresEliminarPiezas(resultadoEliminarPiezas);
			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				new VentanaError("Error al eliminar las piezas", errores, apilador.getStage());
			}

			return true;
		}
		else{
			partesAEliminar.clear();
			new VentanaInformacion("Operación exitosa", "Se han eliminado correctamente las piezas");
			return false;
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
		maquina.getPartes().clear();
		maquina.getPartes().addAll(partesAGuardar);
		for(Parte parte: partesAGuardar){
			parte.setMaquina(maquina);
			if(piezasAGuardar.get(parte) != null && !piezasAGuardar.get(parte).isEmpty()){
				parte.getPiezas().clear();
				parte.getPiezas().addAll(piezasAGuardar.get(parte));
				for(Pieza pieza: parte.getPiezas()){
					pieza.setParte(parte);
				}
			}
		}

		//Inicio transacciones al gestor
		try{
			resultado = coordinador.crearMaquina(maquina); //TODO no olvidar validar la creacion de partes y piezas (si tiene seteada alguna)
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

		if(this.eliminarPiezas()){
			return true;
		}
		if(this.eliminarPartes()){
			return true;
		}

		maquina.getPartes().addAll(partesAGuardar);
		maquina.getPartes().removeAll(tablaPartes.getItems());
		maquina.getPartes().addAll(tablaPartes.getItems());

		for(Parte parte: maquina.getPartes()){
			if(piezasAGuardar.get(parte) != null && !piezasAGuardar.get(parte).isEmpty()){
				parte.getPiezas().addAll(piezasAGuardar.get(parte));
			}
		}

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
				case ERROR_AL_CREAR_O_MODIFICAR_PARTES:
					erroresBfr.append(tratarErroresCrearModificarPartes(resultado.getResultadoCrearModificarPartes(), 1));
				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				new VentanaError("Error al modificar la máquina", errores, apilador.getStage());
			}
			return true;
		}
		else{
			new VentanaInformacion("Operación exitosa", "Se ha modificado la máquina con éxito");
			return false;
		}
	}

	private String tratarErroresCrearModificarPartes(ResultadoCrearModificarPartes resultadoCrearModificarPartes, int nivelIndentacion) {
		StringBuffer erroresBfr = new StringBuffer();
		StringBuffer indentacion = new StringBuffer();
		for(int i = 0; i < nivelIndentacion; i++){
			indentacion.append('\t');
		}

		for(ErrorCrearModificarPartes e: resultadoCrearModificarPartes.getErrores()){
			switch(e) {
			case NOMBRE_INCOMPLETO:
				erroresBfr.append(indentacion);
				erroresBfr.append("Hay partes con nombre vacío.\n");
				break;
			case NOMBRE_INGRESADO_REPETIDO:
				erroresBfr.append(indentacion);
				erroresBfr.append("Hay partes nuevas o modificadas con el mismo nombre.\n");
				break;
			case NOMBRE_YA_EXISTENTE:
				erroresBfr.append(indentacion);
				erroresBfr.append("Estas partes ya existen en el sistema:\n");
				for(String parte: resultadoCrearModificarPartes.getNombresYaExistentes()){
					erroresBfr.append("\t<");
					erroresBfr.append(parte);
					erroresBfr.append(">\n");
				}
				break;
			case ERROR_AL_CREAR_PIEZAS:
				for(Map.Entry<String, ResultadoCrearPiezas> ParteYResultadoCrearPiezas: resultadoCrearModificarPartes.getResultadosCrearPiezas().entrySet()){
					erroresBfr.append(indentacion);
					erroresBfr.append("Errores en la creación de las piezas para la parte <");
					erroresBfr.append(ParteYResultadoCrearPiezas.getKey());
					erroresBfr.append(">:\n");
					erroresBfr.append(tratarErroresCrearPiezas(ParteYResultadoCrearPiezas.getValue(), nivelIndentacion + 1));
				}
			}
		}

		return erroresBfr.toString();
	}

	private String tratarErroresCrearPiezas(ResultadoCrearPiezas resultadoCrearPiezas, int nivelIndentacion) {
		StringBuffer erroresBfr = new StringBuffer();
		StringBuffer indentacion = new StringBuffer();
		for(int i = 0; i < nivelIndentacion; i++){
			indentacion.append('\t');
		}

		for(ErrorCrearPiezas e: resultadoCrearPiezas.getErrores()){
			switch(e) {
			case NOMBRE_INCOMPLETO:
				erroresBfr.append(indentacion);
				erroresBfr.append("Hay piezas con nombre vacío.\n");
				break;
			case NOMBRE_INGRESADO_REPETIDO:
				erroresBfr.append(indentacion);
				erroresBfr.append("Hay piezas nuevas con el mismo nombre.\n");
				break;
			case NOMBRE_YA_EXISTENTE:
				erroresBfr.append(indentacion);
				erroresBfr.append("Estas piezas ya existen en el sistema:\n");
				for(String pieza: resultadoCrearPiezas.getNombresYaExistentes()){
					erroresBfr.append(indentacion);
					erroresBfr.append("\t<");
					erroresBfr.append(pieza);
					erroresBfr.append(">\n");
				}
				break;
			}
		}

		return erroresBfr.toString();
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
					tablaPartes.getItems().removeAll(partesAEliminar);
					tablaPartes.getItems().addAll(partesAGuardar);
					tablaPiezas.getItems().clear();
				}
			} catch(PersistenciaException e){
				PresentadorExcepciones.presentarExcepcion(e, apilador.getStage());
			}
			apilador.getStage().setTitle(titulo);
		});
	}
}
