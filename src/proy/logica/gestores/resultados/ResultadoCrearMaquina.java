/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoCrearMaquina.ErrorCrearMaquina;

public class ResultadoCrearMaquina extends Resultado<ErrorCrearMaquina> {

	public enum ErrorCrearMaquina {
		NOMBRE_INCOMPLETO, NOMBRE_REPETIDO, ERROR_AL_CREAR_PARTES
	}

	private ResultadoCrearPartesAlCrearMaquina resultadoCrearPartes;

	public ResultadoCrearMaquina(ErrorCrearMaquina... errores) {
		super(errores);
	}

	public ResultadoCrearMaquina(ResultadoCrearPartesAlCrearMaquina resultadoCrearPartes, ErrorCrearMaquina... errores) {
		super(errores);
		this.resultadoCrearPartes = resultadoCrearPartes;
	}

	public ResultadoCrearPartesAlCrearMaquina getResultadoCrearPartes() {
		return resultadoCrearPartes;
	}
}
