package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoEliminarProceso.ErrorEliminarProceso;

public class ResultadoEliminarProceso extends Resultado<ErrorEliminarProceso> {

	public enum ErrorEliminarProceso {

	}

	public ResultadoEliminarProceso(ErrorEliminarProceso... errores) {
		super(errores);
	}
}
