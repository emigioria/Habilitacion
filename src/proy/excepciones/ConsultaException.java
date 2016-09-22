/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.excepciones;

/**
 * Representa un error en una petici�n de datos a la base de datos
 *
 * @author Acosta - Gioria - Moretti - Rebechi
 *
 */
public class ConsultaException extends PersistenciaException {

	private static final long serialVersionUID = 1L;

	public ConsultaException() {
		super("Error inesperado interactuando con la base de datos.\nNo se pudo obtener los datos deseados");
	}
}
