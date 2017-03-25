/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
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

	private List<Runnable> pararRelojes = new ArrayList<>();

	private Semaphore semaforo = new Semaphore(1);

	private Long anterior;

	@Override
	protected void inicializar() {
		timer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				if(now - anterior > 60000000000L){
					actualizacionPeriodica();
					anterior = now;
				}
			}
		};
		actualizar();
	}

	@Override
	public void dejarDeMostrar() {
		pararRelojes();
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
		stage.setTitle("Sistema de asignaciòn de tareas Romano FasTask");
		anterior = -1L;
		timer.start();
	}

	public void actualizacionPeriodica() {
		try{
			semaforo.acquire();
		} catch(InterruptedException e){

		}

		//Guardo qué pestaña se estaba mostrando antes
		int indiceAnterior = operarioBox.getSelectionModel().getSelectedIndex();

		//Quito las pestañas viejas, cargo los operarios en pestañas y guardo sus controladores
		operarioBox.getTabs().clear();
		List<VTareasOperarioTabController> tabControllers = new ArrayList<>();
		try{
			List<Operario> operarios = coordinador.listarOperarios(new FiltroOperario.Builder().build());

			for(Operario o: operarios){
				final VTareasOperarioTabController tabController = new VTareasOperarioTabController(o, () -> actualizacionPeriodica(), coordinador, stage);
				operarioBox.getTabs().add(tabController.getTab());
				pararRelojes.add(tabController.getPararReloj());
				tabControllers.add(tabController);
			}
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
		}

		//Arreglo bug gráfico donde aparecen todas las pestañas seleccionadas
		for(Tab tab: operarioBox.getTabs()){
			operarioBox.getSelectionModel().select(tab);
		}

		//Inicializo el índice la primera vez o cuando el índice queda fuera del rango
		if(indiceAnterior == -1 || operarioBox.getTabs().size() < indiceAnterior){
			indiceAnterior = 0;
		}
		operarioBox.getSelectionModel().select(indiceAnterior);

		//Cuando se selecciona una pestaña se actualiza su contenido
		for(VTareasOperarioTabController tabController: tabControllers){
			tabController.getTab().selectedProperty().addListener((obs, oldV, newV) -> {
				if(newV){
					tabController.actualizar();
				}
			});
		}

		//Si hay operarios, se muestran las tareas de la pestaña seleccionada
		if(operarioBox.getTabs().size() > 0){
			VTareasOperarioTabController pestañaMostrada = tabControllers.get(indiceAnterior);
			if(pestañaMostrada != null){
				pestañaMostrada.actualizar();
			}
		}
		semaforo.release();
	}

	@FXML
	private void enviarComentario() {
		presentadorVentanas.presentarVentanaPersonalizada(NComentarioController.URL_VISTA, coordinador, stage).showAndWait();
	}

	private void pararRelojes() {
		timer.stop();
		for(Runnable r: pararRelojes){
			r.run();
		}
	}

	@Override
	public Boolean sePuedeSalir() {
		pararRelojes();
		try{
			semaforo.acquire();
		} catch(InterruptedException e){

		}
		return true;
	}
}
