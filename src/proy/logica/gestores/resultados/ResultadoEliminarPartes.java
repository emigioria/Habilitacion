/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoEliminarPartes.ErrorEliminarPartes;

public class ResultadoEliminarPartes extends Resultado<ErrorEliminarPartes> {

	private ResultadoEliminarTareas resultadoTareas;

	public enum ErrorEliminarPartes {
		ErrorAlEliminarTareas
	}

	public ResultadoEliminarPartes(ErrorEliminarPartes... errores) {
		super(errores);
	}

	public ResultadoEliminarPartes(ResultadoEliminarTareas resultadoTareas, ErrorEliminarPartes... errores) {
		this(errores);
		this.resultadoTareas = resultadoTareas;
	}

	public ResultadoEliminarTareas getResultadoTareas() {
		return resultadoTareas;
	}
}
