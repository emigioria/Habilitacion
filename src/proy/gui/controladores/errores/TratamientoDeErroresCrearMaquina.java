/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores.errores;

import java.util.Map;

import proy.logica.gestores.resultados.ResultadoCrearMaquina;
import proy.logica.gestores.resultados.ResultadoCrearMaquina.ErrorCrearMaquina;
import proy.logica.gestores.resultados.ResultadoCrearPartesMaquinaNueva;
import proy.logica.gestores.resultados.ResultadoCrearPartesMaquinaNueva.ErrorCrearPartesMaquinaNueva;
import proy.logica.gestores.resultados.ResultadoCrearPiezasMaquinaNueva;
import proy.logica.gestores.resultados.ResultadoCrearPiezasMaquinaNueva.ErrorCrearPiezasMaquinaNueva;

public class TratamientoDeErroresCrearMaquina {

	/**
	 * Traduce un ResultadoCrearMaquina a un String entendible por el usuario
	 *
	 * @param resultadoCrearMaquina
	 *            resultado a traducir
	 * @return
	 */
	public String tratarErroresCrearMaquina(ResultadoCrearMaquina resultadoCrearMaquina) {
		StringBuffer erroresBfr = new StringBuffer();
		for(ErrorCrearMaquina e: resultadoCrearMaquina.getErrores()){
			switch(e) {
			case NOMBRE_INCOMPLETO:
				erroresBfr.append("El nombre de la máquina está vacío.\n");
				break;
			case NOMBRE_REPETIDO:
				erroresBfr.append("Ya existe una máquina con ese nombre en la Base de Datos.\n");
				break;
			case ERROR_AL_CREAR_PARTES:
				erroresBfr.append(tratarErroresCrearPartesNuevas(resultadoCrearMaquina.getResultadosCrearPartes(), 1));
				break;
			}
		}
		return erroresBfr.toString();
	}

	private String tratarErroresCrearPartesNuevas(ResultadoCrearPartesMaquinaNueva resultadoCrearPartes, int nivelIndentacion) {
		StringBuffer erroresBfr = new StringBuffer();
		StringBuffer indentacion = new StringBuffer();
		for(int i = 0; i < nivelIndentacion; i++){
			indentacion.append('\t');
		}

		for(ErrorCrearPartesMaquinaNueva e: resultadoCrearPartes.getErrores()){
			switch(e) {
			case NOMBRE_INCOMPLETO:
				erroresBfr.append(indentacion);
				erroresBfr.append("Hay partes con nombre vacío.\n");
				break;
			case NOMBRE_INGRESADO_REPETIDO:
				erroresBfr.append(indentacion);
				erroresBfr.append("Hay partes nuevas o modificadas con el mismo nombre:\n");
				for(String parte: resultadoCrearPartes.getNombresRepetidos()){
					erroresBfr.append("\t<");
					erroresBfr.append(parte);
					erroresBfr.append(">\n");
				}
				break;
			case CANTIDAD_INCOMPLETA:
				erroresBfr.append(indentacion);
				erroresBfr.append("Hay partes sin cantidad o con una cantidad menor a 1.\n");
				break;
			case ERROR_AL_CREAR_PIEZAS:
				for(Map.Entry<String, ResultadoCrearPiezasMaquinaNueva> ParteYResultadoCrearPiezas: resultadoCrearPartes.getResultadosCrearPiezas().entrySet()){
					erroresBfr.append(indentacion);
					erroresBfr.append("Errores en la creación de las piezas para la parte <");
					erroresBfr.append(ParteYResultadoCrearPiezas.getKey());
					erroresBfr.append(">:\n");
					erroresBfr.append(tratarErroresCrearPiezasNuevas(ParteYResultadoCrearPiezas.getValue(), nivelIndentacion + 1));
				}
			}
		}

		return erroresBfr.toString();
	}

	private String tratarErroresCrearPiezasNuevas(ResultadoCrearPiezasMaquinaNueva resultadoCrearPiezas, int nivelIndentacion) {
		StringBuffer erroresBfr = new StringBuffer();
		StringBuffer indentacion = new StringBuffer();
		for(int i = 0; i < nivelIndentacion; i++){
			indentacion.append('\t');
		}

		for(ErrorCrearPiezasMaquinaNueva e: resultadoCrearPiezas.getErrores()){
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

}
