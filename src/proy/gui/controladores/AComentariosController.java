/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.util.Date;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import proy.datos.entidades.Comentario;
import proy.datos.entidades.Operario;
import proy.datos.filtros.implementacion.FiltroComentario;
import proy.datos.filtros.implementacion.FiltroOperario;
import proy.excepciones.PersistenciaException;
import proy.gui.ControladorRomano;

public class AComentariosController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/AComentarios.fxml";

	@FXML
	private VBox listaComentariosBox;

	@FXML
	private ComboBox<Operario> cbOperarioBuscado;

	private Operario nullOperario = new Operario() {
		@Override
		public String toString() {
			return "Operario";
		}
	};

	@FXML
	private DatePicker dpDespuesDeBuscado;

	@FXML
	private DatePicker dpAntesDeBuscado;

	private ObservableList<Comentario> listaComentarios = FXCollections.observableArrayList();;

	@Override
	protected void inicializar() {
		listaComentarios.addListener((javafx.collections.ListChangeListener.Change<? extends Comentario> c) -> {
			listaComentariosBox.getChildren().clear();
			for(Comentario comentario: c.getList()){
				VBox contenedor = new VBox();

				//Setear nombre operario
				Label operario = new Label();
				operario.setStyle("-fx-background-color: transparent; -fx-background-insets: 0px;");
				operario.setText("Operario: " + comentario.getOperario().toString());
				contenedor.getChildren().add(operario);

				//Setear fecha comentario
				Label fecha = new Label();
				fecha.setStyle("-fx-background-color: transparent; -fx-background-insets: 0px;");
				fecha.setText("Fecha: " + conversorTiempos.diaMesAnioHoraYMinutosToString(comentario.getFechaComentario()));
				contenedor.getChildren().add(fecha);

				//Setear texto comentario
				Label comentarioStr = new Label();
				comentarioStr.setText(comentario.getTexto());
				comentarioStr.setWrapText(true);
				comentarioStr.setTextAlignment(TextAlignment.JUSTIFY);
				contenedor.getChildren().add(comentarioStr);

				TextArea comentarioStrArea = new TextArea();
				comentarioStrArea.setText(comentario.getTexto());
				comentarioStrArea.setWrapText(true);
				comentarioStrArea.setEditable(false);

				comentarioStrArea.focusedProperty().addListener((obs, oldV, newV) -> {
					if(!newV){
						contenedor.getChildren().add(comentarioStr);
						contenedor.getChildren().remove(comentarioStrArea);
					}
				});

				comentarioStr.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
					contenedor.getChildren().remove(comentarioStr);
					contenedor.getChildren().add(comentarioStrArea);
					comentarioStrArea.requestFocus();
				});
				listaComentariosBox.getChildren().add(contenedor);
			}
		});

		actualizar();
	}

	@FXML
	public void buscar() {
		Operario operarioBuscado = cbOperarioBuscado.getValue();
		if(cbOperarioBuscado.getValue() == nullOperario){
			operarioBuscado = null;
		}
		Date fechaInicio = conversorTiempos.getDate(dpDespuesDeBuscado.getValue());
		Date fechaFin = conversorTiempos.getDate(dpAntesDeBuscado.getValue());
		listaComentarios.clear();
		try{
			listaComentarios.addAll(coordinador.listarComentarios(new FiltroComentario.Builder().operario(operarioBuscado).fechaInicio(fechaInicio).fechaFin(fechaFin).build()));
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
		}
	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			stage.setTitle("Lista de comentarios");

			listaComentarios.clear();

			cbOperarioBuscado.getItems().clear();
			cbOperarioBuscado.getItems().add(nullOperario);

			try{
				listaComentarios.addAll(coordinador.listarComentarios(new FiltroComentario.Builder().build()));

				cbOperarioBuscado.getItems().addAll(coordinador.listarOperarios(new FiltroOperario.Builder().build()));
			} catch(PersistenciaException e){
				presentadorVentanas.presentarExcepcion(e, stage);
			}

			listaComentarios.sort((c1, c2) -> {
				return c2.getFechaComentario().compareTo(c1.getFechaComentario());
			});
		});
	}

}
