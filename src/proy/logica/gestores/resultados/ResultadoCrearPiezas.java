/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import java.util.Set;

import proy.logica.gestores.resultados.ResultadoCrearPiezas.ErrorCrearPiezas;

public class ResultadoCrearPiezas extends Resultado<ErrorCrearPiezas> {

	private Set<String> nombresYaExistentes;

	public enum ErrorCrearPiezas {
		NOMBRE_INCOMPLETO, NOMBRE_YA_EXISTENTE, NOMBRE_INGRESADO_REPETIDO, MATERIAL_NULO
	}

	public ResultadoCrearPiezas(Set<String> nombresYaExistentes, ErrorCrearPiezas... errores) {
		super(errores);
		this.nombresYaExistentes = nombresYaExistentes;
	}

	public Set<String> getNombresYaExistentes() {
		return this.nombresYaExistentes;
	}
}
