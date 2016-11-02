/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import java.util.Map;

import proy.logica.gestores.resultados.ResultadoCrearModificarPartes.ErrorCrearModificarPartes;

public class ResultadoCrearModificarPartes extends Resultado<ErrorCrearModificarPartes> {

	private Map<String, ResultadoCrearModificarPiezas> resultadosCrearModificarPiezas;

	public enum ErrorCrearModificarPartes {
		ERROR_AL_CREAR_O_MODIFICAR_PIEZAS
	}

	public ResultadoCrearModificarPartes(ErrorCrearModificarPartes... errores) {
		super(errores);
	}

	public ResultadoCrearModificarPartes(Map<String, ResultadoCrearModificarPiezas> resultadosCrearModificarPiezas, ErrorCrearModificarPartes... errores) {
		super(errores);
		this.resultadosCrearModificarPiezas = resultadosCrearModificarPiezas;
	}

	public Map<String, ResultadoCrearModificarPiezas> getResultadosCrearModificarPiezas() {
		return resultadosCrearModificarPiezas;
	}
}
