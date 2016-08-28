package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoEliminarMaquina.ErrorEliminarMaquina;

public class ResultadoEliminarMaquina extends Resultado<ErrorEliminarMaquina> {

	public enum ErrorEliminarMaquina {

	}

	public ResultadoEliminarMaquina(ErrorEliminarMaquina... errores) {
		super(errores);
	}
}
