/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import java.util.ArrayList;
import proy.datos.entidades.Pieza;
import proy.logica.gestores.resultados.ResultadoCrearPiezas.ErrorCrearPiezas;

public class ResultadoCrearPiezas extends Resultado<ErrorCrearPiezas> {

	ArrayList<Pieza> piezasConNombreYaExistente;
	
	public enum ErrorCrearPiezas {
		NOMBRE_INCOMPLETO, NOMBRE_YA_EXISTENTE, NOMBRE_INGRESADO_REPETIDO
	}

	public ResultadoCrearPiezas(ArrayList<Pieza> piezasConNombreYaExistente, ErrorCrearPiezas... errores) {
		super(errores);
		this.piezasConNombreYaExistente = piezasConNombreYaExistente;
	}
	
	public ArrayList<Pieza> getPiezasConNombreYaExistente(){
		return this.piezasConNombreYaExistente;
	}
}
