/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoEliminarPiezasDeParte.ErrorEliminarPiezasDeParte;

public class ResultadoEliminarPiezasDeParte extends Resultado<ErrorEliminarPiezasDeParte> {

	public enum ErrorEliminarPiezasDeParte {

	}

	public ResultadoEliminarPiezasDeParte(ErrorEliminarPiezasDeParte... errores) {
		super(errores);
	}

}
