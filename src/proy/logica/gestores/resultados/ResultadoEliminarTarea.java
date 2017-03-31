/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoEliminarTarea.ErrorEliminarTarea;

public class ResultadoEliminarTarea extends Resultado<ErrorEliminarTarea> {

	public enum ErrorEliminarTarea {
		TAREA_NO_PLANIFICADA
	}

	public ResultadoEliminarTarea(ErrorEliminarTarea... errores) {
		super(errores);
	}
}
