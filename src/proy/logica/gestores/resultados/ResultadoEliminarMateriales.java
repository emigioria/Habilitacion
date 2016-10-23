/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import java.util.ArrayList;

import proy.datos.entidades.Pieza;
import proy.logica.gestores.resultados.ResultadoEliminarMateriales.ErrorEliminarMaterial;

public class ResultadoEliminarMateriales extends Resultado<ErrorEliminarMaterial> {

	ArrayList<ArrayList<Pieza>> piezasAsociadas;

	public enum ErrorEliminarMaterial {
		PiezasActivasAsociadas;
	}

	public ResultadoEliminarMateriales(ArrayList<ArrayList<Pieza>> piezasAsociadas, ErrorEliminarMaterial... errores) {
		super(errores);
		this.piezasAsociadas = piezasAsociadas;
	}

	public ArrayList<ArrayList<Pieza>> getPiezasAsociadas() {
		return piezasAsociadas;
	}
}
