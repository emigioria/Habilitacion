/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import proy.logica.gestores.resultados.ResultadoEliminarMateriales.ErrorEliminarMateriales;

public class ResultadoEliminarMateriales extends Resultado<ErrorEliminarMateriales> {

	Map<String, List<String>> piezasAsociadasPorMaterial;

	public enum ErrorEliminarMateriales {
		PiezasActivasAsociadas;
	}

	public ResultadoEliminarMateriales(Map<String, List<String>> piezasAsociadasPorMaterial, ErrorEliminarMateriales... errores) {
		super(errores);
		this.piezasAsociadasPorMaterial = new HashMap<>(piezasAsociadasPorMaterial);
	}

	public Map<String, List<String>> getPiezasAsociadasPorMaterial() {
		return piezasAsociadasPorMaterial;
	}
}
