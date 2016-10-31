/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoModificarProceso.ErrorModificarProceso;

public class ResultadoModificarProceso extends Resultado<ErrorModificarProceso> {

	public enum ErrorModificarProceso {
		NOMBRE_INCOMPLETO, MAQUINA_INCOMPLETA, PARTE_INCOMPLETA, DESCRIPCION_INCOMPLETA, TIPO_PROCESO_INCOMPLETO, TIEMPO_TEORICO_PREPARACION_INCOMPLETO, TIEMPO_TEORICO_PROCESO_INCOMPLETO, MAQUINA_PARTE_DESCRIPCION_Y_TIPO_REPETIDO
	}

	public ResultadoModificarProceso(ErrorModificarProceso... errores) {
		super(errores);
	}
}
