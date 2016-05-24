package proy.datos.entidades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import proy.datos.clases.EstadoTarea;

@Entity
@Table(name = "tarea")
public class Tarea {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "codigo")
	private Integer codigo;

	@Version
	@Column(name = "version")
	private Long version;

	@Column(name = "cantidad_teorica", nullable = false)
	private Integer cantidadTeorica;

	@Column(name = "cantidad_real", nullable = false)
	private Integer cantidadReal;

	@Temporal(TemporalType.TIME)
	@Column(name = "fecha_planificada", nullable = false)
	Date fechaPlanificada;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_hora_inicio", nullable = false)
	Date fechaHoraInicio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_hora_fin", nullable = false)
	Date fechaHoraFin;

	@Column(name = "observaciones", length = 500)
	private String observaciones;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado", length = 15, nullable = false)
	private EstadoTarea estado;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "codproceso", referencedColumnName = "codigo", foreignKey = @ForeignKey(name = "tarea_codProceso_fk"))
	private Proceso proceso;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "codoperario", referencedColumnName = "codusuario", foreignKey = @ForeignKey(name = "tarea_codoperario_fk"))
	private Operario operario;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "tarea")
	private List<Pausa> pausas;

	public Tarea() {
		pausas = new ArrayList<Pausa>();
	}

	public Tarea(Integer codigo, Long version, Integer cantidadTeorica, Integer cantidadReal, Date fechaPlanificada, Date fechaHoraInicio, Date fechaHoraFin, String observaciones, EstadoTarea estado, Proceso proceso, Operario operario) {
		super();
		this.codigo = codigo;
		this.version = version;
		this.cantidadTeorica = cantidadTeorica;
		this.cantidadReal = cantidadReal;
		this.fechaPlanificada = fechaPlanificada;
		this.fechaHoraInicio = fechaHoraInicio;
		this.fechaHoraFin = fechaHoraFin;
		this.observaciones = observaciones;
		this.estado = estado;
		this.proceso = proceso;
		this.operario = operario;
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

	public Integer getCantidadTeorica() {
		return cantidadTeorica;
	}

	public void setCantidadTeorica(Integer cantidadTeorica) {
		this.cantidadTeorica = cantidadTeorica;
	}

	public Integer getCantidadReal() {
		return cantidadReal;
	}

	public void setCantidadReal(Integer cantidadReal) {
		this.cantidadReal = cantidadReal;
	}

	public Date getFechaPlanificada() {
		return fechaPlanificada;
	}

	public void setFechaPlanificada(Date fechaPlanificada) {
		this.fechaPlanificada = fechaPlanificada;
	}

	public Date getFechaHoraInicio() {
		return fechaHoraInicio;
	}

	public void setFechaHoraInicio(Date fechaHoraInicio) {
		this.fechaHoraInicio = fechaHoraInicio;
	}

	public Date getFechaHoraFin() {
		return fechaHoraFin;
	}

	public void setFechaHoraFin(Date fechaHoraFin) {
		this.fechaHoraFin = fechaHoraFin;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public EstadoTarea getEstado() {
		return estado;
	}

	public void setEstado(EstadoTarea estado) {
		this.estado = estado;
	}

	public Proceso getProceso() {
		return proceso;
	}

	public void setProceso(Proceso proceso) {
		this.proceso = proceso;
	}

	public Operario getOperario() {
		return operario;
	}

	public void setOperario(Operario operario) {
		this.operario = operario;
	}

	public List<Pausa> getPausas() {
		return pausas;
	}

	public void setPausas(List<Pausa> pausas) {
		this.pausas = pausas;
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
		Tarea other = (Tarea) obj;
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