package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoEliminarMaterial.ErrorEliminarMaterial;

public class ResultadoEliminarMaterial extends Resultado<ErrorEliminarMaterial> {

	public enum ErrorEliminarMaterial {
		PiezasActivasAsociadas;
	}

	public ResultadoEliminarMaterial(ErrorEliminarMaterial... errores) {
		super(errores);
	}
}
