package proy.logica.gestores.filtros;

import org.hibernate.Query;
import org.hibernate.Session;

import proy.datos.servicios.Filtro;

public class FiltroMaquina extends Filtro {

	private String nombreMaquina;

	public FiltroMaquina() {
		super();
	}

	public FiltroMaquina(String nombreMaquina) {
		super();
		this.nombreMaquina = nombreMaquina;
	}

	@Override
	public String getConsulta() {
		String nombreEntidad = "m";
		String consulta = this.getSelect(nombreEntidad) + this.getFrom(nombreEntidad) + this.getWhere(nombreEntidad) + this.getOrden(nombreEntidad);
		return consulta;
	}

	@Override
	public String getSelect(String nombreEntidad) {
		String select = "SELECT " + nombreEntidad;
		return select;
	}

	@Override
	public String getFrom(String nombreEntidad) {
		String from = " FROM Maquina " + nombreEntidad;
		return from;
	}

	@Override
	public String getWhere(String nombreEntidad) {
		String where =
				((nombreMaquina != null) ? (nombreEntidad + ".nombre LIKE :nom") : (""));
		if(!where.isEmpty()){
			where = " WHERE " + where;
		}
		return where;
	}

	@Override
	public String getOrden(String nombreEntidad) {
		String orden = " ORDER BY " + nombreEntidad + ".nombre ASC";
		return orden;
	}

	@Override
	public void setParametros(Query query) {
		if(nombreMaquina != null){
			query.setParameter("nom", "%" + nombreMaquina + "%");
		}
	}

	@Override
	public void updateParametros(Session session) {

	}
}
