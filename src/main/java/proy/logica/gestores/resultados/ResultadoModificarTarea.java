/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoModificarTarea.ErrorModificarTarea;

public class ResultadoModificarTarea extends Resultado<ErrorModificarTarea> {

	public enum ErrorModificarTarea {
		OPERARIO_INCOMPLETO, PROCESO_INCOMPLETO, CANTIDAD_INCOMPLETA, FECHA_INCOMPLETA, FECHA_ANTERIOR_A_HOY, TAREA_NO_PLANIFICADA

	}

	public ResultadoModificarTarea(ErrorModificarTarea... errores) {
		super(errores);
	}
}
