/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import java.util.Map;

import proy.logica.gestores.resultados.ResultadoEliminarPartes.ErrorEliminarPartes;

public class ResultadoEliminarPartes extends Resultado<ErrorEliminarPartes> {

	private ResultadoEliminarTareas resultadoTareas;
	private Map<String, ResultadoEliminarPiezas> resultadosEliminarPiezas;
	private Map<String, ResultadoEliminarProcesos> resultadosEliminarProcesos;

	public enum ErrorEliminarPartes {
		ERROR_AL_ELIMINAR_TAREAS, ERROR_AL_ELIMINAR_PIEZAS, ERROR_AL_ELIMINAR_PROCESOS
	}

	public ResultadoEliminarPartes(ErrorEliminarPartes... errores) {
		super(errores);
	}

	public ResultadoEliminarPartes(Map<String, ResultadoEliminarPiezas> resultadosEliminarPiezas, Map<String, ResultadoEliminarProcesos> resultadosEliminarProcesos, ErrorEliminarPartes... errores) {
		super(errores);
		this.resultadosEliminarPiezas = resultadosEliminarPiezas;
		this.resultadosEliminarProcesos = resultadosEliminarProcesos;
	}

	public ResultadoEliminarPartes(ResultadoEliminarTareas resultadoTareas, ErrorEliminarPartes... errores) {
		super(errores);
		this.resultadoTareas = resultadoTareas;
	}

	public ResultadoEliminarTareas getResultadoTareas() {
		return resultadoTareas;
	}

	public Map<String, ResultadoEliminarPiezas> getResultadosEliminarPiezas() {
		return resultadosEliminarPiezas;
	}

	public Map<String, ResultadoEliminarProcesos> getResultadosEliminarProcesos() {
		return resultadosEliminarProcesos;
	}
}
