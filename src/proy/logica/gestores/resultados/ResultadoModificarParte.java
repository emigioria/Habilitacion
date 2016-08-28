package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoModificarParte.ErrorModificarParte;

public class ResultadoModificarParte extends Resultado<ErrorModificarParte> {

	public enum ErrorModificarParte {
		NombreIncompleto, CantidadIncompleta, NombreRepetido;
	}

	public ResultadoModificarParte(ErrorModificarParte... errores) {
		super(errores);
	}
}
