/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import java.util.Collection;
import java.util.Map;

import proy.logica.gestores.resultados.ResultadoCrearModificarPartesAlModificarMaquina.ErrorCrearModificarPartesAlModificarMaquina;

public class ResultadoCrearModificarPartesAlModificarMaquina extends Resultado<ErrorCrearModificarPartesAlModificarMaquina> {

	private Map<String, ResultadoCrearPiezasAlModificarMaquina> resultadosCrearPiezas;
	private Collection<String> nombresYaExistentes;

	public enum ErrorCrearModificarPartesAlModificarMaquina {
		NOMBRE_INCOMPLETO, NOMBRE_YA_EXISTENTE, NOMBRE_INGRESADO_REPETIDO, CANTIDAD_INCOMPLETA, ERROR_AL_CREAR_PIEZAS
	}

	public ResultadoCrearModificarPartesAlModificarMaquina(ErrorCrearModificarPartesAlModificarMaquina... errores) {
		super(errores);
	}

	public ResultadoCrearModificarPartesAlModificarMaquina(Map<String, ResultadoCrearPiezasAlModificarMaquina> resultadosCrearPiezas, Collection<String> nombresYaExistentes, ErrorCrearModificarPartesAlModificarMaquina... errores) {
		super(errores);
		this.nombresYaExistentes = nombresYaExistentes;
		this.resultadosCrearPiezas = resultadosCrearPiezas;
	}

	public Collection<String> getNombresYaExistentes() {
		return this.nombresYaExistentes;
	}

	public Map<String, ResultadoCrearPiezasAlModificarMaquina> getResultadosCrearPiezas() {
		return resultadosCrearPiezas;
	}
}
