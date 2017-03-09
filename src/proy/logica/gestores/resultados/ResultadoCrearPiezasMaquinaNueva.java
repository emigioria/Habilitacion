/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import java.util.Set;

import proy.logica.gestores.resultados.ResultadoCrearPiezasMaquinaNueva.ErrorCrearPiezasMaquinaNueva;

public class ResultadoCrearPiezasMaquinaNueva extends Resultado<ErrorCrearPiezasMaquinaNueva> {

	private Set<String> nombresRepetidos;

	public enum ErrorCrearPiezasMaquinaNueva {
		NOMBRE_INCOMPLETO, NOMBRE_INGRESADO_REPETIDO, CANTIDAD_INCOMPLETA, MATERIAL_INCOMPLETO
	}

	public ResultadoCrearPiezasMaquinaNueva(Set<String> nombresRepetidos, ErrorCrearPiezasMaquinaNueva... errores) {
		super(errores);
		this.nombresRepetidos = nombresRepetidos;
	}

	public Set<String> getNombresRepetidos() {
		return this.nombresRepetidos;
	}
}
