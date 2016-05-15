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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import proy.datos.clases.Estado;

@Entity
@Table(name = "proceso")
public class Proceso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "codigo")
	private Integer codigo;

	@Version
	@Column(name = "version")
	private Long version;

	@Column(name = "descripcion", length = 100)
	private String descripcion;

	@Column(name = "tiempo_proc", length = 100)
	private String tiempoTeoricoProceso;

	@Column(name = "tiempo_prep", length = 100)
	private String tiempoTeoricoPreparacion;

	@Column(name = "observaciones", length = 500)
	private String observaciones;

	@Column(name = "tipo", length = 100)
	private String tipo;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado", length = 10, nullable = false)
	private Estado estado;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "codparte", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "proceso_codparte_fk"))
	private Parte parte;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "pieza_proceso", joinColumns = @JoinColumn(name = "codproceso"), foreignKey = @ForeignKey(name = "pieza_proceso_codproceso_fk"), inverseJoinColumns = @JoinColumn(name = "codpieza"), inverseForeignKey = @ForeignKey(name = "pieza_proceso_codpieza_fk"))
	private List<Pieza> piezas;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "herramienta_proceso", joinColumns = @JoinColumn(name = "codproceso"), foreignKey = @ForeignKey(name = "herramienta_proceso_codproceso_fk"), inverseJoinColumns = @JoinColumn(name = "codherramienta"), inverseForeignKey = @ForeignKey(name = "herramienta_proceso_codherramienta_fk"))
	private List<Herramienta> herramientas;

	public Proceso() {
		herramientas = new ArrayList<Herramienta>();
		piezas = new ArrayList<Pieza>();
	}

	public Proceso(Integer codigo, Long version, String descripcion, String tiempoTeoricoProceso, String tiempoTeoricoPreparacion, String observaciones, String tipo, Estado estado, Parte parte) {
		this();
		this.codigo = codigo;
		this.version = version;
		this.descripcion = descripcion;
		this.tiempoTeoricoProceso = tiempoTeoricoProceso;
		this.tiempoTeoricoPreparacion = tiempoTeoricoPreparacion;
		this.observaciones = observaciones;
		this.tipo = tipo;
		this.estado = estado;
		this.parte = parte;
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTiempoTeoricoProceso() {
		return tiempoTeoricoProceso;
	}

	public void setTiempoTeoricoProceso(String tiempoTeoricoProceso) {
		this.tiempoTeoricoProceso = tiempoTeoricoProceso;
	}

	public String getTiempoTeoricoPreparacion() {
		return tiempoTeoricoPreparacion;
	}

	public void setTiempoTeoricoPreparacion(String tiempoTeoricoPreparacion) {
		this.tiempoTeoricoPreparacion = tiempoTeoricoPreparacion;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
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

	public List<Pieza> getPiezas() {
		return piezas;
	}

	public void setPiezas(List<Pieza> piezas) {
		this.piezas = piezas;
	}

	public List<Herramienta> getHerramientas() {
		return herramientas;
	}

	public void setHerramientas(List<Herramienta> herramientas) {
		this.herramientas = herramientas;
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
		Proceso other = (Proceso) obj;
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
