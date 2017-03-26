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

	private ToggleGroup toggleGroup = new ToggleGroup();

	@FXML
	private Pane background;

	private PilaPane backgroundApilador;

	private Toggle botonAnteriormenteSeleccionado;

	@FXML
	private void administrarOperarios() {
		if(this.nuevaScene(AOperariosController.URL_VISTA) == null){
			botonAnteriormenteSeleccionado.setSelected(true);
		}
	}

	@FXML
	private void administrarMaquinas() {
		if(this.nuevaScene(AMaquinasController.URL_VISTA) == null){
			botonAnteriormenteSeleccionado.setSelected(true);
		}
	}

	@FXML
	private void administrarProcesos() {
		if(this.nuevaScene(AProcesosController.URL_VISTA) == null){
			botonAnteriormenteSeleccionado.setSelected(true);
		}
	}

	@FXML
	private void administrarMateriales() {
		if(this.nuevaScene(AMaterialesController.URL_VISTA) == null){
			botonAnteriormenteSeleccionado.setSelected(true);
		}
	}

	@FXML
	private void administrarHerramientas() {
		if(this.nuevaScene(AHerramientasController.URL_VISTA) == null){
			botonAnteriormenteSeleccionado.setSelected(true);
		}
	}

	@FXML
	private void administrarTareas() {
		if(this.nuevaScene(ATareasController.URL_VISTA) == null){
			botonAnteriormenteSeleccionado.setSelected(true);
		}
	}

	@FXML
	private void administrarComentarios() {
		if(this.nuevaScene(AComentariosController.URL_VISTA) == null){
			botonAnteriormenteSeleccionado.setSelected(true);
		}
	}

	@Override
	public void actualizar() {
		stage.setTitle("Panel de administración");
	}

	@Override
	protected void inicializar() {
		//Agrupados botones
		toggleButtonComentarios.setToggleGroup(toggleGroup);
		toggleButtonMateriales.setToggleGroup(toggleGroup);
		toggleButtonHerramientas.setToggleGroup(toggleGroup);
		toggleButtonOperarios.setToggleGroup(toggleGroup);
		toggleButtonMaquinas.setToggleGroup(toggleGroup);
		toggleButtonProcesos.setToggleGroup(toggleGroup);
		toggleButtonTareas.setToggleGroup(toggleGroup);
		addAlwaysOneSelectedSupport(toggleGroup);

		for(Toggle t: toggleGroup.getToggles()){
			agregarListenerSeleccionado(t);
		}

		actualizar();

		//Primera pantalla a mostrar
		toggleButtonComentarios.setSelected(true);
		administrarComentarios();
	}

	private void agregarListenerSeleccionado(Toggle t) {
		t.selectedProperty().addListener((obs, oldV, newV) -> {
			if(!newV){
				botonAnteriormenteSeleccionado = t;
			}
		});
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
		if(backgroundApilador == null || backgroundApilador.sePuedeSalir()){
			backgroundApilador = new PilaPane(background, this);
			return nuevaCambiarScene(URLVista, backgroundApilador, coordinador, false);
		}
		return null;
	}

	@Override
	protected ControladorRomano cambiarScene(String URLVista) {
		throw new RuntimeException();
	}
}
