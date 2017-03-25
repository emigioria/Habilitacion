/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import javafx.collections.ListChangeListener.Change;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import proy.gui.ControladorRomano;
import proy.gui.PilaPane;

public class MenuAdministracionController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/MenuAdministracion.fxml";

	@FXML
	private ToggleButton toggleButtonComentarios;

	@FXML
	private ToggleButton toggleButtonMateriales;

	@FXML
	private ToggleButton toggleButtonHerramientas;

	@FXML
	private ToggleButton toggleButtonOperarios;

	@FXML
	private ToggleButton toggleButtonMaquinas;

	@FXML
	private ToggleButton toggleButtonProcesos;

	@FXML
	private ToggleButton toggleButtonTareas;

	private ToggleGroup toggleGroupSidebar = new ToggleGroup();

	@FXML
	private Pane background;

	@FXML
	public void administrarOperarios() {
		this.nuevaScene(AOperariosController.URL_VISTA);
	}

	@FXML
	public void administrarMaquinas() {
		this.nuevaScene(AMaquinasController.URL_VISTA);
	}

	@FXML
	public void administrarProcesos() {
		this.nuevaScene(AProcesosController.URL_VISTA);
	}

	@FXML
	public void administrarMateriales() {
		this.nuevaScene(AMaterialesController.URL_VISTA);
	}

	@FXML
	public void administrarHerramientas() {
		this.nuevaScene(AHerramientasController.URL_VISTA);
	}

	@FXML
	public void administrarTareas() {
		this.nuevaScene(ATareasController.URL_VISTA);
	}

	@FXML
	public void administrarComentarios() {
		this.nuevaScene(AComentariosController.URL_VISTA);
	}

	@Override
	public void actualizar() {
		stage.setTitle("Panel de administración");
	}

	@Override
	protected void inicializar() {
		//Primera pantalla a mostrar
		administrarComentarios();

		//Agrupados botones
		toggleButtonComentarios.setToggleGroup(toggleGroupSidebar);
		toggleButtonMateriales.setToggleGroup(toggleGroupSidebar);
		toggleButtonHerramientas.setToggleGroup(toggleGroupSidebar);
		toggleButtonOperarios.setToggleGroup(toggleGroupSidebar);
		toggleButtonMaquinas.setToggleGroup(toggleGroupSidebar);
		toggleButtonProcesos.setToggleGroup(toggleGroupSidebar);
		toggleButtonTareas.setToggleGroup(toggleGroupSidebar);
		addAlwaysOneSelectedSupport(toggleGroupSidebar);

		actualizar();
	}

	private void addAlwaysOneSelectedSupport(final ToggleGroup toggleGroup) {
		toggleGroup.getToggles().addListener((Change<? extends Toggle> c) -> {
			while(c.next()){
				for(final Toggle addedToggle: c.getAddedSubList()){
					addConsumeMouseEventfilter(addedToggle);
				}
			}
		});
		toggleGroup.getToggles().forEach(t -> {
			addConsumeMouseEventfilter(t);
		});
	}

	private void addConsumeMouseEventfilter(Toggle toggle) {
		EventHandler<MouseEvent> consumeMouseEventfilter = mouseEvent -> {
			if(((Toggle) mouseEvent.getSource()).isSelected()){
				mouseEvent.consume();
			}
		};

		((ToggleButton) toggle).addEventFilter(MouseEvent.MOUSE_PRESSED, consumeMouseEventfilter);
		((ToggleButton) toggle).addEventFilter(MouseEvent.MOUSE_RELEASED, consumeMouseEventfilter);
		((ToggleButton) toggle).addEventFilter(MouseEvent.MOUSE_CLICKED, consumeMouseEventfilter);
	}

	@Override
	protected ControladorRomano nuevaScene(String URLVista) {
		background.getChildren().clear();
		return nuevaCambiarScene(URLVista, new PilaPane(background, this), coordinador, false);
	}

	@Override
	protected ControladorRomano cambiarScene(String URLVista) {
		background.getChildren().clear();
		return nuevaCambiarScene(URLVista, new PilaPane(background, this), coordinador, true);
	}
}
