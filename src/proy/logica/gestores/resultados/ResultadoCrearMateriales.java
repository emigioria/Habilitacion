/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import java.util.Collection;
import java.util.HashSet;

import proy.logica.gestores.resultados.ResultadoCrearMateriales.ErrorCrearMateriales;

public class ResultadoCrearMateriales extends Resultado<ErrorCrearMateriales> {

	private HashSet<String> nombresYaExistentes;

	public enum ErrorCrearMateriales {
		NOMBRE_INCOMPLETO, NOMBRE_YA_EXISTENTE, NOMBRE_INGRESADO_REPETIDO
	}

	public ResultadoCrearMateriales(Collection<String> nombresYaExistentes, ErrorCrearMateriales... errores) {
		super(errores);
		this.nombresYaExistentes = new HashSet<>(nombresYaExistentes);
	}

	public HashSet<String> getRepetidos() {
		return nombresYaExistentes;
	}
}
