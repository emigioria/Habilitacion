package proy.logica.gestores.filtros;

import org.hibernate.Query;
import org.hibernate.Session;

import proy.datos.servicios.Filtro;

public class FiltroAdministrador extends Filtro {

	public FiltroAdministrador() {
		super();
	}

	@Override
	public String getConsulta() {
		String nombreEntidad = "a";
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
		String from = " FROM Administrador " + nombreEntidad;
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
