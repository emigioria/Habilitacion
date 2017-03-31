/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import proy.datos.entidades.Comentario;
import proy.datos.entidades.Operario;
import proy.datos.filtros.implementacion.FiltroOperario;
import proy.excepciones.PersistenciaException;
import proy.gui.ControladorDialogo;
import proy.logica.gestores.resultados.ResultadoCrearComentario;
import proy.logica.gestores.resultados.ResultadoCrearComentario.ErrorCrearComentario;

public class NComentarioController extends ControladorDialogo {

	public static final String URL_VISTA = "/proy/gui/vistas/NComentario.fxml";

	private Comentario comentario;

	@FXML
	private ComboBox<Operario> cbOperario;

	@FXML
	private TextArea taComentarioStr;

	@FXML
	private Button guardar;

	@FXML
	private void guardar() {
		ResultadoCrearComentario resultadoCrearComentario;
		StringBuffer erroresBfr = new StringBuffer();

		//Tomar datos de la vista
		comentario = new Comentario();
		comentario.setFechaComentario(new Date());
		comentario.setOperario(cbOperario.getValue());
		comentario.setTexto(taComentarioStr.getText().trim());

		//Inicio transacciones al gestor
		try{
			resultadoCrearComentario = coordinador.crearComentario(comentario);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}

		//Tratamiento de errores
		if(resultadoCrearComentario.hayErrores()){
			for(ErrorCrearComentario err: resultadoCrearComentario.getErrores()){
				switch(err) {
				case SIN_OPERARIO:
					erroresBfr.append("No ha seleccionado ningún operario para su comentario.\n");
					break;
				case TEXTO_VACIO:
					erroresBfr.append("No ha ingresado ningún texto en su comentario.\n");
					break;
				}
			}

			String errores = erroresBfr.toString();
			if(!errores.isEmpty()){
				presentadorVentanas.presentarError("Error al crear comentario", errores, stage);
			}
		}
		else{
			presentadorVentanas.presentarInformacion("Operación exitosa", "Se ha creado correctamente el comentario.", stage);
			salir();
		}
	}

	@Override
	protected void inicializar() {
		stage.setTitle("Nuevo comentario");
		try{
			cbOperario.getItems().addAll(coordinador.listarOperarios(new FiltroOperario.Builder().build()));
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
		}

		cbOperario.valueProperty().addListener((obs, oldO, newO) -> {
			cambiarGuardar();
		});
		taComentarioStr.textProperty().addListener((obs, oldO, newO) -> {
			cambiarGuardar();
		});
	}

	private void cambiarGuardar() {
		guardar.setDisable(cbOperario.getValue() == null || taComentarioStr.getText().trim().isEmpty());
	}
}
