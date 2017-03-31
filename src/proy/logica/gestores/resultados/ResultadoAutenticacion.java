/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import proy.datos.entidades.Administrador;
import proy.logica.gestores.resultados.ResultadoAutenticacion.ErrorAutenticacion;

public class ResultadoAutenticacion extends Resultado<ErrorAutenticacion> {

	public enum ErrorAutenticacion {
		DATOS_INVALIDOS
	}

	private Administrador administrador;

	public ResultadoAutenticacion(ErrorAutenticacion... errores) {
		super(errores);
	}

	public ResultadoAutenticacion(Administrador administrador, ErrorAutenticacion... errores) {
		super(errores);
		this.administrador = administrador;
	}

	public Administrador getAdministrador() {
		return administrador;
	}
}
