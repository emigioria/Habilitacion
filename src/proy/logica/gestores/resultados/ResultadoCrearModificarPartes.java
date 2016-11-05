/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import proy.datos.entidades.Maquina;
import proy.logica.gestores.resultados.ResultadoCrearModificarPartes.ErrorCrearModificarPartes;

public class ResultadoCrearModificarPartes extends Resultado<ErrorCrearModificarPartes> {

	private Map<String, ResultadoCrearModificarPiezas> resultadosCrearModificarPiezas;
	Map<Maquina, ArrayList<ErrorCrearModificarPartes>> erroresPorMaquina;
	Map<Maquina, HashSet<String>> nombresYaExistentesPorMaquina;

	public enum ErrorCrearModificarPartes {
		NOMBRE_INCOMPLETO, NOMBRE_YA_EXISTENTE, NOMBRE_INGRESADO_REPETIDO,
		ERROR_AL_CREAR_O_MODIFICAR_PIEZAS
	}

	public ResultadoCrearModificarPartes(ErrorCrearModificarPartes... errores) {
		super(errores);
	}

	public ResultadoCrearModificarPartes(Map<Maquina, ArrayList<ErrorCrearModificarPartes>> erroresPorMaquina, Map<Maquina, HashSet<String>> nombresYaExistentesPorMaquina, Map<String, ResultadoCrearModificarPiezas> resultadosCrearModificarPiezas, ErrorCrearModificarPartes... errores) {
		super(errores);
		this.erroresPorMaquina = erroresPorMaquina;
		this.nombresYaExistentesPorMaquina = nombresYaExistentesPorMaquina;
		this.resultadosCrearModificarPiezas = resultadosCrearModificarPiezas;
	}

	public Map<String, ResultadoCrearModificarPiezas> getResultadosCrearModificarPiezas() {
		return resultadosCrearModificarPiezas;
	}
}
