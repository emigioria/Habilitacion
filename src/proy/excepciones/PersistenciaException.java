/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.excepciones;

/**
 * Representa un error pertinente a la persistencia de datos
 */
public abstract class PersistenciaException extends Exception {

	private static final long serialVersionUID = 1L;

	public PersistenciaException(String msg, Throwable causa) {
		super(msg, causa);
	}
}
