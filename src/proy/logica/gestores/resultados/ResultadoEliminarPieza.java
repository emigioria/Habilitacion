package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoEliminarPieza.ErrorEliminarPieza;

public class ResultadoEliminarPieza extends Resultado<ErrorEliminarPieza> {

	public enum ErrorEliminarPieza {
		TareasNoTerminadasAsociadas;
	}

	public ResultadoEliminarPieza(ErrorEliminarPieza... errores) {
		super(errores);
	}
}
