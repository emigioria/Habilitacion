package proy.logica.gestores.resultados;

import java.util.List;

public interface Resultado<T> {

	public Boolean hayErrores();

	public List<T> getErrores();
}
