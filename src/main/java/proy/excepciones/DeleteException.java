/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.excepciones;

/**
 * Representa un error al borrar datos de la base de datos
 */
public class DeleteException extends PersistenciaException {

	private static final long serialVersionUID = 1L;

	public DeleteException(Throwable causa) {
		super("Error inesperado interactuando con la base de datos.\nNo se pudieron eliminar los datos deseados.", causa);
	}
}
