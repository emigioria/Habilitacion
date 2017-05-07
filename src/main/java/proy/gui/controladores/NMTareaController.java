/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.time.LocalDate;
import java.util.Date;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;
import proy.datos.entidades.Maquina;
import proy.datos.entidades.Operario;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Proceso;
import proy.datos.entidades.Tarea;
import proy.datos.filtros.implementacion.FiltroMaquina;
import proy.datos.filtros.implementacion.FiltroOperario;
import proy.datos.filtros.implementacion.FiltroParte;
import proy.datos.filtros.implementacion.FiltroProceso;
import proy.excepciones.PersistenciaException;
import proy.gui.ControladorRomano;
import proy.gui.componentes.ventanas.VentanaConfirmacion;
import proy.logica.gestores.resultados.ResultadoCrearTarea;
import proy.logica.gestores.resultados.ResultadoCrearTarea.ErrorCrearTarea;
import proy.logica.gestores.resultados.ResultadoModificarTarea;
import proy.logica.gestores.resultados.ResultadoModificarTarea.ErrorModificarTarea;

public class NMTareaController extends ControladorRomano {

	public static final String URL_VISTA = "vistas/NMTarea.fxml";

	@FXML
	private ComboBox<Parte> cbParte;

	private Parte nullParte = new Parte() {
		@Override
		public String toString() {
			return "Parte";
		}
	};

	@FXML
	private ComboBox<Maquina> cbMaquina;

	private Maquina nullMaquina = new Maquina() {
		@Override
		public String toString() {
			return "Máquina";
		}
	};

	@FXML
	private Spinner<Integer> cantidad;

	@FXML
	private ComboBox<Operario> cbOperario;

	@FXML
	private DatePicker fechaTarea;

	@FXML
	private TableView<Proceso> tablaProcesos;

	@FXML
	private TableColumn<Proceso, String> columnaProceso;

	@FXML
	private TableColumn<Proceso, String> columnaMaquina;

	@FXML
	private TableColumn<Proceso, String> columnaParte;

	@FXML
	private TextArea observaciones;

	private Tarea tarea;

	private String titulo;

	private Boolean guardado = false;

	@FXML
	private void buscar() {
		Maquina maquinaBuscada = cbMaquina.getValue();
		if(maquinaBuscada == nullMaquina){
			maquinaBuscada = null;
		}
		Parte parteBuscada = cbParte.getValue();
		if(parteBuscada == nullParte){
			parteBuscada = null;
		}
		tablaProcesos.getItems().clear();
		try{
			tablaProcesos.getItems().addAll(coordinador.listarProcesos(new FiltroProceso.Builder().maquina(maquinaBuscada).parte(parteBuscada).build()));
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
		}
	}

	@Override
	protected void inicializar() {
		columnaProceso.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaMaquina.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().getParte().getMaquina().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaParte.setCellValueFactory((CellDataFeatures<Proceso, String> param) -> {
			try{
				return new SimpleStringProperty(param.getValue().getParte().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});

		cbMaquina.getSelectionModel().selectedItemProperty().addListener((obs, olvV, newV) -> {
			cbParte.getItems().clear();
			cbParte.getItems().add(nullParte);
			if(newV != null && newV != nullMaquina){
				try{
					cbParte.getItems().addAll(coordinador.listarPartes(new FiltroParte.Builder().maquina(newV).build()));
				} catch(PersistenciaException e){
					presentadorVentanas.presentarExcepcion(e, stage);
				}
			}
		});

		//Inicialización del spinner de cantidad
		cantidad.getEditor().setTextFormatter(new TextFormatter<>(
				new IntegerStringConverter(), 0,
				c -> {
					if(c.isContentChange()){
						Integer numeroIngresado = null;
						try{
							numeroIngresado = new Integer(c.getControlNewText());
						} catch(Exception e){
							//No ingreso un entero;
						}
						if(numeroIngresado == null){
							return null;
						}
					}
					return c;
				}));
		cantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, 1));
		cantidad.focusedProperty().addListener((obs, oldV, newV) -> {
			cantidad.increment(0);
		});

		fechaTarea.setDayCellFactory(p -> new DateCell() {
			@Override
			public void updateItem(LocalDate ld, boolean bln) {
				super.updateItem(ld, bln);

				//Desactivo las fechas previas a hoy
				setDisable(ld.isBefore(conversorTiempos.getLocalDate(new Date())));
			}
		});
		fechaTarea.setValue(conversorTiempos.getLocalDate(new Date()));

		actualizar();

		if(tarea != null){
			cargarDatos(tarea);
		}
	}

	@FXML
	private void guardar() {
		Boolean hayErrores = true;
		if(tarea == null){
			hayErrores = crearTarea();
		}
		else{
			hayErrores = modificarTarea();
		}
		if(!hayErrores){
			guardado = true;
			salir();
		}
	}

