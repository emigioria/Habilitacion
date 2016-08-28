package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoModificarProceso.ErrorModificarProceso;

public class ResultadoModificarProceso extends Resultado<ErrorModificarProceso> {

	public enum ErrorModificarProceso {
		NombreIncompleto, MáquinaIncompleta, ParteIncompleta, DescripciónIncompleta, TipoProcesoIncompleto, TiempoTeoricoPreparaciónIncompleto, TiempoTeoricoProcesoIncompleto, MaquinaParteDescripcionyTipoRepetido;
	}

	public ResultadoModificarProceso(ErrorModificarProceso... errores) {
		super(errores);
	}
}
