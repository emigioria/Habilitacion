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
	private Set<String> nombresRepetidos;

	public enum ErrorCrearPiezas {
		NOMBRE_INCOMPLETO, NOMBRE_YA_EXISTENTE, NOMBRE_INGRESADO_REPETIDO, CANTIDAD_INCOMPLETA, MATERIAL_INCOMPLETO
	}

	public ResultadoCrearPiezas(Set<String> nombresYaExistentes, Set<String> nombresRepetidos, ErrorCrearPiezas... errores) {
		super(errores);
		this.nombresYaExistentes = nombresYaExistentes;
		this.nombresRepetidos = nombresRepetidos;
	}

	public Set<String> getNombresYaExistentes() {
		return this.nombresYaExistentes;
	}

	public Set<String> getNombresRepetidos() {
		return nombresRepetidos;
	}
}
