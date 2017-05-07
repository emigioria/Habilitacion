/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import java.util.Set;

import proy.logica.gestores.resultados.ResultadoCrearPiezasAlCrearMaquina.ErrorCrearPiezasALCrearMaquina;

public class ResultadoCrearPiezasAlCrearMaquina extends Resultado<ErrorCrearPiezasALCrearMaquina> {

	private Set<String> nombresRepetidos;

	public enum ErrorCrearPiezasALCrearMaquina {
		NOMBRE_INCOMPLETO, NOMBRE_INGRESADO_REPETIDO, CANTIDAD_INCOMPLETA, MATERIAL_INCOMPLETO
	}

	public ResultadoCrearPiezasAlCrearMaquina(Set<String> nombresRepetidos, ErrorCrearPiezasALCrearMaquina... errores) {
		super(errores);
		this.nombresRepetidos = nombresRepetidos;
	}

	public Set<String> getNombresRepetidos() {
		return this.nombresRepetidos;
	}
}
