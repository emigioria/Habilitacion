package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoCrearProceso.ErrorCrearProceso;

public class ResultadoCrearProceso extends Resultado<ErrorCrearProceso> {

	public enum ErrorCrearProceso {
		NombreIncompleto, MáquinaIncompleta, ParteIncompleta, DescripciónIncompleta, TipoProcesoIncompleto, TiempoTeoricoPreparaciónIncompleto, TiempoTeoricoProcesoIncompleto, MaquinaParteDescripcionyTipoRepetido;
	}

	public ResultadoCrearProceso(ErrorCrearProceso... errores) {
		super(errores);
	}
}
