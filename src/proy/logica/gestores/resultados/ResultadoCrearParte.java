package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoCrearParte.ErrorCrearParte;

public class ResultadoCrearParte extends Resultado<ErrorCrearParte> {

	public enum ErrorCrearParte {
		NombreIncompleto, CantidadIncompleta, NombreRepetido;
	}

	public ResultadoCrearParte(ErrorCrearParte... errores) {
		super(errores);
	}
}
