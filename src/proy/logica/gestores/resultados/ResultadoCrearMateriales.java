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

	HashSet<String> nombresRepetidos;

	public enum ErrorCrearMateriales {
		NombreIncompleto, NombreYaExistente, NombreIngresadoRepetido
	}

	public ResultadoCrearMateriales(Collection<String> nombresRepetidos, ErrorCrearMateriales... errores) {
		super(errores);
		this.nombresRepetidos = new HashSet<>(nombresRepetidos);
	}

	public HashSet<String> getRepetidos() {
		return nombresRepetidos;
	}
}
