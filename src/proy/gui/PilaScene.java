package proy.gui;

import java.util.Stack;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class PilaScene {

	private Stage stagePrincipal;

	private Stack<Scene> scenes;

	private Stack<SceneApilable> controllers;

	public PilaScene(Stage stagePrincipal) {
		this.stagePrincipal = stagePrincipal;
		scenes = new Stack<Scene>();
		controllers = new Stack<SceneApilable>();
	}

	public void apilarScene(Scene scene, SceneApilable controller) {
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
			stagePrincipal.show();
		}
	}

	public void cambiarScene(Scene scene, SceneApilable controller) {
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

	public SceneApilable getPeekSceneApilable() {
		return controllers.peek();
	}
}
