/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui;

import java.util.Stack;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class PilaScene {

	private Stage stagePrincipal;

	private Stack<Scene> scenes;

	private Stack<ControladorApilable> controllers;

	public PilaScene(Stage stagePrincipal) {
		this.stagePrincipal = stagePrincipal;
		scenes = new Stack<>();
		controllers = new Stack<>();
	}

	public void apilarScene(Scene scene, ControladorApilable controller) {
		scenes.push(scene);
		if(!isEmpty()){
			controllers.peek().dejarDeMostrar();
		}
		controllers.push(controller);
		stagePrincipal.setScene(scene);
		stagePrincipal.show();
	}

	public void cambiarScene(Scene scene, ControladorApilable controller) {
		scenes.pop();
		scenes.push(scene);
		controllers.pop().dejarDeMostrar();
		controllers.push(controller);
		stagePrincipal.setScene(scene);
		stagePrincipal.show();
	}

	public void desapilarScene() {
		if(sePuedeSalir()){
			scenes.pop();
			controllers.pop().dejarDeMostrar();
			if(!isEmpty()){
				stagePrincipal.setScene(scenes.peek());
				controllers.peek().actualizar();
				stagePrincipal.show();
			}
		}
	}

	public Boolean sePuedeSalir() {
		return controllers.peek().sePuedeSalir();
	}

	private boolean isEmpty() {
		return controllers.isEmpty() || scenes.isEmpty();
	}
}
