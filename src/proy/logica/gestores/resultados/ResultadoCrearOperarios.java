/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import java.util.Collection;
import java.util.HashSet;

import proy.logica.gestores.resultados.ResultadoCrearOperarios.ErrorCrearOperarios;

public class ResultadoCrearOperarios extends Resultado<ErrorCrearOperarios> {

	HashSet<String> dnisYaExistentes;

	public enum ErrorCrearOperarios {
		NOMBRE_INCOMPLETO, APELLIDO_INCOMPLETO, DNI_INCOMPLETO, DNI_YA_EXISTENTE, DNI_INGRESADO_REPETIDO
	}

	public ResultadoCrearOperarios(Collection<String> dnisYaExistentes, ErrorCrearOperarios... errores) {
		super(errores);
		this.dnisYaExistentes = new HashSet<>(dnisYaExistentes);
	}

	public HashSet<String> getDNIRepetidos() {
		return dnisYaExistentes;
	}
}
