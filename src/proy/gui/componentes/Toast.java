/**
 * Copyright (C) 2016 Fernando Berti - Daniel Campodonico - Emiliano Gioria - Lucas Moretti - Esteban Rebechi - Andres Leonel Rico
 * This file is part of Olimpo.
 *
 * Olimpo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Olimpo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Olimpo. If not, see <http://www.gnu.org/licenses/>.
 */
package proy.gui.componentes;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

public final class Toast {
	public Toast(Window padre, String toastMsg, int toastDelay, int fadeInDelay, int fadeOutDelay, int ajusteHeight) {
		Stage toastStage = new Stage();
		toastStage.initOwner(padre);
		toastStage.setResizable(false);
		toastStage.initStyle(StageStyle.TRANSPARENT);

		Text text = new Text(toastMsg);
		text.setFont(Font.font("Verdana", 15));
		text.setFill(Color.WHITE);

		StackPane root = new StackPane(text);
		root.setStyle("-fx-background-radius: 15; -fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 14px;");
		root.setOpacity(0);

		Scene scene = new Scene(root);
		scene.setFill(Color.TRANSPARENT);
		toastStage.setScene(scene);

		final double midX = (padre.getX() + padre.getWidth() / 2);
		final double midY = (padre.getY() + padre.getHeight());

		toastStage.widthProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue.intValue() > 1){
				toastStage.setX(midX - newValue.intValue() / 2);
			}
		});

		toastStage.heightProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue.intValue() > 1){
				toastStage.setY(midY - newValue.intValue() - ajusteHeight);
			}
		});

		toastStage.show();

		Timeline fadeInTimeline = new Timeline();
		KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(fadeInDelay), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 1));
		fadeInTimeline.getKeyFrames().add(fadeInKey1);
		fadeInTimeline.setOnFinished((ae) -> {
			new Thread(() -> {
				try{
					Thread.sleep(toastDelay);
				} catch(InterruptedException e){

				}
				Timeline fadeOutTimeline = new Timeline();
				KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(fadeOutDelay), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 0));
				fadeOutTimeline.getKeyFrames().add(fadeOutKey1);
				fadeOutTimeline.setOnFinished((aeb) -> toastStage.close());
				fadeOutTimeline.play();
			}).start();
		});
		fadeInTimeline.play();
	}
}