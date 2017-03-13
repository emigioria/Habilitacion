/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import java.util.Map;
import java.util.Set;

import proy.logica.gestores.resultados.ResultadoCrearPartesAlCrearMaquina.ErrorCrearPartesAlCrearMaquina;

public class ResultadoCrearPartesAlCrearMaquina extends Resultado<ErrorCrearPartesAlCrearMaquina> {

	private Map<String, ResultadoCrearPiezasAlCrearMaquina> resultadosCrearPiezas;
	private Set<String> nombresRepetidos;

	public enum ErrorCrearPartesAlCrearMaquina {
		NOMBRE_INCOMPLETO, NOMBRE_INGRESADO_REPETIDO, CANTIDAD_INCOMPLETA, ERROR_AL_CREAR_PIEZAS
	}

	public ResultadoCrearPartesAlCrearMaquina(ErrorCrearPartesAlCrearMaquina... errores) {
		super(errores);
	}

	public ResultadoCrearPartesAlCrearMaquina(Map<String, ResultadoCrearPiezasAlCrearMaquina> resultadosCrearPiezas, Set<String> nombresRepetidos, ErrorCrearPartesAlCrearMaquina... errores) {
		super(errores);
		this.nombresRepetidos = nombresRepetidos;
		this.resultadosCrearPiezas = resultadosCrearPiezas;
	}

	public Set<String> getNombresRepetidos() {
		return nombresRepetidos;
	}

	public Map<String, ResultadoCrearPiezasAlCrearMaquina> getResultadosCrearPiezas() {
		return resultadosCrearPiezas;
	}
}
