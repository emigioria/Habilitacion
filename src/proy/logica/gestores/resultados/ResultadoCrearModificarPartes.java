/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import java.util.Map;
import java.util.Set;

import proy.logica.gestores.resultados.ResultadoCrearModificarPartes.ErrorCrearModificarPartes;

public class ResultadoCrearModificarPartes extends Resultado<ErrorCrearModificarPartes> {

	private Map<String, ResultadoCrearPiezas> resultadosCrearPiezas;
	private Set<String> nombresYaExistentes;

	public enum ErrorCrearModificarPartes {
		NOMBRE_INCOMPLETO, NOMBRE_YA_EXISTENTE, NOMBRE_INGRESADO_REPETIDO,
		ERROR_AL_CREAR_PIEZAS
	}

	public ResultadoCrearModificarPartes(ErrorCrearModificarPartes... errores) {
		super(errores);
	}

	public ResultadoCrearModificarPartes(Map<String, ResultadoCrearPiezas> resultadosCrearPiezas, Set<String> nombresYaExistentes, ErrorCrearModificarPartes... errores) {
		super(errores);
		this.nombresYaExistentes = nombresYaExistentes;
		this.resultadosCrearPiezas = resultadosCrearPiezas;
	}

	public Set<String> getNombresYaExistentes() {
		return this.nombresYaExistentes;
	}

	public Map<String, ResultadoCrearPiezas> getResultadosCrearPiezas() {
		return resultadosCrearPiezas;
	}
}
