package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoModificarMaquina.ErrorModificarMaquina;

public class ResultadoModificarMaquina extends Resultado<ErrorModificarMaquina> {

	public enum ErrorModificarMaquina {
		NombreIncompleto, NombreRepetido;
	}

	public ResultadoModificarMaquina(ErrorModificarMaquina... errores) {
		super(errores);
	}
}
