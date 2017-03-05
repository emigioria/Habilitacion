/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.comun;

import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

/**
 * Clase encargada de la conversion de strings
 */
@Service
public class FormateadorString {

	/**
	 * Retorna el string con la primera letra en mayúscula
	 *
	 * @param entrada
	 *            string a convertir
	 * @return string de entrada con la primera letra en mayúscula
	 */
	public String primeraMayuscula(String entrada) {
		if(entrada == null){
			return null;
		}
		switch(entrada.length()) {
		// Los strings vacíos se retornan como están.
		case 0:
			entrada = "";
			break;
		// Los strings de un solo caracter se devuelven en mayúscula.
		case 1:
			entrada = entrada.toUpperCase();
			break;
		// Sino, mayúscula la primera letra, minúscula el resto.
		default:
			entrada = entrada.substring(0, 1).toUpperCase()
					+ entrada.substring(1).toLowerCase();
		}
		return entrada;
	}

	/**
	 * Retorna el string con la primera letra de cada palabra en mayúscula a menos que sean "a|de|del|el|en|la|las|lo|los|of|y"
	 *
	 * @param entrada
	 *            string a convertir
	 * @return string de entrada con la primera letra de cada palabra en mayúscula
	 */
	public String nombrePropio(String entrada) {
		if(entrada == null){
			return null;
		}

		//Si la entrada consiste solo de espacios en blanco, se devuelve una cadena vacía
		entrada.trim();
		if(entrada.equals("")){
			return "";
		}

		//Se convierte toda la cadena a minúsculas
		entrada.toLowerCase();

		//Se separan las palabras
		String[] partes = entrada.split(" ");
		StringBuffer salida = new StringBuffer();

		//Si la palabra es alguna de las siguientes, no se debe convertir a mayúscula su primer letra
		Pattern patron = Pattern.compile("a|de|del|el|en|la|las|lo|los|of|y");
		for(String parte: partes){
			if(patron.matcher(parte).matches()){
				salida.append(parte + " ");
			}
			else{
				if(!parte.equals("")){
					salida.append(parte.substring(0, 1).toUpperCase() + parte.substring(1) + " ");
				}
			}
		}

		//Se convierte la primera letra a mayúscula en caso de no haberlo hecho antes y se recortan los espacios al final
		return (salida.substring(0, 1).toUpperCase() + salida.substring(1)).trim();
	}
}
