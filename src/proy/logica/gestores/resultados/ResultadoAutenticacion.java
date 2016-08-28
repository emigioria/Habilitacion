package proy.logica.gestores.resultados;

import proy.datos.entidades.Administrador;
import proy.logica.gestores.resultados.ResultadoAutenticacion.ErrorResultadoAutenticacion;

public class ResultadoAutenticacion extends Resultado<ErrorResultadoAutenticacion> {

	public enum ErrorResultadoAutenticacion {
		DatosInvalidos;
	}

	private Administrador administrador;

	public ResultadoAutenticacion(ErrorResultadoAutenticacion... errores) {
		super(errores);
	}

	public ResultadoAutenticacion(Administrador administrador, ErrorResultadoAutenticacion... errores) {
		super(errores);
		this.administrador = administrador;
	}

	public Administrador getAdministrador() {
		return administrador;
	}
}
