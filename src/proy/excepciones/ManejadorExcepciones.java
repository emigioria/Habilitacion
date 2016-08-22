package proy.excepciones;

import javafx.stage.Window;
import proy.gui.componentes.VentanaErrorExcepcion;

public abstract class ManejadorExcepciones {
	public static void presentarExcepcion(Exception e, Window w) {
		e.printStackTrace();
		new VentanaErrorExcepcion(e.getMessage(), w);
	}
}
