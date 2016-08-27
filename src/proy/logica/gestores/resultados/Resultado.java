package proy.logica.gestores.resultados;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Resultado<T> {

	List<T> errores;

	@SafeVarargs
	public Resultado(T... errores) {
		this.errores = Arrays.asList(errores);
	}

	public Boolean hayErrores() {
		return !errores.isEmpty();
	}

	public List<T> getErrores() {
		return new ArrayList<T>(errores);
	}

}
