package proy.excepciones;

import javafx.stage.Window;
import proy.gui.componentes.VentanaErrorExcepcion;
import proy.gui.componentes.VentanaErrorExcepcionInesperada;

public abstract class ManejadorExcepciones {
	public static void presentarExcepcion(Exception e, Window w) {
		e.printStackTrace();
		new VentanaErrorExcepcion(e.getMessage(), w);
	}

	public static void presentarExcepcionInesperada(Exception e, Window w) {
		System.err.println("Excepción inesperada!!");
		e.printStackTrace();
		new VentanaErrorExcepcionInesperada(w);
	}
}
