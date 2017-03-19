/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import proy.datos.clases.EstadoTareaStr;
import proy.datos.entidades.Tarea;
import proy.datos.filtros.implementacion.FiltroTarea;
import proy.excepciones.PersistenciaException;
import proy.gui.ControladorRomano;

public class VHistorialTareasController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/VHistorialTareas.fxml";

	@FXML
	private VBox grupoVBox;

	@Override
	protected void inicializar() {
		actualizar();
	}

	@Override
	public void actualizar() {
		grupoVBox.getChildren().clear();

		try{
			List<Tarea> tareas = coordinador.listarTareas(new FiltroTarea.Builder().estado(EstadoTareaStr.FINALIZADA).ordenFechaFinalizada().build());
			tareas.addAll(tareas);
			tareas.addAll(tareas);
			tareas.addAll(tareas);
			Map<Date, List<Tarea>> tareasPorFecha = tareas.stream().collect(Collectors.groupingBy(Tarea::getFechaHoraFin));
			tareasPorFecha.forEach((f, l) -> {
				try{
					grupoVBox.getChildren().add(new VHistorialTareasGrupoController(f, l).getNode());
				} catch(IOException e){
					presentadorVentanas.presentarExcepcionInesperada(e, stage);
				}
			});
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
		}
	}

}
