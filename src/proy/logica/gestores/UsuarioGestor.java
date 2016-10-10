/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Service;

import proy.datos.clases.DatosLogin;
import proy.datos.entidades.Administrador;
import proy.datos.entidades.Comentario;
import proy.datos.entidades.Operario;
import proy.datos.servicios.UsuarioService;
import proy.excepciones.PersistenciaException;
import proy.gui.Contra;
import proy.logica.gestores.filtros.FiltroAdministrador;
import proy.logica.gestores.filtros.FiltroComentario;
import proy.logica.gestores.filtros.FiltroOperario;
import proy.logica.gestores.resultados.ResultadoAutenticacion;
import proy.logica.gestores.resultados.ResultadoAutenticacion.ErrorResultadoAutenticacion;
import proy.logica.gestores.resultados.ResultadoCrearComentario;
import proy.logica.gestores.resultados.ResultadoCrearOperario;
import proy.logica.gestores.resultados.ResultadoEliminarOperario;

@Service
public class UsuarioGestor {

	@Resource
	private UsuarioService persistidorUsuario;

	public ResultadoAutenticacion autenticarAdministrador(DatosLogin login) throws PersistenciaException {
		ArrayList<ErrorResultadoAutenticacion> errores = new ArrayList<>();
		//Obtengo los administradores
		ArrayList<Administrador> administradores = persistidorUsuario.obtenerAdministradores(new FiltroAdministrador.Builder().dni(login.getDNI()).build());
		if(administradores.size() != 1){
			//Si no lo encuentra falla
			errores.add(ErrorResultadoAutenticacion.DatosInvalidos);
		}
		else{
			//Si lo encuentra comprueba que la contraseña ingresada coincida con la de la base de datos
			Administrador admin = administradores.get(0);
			String sal = admin.getSal();
			String contraIngresada = Contra.encriptarMD5(login.getContrasenia(), sal);
			if(!contraIngresada.equals(admin.getContrasenia())){
				//Si no coincide falla
				errores.add(ErrorResultadoAutenticacion.DatosInvalidos);
			}
		}
		return new ResultadoAutenticacion(errores.toArray(new ErrorResultadoAutenticacion[0]));
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
		return persistidorUsuario.obtenerComentarios(filtro);
	}

	public ArrayList<Operario> listarOperarios(FiltroOperario filtro) throws PersistenciaException {
		return persistidorUsuario.obtenerOperarios(filtro);
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
