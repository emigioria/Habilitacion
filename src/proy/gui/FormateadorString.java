package proy.gui;

public abstract class FormateadorString {

	public static String primeraMayuscula(String entrada) {
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
}
