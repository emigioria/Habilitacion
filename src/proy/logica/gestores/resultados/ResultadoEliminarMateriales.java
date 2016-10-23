/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import proy.datos.entidades.Pieza;
import proy.logica.gestores.resultados.ResultadoEliminarMateriales.ErrorEliminarMateriales;

public class ResultadoEliminarMateriales extends Resultado<ErrorEliminarMateriales> {

	Set<Pieza> piezasAsociadas;

	public enum ErrorEliminarMateriales {
		PiezasActivasAsociadas;
	}

	public ResultadoEliminarMateriales(Collection<Pieza> piezasAsociadas, ErrorEliminarMateriales... errores) {
		super(errores);
		this.piezasAsociadas = new HashSet<>(piezasAsociadas);
	}

	public Set<Pieza> getPiezasAsociadas() {
		return piezasAsociadas;
	}
}
