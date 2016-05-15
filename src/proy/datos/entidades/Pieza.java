package proy.datos.entidades;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import proy.datos.clases.Estado;

@Entity
@Table(name = "pieza")
public class Pieza {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "codigo")
	private Integer codigo;

	@Version
	@Column(name = "version")
	private Long version;

	@Column(name = "nombre", length = 100, nullable = false)
	private String nombre;

	@Column(name = "codigo_plano", length = 100, nullable = false)
	private String codigoPlano;

	@Column(name = "cantidad", nullable = false)
	private Integer cantidad;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado", length = 10, nullable = false)
	private Estado estado;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "codparte", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "pieza_codparte_fk"))
	private Parte parte;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "codmaterial", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "pieza_codmaterial_fk"))
	private Material material;

	@ManyToMany(mappedBy = "piezas", fetch = FetchType.EAGER)
	private List<Proceso> procesos;

	public Pieza() {
		procesos = new ArrayList<Proceso>();
	}

	public Pieza(Integer codigo, Long version, String nombre, String codigoPlano, Integer cantidad, Estado estado, Parte parte, Material material) {
		this();
		this.codigo = codigo;
		this.version = version;
		this.nombre = nombre;
		this.codigoPlano = codigoPlano;
		this.cantidad = cantidad;
		this.estado = estado;
		this.parte = parte;
		this.material = material;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
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

	public String getCodigoPlano() {
		return codigoPlano;
	}

	public void setCodigoPlano(String codigoPlano) {
		this.codigoPlano = codigoPlano;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Parte getParte() {
		return parte;
	}

	public void setParte(Parte parte) {
		this.parte = parte;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
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
		Pieza other = (Pieza) obj;
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
