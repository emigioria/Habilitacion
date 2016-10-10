package proy.datos.clases;

public class DatosLogin {

	private String dni;

	private char[] contrasenia;

	public DatosLogin(String dni, char[] contrasenia) {
		this.dni = dni;
		this.contrasenia = contrasenia;
	}

	public String getDNI() {
		return dni;
	}

	public char[] getContrasenia() {
		return contrasenia;
	}

}
