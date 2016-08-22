package proy.datos.entidades;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@PrimaryKeyJoinColumn(name = "codusuario", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "operario_codusuario_fk"))
@Table(name = "operario")
public class Operario extends Usuario {

	public Operario(String nombre, String apellido) {
		super(nombre, apellido);
	}

}
