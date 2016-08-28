package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoCrearMaterial.ErrorCrearMaterial;

public class ResultadoCrearMaterial extends Resultado<ErrorCrearMaterial> {

	public enum ErrorCrearMaterial {
		NombreIncompleto, MedidasIncompletas, NombreRepetido;
	}

	public ResultadoCrearMaterial(ErrorCrearMaterial... errores) {
		super(errores);
	}
}
