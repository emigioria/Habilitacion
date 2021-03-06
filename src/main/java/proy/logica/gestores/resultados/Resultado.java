/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores.resultados;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Resultado<T> {

	private List<T> errores;

	@SafeVarargs
	public Resultado(T... errores) {
		this.errores = Arrays.asList(errores);
	}

	public Boolean hayErrores() {
		return !errores.isEmpty();
	}

	public List<T> getErrores() {
		return new ArrayList<>(errores);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		if(getClass() != obj.getClass()){
			return false;
		}
		Resultado<?> other = (Resultado<?>) obj;
		if(errores == null){
			if(other.errores != null){
				return false;
			}
		}
		else if(!errores.equals(other.errores)){
			return false;
		}
		return true;
	}
}
