package proy.datos.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@PrimaryKeyJoinColumn(name = "codusuario", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "operario_codusuario_fk"))
@Table(name = "operario")
public class Operario extends Usuario {

	@Column(name = "dni", length = 10, nullable = false)
	private String dni;

	public Operario() {
		super();
	}

	public Operario(String nombre, String apellido, String dni) {
		super(nombre, apellido);
		this.dni = dni;
	}

	public String getDNI() {
		return dni;
	}

	public void setDNI(String dni) {
		this.dni = dni;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((dni == null) ? 0 : dni.hashCode());
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
		Operario other = (Operario) obj;
		if(dni == null){
			if(other.dni != null){
				return false;
			}
		}
		else if(!dni.equals(other.dni)){
			return false;
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return this.getNombre() + " " + this.getApellido();
	}
}
