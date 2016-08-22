package proy.datos.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@PrimaryKeyJoinColumn(name = "codusuario", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "administrador_codusuario_fk"))
@Table(name = "administrador")
public class Administrador extends Usuario {

	@Column(name = "contrasenia", length = 100, nullable = false)
	private String contrasenia;

	public Administrador() {
		super();
	}

	public Administrador(String nombre, String apellido, String contrasenia) {
		super(nombre, apellido);
		this.contrasenia = contrasenia;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((contrasenia == null) ? 0 : contrasenia.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		if(!super.equals(obj)){
			return false;
		}
		if(getClass() != obj.getClass()){
			return false;
		}
		Administrador other = (Administrador) obj;
		if(contrasenia == null){
			if(other.contrasenia != null){
				return false;
			}
		}
		else if(!contrasenia.equals(other.contrasenia)){
			return false;
		}
		return super.equals(obj);
	}

}
