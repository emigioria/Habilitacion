package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoEliminarHerramienta.ErrorEliminarHerramienta;

public class ResultadoEliminarHerramienta extends Resultado<ErrorEliminarHerramienta> {

	public enum ErrorEliminarHerramienta {
		TareasNoTerminadasAsociadas;
	}

	public ResultadoEliminarHerramienta(ErrorEliminarHerramienta... errores) {
		super(errores);
	}
}
