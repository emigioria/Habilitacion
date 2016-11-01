/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import java.util.Map;

import proy.logica.gestores.resultados.ResultadoEliminarPiezas.ErrorEliminarPiezas;

public class ResultadoEliminarPiezas extends Resultado<ErrorEliminarPiezas> {

	private ResultadoEliminarTareas resultadoTareas;
	private Map<String, ResultadoEliminarProcesos> resultadosEliminarProcesos;

	public enum ErrorEliminarPiezas {
		ERROR_AL_ELIMINAR_TAREAS, ERROR_AL_ELIMINAR_PROCESOS
	}

	public ResultadoEliminarPiezas(ErrorEliminarPiezas... errores) {
		super(errores);
	}

	public ResultadoEliminarPiezas(Map<String, ResultadoEliminarProcesos> resultadosEliminarProcesos, ErrorEliminarPiezas... errores) {
		super(errores);
		this.resultadosEliminarProcesos = resultadosEliminarProcesos;
	}

	public ResultadoEliminarPiezas(ResultadoEliminarTareas resultadoTareas, ErrorEliminarPiezas... errores) {
		super(errores);
		this.resultadoTareas = resultadoTareas;
	}

	public ResultadoEliminarTareas getResultadoTareas() {
		return resultadoTareas;
	}

	public Map<String, ResultadoEliminarProcesos> getResultadosEliminarProcesos() {
		return resultadosEliminarProcesos;
	}
}
