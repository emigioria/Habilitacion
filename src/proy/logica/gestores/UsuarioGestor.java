package proy.logica.gestores;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Service;

import proy.datos.entidades.Comentario;
import proy.datos.entidades.Operario;
import proy.datos.servicios.UsuarioService;
import proy.excepciones.PersistenciaException;
import proy.logica.gestores.filtros.FiltroAdministrador;
import proy.logica.gestores.filtros.FiltroComentario;
import proy.logica.gestores.filtros.FiltroOperario;
import proy.logica.gestores.resultados.ResultadoAutenticacion;
import proy.logica.gestores.resultados.ResultadoCrearComentario;
import proy.logica.gestores.resultados.ResultadoCrearOperario;
import proy.logica.gestores.resultados.ResultadoEliminarOperario;

@Service
public class UsuarioGestor {

	@Resource
	private UsuarioService persistidorUsuario;

	public ResultadoAutenticacion autenticarAdministrador(FiltroAdministrador filtro) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoCrearComentario crearComentario(Comentario comentario) throws PersistenciaException {
		ResultadoCrearComentario resultado = validarCrearComentario(comentario);
		if(!resultado.hayErrores()){
			//hacer las cosas
		}
		throw new NotYetImplementedException();
	}

	private ResultadoCrearComentario validarCrearComentario(Comentario comentario) {
		// TODO Auto-generated method stub
		return new ResultadoCrearComentario();
	}

	public ArrayList<Comentario> listarComentarios(FiltroComentario filtro) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ArrayList<Operario> listarOperarios(FiltroOperario filtro) throws PersistenciaException {
		throw new NotYetImplementedException();
	}

	public ResultadoCrearOperario crearOperario(Operario operario) throws PersistenciaException {
		ResultadoCrearOperario resultado = validarCrearOperario(operario);
		if(!resultado.hayErrores()){
			//hacer las cosas
		}
		throw new NotYetImplementedException();
	}

	private ResultadoCrearOperario validarCrearOperario(Operario operario) {
		// TODO Auto-generated method stub
		return new ResultadoCrearOperario();
	}

	public ResultadoEliminarOperario eliminarOperario(Operario operario) throws PersistenciaException {
		ResultadoEliminarOperario resultado = validarEliminarOperario(operario);
		if(!resultado.hayErrores()){
			//hacer las cosas
		}
		throw new NotYetImplementedException();
	}

	private ResultadoEliminarOperario validarEliminarOperario(Operario operario) {
		// TODO Auto-generated method stub
		return new ResultadoEliminarOperario();
	}
}
