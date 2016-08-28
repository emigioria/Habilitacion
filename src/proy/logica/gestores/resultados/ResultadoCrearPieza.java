package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoCrearPieza.ErrorCrearPieza;

public class ResultadoCrearPieza extends Resultado<ErrorCrearPieza> {

	public enum ErrorCrearPieza {
		NombreIncompleto, CantidadIncompleta, NombreRepetido;
	}

	public ResultadoCrearPieza(ErrorCrearPieza... errores) {
		super(errores);
	}
}
