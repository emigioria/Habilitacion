package proy.logica.gestores.resultados;

import proy.logica.gestores.resultados.ResultadoCrearProceso.ErrorCrearProceso;

public class ResultadoCrearProceso extends Resultado<ErrorCrearProceso> {

	public enum ErrorCrearProceso {
		NombreIncompleto, M�quinaIncompleta, ParteIncompleta, Descripci�nIncompleta, TipoProcesoIncompleto, TiempoTeoricoPreparaci�nIncompleto, TiempoTeoricoProcesoIncompleto, MaquinaParteDescripcionyTipoRepetido;
	}

	public ResultadoCrearProceso(ErrorCrearProceso... errores) {
		super(errores);
	}
}
