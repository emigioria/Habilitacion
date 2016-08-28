package proy.gui.modelos;

import proy.gui.Contra;

public class DatosLogin {

	String nombreUsuario;

	String contra;

	public DatosLogin(String nombreUsuario, String contra) {
		super();
		this.nombreUsuario = nombreUsuario;
		this.contra = contra;
	}

	public DatosLogin(String nombreUsuario, char[] contra) {
		super();
		this.nombreUsuario = nombreUsuario;
		this.contra = Contra.encriptarMD5(contra);
	}
}
