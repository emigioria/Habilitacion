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
		scenes = new Stack<Scene>();
		controllers = new Stack<ControladorApilable>();
	}

	public void apilarScene(Scene scene, ControladorApilable controller) {
		scenes.push(scene);
		controllers.push(controller);
		stagePrincipal.setScene(scene);
		stagePrincipal.show();
	}

	public void desapilarScene() {
		scenes.pop();
		controllers.pop();
		if(!isEmpty()){
			stagePrincipal.setScene(scenes.peek());
			controllers.peek().actualizar();
			stagePrincipal.show();
		}
	}

	public void cambiarScene(Scene scene, ControladorApilable controller) {
		scenes.pop();
		controllers.pop();
		scenes.push(scene);
		controllers.push(controller);
		stagePrincipal.setScene(scene);
		stagePrincipal.show();
	}

	public boolean isEmpty() {
		return scenes.isEmpty();
	}

	public Stage getStage() {
		return stagePrincipal;
	}
}
