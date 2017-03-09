package proy.gui.controladores.errores;

import java.util.Map;

import proy.logica.gestores.resultados.ResultadoCrearModificarPartes;
import proy.logica.gestores.resultados.ResultadoCrearModificarPartes.ErrorCrearModificarPartes;
import proy.logica.gestores.resultados.ResultadoCrearPiezas;
import proy.logica.gestores.resultados.ResultadoCrearPiezas.ErrorCrearPiezas;
import proy.logica.gestores.resultados.ResultadoEliminarPartes;
import proy.logica.gestores.resultados.ResultadoEliminarPartes.ErrorEliminarPartes;
import proy.logica.gestores.resultados.ResultadoEliminarPiezas;
import proy.logica.gestores.resultados.ResultadoEliminarPiezas.ErrorEliminarPiezas;
import proy.logica.gestores.resultados.ResultadoEliminarProcesos;
import proy.logica.gestores.resultados.ResultadoEliminarProcesos.ErrorEliminarProcesos;
import proy.logica.gestores.resultados.ResultadoEliminarTareas;
import proy.logica.gestores.resultados.ResultadoEliminarTareas.ErrorEliminarTareas;
import proy.logica.gestores.resultados.ResultadoModificarMaquina;
import proy.logica.gestores.resultados.ResultadoModificarMaquina.ErrorModificarMaquina;

public class TratamientoDeErroresModificarMaquina {

	/**
	 * Traduce un ResultadoModificarMaquina a un String entendible por el usuario
	 *
	 * @param resultadoModificarMaquina
	 *            resultado a traducir
	 * @return
	 */
	public String tratarErroresModificarMaquina(ResultadoModificarMaquina resultadoModificarMaquina) {
		StringBuffer erroresBfr = new StringBuffer();
		for(ErrorModificarMaquina e: resultadoModificarMaquina.getErrores()){
			switch(e) {
			case NOMBRE_INCOMPLETO:
				erroresBfr.append("El nombre de la máquina está vacío.\n");
				break;
			case NOMBRE_REPETIDO:
				erroresBfr.append("Ya existe una máquina con ese nombre en la Base de Datos.\n");
				break;
			case ERROR_AL_CREAR_O_MODIFICAR_PARTES:
				erroresBfr.append(tratarErroresCrearModificarPartes(resultadoModificarMaquina.getResultadoCrearModificarPartes(), 1));
			}
		}
		return erroresBfr.toString();
	}

	private String tratarErroresCrearModificarPartes(ResultadoCrearModificarPartes resultadoCrearModificarPartes, int nivelIndentacion) {
		StringBuffer erroresBfr = new StringBuffer();
		StringBuffer indentacion = new StringBuffer();
		for(int i = 0; i < nivelIndentacion; i++){
			indentacion.append('\t');
		}

		for(ErrorCrearModificarPartes e: resultadoCrearModificarPartes.getErrores()){
			switch(e) {
			case NOMBRE_INCOMPLETO:
				erroresBfr.append(indentacion);
				erroresBfr.append("Hay partes con nombre vacío.\n");
				break;
			case NOMBRE_INGRESADO_REPETIDO:
				erroresBfr.append(indentacion);
				erroresBfr.append("Hay partes nuevas o modificadas con el mismo nombre.\n");
				break;
			case NOMBRE_YA_EXISTENTE:
				erroresBfr.append(indentacion);
				erroresBfr.append("Estas partes ya existen en el sistema:\n");
				for(String parte: resultadoCrearModificarPartes.getNombresYaExistentes()){
					erroresBfr.append("\t<");
					erroresBfr.append(parte);
					erroresBfr.append(">\n");
				}
				break;
			case ERROR_AL_CREAR_PIEZAS:
				for(Map.Entry<String, ResultadoCrearPiezas> ParteYResultadoCrearPiezas: resultadoCrearModificarPartes.getResultadosCrearPiezas().entrySet()){
					erroresBfr.append(indentacion);
					erroresBfr.append("Errores en la creación de las piezas para la parte <");
					erroresBfr.append(ParteYResultadoCrearPiezas.getKey());
					erroresBfr.append(">:\n");
					erroresBfr.append(tratarErroresCrearPiezas(ParteYResultadoCrearPiezas.getValue(), nivelIndentacion + 1));
				}
			}
		}

		return erroresBfr.toString();
	}

