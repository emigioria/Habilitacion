/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import java.util.ArrayList;

import proy.datos.entidades.Parte;
import proy.logica.gestores.resultados.ResultadoEliminarPartes.ErrorEliminarPartes;

public class ResultadoEliminarPartes extends Resultado<ErrorEliminarPartes> {

	private ResultadoEliminarTareas resultadoTareas;
	private ArrayList<Parte> partesDadasBajaLogica;

	public enum ErrorEliminarPartes {
		ERROR_AL_ELIMINAR_TAREAS
	}

	public ResultadoEliminarPartes(ErrorEliminarPartes... errores) {
		super(errores);
	}

	public ResultadoEliminarPartes(ResultadoEliminarTareas resultadoTareas, ArrayList<Parte> partesDadasBajaLogica, ErrorEliminarPartes... errores) {
		this(errores);
		this.resultadoTareas = resultadoTareas;
		this.partesDadasBajaLogica = partesDadasBajaLogica;
	}

	public ResultadoEliminarTareas getResultadoTareas() {
		return resultadoTareas;
	}
	
	public ArrayList<Parte> getPartesDadasBajaLogica(){
		return partesDadasBajaLogica;
	}
}
