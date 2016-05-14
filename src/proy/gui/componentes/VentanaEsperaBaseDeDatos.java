package proy.gui.componentes;

import javafx.scene.control.Dialog;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class VentanaEsperaBaseDeDatos extends Dialog<Void> {

	public VentanaEsperaBaseDeDatos() {
		this(null);
	}

	public VentanaEsperaBaseDeDatos(Window padre) {
		super();
		this.initStyle(StageStyle.UNDECORATED);
		if(padre != null){
			this.initOwner(padre);
		}
		this.setContentText("Esperando a la base de datos...");
		this.setHeaderText(null);
		//		this.setTitle("Esperando");
	}
}
