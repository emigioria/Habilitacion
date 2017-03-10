/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoEliminarOperario.ErrorEliminarOperario;

public class ResultadoEliminarOperario extends Resultado<ErrorEliminarOperario> {

	public enum ErrorEliminarOperario {
		ERROR_AL_ELIMINAR_TAREAS
	}

	private ResultadoEliminarTareas resultadoEliminarTareas;

	public ResultadoEliminarOperario(ErrorEliminarOperario... errores) {
		super(errores);
	}

	public ResultadoEliminarOperario(ResultadoEliminarTareas resultadoEliminarTareas, ErrorEliminarOperario... errores) {
		super(errores);
		this.resultadoEliminarTareas = resultadoEliminarTareas;
	}

	public ResultadoEliminarTareas getResultadoEliminarTareas() {
		return resultadoEliminarTareas;
	}
}
