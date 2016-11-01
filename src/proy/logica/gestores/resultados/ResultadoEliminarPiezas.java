/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import java.util.ArrayList;

import proy.datos.entidades.Pieza;
import proy.logica.gestores.resultados.ResultadoEliminarPiezas.ErrorEliminarPiezas;

public class ResultadoEliminarPiezas extends Resultado<ErrorEliminarPiezas> {

	private ResultadoEliminarTareas resultadoTareas;
	private ArrayList<Pieza> piezasDadasBajaLogica;
	
	public enum ErrorEliminarPiezas {
		ERROR_AL_ELIMINAR_TAREAS
	}

	public ResultadoEliminarPiezas(ErrorEliminarPiezas... errores) {
		super(errores);
	}
	
	public ResultadoEliminarPiezas(ResultadoEliminarTareas resultadoTareas, ArrayList<Pieza> piezasDadasBajaLogica, ErrorEliminarPiezas... errores) {
		super(errores);
		this.resultadoTareas = resultadoTareas;
		this.piezasDadasBajaLogica = piezasDadasBajaLogica;
	}
	
	public ResultadoEliminarTareas getResultadoTareas() {
		return resultadoTareas;
	}
	
	public ArrayList<Pieza> getPiezasDadasBajaLogica(){
		return piezasDadasBajaLogica;
	}
}
