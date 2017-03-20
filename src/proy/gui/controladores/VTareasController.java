/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import proy.datos.entidades.Operario;
import proy.datos.filtros.implementacion.FiltroOperario;
import proy.excepciones.PersistenciaException;
import proy.gui.ControladorRomano;
import proy.gui.componentes.ventanas.VentanaPersonalizada;

public class VTareasController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/VTareas.fxml";

	@FXML
	private TabPane operarioBox;

	private AnimationTimer timer;

	@Override
	protected void inicializar() {
		timer = new AnimationTimer() {

			long anterior = -1;

			@Override
			public void handle(long now) {
				if(now - anterior > 60000000000L){
					actualizar();
					anterior = now;
				}
			}
		};
		timer.start();
	}

	@FXML
	private void loguearse() {
		VentanaPersonalizada ventanaLogin = presentadorVentanas.presentarVentanaPersonalizada(LoguearAdminController.URL_VISTA, coordinador, stage);
		ventanaLogin.showAndWait();
		LoguearAdminController pantallaLogin = (LoguearAdminController) ventanaLogin.getControlador();
		if(pantallaLogin.fueExitosoLogin()){
			this.nuevaScene(MenuAdministracionController.URL_VISTA);
		}
	}

	@Override
	public void actualizar() {
		int indiceAnterior = operarioBox.getSelectionModel().getSelectedIndex();
		operarioBox.getTabs().clear();

		try{
			List<Operario> operarios = coordinador.listarOperarios(new FiltroOperario.Builder().build());

			for(Operario o: operarios){
				final VTareasOperarioTabController renglonController = new VTareasOperarioTabController(o);
				renglonController.setCoordinador(coordinador);
				renglonController.setStage(stage);
				operarioBox.getTabs().add(renglonController.getTab());
				renglonController.getTab().selectedProperty().addListener((obs, oldV, newV) -> {
					if(newV){
						renglonController.actualizar();
					}
				});
			}
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
		}

		for(int i = 0; i < operarioBox.getTabs().size(); i++){
			operarioBox.getSelectionModel().select(i);
		}

		if(indiceAnterior == -1 && operarioBox.getTabs().size() > 0){
			indiceAnterior = 0;
		}
		operarioBox.getSelectionModel().select(indiceAnterior);
	}

	@FXML
	private void enviarComentario() {
		presentadorVentanas.presentarVentanaPersonalizada(NComentarioController.URL_VISTA, coordinador, stage).showAndWait();
	}

	@Override
	public Boolean sePuedeSalir() {
		timer.stop();
		return super.sePuedeSalir();
	}
}
