/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoCrearMaquina.ErrorCrearMaquina;

public class ResultadoCrearMaquina extends Resultado<ErrorCrearMaquina> {

	public enum ErrorCrearMaquina {
		NombreIncompleto, NombreRepetido
	}

	public ResultadoCrearMaquina(ErrorCrearMaquina... errores) {
		super(errores);
	}
}
