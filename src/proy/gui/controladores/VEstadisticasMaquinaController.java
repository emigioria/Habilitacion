/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import proy.datos.clases.EstadoStr;
import proy.datos.entidades.Herramienta;
import proy.datos.entidades.Maquina;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.datos.entidades.Proceso;
import proy.datos.filtros.implementacion.FiltroProceso;
import proy.excepciones.PersistenciaException;
import proy.gui.ControladorRomano;
import proy.gui.componentes.ventanas.VentanaPersonalizada;

public class VEstadisticasMaquinaController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/VEstadisticasMaquina.fxml";

	@FXML
	private TableView<Proceso> tablaProcesos;

	@FXML
	private TableColumn<Proceso, String> columnaNombreProceso;

	@FXML
	private TableColumn<Proceso, String> columnaTTTeoricoProceso;

	@FXML
	private TableColumn<Proceso, String> columnaPorcentajeTTTProceso;

	@FXML
	private TableColumn<Proceso, String> columnaTTPromedioProceso;

	@FXML
	private TableColumn<Proceso, String> columnaPorcentajeTTPProceso;

	@FXML
	private TableView<Herramienta> tablaProcesosHerramienta;

	@FXML
	private TableColumn<Herramienta, String> columnaNombreHerramienta;

	@FXML
	private TableColumn<Herramienta, String> columnaTTTeoricoHerramienta;

	@FXML
	private TableColumn<Herramienta, String> columnaPorcentajeTTTHerramienta;

	@FXML
	private TableColumn<Herramienta, String> columnaTTPromedioHerramienta;

	@FXML
	private TableColumn<Herramienta, String> columnaPorcentajeTTPHerramienta;

	@FXML
	private TableView<Parte> tablaProcesosParte;

	@FXML
	private TableColumn<Parte, String> columnaNombreParte;

	@FXML
	private TableColumn<Parte, String> columnaTTTeoricoParte;

	@FXML
	private TableColumn<Parte, String> columnaPorcentajeTTTParte;

	@FXML
	private TableColumn<Parte, String> columnaTTPromedioParte;

	@FXML
	private TableColumn<Parte, String> columnaPorcentajeTTPParte;

	@FXML
	private TableView<Pieza> tablaProcesosPieza;

	@FXML
	private TableColumn<Pieza, String> columnaNombrePieza;

	@FXML
	private TableColumn<Pieza, String> columnaTTTeoricoPieza;

	@FXML
	private TableColumn<Pieza, String> columnaPorcentajeTTTPieza;

	@FXML
	private TableColumn<Pieza, String> columnaTTPromedioPieza;

	@FXML
	private TableColumn<Pieza, String> columnaPorcentajeTTPPieza;

	@FXML
	private Label labelMaquina;

	@FXML
	private Label labelTTT;

	@FXML
	private Label labelTPT;

	@FXML
	private TabPane tabPane;

	@FXML
	private Tab tabProceso;

	@FXML
	private Tab tabHerramienta;

	@FXML
	private Tab tabParte;

	@FXML
	private Tab tabPieza;

	private long tiempoTeoricoTotal;

	private long tiempoPromedioTotal;

	private Maquina maquina;

	private List<Proceso> procesos;

	private Map<Herramienta, List<Proceso>> procesosPorHerramienta;

	private Map<Parte, List<Proceso>> procesosPorParte;

	private Map<Pieza, List<Proceso>> procesosPorPieza;

	@Override
	protected void inicializar() {
		columnaNombreProceso.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaTTTeoricoProceso.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(conversorTiempos.milisAHsMsSsConTexto(param.getValue().getTiempoTeoricoPreparacion().longValue() + param.getValue().getTiempoTeoricoProceso().longValue()));
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaPorcentajeTTTProceso.setCellValueFactory(param -> {
			try{
				Long tiempoTeorico = param.getValue().getTiempoTeoricoPreparacion().longValue() + param.getValue().getTiempoTeoricoProceso().longValue();
				Double porcentajeTTT = tiempoTeorico.doubleValue() / tiempoTeoricoTotal;
				Double porcentajeRedondeado = ((long) (porcentajeTTT * 10000)) / 100.0;
				return new SimpleStringProperty(porcentajeRedondeado + " %");
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaTTPromedioProceso.setCellValueFactory(param -> {
			try{
				Long tiempoPromedio = param.getValue().getTiempoPromedioProceso().longValue();
				if(tiempoPromedio > 0){
					return new SimpleStringProperty(conversorTiempos.milisAHsMsSsConTexto(tiempoPromedio));
				}
				else{
					return new SimpleStringProperty("<Sin datos>");
				}
			} catch(NullPointerException e){
				return new SimpleStringProperty("<Sin datos>");
			}
		});
		columnaPorcentajeTTPProceso.setCellValueFactory(param -> {
			try{
				Long tiempoPromedio = param.getValue().getTiempoPromedioProceso().longValue();
				if(tiempoPromedio > 0){
					Double porcentajeTTT = tiempoPromedio.doubleValue() / tiempoPromedioTotal;
					Double porcentajeRedondeado = ((long) (porcentajeTTT * 10000)) / 100.0;
					return new SimpleStringProperty(porcentajeRedondeado + " %");
				}
				else{
					return new SimpleStringProperty("<Sin datos<");
				}
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaNombreHerramienta.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaTTTeoricoHerramienta.setCellValueFactory(param -> {
			try{
				Long tiempoTeorico = 0L;
				for(Proceso pro: procesosPorHerramienta.get(param.getValue())){
					tiempoTeorico += pro.getTiempoTeoricoPreparacion().longValue() + pro.getTiempoTeoricoProceso().longValue();
				}
				return new SimpleStringProperty(conversorTiempos.milisAHsMsSsConTexto(tiempoTeorico));
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaPorcentajeTTTHerramienta.setCellValueFactory(param -> {
			try{
				Long tiempoTeorico = 0L;
				for(Proceso pro: procesosPorHerramienta.get(param.getValue())){
					tiempoTeorico += pro.getTiempoTeoricoPreparacion().longValue() + pro.getTiempoTeoricoProceso().longValue();
				}
				Double porcentajeTTT = tiempoTeorico.doubleValue() / tiempoTeoricoTotal;
				Double porcentajeRedondeado = ((long) (porcentajeTTT * 10000)) / 100.0;
				return new SimpleStringProperty(porcentajeRedondeado + " %");
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaTTPromedioHerramienta.setCellValueFactory(param -> {
			try{
				Long tiempoPromedio = 0L;
				for(Proceso pro: procesosPorHerramienta.get(param.getValue())){
					tiempoPromedio += pro.getTiempoPromedioProceso().longValue();
				}
				if(tiempoPromedio > 0){
					return new SimpleStringProperty(conversorTiempos.milisAHsMsSsConTexto(tiempoPromedio));
				}
				else{
					return new SimpleStringProperty("<Sin datos>");
				}
			} catch(NullPointerException e){
				return new SimpleStringProperty("<Sin datos>");
			}
		});
		columnaPorcentajeTTPHerramienta.setCellValueFactory(param -> {
			try{
				Long tiempoPromedio = 0L;
				for(Proceso pro: procesosPorHerramienta.get(param.getValue())){
					tiempoPromedio += pro.getTiempoPromedioProceso().longValue();
				}
				if(tiempoPromedio > 0){
					Double porcentajeTTT = tiempoPromedio.doubleValue() / tiempoPromedioTotal;
					Double porcentajeRedondeado = ((long) (porcentajeTTT * 10000)) / 100.0;
					return new SimpleStringProperty(porcentajeRedondeado + " %");
				}
				else{
					return new SimpleStringProperty("<Sin datos<");
				}
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaNombreParte.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaTTTeoricoParte.setCellValueFactory(param -> {
			try{
				Long tiempoTeorico = 0L;
				for(Proceso pro: procesosPorParte.get(param.getValue())){
					tiempoTeorico += pro.getTiempoTeoricoPreparacion().longValue() + pro.getTiempoTeoricoProceso().longValue();
				}
				return new SimpleStringProperty(conversorTiempos.milisAHsMsSsConTexto(tiempoTeorico));
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaPorcentajeTTTParte.setCellValueFactory(param -> {
			try{
				Long tiempoTeorico = 0L;
				for(Proceso pro: procesosPorParte.get(param.getValue())){
					tiempoTeorico += pro.getTiempoTeoricoPreparacion().longValue() + pro.getTiempoTeoricoProceso().longValue();
				}
				Double porcentajeTTT = tiempoTeorico.doubleValue() / tiempoTeoricoTotal;
				Double porcentajeRedondeado = ((long) (porcentajeTTT * 10000)) / 100.0;
				return new SimpleStringProperty(porcentajeRedondeado + " %");
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaTTPromedioParte.setCellValueFactory(param -> {
			try{
				Long tiempoPromedio = 0L;
				for(Proceso pro: procesosPorParte.get(param.getValue())){
					tiempoPromedio += pro.getTiempoPromedioProceso().longValue();
				}
				if(tiempoPromedio > 0){
					return new SimpleStringProperty(conversorTiempos.milisAHsMsSsConTexto(tiempoPromedio));
				}
				else{
					return new SimpleStringProperty("<Sin datos>");
				}
			} catch(NullPointerException e){
				return new SimpleStringProperty("<Sin datos>");
			}
		});
		columnaPorcentajeTTPParte.setCellValueFactory(param -> {
			try{
				Long tiempoPromedio = 0L;
				for(Proceso pro: procesosPorParte.get(param.getValue())){
					tiempoPromedio += pro.getTiempoPromedioProceso().longValue();
				}
				if(tiempoPromedio > 0){
					Double porcentajeTTT = tiempoPromedio.doubleValue() / tiempoPromedioTotal;
					Double porcentajeRedondeado = ((long) (porcentajeTTT * 10000)) / 100.0;
					return new SimpleStringProperty(porcentajeRedondeado + " %");
				}
				else{
					return new SimpleStringProperty("<Sin datos<");
				}
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaNombrePieza.setCellValueFactory(param -> {
			try{
				return new SimpleStringProperty(param.getValue().toString());
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaTTTeoricoPieza.setCellValueFactory(param -> {
			try{
				Long tiempoTeorico = 0L;
				for(Proceso pro: procesosPorPieza.get(param.getValue())){
					tiempoTeorico += pro.getTiempoTeoricoPreparacion().longValue() + pro.getTiempoTeoricoProceso().longValue();
				}
				return new SimpleStringProperty(conversorTiempos.milisAHsMsSsConTexto(tiempoTeorico));
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaPorcentajeTTTPieza.setCellValueFactory(param -> {
			try{
				Long tiempoTeorico = 0L;
				for(Proceso pro: procesosPorPieza.get(param.getValue())){
					tiempoTeorico += pro.getTiempoTeoricoPreparacion().longValue() + pro.getTiempoTeoricoProceso().longValue();
				}
				Double porcentajeTTT = tiempoTeorico.doubleValue() / tiempoTeoricoTotal;
				Double porcentajeRedondeado = ((long) (porcentajeTTT * 10000)) / 100.0;
				return new SimpleStringProperty(porcentajeRedondeado + " %");
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});
		columnaTTPromedioPieza.setCellValueFactory(param -> {
			try{
				Long tiempoPromedio = 0L;
				for(Proceso pro: procesosPorPieza.get(param.getValue())){
					tiempoPromedio += pro.getTiempoPromedioProceso().longValue();
				}
				if(tiempoPromedio > 0){
					return new SimpleStringProperty(conversorTiempos.milisAHsMsSsConTexto(tiempoPromedio));
				}
				else{
					return new SimpleStringProperty("<Sin datos>");
				}
			} catch(NullPointerException e){
				return new SimpleStringProperty("<Sin datos>");
			}
		});
		columnaPorcentajeTTPPieza.setCellValueFactory(param -> {
			try{
				Long tiempoPromedio = 0L;
				for(Proceso pro: procesosPorPieza.get(param.getValue())){
					tiempoPromedio += pro.getTiempoPromedioProceso().longValue();
				}
				if(tiempoPromedio > 0){
					Double porcentajeTTT = tiempoPromedio.doubleValue() / tiempoPromedioTotal;
					Double porcentajeRedondeado = ((long) (porcentajeTTT * 10000)) / 100.0;
					return new SimpleStringProperty(porcentajeRedondeado + " %");
				}
				else{
					return new SimpleStringProperty("<Sin datos<");
				}
			} catch(NullPointerException e){
				return new SimpleStringProperty("");
			}
		});

		labelMaquina.setText("Máquina: " + maquina.toString());

		actualizar();
	}

	@FXML
	private void verGrafico() {
		ObservableList<Data> datos = FXCollections.observableArrayList();
		VentanaPersonalizada ventanaGrafico = presentadorVentanas.presentarVentanaPersonalizada(VEstadisticasMaquinaGraficoController.URL_VISTA, coordinador, stage);
		((VEstadisticasMaquinaGraficoController) ventanaGrafico.getControlador()).formatearTituloVentana("Estadísticas de máquina");
		((VEstadisticasMaquinaGraficoController) ventanaGrafico.getControlador()).formatearDatos(datos);

		if(tabPane.getSelectionModel().getSelectedItem().equals(tabProceso)){
			((VEstadisticasMaquinaGraficoController) ventanaGrafico.getControlador()).formatearTituloGrafico("Porcentaje de tiempo por proceso");
			for(Proceso pro: procesos){
				Long tiempoPromedio = pro.getTiempoPromedioProceso().longValue();
				if(tiempoPromedio > 0){
					Double porcentajeTTT = tiempoPromedio.doubleValue() / tiempoPromedioTotal;
					Double porcentajeRedondeado = ((long) (porcentajeTTT * 10000)) / 100.0;
					datos.add(new Data(pro.toString(), porcentajeRedondeado));
				}
			}
		}
		else if(tabPane.getSelectionModel().getSelectedItem().equals(tabHerramienta)){
			((VEstadisticasMaquinaGraficoController) ventanaGrafico.getControlador()).formatearTituloGrafico("Porcentaje de tiempo por herramienta");
			for(Herramienta h: procesosPorHerramienta.keySet()){
				Long tiempoPromedio = 0L;
				for(Proceso pro: procesosPorHerramienta.get(h)){
					tiempoPromedio += pro.getTiempoPromedioProceso().longValue();
				}
				if(tiempoPromedio > 0){
					Double porcentajeTTT = tiempoPromedio.doubleValue() / tiempoPromedioTotal;
					Double porcentajeRedondeado = ((long) (porcentajeTTT * 10000)) / 100.0;
					datos.add(new Data(h.toString(), porcentajeRedondeado));
				}
			}
		}
		else if(tabPane.getSelectionModel().getSelectedItem().equals(tabParte)){
			((VEstadisticasMaquinaGraficoController) ventanaGrafico.getControlador()).formatearTituloGrafico("Porcentaje de tiempo por parte");
			for(Parte p: procesosPorParte.keySet()){
				Long tiempoPromedio = 0L;
				for(Proceso pro: procesosPorParte.get(p)){
					tiempoPromedio += pro.getTiempoPromedioProceso().longValue();
				}
				if(tiempoPromedio > 0){
					Double porcentajeTTT = tiempoPromedio.doubleValue() / tiempoPromedioTotal;
					Double porcentajeRedondeado = ((long) (porcentajeTTT * 10000)) / 100.0;
					datos.add(new Data(p.toString(), porcentajeRedondeado));
				}
			}
		}
		else if(tabPane.getSelectionModel().getSelectedItem().equals(tabPieza)){
			((VEstadisticasMaquinaGraficoController) ventanaGrafico.getControlador()).formatearTituloGrafico("Porcentaje de tiempo por pieza");
			for(Pieza p: procesosPorPieza.keySet()){
				Long tiempoPromedio = 0L;
				for(Proceso pro: procesosPorPieza.get(p)){
					tiempoPromedio += pro.getTiempoPromedioProceso().longValue();
				}
				if(tiempoPromedio > 0){
					Double porcentajeTTT = tiempoPromedio.doubleValue() / tiempoPromedioTotal;
					Double porcentajeRedondeado = ((long) (porcentajeTTT * 10000)) / 100.0;
					datos.add(new Data(p.toString(), porcentajeRedondeado));
				}
			}
		}
		ventanaGrafico.showAndWait();
	}

	public void formatearMaquina(Maquina maquina) {
		this.maquina = maquina;
	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			stage.setTitle("Estadísticas de máquina");

			tablaProcesos.getItems().clear();
			tablaProcesosHerramienta.getItems().clear();
			tablaProcesosParte.getItems().clear();
			tablaProcesosPieza.getItems().clear();
			try{
				procesos = coordinador.listarProcesos(new FiltroProceso.Builder().maquina(maquina).build());
				procesosPorHerramienta = new IdentityHashMap<>();
				for(Proceso pro: procesos){
					for(Herramienta h: pro.getHerramientas()){
						if(EstadoStr.ALTA.equals(h.getEstado().getNombre())){
							procesosPorHerramienta.put(h, new ArrayList<>());
						}
					}
				}
				for(Proceso pro: procesos){
					for(Herramienta h: pro.getHerramientas()){
						if(EstadoStr.ALTA.equals(h.getEstado().getNombre())){
							procesosPorHerramienta.get(h).add(pro);
						}
					}
				}
				procesosPorParte = new IdentityHashMap<>();
				for(Proceso pro: procesos){
					Parte p = pro.getParte();
					if(EstadoStr.ALTA.equals(p.getEstado().getNombre())){
						procesosPorParte.put(p, new ArrayList<>());
					}
				}
				for(Proceso pro: procesos){
					Parte p = pro.getParte();
					if(EstadoStr.ALTA.equals(p.getEstado().getNombre())){
						procesosPorParte.get(p).add(pro);
					}
				}
				procesosPorPieza = new IdentityHashMap<>();
				for(Proceso pro: procesos){
					for(Pieza p: pro.getPiezas()){
						if(EstadoStr.ALTA.equals(p.getEstado().getNombre())){
							procesosPorPieza.put(p, new ArrayList<>());
						}
					}
				}
				for(Proceso pro: procesos){
					for(Pieza p: pro.getPiezas()){
						if(EstadoStr.ALTA.equals(p.getEstado().getNombre())){
							procesosPorPieza.get(p).add(pro);
						}
					}
				}

				tiempoTeoricoTotal = 0L;
				tiempoPromedioTotal = 0L;
				for(Proceso pro: procesos){
					tiempoTeoricoTotal += pro.getTiempoTeoricoPreparacion() + pro.getTiempoTeoricoProceso();
					tiempoPromedioTotal += pro.getTiempoPromedioProceso();
				}
				labelTTT.setText("Tiempo teórico total: " + conversorTiempos.milisAHsMsSsConTexto(tiempoTeoricoTotal));
				labelTPT.setText("Tiempo promedio total: " + conversorTiempos.milisAHsMsSsConTexto(tiempoPromedioTotal));

				tablaProcesos.getItems().addAll(procesos);
				tablaProcesosHerramienta.getItems().addAll(procesosPorHerramienta.keySet());
				tablaProcesosParte.getItems().addAll(procesosPorParte.keySet());
				tablaProcesosPieza.getItems().addAll(procesosPorPieza.keySet());
			} catch(PersistenciaException e){
				presentadorVentanas.presentarExcepcion(e, stage);
			}
		});
	}
}
