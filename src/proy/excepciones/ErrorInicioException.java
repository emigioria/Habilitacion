/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.excepciones;

/**
 * Representa un error en la gestión de datos
 */
public class ErrorInicioException extends Exception {

	private static final long serialVersionUID = 1L;

	public ErrorInicioException(Throwable causa) {
		super("Error al iniciar la aplicación.\n"
				+ "Puede deberse a un error de conexión a la base de datos.\n"
				+ "Haga click en aceptar para cerrarla", causa);
	}
}
