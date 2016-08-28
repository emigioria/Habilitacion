package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoCrearHerramienta.ErrorCrearHerramienta;

public class ResultadoCrearHerramienta extends Resultado<ErrorCrearHerramienta> {

	public enum ErrorCrearHerramienta {
		NombreIncompleto, NombreRepetido;
	}

	public ResultadoCrearHerramienta(ErrorCrearHerramienta... errores) {
		super(errores);
	}
}
