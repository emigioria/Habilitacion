/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import java.util.List;

import proy.logica.gestores.resultados.ResultadoEliminarMaterial.ErrorEliminarMaterial;

public class ResultadoEliminarMaterial extends Resultado<ErrorEliminarMaterial> {

	private List<String> piezasAsociadasAlMaterial;

	public enum ErrorEliminarMaterial {
		PIEZAS_ACTIVAS_ASOCIADAS
	}

	public ResultadoEliminarMaterial(List<String> piezasAsociadasAlMaterial, ErrorEliminarMaterial... errores) {
		super(errores);
		this.piezasAsociadasAlMaterial = piezasAsociadasAlMaterial;
	}

	public List<String> getPiezasAsociadasPorMaterial() {
		return piezasAsociadasAlMaterial;
	}
}
