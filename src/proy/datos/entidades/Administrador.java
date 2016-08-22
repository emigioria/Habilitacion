package proy.datos.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import proy.datos.clases.Estado;

@Entity
@PrimaryKeyJoinColumn(name = "codusuario", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "administrador_codusuario_fk"))
@Table(name = "administrador")
public class Administrador extends Usuario {

	@Column(name = "contrasenia", length = 100, nullable = false)
	private String contrasenia;

	public Administrador() {
		super();
	}

	public Administrador(Long version, String nombre, Estado estado, String contrasenia) {
		super(version, nombre, estado);
		this.contrasenia = contrasenia;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}
}
