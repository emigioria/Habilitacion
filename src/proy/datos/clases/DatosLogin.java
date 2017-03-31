/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.datos.clases;

public class DatosLogin {

	private String dni;

	private char[] contrasenia;

	public DatosLogin(String dni, char[] contrasenia) {
		this.dni = dni;
		this.contrasenia = contrasenia;
	}

	public String getDNI() {
		return dni;
	}

	public char[] getContrasenia() {
		return contrasenia;
	}

}
