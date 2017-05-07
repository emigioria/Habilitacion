/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoModificarMaquina.ErrorModificarMaquina;

public class ResultadoModificarMaquina extends Resultado<ErrorModificarMaquina> {

	private ResultadoCrearModificarPartesAlModificarMaquina resultadoCrearModificarPartes;
	
	public enum ErrorModificarMaquina {
		NOMBRE_INCOMPLETO, NOMBRE_REPETIDO, ERROR_AL_CREAR_O_MODIFICAR_PARTES
	}

	public ResultadoModificarMaquina(ResultadoCrearModificarPartesAlModificarMaquina resultadoCrearModificarPartes, ErrorModificarMaquina... errores) {
		super(errores);
		this.resultadoCrearModificarPartes = resultadoCrearModificarPartes;
	}
	
	public ResultadoCrearModificarPartesAlModificarMaquina getResultadoCrearModificarPartes(){
		return resultadoCrearModificarPartes;
	}
}
