/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoEliminarParte.ErrorEliminarParte;

public class ResultadoEliminarParte extends Resultado<ErrorEliminarParte> {

	private ResultadoEliminarTareas resultadoEliminarTareas;
	private ResultadoEliminarPiezasDeParte resultadoEliminarPiezasDeParte;
	private ResultadoEliminarProcesos resultadoEliminarProcesos;

	public enum ErrorEliminarParte {
		ERROR_AL_ELIMINAR_TAREAS, ERROR_AL_ELIMINAR_PIEZAS, ERROR_AL_ELIMINAR_PROCESOS
	}

	public ResultadoEliminarParte(ErrorEliminarParte... errores) {
		super(errores);
	}

	public ResultadoEliminarParte(ResultadoEliminarPiezasDeParte resultadoEliminarPiezasDeParte, ResultadoEliminarProcesos resultadoEliminarProcesos, ErrorEliminarParte... errores) {
		super(errores);
		this.resultadoEliminarPiezasDeParte = resultadoEliminarPiezasDeParte;
		this.resultadoEliminarProcesos = resultadoEliminarProcesos;
	}

	public ResultadoEliminarParte(ResultadoEliminarTareas resultadoEliminarTareas, ErrorEliminarParte... errores) {
		super(errores);
		this.resultadoEliminarTareas = resultadoEliminarTareas;
	}

	public ResultadoEliminarTareas getResultadoTareas() {
		return resultadoEliminarTareas;
	}

	public ResultadoEliminarPiezasDeParte getResultadosEliminarPiezasDeParte() {
		return resultadoEliminarPiezasDeParte;
	}

	public ResultadoEliminarProcesos getResultadosEliminarProcesos() {
		return resultadoEliminarProcesos;
	}
}
