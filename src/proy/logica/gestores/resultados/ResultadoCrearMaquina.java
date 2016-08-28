package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoCrearMaquina.ErrorCrearMaquina;

public class ResultadoCrearMaquina extends Resultado<ErrorCrearMaquina> {

	public enum ErrorCrearMaquina {
		NombreIncompleto, NombreRepetido;
	}

	public ResultadoCrearMaquina(ErrorCrearMaquina... errores) {
		super(errores);
	}
}
