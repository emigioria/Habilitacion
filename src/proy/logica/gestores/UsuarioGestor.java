package proy.logica.gestores;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Service;

import proy.datos.entidades.Comentario;
import proy.datos.entidades.Operario;
import proy.datos.servicios.UsuarioService;
import proy.excepciones.PersistenciaException;
import proy.gui.modelos.DatosLogin;
import proy.logica.gestores.filtros.FiltroComentario;
import proy.logica.gestores.filtros.FiltroOperario;
import proy.logica.gestores.resultados.ResultadoAutenticacion;
import proy.logica.gestores.resultados.ResultadoCrearComentario;
import proy.logica.gestores.resultados.ResultadoCrearOperario;
import proy.logica.gestores.resultados.ResultadoEliminarOperario;

@Service
public class UsuarioGestor {

	@Resource
	private UsuarioService persistidorDocumento;

	public ResultadoAutenticacion autenticarAdministrador(DatosLogin login) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoCrearComentario crearComentario(Comentario comentario) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ArrayList<Comentario> listarComentarios(FiltroComentario filtro) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ArrayList<Operario> listarOperarios(FiltroOperario filtro) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoCrearOperario crearOperario(Operario operario) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoEliminarOperario eliminarOperario(Operario operario) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

}
