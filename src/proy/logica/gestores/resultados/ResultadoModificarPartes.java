/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import java.util.Collection;
import java.util.HashSet;

import proy.logica.gestores.resultados.ResultadoModificarPartes.ErrorModificarPartes;

public class ResultadoModificarPartes extends Resultado<ErrorModificarPartes> {
	
	HashSet<String> nombresYaExistentes;

	public enum ErrorModificarPartes {
		NOMBRE_INCOMPLETO, CANTIDAD_INCOMPLETA, NOMBRE_YA_EXISTENTE, NOMBRE_INGRESADO_REPETIDO
	}

	public ResultadoModificarPartes(Collection<String> nombresYaExistentes, ErrorModificarPartes... errores) {
		super(errores);
		this.nombresYaExistentes = new HashSet<>(nombresYaExistentes);
	}
	
	public HashSet<String> getRepetidos() {
		return nombresYaExistentes;
	}
}
