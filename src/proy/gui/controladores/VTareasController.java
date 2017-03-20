/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import proy.datos.clases.EstadoTareaStr;
import proy.datos.entidades.Operario;
import proy.datos.entidades.Tarea;
import proy.datos.filtros.implementacion.FiltroOperario;
import proy.datos.filtros.implementacion.FiltroTarea;
import proy.excepciones.PersistenciaException;
import proy.gui.ControladorRomano;
import proy.gui.componentes.ventanas.VentanaPersonalizada;

public class VTareasController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/VTareas.fxml";

	@FXML
	private TabPane operarioBox;

	@Override
	protected void inicializar() {
		actualizar();
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
		operarioBox.getTabs().clear();

		try{
			ArrayList<EstadoTareaStr> estados = new ArrayList<>();
			estados.add(EstadoTareaStr.EJECUTANDO);
			estados.add(EstadoTareaStr.PAUSADA);
			estados.add(EstadoTareaStr.PLANIFICADA);

			List<Tarea> tareas = coordinador.listarTareas(new FiltroTarea.Builder().estados(estados).ordenFechaFinalizada().build());
			List<Operario> operarios = coordinador.listarOperarios(new FiltroOperario.Builder().build());
			Map<Operario, List<Tarea>> tareasPorOperario = tareas.stream().collect(Collectors.groupingBy(Tarea::getOperario));

			for(Operario o: operarios){
				operarioBox.getTabs().add(new VTareasOperarioTabController(o, tareasPorOperario.getOrDefault(o, new ArrayList<>()), () -> actualizar()).getTab());
			}
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
		}

		for(int i = 0; i < operarioBox.getTabs().size(); i++){
			operarioBox.getSelectionModel().select(i);
			operarioBox.getSelectionModel().select(0);
		}
	}

	@FXML
	private void enviarComentario() {
		presentadorVentanas.presentarVentanaPersonalizada(NComentarioController.URL_VISTA, coordinador, stage).showAndWait();
	}
}
