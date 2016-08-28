package proy.logica.gestores.filtros;

import org.hibernate.Query;
import org.hibernate.Session;

import proy.datos.servicios.Filtro;

public class FiltroHerramienta extends Filtro {

	public FiltroHerramienta() {
		super();
	}

	@Override
	public String getConsulta() {
		String nombreEntidad = "h";
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
		String from = " FROM Herramienta " + nombreEntidad;
		return from;
	}

	@Override
	public String getWhere(String nombreEntidad) {
		String where =
				"";
		return where;
	}

	@Override
	public String getOrden(String nombreEntidad) {
		String orden = "";
		return orden;
	}

	@Override
	public void setParametros(Query query) {

	}

	@Override
	public void updateParametros(Session session) {

	}
}
