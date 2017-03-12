/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores.errores;

import java.util.Map;

import proy.logica.gestores.resultados.ResultadoCrearModificarPartes;
import proy.logica.gestores.resultados.ResultadoCrearModificarPartes.ErrorCrearModificarPartes;
import proy.logica.gestores.resultados.ResultadoCrearPiezas;
import proy.logica.gestores.resultados.ResultadoCrearPiezas.ErrorCrearPiezas;
import proy.logica.gestores.resultados.ResultadoEliminarParte;
import proy.logica.gestores.resultados.ResultadoEliminarParte.ErrorEliminarParte;
import proy.logica.gestores.resultados.ResultadoEliminarPieza;
import proy.logica.gestores.resultados.ResultadoEliminarPieza.ErrorEliminarPieza;
import proy.logica.gestores.resultados.ResultadoEliminarPiezasDeParte;
import proy.logica.gestores.resultados.ResultadoEliminarPiezasDeParte.ErrorEliminarPiezasDeParte;
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
					if(ParteYResultadoCrearPiezas.getValue().hayErrores()){
						erroresBfr.append(indentacion);
						erroresBfr.append("Errores en la creación de las piezas para la parte <");
						erroresBfr.append(ParteYResultadoCrearPiezas.getKey());
						erroresBfr.append(">:\n");
						erroresBfr.append(tratarErroresCrearPiezas(ParteYResultadoCrearPiezas.getValue(), nivelIndentacion + 1));
					}
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
	 * Traduce un ResultadoEliminarParte a un String entendible por el usuario
	 *
	 * @param resultadoEliminarPiezas
	 *            resultado a traducir
	 * @return
	 */
	public String tratarErroresEliminarParte(ResultadoEliminarParte resultadoEliminarParte) {
		StringBuffer erroresBfr = new StringBuffer();
		for(ErrorEliminarParte ep: resultadoEliminarParte.getErrores()){
			switch(ep) {
			case ERROR_AL_ELIMINAR_TAREAS:
				erroresBfr.append(tratarErroresEliminarTarea(resultadoEliminarParte.getResultadoTareas()));
				break;
			case ERROR_AL_ELIMINAR_PIEZAS:
				erroresBfr.append(tratarErroresEliminarPiezasDeParte(resultadoEliminarParte.getResultadosEliminarPiezasDeParte()));
				break;
			case ERROR_AL_ELIMINAR_PROCESOS:
				erroresBfr.append(tratarErroresEliminarProcesos(resultadoEliminarParte.getResultadosEliminarProcesos()));
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

	private String tratarErroresEliminarPiezasDeParte(ResultadoEliminarPiezasDeParte resultadoEliminarPiezasDeParte) {
		StringBuffer erroresBfr = new StringBuffer();
		if(resultadoEliminarPiezasDeParte.hayErrores()){
			for(ErrorEliminarPiezasDeParte ep: resultadoEliminarPiezasDeParte.getErrores()){
				switch(ep) {
				//No hay errores todavía
				}
			}
		}

		return erroresBfr.toString();
	}

	private String tratarErroresEliminarProcesos(ResultadoEliminarProcesos resultadoEliminarProcesos) {
		String errores = "";
		if(resultadoEliminarProcesos.hayErrores()){
			for(ErrorEliminarProcesos ep: resultadoEliminarProcesos.getErrores()){
				switch(ep) {
				//Todavia no hay errores en eliminar procesos
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
	public String tratarErroresEliminarPieza(ResultadoEliminarPieza resultadoEliminarPieza) {
		StringBuffer erroresBfr = new StringBuffer();
		for(ErrorEliminarPieza ep: resultadoEliminarPieza.getErrores()){
			switch(ep) {
			case ERROR_AL_ELIMINAR_TAREAS:
				erroresBfr.append(tratarErroresEliminarTarea(resultadoEliminarPieza.getResultadoEliminarTareas()));
				break;
			case ERROR_AL_ELIMINAR_PROCESOS:
				erroresBfr.append(tratarErroresEliminarProcesos(resultadoEliminarPieza.getResultadoEliminarProcesos()));
			}
		}
		return erroresBfr.toString();
	}
}