	private Boolean crearTarea() {
		ResultadoCrearTarea resultadoCrearTarea;
		StringBuffer erroresBfr = new StringBuffer();
		Tarea tarea = new Tarea();

		//Toma de datos de la vista
		tarea.setCantidadTeorica(cantidad.getValue());
		tarea.setOperario(cbOperario.getValue());
		tarea.setFechaPlanificada(conversorTiempos.getDate(fechaTarea.getValue()));
		tarea.setProceso(tablaProcesos.getSelectionModel().getSelectedItem());
		tarea.setObservacionesTarea(observaciones.getText().trim());

		//Inicio transacción al gestor
		try{
			resultadoCrearTarea = coordinador.crearTarea(tarea);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return true;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return true;
		}

		//Tratamiento de errores
		if(resultadoCrearTarea.hayErrores()){
			for(ErrorCrearTarea r: resultadoCrearTarea.getErrores()){
				switch(r) {
				case CANTIDAD_INCOMPLETA:
					erroresBfr.append("Cantidad incompleta o menor a 1.\n");
					break;
				case FECHA_ANTERIOR_A_HOY:
					erroresBfr.append("Fecha de planificación anterior a hoy.\n");
					break;
				case FECHA_INCOMPLETA:
					erroresBfr.append("Fecha de planificación incompleta.\n");
					break;
				case OPERARIO_INCOMPLETO:
					erroresBfr.append("Operario incompleto.\n");
					break;
				case PROCESO_INCOMPLETO:
					erroresBfr.append("Proceso incompleto.\n");
					break;
				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				presentadorVentanas.presentarError("Error al crear la tarea", errores, stage);
			}
			return true;
		}
		else{
			presentadorVentanas.presentarInformacion("Operación exitosa", "Se ha creado la tarea con éxito.", stage);
			return false;
		}
	}

	private Boolean modificarTarea() {
		ResultadoModificarTarea resultadoModificarTarea;
		StringBuffer erroresBfr = new StringBuffer();

		//Toma de datos de la vista
		tarea.setCantidadTeorica(cantidad.getValue());
		tarea.setOperario(cbOperario.getValue());
		tarea.setFechaPlanificada(conversorTiempos.getDate(fechaTarea.getValue()));
		tarea.setProceso(tablaProcesos.getSelectionModel().getSelectedItem());
		tarea.setObservacionesTarea(observaciones.getText().trim());

		//Inicio transacción al gestor
		try{
			resultadoModificarTarea = coordinador.modificarTarea(tarea);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return true;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return true;
		}

		//Tratamiento de errores
		if(resultadoModificarTarea.hayErrores()){
			for(ErrorModificarTarea r: resultadoModificarTarea.getErrores()){
				switch(r) {
				case CANTIDAD_INCOMPLETA:
					erroresBfr.append("Cantidad incompleta o menor a 1.\n");
					break;
				case FECHA_ANTERIOR_A_HOY:
					erroresBfr.append("Fecha de planificación anterior a hoy.\n");
					break;
				case FECHA_INCOMPLETA:
					erroresBfr.append("Fecha de planificación incompleta.\n");
					break;
				case OPERARIO_INCOMPLETO:
					erroresBfr.append("Operario incompleto.\n");
					break;
				case PROCESO_INCOMPLETO:
					erroresBfr.append("Proceso incompleto.\n");
					break;
				case TAREA_NO_PLANIFICADA:
					erroresBfr.append("La tarea seleccionada ya fue empezada por un operario, por lo que no se puede modificar.\n");
					break;
				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				presentadorVentanas.presentarError("Error al modificar la tarea", errores, stage);
			}
			return true;
		}
		else{
			presentadorVentanas.presentarInformacion("Operación exitosa", "Se ha modificado la tarea con éxito.", stage);
			return false;
		}
	}

	public void formatearNuevaTarea() {
		titulo = "Nueva tarea";
	}

	public void formatearModificarTarea(Tarea tarea) {
		titulo = "Modificar tarea";
		this.tarea = tarea;
	}

	private void cargarDatos(Tarea tarea) {
		tablaProcesos.getSelectionModel().select(tarea.getProceso());
		cantidad.getValueFactory().setValue(tarea.getCantidadTeorica());
		cbOperario.getSelectionModel().select(tarea.getOperario());
		fechaTarea.setValue(conversorTiempos.getLocalDate(tarea.getFechaPlanificada()));
		observaciones.setText(tarea.getObservacionesTarea());
	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			stage.setTitle(titulo);

			cbOperario.getItems().clear();
			Operario operarioAnterior = cbOperario.getValue();
			tablaProcesos.getItems().clear();
			Proceso procesoAnterior = tablaProcesos.getSelectionModel().getSelectedItem();

			cbMaquina.getItems().clear();
			cbMaquina.getItems().add(nullMaquina);
			cbParte.getItems().clear();

			try{
				cbOperario.getItems().addAll(coordinador.listarOperarios(new FiltroOperario.Builder().build()));
				tablaProcesos.getItems().addAll(coordinador.listarProcesos(new FiltroProceso.Builder().build()));

				cbMaquina.getItems().addAll(coordinador.listarMaquinas(new FiltroMaquina.Builder().build()));
			} catch(PersistenciaException e){
				presentadorVentanas.presentarExcepcion(e, stage);
			}

			cbOperario.getSelectionModel().select(operarioAnterior);
			tablaProcesos.getSelectionModel().select(procesoAnterior);
		});
	}

	@Override
	public Boolean sePuedeSalir() {
		if(guardado){
			return true;
		}
		VentanaConfirmacion confirmacion = presentadorVentanas.presentarConfirmacion("¿Quiere salir sin guardar?",
				"Si sale ahora se perderán los cambios.", stage);
		if(confirmacion.acepta()){
			return true;
		}
		return false;
	}
}
