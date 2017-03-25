/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui;

import java.util.Stack;

import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class PilaPane extends PilaJavaFX {

	private Pane fondo;

	private Stack<Parent> pantallas;

	private Stack<ControladorApilable> controllers;

	private ControladorApilable padre;

	public PilaPane(Pane fondo, ControladorApilable padre) {
		this.padre = padre;
		this.fondo = fondo;
		pantallas = new Stack<>();
		controllers = new Stack<>();
	}

	@Override
	public void apilarPantalla(Parent pantalla, ControladorApilable controller) {
		pantallas.push(pantalla);
		if(!isEmpty()){
			controllers.peek().dejarDeMostrar();
		}
		controllers.push(controller);
		fondo.getChildren().clear();
		fondo.getChildren().add(pantalla);
	}

	@Override
	public void cambiarPantalla(Parent pantalla, ControladorApilable controller) {
		if(!isEmpty()){
			pantallas.pop();
			controllers.pop().dejarDeMostrar();
		}
		pantallas.push(pantalla);
		controllers.push(controller);
		fondo.getChildren().clear();
		fondo.getChildren().add(pantalla);
	}

	@Override
	public void desapilarPantalla() {
		if(!hayUnaSolaPantalla() && sePuedeSalir()){
			pantallas.pop();
			controllers.pop().dejarDeMostrar();
			if(!isEmpty()){
				controllers.peek().actualizar();
				fondo.getChildren().clear();
				fondo.getChildren().add(pantallas.peek());
			}
		}
		else{
			padre.salir();
		}
	}

	@Override
	public Boolean sePuedeSalir() {
		if(!isEmpty()){
			return controllers.peek().sePuedeSalir();
		}
		else{
			return null;
		}
	}

	private boolean isEmpty() {
		return controllers.isEmpty() || pantallas.isEmpty();
	}

	private boolean hayUnaSolaPantalla() {
		return controllers.size() == 1 || pantallas.size() == 1;
	}
}
