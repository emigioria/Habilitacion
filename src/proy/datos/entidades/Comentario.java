package proy.datos.entidades;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "comentario")
public class Comentario {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "codigo")
	private Integer codigo;

	@Version
	@Column(name = "version")
	private Long version;

	@Column(name = "texto", length = 500, nullable = false)
	private String texto;

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha", nullable = false)
	Date fechaComentario;

	public Comentario(String texto, Date fecha) {
		super();
		this.texto = texto;
		this.fechaComentario = fecha;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Date getFechaComentario() {
		return fechaComentario;
	}

	public void setFechaComentario(Date fechaComentario) {
		this.fechaComentario = fechaComentario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((texto == null) ? 0 : texto.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((fechaComentario == null) ? 0 : fechaComentario.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		if(getClass() != obj.getClass()){
			return false;
		}
		Comentario other = (Comentario) obj;
		if(texto == null){
			if(other.texto != null){
				return false;
			}
		}
		else if(!texto.equals(other.texto)){
			return false;
		}
		if(codigo == null){
			if(other.codigo != null){
				return false;
			}
		}
		else if(!codigo.equals(other.codigo)){
			return false;
		}
		if(fechaComentario == null){
			if(other.fechaComentario != null){
				return false;
			}
		}
		else if(!fechaComentario.equals(other.fechaComentario)){
			return false;
		}
		if(version == null){
			if(other.version != null){
				return false;
			}
		}
		else if(!version.equals(other.version)){
			return false;
		}
		return true;
	}
}
