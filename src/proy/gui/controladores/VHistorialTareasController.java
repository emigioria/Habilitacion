/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import proy.datos.clases.EstadoTareaStr;
import proy.datos.entidades.Tarea;
import proy.datos.filtros.implementacion.FiltroFechaFinalizadaTarea;
import proy.datos.filtros.implementacion.FiltroTarea;
import proy.excepciones.PersistenciaException;
import proy.gui.ControladorRomano;

public class VHistorialTareasController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/VHistorialTareas.fxml";

	@FXML
	private VBox renglonBox;

	@FXML
	private ComboBox<LocalDate> cbFecha;

	@FXML
	private Button botonAnterior;

	@FXML
	private Button botonSiguiente;

	@Override
	protected void inicializar() {
		cbFecha.getSelectionModel().selectedIndexProperty().addListener((obs, oldV, newV) -> {
			botonAnterior.setVisible(true);
			botonSiguiente.setVisible(true);
			int actual = cbFecha.getSelectionModel().getSelectedIndex();
			int primero = 0;
			int ultimo = cbFecha.getItems().size() - 1;
			if(actual < primero || actual > ultimo){
				botonAnterior.setVisible(false);
				botonSiguiente.setVisible(false);
			}
			if(actual == primero){
				botonAnterior.setVisible(false);
			}
			if(actual == ultimo){
				botonSiguiente.setVisible(false);
			}
		});
		cbFecha.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
			try{
				LocalDate fechaFin = cbFecha.getSelectionModel().getSelectedItem();
				Date fechaFinalizadaInicio = conversorTiempos.getDate(fechaFin);
				Date fechaFinalizadaFin = conversorTiempos.getDate(fechaFin.plusDays(1));
				//Obtener tareas
				List<Tarea> tareas = coordinador.listarTareas(new FiltroTarea.Builder()
						.estado(EstadoTareaStr.FINALIZADA)
						.fechaFinalizadaInicio(fechaFinalizadaInicio)
						.fechaFinalizadaFin(fechaFinalizadaFin)
						.ordenFechaFinalizada()
						.build());
				//Cargar renglones de tareas
				renglonBox.getChildren().clear();
				for(Tarea t: tareas){
					renglonBox.getChildren().add(new VHistorialTareasRenglonController(t).getRenglon());
				}
			} catch(PersistenciaException e){
				presentadorVentanas.presentarExcepcion(e, stage);
			} catch(Exception e){
				presentadorVentanas.presentarExcepcionInesperada(e, stage);
			}
		});
		actualizar();
	}

	@Override
	public void actualizar() {
		stage.setTitle("Historial de tareas finalizadas");

		cbFecha.getItems().clear();

		try{
			List<Date> fechasFin = coordinador.listarFechasFinTareas(new FiltroFechaFinalizadaTarea.Builder().estado(EstadoTareaStr.FINALIZADA).build());
			Set<LocalDate> fechasFinLD = fechasFin.stream().map(t -> conversorTiempos.getLocalDate(t)).collect(Collectors.toSet());
			cbFecha.getItems().addAll(fechasFinLD);
			cbFecha.getSelectionModel().select(0);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
		}
	}

	@FXML
	private void anterior() {
		cbFecha.getSelectionModel().select(cbFecha.getSelectionModel().getSelectedIndex() - 1);
	}

	@FXML
	private void siguiente() {
		cbFecha.getSelectionModel().select(cbFecha.getSelectionModel().getSelectedIndex() + 1);
	}

}
