/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import java.util.Set;

import proy.logica.gestores.resultados.ResultadoCrearPiezasAlModificarMaquina.ErrorCrearPiezasAlModificarMaquina;

public class ResultadoCrearPiezasAlModificarMaquina extends Resultado<ErrorCrearPiezasAlModificarMaquina> {

	private Set<String> nombresYaExistentes;

	public enum ErrorCrearPiezasAlModificarMaquina {
		NOMBRE_INCOMPLETO, NOMBRE_YA_EXISTENTE, NOMBRE_INGRESADO_REPETIDO, CANTIDAD_INCOMPLETA, MATERIAL_INCOMPLETO
	}

	public ResultadoCrearPiezasAlModificarMaquina(Set<String> nombresYaExistentes, ErrorCrearPiezasAlModificarMaquina... errores) {
		super(errores);
		this.nombresYaExistentes = nombresYaExistentes;
	}

	public Set<String> getNombresYaExistentes() {
		return this.nombresYaExistentes;
	}
}
