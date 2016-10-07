package proy.datos.clases;

public class DatosLogin {

	private String nombreUsuario;

	private char[] contrasenia;

	public DatosLogin(String usuario, char[] contrasenia) {
		this.nombreUsuario = usuario;
		this.contrasenia = contrasenia;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public char[] getContrasenia() {
		return contrasenia;
	}

}
