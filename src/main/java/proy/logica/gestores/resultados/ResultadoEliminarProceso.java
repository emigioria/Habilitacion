/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoEliminarProceso.ErrorEliminarProceso;

public class ResultadoEliminarProceso extends Resultado<ErrorEliminarProceso> {

	public enum ErrorEliminarProceso {
		ERROR_AL_ELIMINAR_TAREAS
	}

	private ResultadoEliminarTareas resultadoEliminarTareas;

	public ResultadoEliminarProceso(ErrorEliminarProceso... errores) {
		super(errores);
	}

	public ResultadoEliminarProceso(ResultadoEliminarTareas resultadoEliminarTareas, ErrorEliminarProceso... errores) {
		super(errores);
		this.resultadoEliminarTareas = resultadoEliminarTareas;
	}

	public ResultadoEliminarTareas getResultadoEliminarTareas() {
		return resultadoEliminarTareas;
	}
}
