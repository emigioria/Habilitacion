/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import java.util.ArrayList;
import java.util.Map;

import proy.datos.entidades.Parte;
import proy.logica.gestores.resultados.ResultadoCrearModificarPartes.ErrorCrearModificarPartes;
import proy.logica.gestores.resultados.ResultadoCrearPiezas;

public class ResultadoCrearModificarPartes extends Resultado<ErrorCrearModificarPartes> {

	private Map<Parte, ResultadoCrearPiezas> resultadosCrearPiezas;
	ArrayList<Parte> partesConNombreYaExistente;

	public enum ErrorCrearModificarPartes {
		NOMBRE_INCOMPLETO, NOMBRE_YA_EXISTENTE, NOMBRE_INGRESADO_REPETIDO,
		ERROR_AL_CREAR_PIEZAS
	}

	public ResultadoCrearModificarPartes(ErrorCrearModificarPartes... errores) {
		super(errores);
	}

	public ResultadoCrearModificarPartes(Map<Parte, ResultadoCrearPiezas> resultadosCrearPiezas, ArrayList<Parte> partesConNombreYaExistente, ErrorCrearModificarPartes... errores) {
		super(errores);
		this.partesConNombreYaExistente = partesConNombreYaExistente;
		this.resultadosCrearPiezas = resultadosCrearPiezas;
	}

	public ArrayList<Parte> getPartesConNombreYaExistente(){
		return partesConNombreYaExistente;
	}
	
	public Map<Parte, ResultadoCrearPiezas> getResultadosCrearPiezas() {
		return resultadosCrearPiezas;
	}
}
