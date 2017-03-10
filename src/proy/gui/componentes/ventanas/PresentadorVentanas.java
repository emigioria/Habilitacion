/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.componentes.ventanas;

import javafx.stage.Stage;
import javafx.stage.Window;
import proy.gui.componentes.Toast;
import proy.logica.CoordinadorJavaFX;

public class PresentadorVentanas {

	public VentanaConfirmacion presentarConfirmacion(String titulo, String mensaje, Window padre) {
		return new VentanaConfirmacion(titulo, mensaje, padre);
	}

	public VentanaError presentarError(String titulo, String mensaje, Window padre) {
		return new VentanaError(titulo, mensaje, padre);
	}

	public VentanaInformacion presentarInformacion(String titulo, String mensaje, Window padre) {
		return new VentanaInformacion(titulo, mensaje, padre);
	}

	public VentanaErrorExcepcion presentarExcepcion(Exception e, Window padre) {
		e.printStackTrace();
		return new VentanaErrorExcepcion(e.getMessage(), padre);
	}

	public VentanaErrorExcepcionInesperada presentarExcepcionInesperada(Exception e, Window padre) {
		System.err.println("Excepción inesperada!!");
		e.printStackTrace();
		return new VentanaErrorExcepcionInesperada(padre);
	}

	public VentanaEsperaBaseDeDatos presentarEsperaBaseDeDatos(Window padre) {
		return new VentanaEsperaBaseDeDatos(padre);
	}

	public VentanaEsperaBaseDeDatos presentarEsperaBaseDeDatos() {
		return new VentanaEsperaBaseDeDatos();
	}

	public VentanaPersonalizada presentarVentanaPersonalizada(String URLVista, CoordinadorJavaFX coordinador, Stage padre) {
		return new VentanaPersonalizada(URLVista, coordinador, padre);
	}

	public void presentarToast(String mensaje, Window padre, int ajusteHeight) {
		int toastMsgTime = 2500; //2.5 seconds
		int fadeInTime = 700; //0.7 seconds
		int fadeOutTime = 500; //0.5 seconds
		new Toast(padre, mensaje, toastMsgTime, fadeInTime, fadeOutTime, ajusteHeight);
	}

	public void presentarToast(String mensaje, Window padre) {
		presentarToast(mensaje, padre, 20);
	}
}
