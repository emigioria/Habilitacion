package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoEliminarParte.ErrorEliminarMaquina;

public class ResultadoEliminarParte extends Resultado<ErrorEliminarMaquina> {

	public enum ErrorEliminarMaquina {
		TareasNoTerminadasAsociadas;
	}

	public ResultadoEliminarParte(ErrorEliminarMaquina... errores) {
		super(errores);
	}
}