	private String tratarErroresCrearPiezas(ResultadoCrearPiezas resultadoCrearPiezas, int nivelIndentacion) {
		StringBuffer erroresBfr = new StringBuffer();
		StringBuffer indentacion = new StringBuffer();
		for(int i = 0; i < nivelIndentacion; i++){
			indentacion.append('\t');
		}

		for(ErrorCrearPiezas e: resultadoCrearPiezas.getErrores()){
			switch(e) {
			case NOMBRE_INCOMPLETO:
				erroresBfr.append(indentacion);
				erroresBfr.append("Hay piezas con nombre vacío.\n");
				break;
			case NOMBRE_INGRESADO_REPETIDO:
				erroresBfr.append(indentacion);
				erroresBfr.append("Hay piezas nuevas con el mismo nombre:\n");
				for(String pieza: resultadoCrearPiezas.getNombresRepetidos()){
					erroresBfr.append(indentacion);
					erroresBfr.append("\t<");
					erroresBfr.append(pieza);
					erroresBfr.append(">\n");
				}
				break;
			case NOMBRE_YA_EXISTENTE:
				erroresBfr.append(indentacion);
				erroresBfr.append("Estas piezas ya existen en el sistema:\n");
				for(String pieza: resultadoCrearPiezas.getNombresYaExistentes()){
					erroresBfr.append(indentacion);
					erroresBfr.append("\t<");
					erroresBfr.append(pieza);
					erroresBfr.append(">\n");
				}
				break;
			case CANTIDAD_INCOMPLETA:
				erroresBfr.append(indentacion);
				erroresBfr.append("Hay piezas sin cantidad o con una cantidad menor a 1.\n");
				break;
			case MATERIAL_INCOMPLETO:
				erroresBfr.append(indentacion);
				erroresBfr.append("Hay piezas sin material.\n");
				break;
			}
		}

		return erroresBfr.toString();
	}

	/**
	 * Traduce un ResultadoEliminarPartes a un String entendible por el usuario
	 *
	 * @param resultadoEliminarPiezas
	 *            resultado a traducir
	 * @return
	 */
	public String tratarErroresEliminarPartes(ResultadoEliminarPartes resultadoEliminarPartes) {
		StringBuffer erroresBfr = new StringBuffer();
		for(ErrorEliminarPartes ep: resultadoEliminarPartes.getErrores()){
			switch(ep) {
			case ERROR_AL_ELIMINAR_TAREAS:
				erroresBfr.append(tratarErroresEliminarTarea(resultadoEliminarPartes.getResultadoTareas()));
				break;
			case ERROR_AL_ELIMINAR_PIEZAS:
				erroresBfr.append(tratarErroresEliminarPiezas(resultadoEliminarPartes.getResultadosEliminarPiezas()));
				break;
			case ERROR_AL_ELIMINAR_PROCESOS:
				erroresBfr.append(tratarErroresEliminarProcesos(resultadoEliminarPartes.getResultadosEliminarProcesos()));
				break;
			}
		}
		return erroresBfr.toString();
	}

	private String tratarErroresEliminarTarea(ResultadoEliminarTareas resultadoTareas) {
		String errores = "";
		if(resultadoTareas.hayErrores()){
			for(ErrorEliminarTareas ep: resultadoTareas.getErrores()){
				switch(ep) {
				//Todavia no hay errores en eliminar tarea
				}
			}
		}
		return errores;
	}

	private String tratarErroresEliminarPiezas(Map<String, ResultadoEliminarPiezas> resultadosEliminarPiezas) {
		StringBuffer erroresBfr = new StringBuffer();
		for(String pieza: resultadosEliminarPiezas.keySet()){
			ResultadoEliminarPiezas resultado = resultadosEliminarPiezas.get(pieza);
			if(resultado.hayErrores()){
				erroresBfr.append(tratarErroresEliminarPiezas(resultado));
			}
		}

		return erroresBfr.toString();
	}

	private String tratarErroresEliminarProcesos(Map<String, ResultadoEliminarProcesos> resultadosEliminarProcesos) {
		String errores = "";
		for(String proceso: resultadosEliminarProcesos.keySet()){
			ResultadoEliminarProcesos resultado = resultadosEliminarProcesos.get(proceso);
			if(resultado.hayErrores()){
				for(ErrorEliminarProcesos ep: resultado.getErrores()){
					switch(ep) {
					//Todavia no hay errores en eliminar procesos
					}
				}
			}
		}
		return errores;
	}

	/**
	 * Traduce un ResultadoEliminarPiezas a un String entendible por el usuario
	 *
	 * @param resultadoEliminarPiezas
	 *            resultado a traducir
	 * @return
	 */
	public String tratarErroresEliminarPiezas(ResultadoEliminarPiezas resultadoEliminarPiezas) {
		StringBuffer erroresBfr = new StringBuffer();
		for(ErrorEliminarPiezas ep: resultadoEliminarPiezas.getErrores()){
			switch(ep) {
			case ERROR_AL_ELIMINAR_TAREAS:
				erroresBfr.append(tratarErroresEliminarTarea(resultadoEliminarPiezas.getResultadoTareas()));
				break;
			case ERROR_AL_ELIMINAR_PROCESOS:
				erroresBfr.append(tratarErroresEliminarProcesos(resultadoEliminarPiezas.getResultadosEliminarProcesos()));
			}
		}
		return erroresBfr.toString();
	}
}
