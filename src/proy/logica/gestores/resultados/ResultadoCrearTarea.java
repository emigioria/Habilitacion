package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoCrearTarea.ErrorCrearTarea;

public class ResultadoCrearTarea extends Resultado<ErrorCrearTarea> {

	public enum ErrorCrearTarea {

	}

	public ResultadoCrearTarea(ErrorCrearTarea... errores) {
		super(errores);
	}
}
