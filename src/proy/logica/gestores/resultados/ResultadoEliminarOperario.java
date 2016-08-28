package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoEliminarOperario.ErrorEliminarOperario;

public class ResultadoEliminarOperario extends Resultado<ErrorEliminarOperario> {

	public enum ErrorEliminarOperario {

	}

	public ResultadoEliminarOperario(ErrorEliminarOperario... errores) {
		super(errores);
	}
}
