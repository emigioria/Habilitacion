package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoCrearOperario.ErrorCrearOperario;

public class ResultadoCrearOperario extends Resultado<ErrorCrearOperario> {

	public enum ErrorCrearOperario {
		NombreIncompleto, ApellidoIncompleto, DNIIncompleto, DNIRepetido;
	}

	public ResultadoCrearOperario(ErrorCrearOperario... errores) {
		super(errores);
	}
}
