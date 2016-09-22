/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.excepciones;

/**
 * Representa un error buscando un elemento sobre la base de datos
 *
 * @author Acosta - Gioria - Moretti - Rebechi
 *
 */
public class ObjNotFoundException extends PersistenciaException {

	private static final long serialVersionUID = 1L;

	public ObjNotFoundException(String accion) {
		super("Error inesperado interactuando con la base de datos.\nNo se pudo encontrar el elemento que se desea " + accion + ".");
	}
}
