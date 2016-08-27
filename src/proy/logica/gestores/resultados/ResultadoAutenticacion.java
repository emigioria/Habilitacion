package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoAutenticacion.ErrorResultadoAutenticacion;

public class ResultadoAutenticacion extends Resultado<ErrorResultadoAutenticacion> {

	public enum ErrorResultadoAutenticacion {
		DatosInvalidos;
	}

}
