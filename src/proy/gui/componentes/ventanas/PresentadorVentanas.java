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
package proy.gui.componentes.ventanas;

import java.io.IOException;

import javafx.scene.Parent;
import javafx.stage.Window;
import proy.gui.componentes.Toast;

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

	public VentanaErrorExcepcion presentarExcepcion(Exception e, Window w) {
		e.printStackTrace();
		return new VentanaErrorExcepcion(e.getMessage(), w);
	}

	public VentanaErrorExcepcionInesperada presentarExcepcionInesperada(Exception e, Window w) {
		System.err.println("Excepción inesperada!!");
		e.printStackTrace();
		return new VentanaErrorExcepcionInesperada(w);
	}

	public VentanaEsperaBaseDeDatos presentarEsperaBaseDeDatos(Window w) {
		return new VentanaEsperaBaseDeDatos(w);
	}

	public VentanaEsperaBaseDeDatos presentarEsperaBaseDeDatos() {
		return new VentanaEsperaBaseDeDatos();
	}

	public VentanaPersonalizada presentarVentanaPersonalizada(String URLVista, String titulo, Window w) throws IOException {
		return new VentanaPersonalizada(URLVista, titulo, w);
	}

	public VentanaPersonalizada presentarVentanaPersonalizada(Parent vista, String titulo, Window w) {
		return new VentanaPersonalizada(vista, titulo, w);
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
