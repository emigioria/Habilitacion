/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoEliminarPieza.ErrorEliminarPieza;

public class ResultadoEliminarPieza extends Resultado<ErrorEliminarPieza> {

	private ResultadoEliminarTareas resultadoEliminarTareas;
	private ResultadoEliminarProcesos resultadoEliminarProcesos;

	public enum ErrorEliminarPieza {
		ERROR_AL_ELIMINAR_TAREAS, ERROR_AL_ELIMINAR_PROCESOS
	}

	public ResultadoEliminarPieza(ErrorEliminarPieza... errores) {
		super(errores);
	}

	public ResultadoEliminarPieza(ResultadoEliminarProcesos resultadoEliminarProcesos, ErrorEliminarPieza... errores) {
		super(errores);
		this.resultadoEliminarProcesos = resultadoEliminarProcesos;
	}

	public ResultadoEliminarPieza(ResultadoEliminarTareas resultadoEliminarTareas, ErrorEliminarPieza... errores) {
		super(errores);
		this.resultadoEliminarTareas = resultadoEliminarTareas;
	}

	public ResultadoEliminarTareas getResultadoEliminarTareas() {
		return resultadoEliminarTareas;
	}

	public ResultadoEliminarProcesos getResultadoEliminarProcesos() {
		return resultadoEliminarProcesos;
	}
}
