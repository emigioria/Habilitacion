package proy.datos.entidades;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "herramienta")
public class Herramienta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "codigo")
	private Integer codigo;

	@Version
	@Column(name = "version")
	private Long version;

	@Column(name = "nombre", length = 100, nullable = false)
	private String nombre;

	@ManyToMany(mappedBy = "herramientas", fetch = FetchType.EAGER)
	private List<Proceso> procesos;

	public Herramienta() {
		procesos = new ArrayList<Proceso>();
	}

	public Herramienta(Long version, String nombre) {
		this();
		this.version = version;
		this.nombre = nombre;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Proceso> getProcesos() {
		return procesos;
	}

	public void setProcesos(List<Proceso> procesos) {
		this.procesos = procesos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
		Herramienta other = (Herramienta) obj;
		if(codigo == null){
			if(other.codigo != null){
				return false;
			}
		}
		else if(!codigo.equals(other.codigo)){
			return false;
		}
		return true;
	}
}
