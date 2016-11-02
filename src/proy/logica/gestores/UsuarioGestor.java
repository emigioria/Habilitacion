/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import proy.comun.EncriptadorPassword;
import proy.datos.clases.DatosLogin;
import proy.datos.clases.EstadoStr;
import proy.datos.clases.EstadoTareaStr;
import proy.datos.entidades.Administrador;
import proy.datos.entidades.Comentario;
import proy.datos.entidades.Estado;
import proy.datos.entidades.Operario;
import proy.datos.entidades.Tarea;
import proy.datos.servicios.ProcesoService;
import proy.datos.servicios.UsuarioService;
import proy.excepciones.PersistenciaException;
import proy.logica.gestores.filtros.FiltroAdministrador;
import proy.logica.gestores.filtros.FiltroComentario;
import proy.logica.gestores.filtros.FiltroOperario;
import proy.logica.gestores.filtros.FiltroTarea;
import proy.logica.gestores.resultados.ResultadoAutenticacion;
import proy.logica.gestores.resultados.ResultadoAutenticacion.ErrorAutenticacion;
import proy.logica.gestores.resultados.ResultadoCrearComentario;
import proy.logica.gestores.resultados.ResultadoCrearOperario;
import proy.logica.gestores.resultados.ResultadoCrearOperario.ErrorCrearOperario;
import proy.logica.gestores.resultados.ResultadoEliminarOperario;

@Service
public class UsuarioGestor {

	@Resource
	private UsuarioService persistidorUsuario;
	@Resource
	private ProcesoService persistidorProcesos;

	public ResultadoAutenticacion autenticarAdministrador(DatosLogin login) throws PersistenciaException {
		ArrayList<ErrorAutenticacion> errores = new ArrayList<>();
		//Obtengo los administradores
		ArrayList<Administrador> administradores = persistidorUsuario.obtenerAdministradores(new FiltroAdministrador.Builder().dni(login.getDNI()).build());
		if(administradores.size() != 1){
			//Si no lo encuentra falla
			errores.add(ErrorAutenticacion.DATOS_INVALIDOS);
		}
		else{
			//Si lo encuentra comprueba que la contraseña ingresada coincida con la de la base de datos
			Administrador admin = administradores.get(0);
			String sal = admin.getSal();
			String contraIngresada = EncriptadorPassword.encriptar(login.getContrasenia(), sal);
			if(!contraIngresada.equals(admin.getContrasenia())){
				//Si no coincide falla
				errores.add(ErrorAutenticacion.DATOS_INVALIDOS);
			}
		}
		return new ResultadoAutenticacion(errores.toArray(new ErrorAutenticacion[0]));
	}

	public ResultadoCrearComentario crearComentario(Comentario comentario) throws PersistenciaException {
		ResultadoCrearComentario resultado = validarCrearComentario(comentario);
		if(!resultado.hayErrores()){
			if(comentario.getFechaComentario() == null){
				comentario.setFechaComentario(new Date());
			}
			persistidorUsuario.guardarComentario(comentario);
		}
		return resultado;
	}

	private ResultadoCrearComentario validarCrearComentario(Comentario comentario) {
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
		Boolean anterior = verificarOperarioAnterior(operario);
		if(!resultado.hayErrores() && !anterior){
			persistidorUsuario.guardarOperario(operario);
		}
		return resultado;
	}

	private ResultadoCrearOperario validarCrearOperario(Operario operario) throws PersistenciaException {
		ArrayList<ErrorCrearOperario> errores = new ArrayList<>();

		if(operario.getDNI() == null || operario.getDNI().isEmpty()){
			errores.add(ErrorCrearOperario.DNI_INCOMPLETO);
		}
		else{
			ArrayList<Operario> operarioMismoDNI = persistidorUsuario.obtenerOperarios(new FiltroOperario.Builder().dni(operario.getDNI()).build());
			if(operarioMismoDNI.size() != 0){
				errores.add(ErrorCrearOperario.DNI_REPETIDO);
			}
		}

		if(operario.getNombre() == null || operario.getNombre().isEmpty()){
			errores.add(ErrorCrearOperario.NOMBRE_INCOMPLETO);
		}
		if(operario.getApellido() == null || operario.getApellido().isEmpty()){
			errores.add(ErrorCrearOperario.APELLIDO_INCOMPLETO);
		}
		return new ResultadoCrearOperario(errores.toArray(new ErrorCrearOperario[errores.size()]));
	}

	private Boolean verificarOperarioAnterior(Operario operario) throws PersistenciaException {
		ArrayList<Operario> anteriores = persistidorUsuario.obtenerOperarios(new FiltroOperario.Builder().dni(operario.getDNI()).estado(EstadoStr.BAJA).build());
		if(anteriores.size() == 1){
			Operario anterior = anteriores.get(0);
			anterior.setApellido(operario.getApellido());
			anterior.setNombre(operario.getNombre());
			anterior.setEstado(new Estado(EstadoStr.ALTA));
			persistidorUsuario.actualizarOperario(anterior);
			return true;
		}
		else{
			return false;
		}

	}

	public ResultadoEliminarOperario eliminarOperario(Operario operario) throws PersistenciaException {
		if(!bajaLogicaOperario(operario)){
			persistidorUsuario.bajaOperario(operario);
		}
		return new ResultadoEliminarOperario();
	}

	//probar a ver si anda!
	private Boolean bajaLogicaOperario(Operario operario) throws PersistenciaException {
		ArrayList<Tarea> tareas = persistidorProcesos.obtenerTareas(new FiltroTarea.Builder().operario(operario).noEstado(EstadoTareaStr.FINALIZADA).build());
		if(!tareas.isEmpty()){
			operario.setEstado(new Estado(EstadoStr.BAJA));
			persistidorUsuario.actualizarOperario(operario);
			return true;
		}
		else{
			return false;
		}
	}
}
