package proy.datos.entidades;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import proy.datos.clases.Estado;

@Entity
@PrimaryKeyJoinColumn(name = "codusuario", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "operario_codusuario_fk"))
@Table(name = "operario")
public class Operario extends Usuario {

	public Operario(Integer codigo, Long version, String nombre, Estado estado) {
		super(codigo, version, nombre, estado);
	}

}
