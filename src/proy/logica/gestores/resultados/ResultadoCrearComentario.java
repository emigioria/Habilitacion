package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoCrearComentario.ErrorCrearComentario;

public class ResultadoCrearComentario extends Resultado<ErrorCrearComentario> {

	public enum ErrorCrearComentario {

	}

	public ResultadoCrearComentario(ErrorCrearComentario... errores) {
		super(errores);
	}
}
