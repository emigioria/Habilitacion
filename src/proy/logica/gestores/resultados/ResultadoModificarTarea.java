package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoModificarTarea.ErrorModificarTarea;

public class ResultadoModificarTarea extends Resultado<ErrorModificarTarea> {

	public enum ErrorModificarTarea {

	}

	public ResultadoModificarTarea(ErrorModificarTarea... errores) {
		super(errores);
	}
}
