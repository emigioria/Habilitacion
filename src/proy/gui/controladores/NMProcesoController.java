/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;
import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Maquina;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.datos.entidades.Proceso;
import proy.datos.filtros.implementacion.FiltroDescripcionProceso;
import proy.datos.filtros.implementacion.FiltroHerramienta;
import proy.datos.filtros.implementacion.FiltroMaquina;
import proy.datos.filtros.implementacion.FiltroParte;
import proy.datos.filtros.implementacion.FiltroPieza;
import proy.datos.filtros.implementacion.FiltroTipoProceso;
import proy.excepciones.PersistenciaException;
import proy.gui.ControladorRomano;
import proy.logica.gestores.resultados.ResultadoCrearProceso;
import proy.logica.gestores.resultados.ResultadoCrearProceso.ErrorCrearProceso;
import proy.logica.gestores.resultados.ResultadoModificarProceso;
import proy.logica.gestores.resultados.ResultadoModificarProceso.ErrorModificarProceso;

public class NMProcesoController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/NMProceso.fxml";

	@FXML
	private ComboBox<Maquina> cbMaquina;

	@FXML
	private ComboBox<Parte> cbParte;

	@FXML
	private ComboBox<String> cbDescripcion;

	@FXML
	private ComboBox<String> cbTipo;

	@FXML
	private Spinner<Integer> spHsTTPreparacion;

	@FXML
	private Spinner<Integer> spMsTTPreparacion;

	@FXML
	private Spinner<Integer> spSsTTPreparacion;

	@FXML
	private Spinner<Integer> spHsTTProceso;

	@FXML
	private Spinner<Integer> spMsTTProceso;

	@FXML
	private Spinner<Integer> spSsTTProceso;

	@FXML
	private Label lbTiempoPromedioProceso;

	@FXML
	private ComboBox<Pieza> cbPieza;

	@FXML
	private ListView<Pieza> listaPiezas;

	@FXML
	private ComboBox<Herramienta> cbHerramienta;

	@FXML
	private ListView<Herramienta> listaHerramientas;

	@FXML
	private TextArea observacionesProceso;

	private String titulo;

	private Proceso proceso;

	@FXML
	public void agregarPieza() {
		Pieza piezaAAgregar = cbPieza.getSelectionModel().getSelectedItem();
		if(piezaAAgregar != null){
			return;
		}
		cbPieza.getItems().remove(piezaAAgregar);
		cbPieza.getSelectionModel().select(null);
		listaPiezas.getItems().add(piezaAAgregar);
	}

	@FXML
	public void quitarPieza() {
		Pieza piezaAQuitar = listaPiezas.getSelectionModel().getSelectedItem();
		if(piezaAQuitar != null){
			return;
		}
		cbPieza.getItems().add(piezaAQuitar);
		listaPiezas.getItems().remove(piezaAQuitar);
	}

	@FXML
	public void agregarHerramienta() {
		Herramienta herramientaAAgregar = cbHerramienta.getSelectionModel().getSelectedItem();
		if(herramientaAAgregar != null){
			return;
		}
		cbHerramienta.getItems().remove(herramientaAAgregar);
		cbHerramienta.getSelectionModel().select(null);
		listaHerramientas.getItems().add(herramientaAAgregar);
	}

	@FXML
	public void quitarHerramienta() {
		Herramienta herramientaAQuitar = listaHerramientas.getSelectionModel().getSelectedItem();
		if(herramientaAQuitar != null){
			return;
		}
		cbHerramienta.getItems().add(herramientaAQuitar);
		listaHerramientas.getItems().remove(herramientaAQuitar);
	}

	@FXML
	public void guardar() {
		Boolean hayErrores = true;
		if(proceso == null){
			hayErrores = crearProceso();
		}
		else{
			hayErrores = modificarProceso();
		}
		if(!hayErrores){
			salir();
		}
	}

	private Boolean crearProceso() {
		ResultadoCrearProceso resultadoCrearProceso;
		StringBuffer erroresBfr = new StringBuffer();
		Proceso proceso = new Proceso();

		//Toma de datos de la vista
		proceso.setParte(cbParte.getValue());
		proceso.setDescripcion(cbDescripcion.getValue().trim());
		proceso.setTipo(cbTipo.getValue().trim());

		long segsTTP = spSsTTPreparacion.getValue();
		long minsTTP = spMsTTPreparacion.getValue();
		long horasTTP = spHsTTPreparacion.getValue();
		long milisTTP = horasTTP * 3600000 + minsTTP * 60000 + segsTTP * 1000;
		proceso.setTiempoTeoricoPreparacion(milisTTP);

		segsTTP = spSsTTProceso.getValue();
		minsTTP = spMsTTProceso.getValue();
		horasTTP = spHsTTProceso.getValue();
		milisTTP = horasTTP * 3600000 + minsTTP * 60000 + segsTTP * 1000;
		proceso.setTiempoTeoricoProceso(milisTTP);

		proceso.getPiezas().clear();
		proceso.getPiezas().addAll(listaPiezas.getItems());

		proceso.getHerramientas().clear();
		proceso.getHerramientas().addAll(listaHerramientas.getItems());

		proceso.setObservaciones(observacionesProceso.getText().trim());

		//Inicio transacciones al gestor
		try{
			resultadoCrearProceso = coordinador.crearProceso(proceso);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return true;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return true;
		}

		//Tratamiento de errores
		if(resultadoCrearProceso.hayErrores()){
			for(ErrorCrearProceso e: resultadoCrearProceso.getErrores()){
				switch(e) {

				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				presentadorVentanas.presentarError("Error al crear el proceso", errores, stage);
			}
			return true;
		}
		else{
			presentadorVentanas.presentarInformacion("Operación exitosa", "Se ha creado el proceso con éxito", stage);
			return false;
		}
	}

	private Boolean modificarProceso() {
		ResultadoModificarProceso resultadoModificarProceso;
		StringBuffer erroresBfr = new StringBuffer();

		//Toma de datos de la vista
		proceso.setParte(cbParte.getValue());
		proceso.setDescripcion(cbDescripcion.getValue().trim());
		proceso.setTipo(cbTipo.getValue().trim());

		long segsTTP = spSsTTPreparacion.getValue();
		long minsTTP = spMsTTPreparacion.getValue();
		long horasTTP = spHsTTPreparacion.getValue();
		long milisTTP = horasTTP * 3600000 + minsTTP * 60000 + segsTTP * 1000;
		proceso.setTiempoTeoricoPreparacion(milisTTP);

		segsTTP = spSsTTProceso.getValue();
		minsTTP = spMsTTProceso.getValue();
		horasTTP = spHsTTProceso.getValue();
		milisTTP = horasTTP * 3600000 + minsTTP * 60000 + segsTTP * 1000;
		proceso.setTiempoTeoricoProceso(milisTTP);

		proceso.getPiezas().clear();
		proceso.getPiezas().addAll(listaPiezas.getItems());

		proceso.getHerramientas().clear();
		proceso.getHerramientas().addAll(listaHerramientas.getItems());

		proceso.setObservaciones(observacionesProceso.getText().trim());

		//Inicio transacciones al gestor
		try{
			resultadoModificarProceso = coordinador.modificarProceso(proceso);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return true;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return true;
		}

		//Tratamiento de errores
		if(resultadoModificarProceso.hayErrores()){
			for(ErrorModificarProceso e: resultadoModificarProceso.getErrores()){
				switch(e) {

				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				presentadorVentanas.presentarError("Error al modificar el proceso", errores, stage);
			}
			return true;
		}
		else{
			presentadorVentanas.presentarInformacion("Operación exitosa", "Se ha modificado el proceso con éxito", stage);
			return false;
		}
	}

	public void formatearNuevoProceso() {
		titulo = "Nuevo proceso";
	}

	public void formatearModificarProceso(Proceso proceso) {
		titulo = "Modificar proceso";
		this.proceso = proceso;
		cargarDatosProceso(proceso);
	}

	private void cargarDatosProceso(Proceso proceso) {
		Platform.runLater(() -> {
			cbMaquina.getSelectionModel().select(proceso.getParte().getMaquina());
			cbParte.getSelectionModel().select(proceso.getParte());
			cbDescripcion.getSelectionModel().select(proceso.getDescripcion());
			cbTipo.getSelectionModel().select(proceso.getTipo());

			long milisTTP = proceso.getTiempoTeoricoPreparacion();
			long segsTTP = (milisTTP / 1000) % 60;
			long minsTTP = (milisTTP / 60000) % 60;
			long horasTTP = milisTTP / 3600000;
			spHsTTPreparacion.getValueFactory().setValue((int) horasTTP);
			spMsTTPreparacion.getValueFactory().setValue((int) minsTTP);
			spSsTTPreparacion.getValueFactory().setValue((int) segsTTP);

			milisTTP = proceso.getTiempoTeoricoProceso();
			segsTTP = (milisTTP / 1000) % 60;
			minsTTP = (milisTTP / 60000) % 60;
			horasTTP = milisTTP / 3600000;
			spHsTTProceso.getValueFactory().setValue((int) horasTTP);
			spMsTTProceso.getValueFactory().setValue((int) minsTTP);
			spSsTTProceso.getValueFactory().setValue((int) segsTTP);

			milisTTP = proceso.getTiempoPromedioProceso();
			segsTTP = (milisTTP / 1000) % 60;
			minsTTP = (milisTTP / 60000) % 60;
			horasTTP = milisTTP / 3600000;
			lbTiempoPromedioProceso.setText(horasTTP + "hs " + minsTTP + "ms " + segsTTP + "ss");

			cbPieza.getItems().removeAll(proceso.getPiezas());
			listaPiezas.getItems().addAll(proceso.getPiezas());
			cbHerramienta.getItems().removeAll(proceso.getHerramientas());
			listaHerramientas.getItems().addAll(proceso.getHerramientas());
			observacionesProceso.setText(proceso.getObservaciones());
		});
	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			stage.setTitle(titulo);

			Maquina maquinaAnterior = cbMaquina.getValue();
			cbMaquina.getItems().clear();
			Parte parteAnterior = cbParte.getValue();
			cbParte.getItems().clear();
			String descripcionAnterior = cbDescripcion.getValue();
			List<Pieza> piezasAnteriores = listaPiezas.getItems();
			cbDescripcion.getItems().clear();
			String tipoAnterior = cbTipo.getValue();
			cbTipo.getItems().clear();
			cbPieza.getItems().clear();
			cbHerramienta.getItems().clear();

			try{
				cbMaquina.getItems().addAll(coordinador.listarMaquinas(new FiltroMaquina.Builder().build()));
				cbDescripcion.getItems().addAll(coordinador.listarDescripciones(new FiltroDescripcionProceso.Builder().build()));
				cbTipo.getItems().addAll(coordinador.listarTipos(new FiltroTipoProceso.Builder().build()));
				cbPieza.getItems().addAll(coordinador.listarPiezas(new FiltroPieza.Builder().build()));
				cbHerramienta.getItems().addAll(coordinador.listarHerramientas(new FiltroHerramienta.Builder().build()));
			} catch(PersistenciaException e){
				presentadorVentanas.presentarExcepcion(e, stage);
			}

			cbMaquina.getSelectionModel().select(maquinaAnterior); //Selecciono la maquina anterior, lo que carga sus partes
			cbParte.getSelectionModel().select(parteAnterior); //Selecciono la parte anterior, lo que carga sus piezas
			piezasAnteriores.removeIf(p -> !cbParte.getItems().contains(p)); //Quito las piezas que no están en la base de datos
			cbPieza.getItems().removeAll(piezasAnteriores);
			listaPiezas.getItems().addAll(piezasAnteriores);

			cbDescripcion.getSelectionModel().select(descripcionAnterior);
			if(cbDescripcion.getValue() == null && descripcionAnterior != null && !descripcionAnterior.isEmpty()){
				cbDescripcion.setValue(descripcionAnterior);
			}
			cbTipo.getSelectionModel().select(tipoAnterior);
			if(cbTipo.getValue() == null && tipoAnterior != null && !tipoAnterior.isEmpty()){
				cbTipo.setValue(tipoAnterior);
			}
		});
	}

	@Override
	protected void inicializar() {
		spHsTTPreparacion.getEditor().setTextFormatter(getTextFormatter());
		spHsTTPreparacion.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 0));
		spMsTTPreparacion.getEditor().setTextFormatter(getTextFormatter());
		spMsTTPreparacion.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
		spSsTTPreparacion.getEditor().setTextFormatter(getTextFormatter());
		spSsTTPreparacion.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
		spHsTTProceso.getEditor().setTextFormatter(getTextFormatter());
		spHsTTProceso.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 0));
		spMsTTProceso.getEditor().setTextFormatter(getTextFormatter());
		spMsTTProceso.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
		spSsTTProceso.getEditor().setTextFormatter(getTextFormatter());
		spSsTTProceso.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));

		cbMaquina.getSelectionModel().selectedItemProperty().addListener((obs, olvV, newV) -> {
			cbParte.getItems().clear();
			if(newV != null){
				try{
					cbParte.getItems().addAll(coordinador.listarPartes(new FiltroParte.Builder().maquina(newV).build()));
				} catch(PersistenciaException e){
					presentadorVentanas.presentarExcepcion(e, stage);
				}
			}
		});
		cbParte.getSelectionModel().selectedItemProperty().addListener((obs, olvV, newV) -> {
			cbPieza.getItems().clear();
			listaPiezas.getItems().clear();
			if(newV != null){
				try{
					cbPieza.getItems().addAll(coordinador.listarPiezas(new FiltroPieza.Builder().parte(newV).build()));
				} catch(PersistenciaException e){
					presentadorVentanas.presentarExcepcion(e, stage);
				}
			}
		});
		actualizar();
	}

	private TextFormatter<Integer> getTextFormatter() {
		return new TextFormatter<>(
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
				});
	}
}
