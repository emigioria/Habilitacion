/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoEliminarHerramienta.ErrorEliminarHerramienta;

public class ResultadoEliminarHerramienta extends Resultado<ErrorEliminarHerramienta> {

	ResultadoEliminarTareas resultadoTareas;

	public enum ErrorEliminarHerramienta {
		ERROR_AL_ELIMINAR_TAREAS
	}

	public ResultadoEliminarHerramienta(ErrorEliminarHerramienta... errores) {
		super(errores);
	}

	public ResultadoEliminarHerramienta(ResultadoEliminarTareas resultadoTareas, ErrorEliminarHerramienta... errores) {
		super(errores);
		this.resultadoTareas = resultadoTareas;
	}

	public ResultadoEliminarTareas getResultadoTareas() {
		return resultadoTareas;
	}
}
