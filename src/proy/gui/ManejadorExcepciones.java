/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui;

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
