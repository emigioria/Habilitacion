package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoEliminarTarea.ErrorEliminarTarea;

public class ResultadoEliminarTarea extends Resultado<ErrorEliminarTarea> {

	public enum ErrorEliminarTarea {

	}

	public ResultadoEliminarTarea(ErrorEliminarTarea... errores) {
		super(errores);
	}
}
