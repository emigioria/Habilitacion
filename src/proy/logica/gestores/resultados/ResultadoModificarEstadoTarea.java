/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoModificarEstadoTarea.ErrorModificarEstadoTarea;

public class ResultadoModificarEstadoTarea extends Resultado<ErrorModificarEstadoTarea> {

	public enum ErrorModificarEstadoTarea {
		ERROR_ESTADO_TRANSICION, DATOS_INCOMPLETOS, DATOS_INVALIDOS

	}

	public ResultadoModificarEstadoTarea(ErrorModificarEstadoTarea... errores) {
		super(errores);
	}
}
