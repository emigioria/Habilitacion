/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.logica.gestores;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import proy.comun.EncriptadorPassword;
import proy.datos.clases.DatosLogin;
import proy.datos.entidades.Administrador;
import proy.datos.entidades.Comentario;
import proy.datos.entidades.Operario;
import proy.datos.entidades.Tarea;
import proy.datos.filtros.Filtro;
import proy.datos.filtros.implementacion.FiltroAdministrador;
import proy.datos.filtros.implementacion.FiltroOperario;
import proy.datos.filtros.implementacion.FiltroTarea;
import proy.datos.servicios.ProcesoService;
import proy.datos.servicios.UsuarioService;
import proy.excepciones.ObjNotFoundException;
import proy.excepciones.PersistenciaException;
import proy.logica.gestores.resultados.ResultadoAutenticacion;
import proy.logica.gestores.resultados.ResultadoAutenticacion.ErrorAutenticacion;
import proy.logica.gestores.resultados.ResultadoCrearComentario;
import proy.logica.gestores.resultados.ResultadoCrearComentario.ErrorCrearComentario;
import proy.logica.gestores.resultados.ResultadoCrearOperarios;
import proy.logica.gestores.resultados.ResultadoCrearOperarios.ErrorCrearOperarios;
import proy.logica.gestores.resultados.ResultadoEliminarOperario;

@Service
public class UsuarioGestor {

	@Resource
	private UsuarioService persistidorUsuario;

	@Resource
	private ProcesoService persistidorProceso;

	@Resource
	private EncriptadorPassword encriptadorPassword;

	public ResultadoAutenticacion autenticarAdministrador(DatosLogin login) throws PersistenciaException {
		ArrayList<ErrorAutenticacion> errores = new ArrayList<>();

		//Compruebo que los datos no sean nulos ni vacios
		if(login.getContrasenia() == null || login.getContrasenia().length < 1 || login.getDNI() == null || login.getDNI().length() < 1){
			errores.add(ErrorAutenticacion.DATOS_INVALIDOS);
		}
		else{
			//Obtengo los administradores
			ArrayList<Administrador> administradores = persistidorUsuario.obtenerAdministradores(new FiltroAdministrador.Builder().dni(login.getDNI()).build());
			if(administradores.size() != 1){
				//Si no lo encuentra falla
				errores.add(ErrorAutenticacion.DATOS_INVALIDOS);
			}
			else{
				//Si lo encuentra comprueba que la contraseña ingresada coincida con la de la base de datos
				Administrador admin = administradores.get(0);
				if(admin == null || admin.getContrasenia() == null ||
						!admin.getContrasenia().equals(encriptadorPassword.encriptar(login.getContrasenia(), admin.getSal()))){
					//Si no coincide falla
					errores.add(ErrorAutenticacion.DATOS_INVALIDOS);
				}
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
		ArrayList<ErrorCrearComentario> errores = new ArrayList<>();
		if(comentario.getOperario() == null){
			errores.add(ErrorCrearComentario.SIN_OPERARIO);
		}
		if(comentario.getTexto() == null || comentario.getTexto().isEmpty()){
			errores.add(ErrorCrearComentario.TEXTO_VACIO);
		}
		return new ResultadoCrearComentario(errores.toArray(new ErrorCrearComentario[0]));
	}

	public ArrayList<Comentario> listarComentarios(Filtro<Comentario> filtro) throws PersistenciaException {
		return persistidorUsuario.obtenerComentarios(filtro);
	}

	public ArrayList<Operario> listarOperarios(Filtro<Operario> filtro) throws PersistenciaException {
		return persistidorUsuario.obtenerOperarios(filtro);
	}

	public ResultadoCrearOperarios crearOperarios(ArrayList<Operario> operarios) throws PersistenciaException {
		ResultadoCrearOperarios resultado = validarCrearOperarios(operarios);
		if(!resultado.hayErrores()){
			persistidorUsuario.guardarOperarios(operarios);
		}
		return resultado;
	}

	private ResultadoCrearOperarios validarCrearOperarios(ArrayList<Operario> operarios) throws PersistenciaException {
		//Creo la lista de errores
		Set<ErrorCrearOperarios> erroresOperarios = new HashSet<>();

		//Creo una lista de los dnis de operarios que voy a buscar en la BD
		ArrayList<String> dnisOperariosABuscarEnLaBD = new ArrayList<>();
		ArrayList<Operario> operariosAValidar = new ArrayList<>();

		//Busco operarios sin nombre
		for(Operario operarioActual: operarios){
			if(operarioActual.getNombre() == null || operarioActual.getNombre().isEmpty()){
				erroresOperarios.add(ErrorCrearOperarios.NOMBRE_INCOMPLETO);
			}
		}

		//Busco operarios sin apellido
		for(Operario operarioActual: operarios){
			if(operarioActual.getApellido() == null || operarioActual.getApellido().isEmpty()){
				erroresOperarios.add(ErrorCrearOperarios.APELLIDO_INCOMPLETO);
			}
		}

		//Reviso que el dni esté completo
		for(Operario operario: operarios){
			if(operario.getDNI() == null || operario.getDNI().isEmpty()){
				//Si encontré un dni incompleto, agrego el error
				erroresOperarios.add(ErrorCrearOperarios.DNI_INCOMPLETO);
			}
			else{
				//Si el dni está completo lo busco en la BD
				dnisOperariosABuscarEnLaBD.add(operario.getDNI());
				operariosAValidar.add(operario);
			}
		}

		//Si hay operarios a buscar
		ArrayList<String> dnisOperariosRepetidosBD = new ArrayList<>();
		if(!dnisOperariosABuscarEnLaBD.isEmpty()){
			//busco en la BD operarios cuyo dni coincida con el de alguno de los nuevos operarios
			List<Operario> operariosCoincidentes = persistidorUsuario.obtenerOperarios(new FiltroOperario.Builder().dnis(dnisOperariosABuscarEnLaBD).build());
			if(!operariosCoincidentes.isEmpty()){
				erroresOperarios.add(ErrorCrearOperarios.DNI_YA_EXISTENTE);
				for(Operario operario: operariosCoincidentes){
					dnisOperariosRepetidosBD.add(operario.getDNI());
				}
			}

		}

		//veo si hay dnis que se repiten entre los nuevos operarios con dnis
		//primero los ordeno por dni
		operariosAValidar.sort((p1, p2) -> {
			return p1.getDNI().compareTo(p2.getDNI());
		});

		//luego busco coincidencias entre los dnis de los operarios ingresados
		if(!operariosAValidar.isEmpty()){
			Iterator<Operario> it1 = operariosAValidar.iterator();
			Operario anterior = it1.next();
			Operario actual;
			while(it1.hasNext()){
				actual = it1.next();
				if(actual.getDNI().equals(anterior.getDNI())){
					erroresOperarios.add(ErrorCrearOperarios.DNI_INGRESADO_REPETIDO);
				}
				anterior = actual;
			}
		}

		return new ResultadoCrearOperarios(dnisOperariosRepetidosBD, erroresOperarios.toArray(new ErrorCrearOperarios[0]));
	}

	public ResultadoEliminarOperario eliminarOperario(Operario operario) throws PersistenciaException {
		ResultadoEliminarOperario resultado = validarEliminarOperario(operario);

		if(!resultado.hayErrores()){
			//si el operario tiene tareas asociadas, se le da baja lógica
			ArrayList<Tarea> tareasDelOperario = persistidorProceso.obtenerTareas(new FiltroTarea.Builder().operario(operario).build());

			if(tareasDelOperario.isEmpty()){
				//si el operario tiene tareas asociadas, se le da de baja lógica
				operario.darDeBaja();
				persistidorUsuario.actualizarOperario(operario);
			}
			else{
				//sino de baja física junto a sus procesos
				try{
					persistidorUsuario.bajaOperario(operario);
				} catch(ObjNotFoundException e){
					//Si no se encontró ya fue eliminado previamente.
				}
			}
		}

		return resultado;
	}

	private ResultadoEliminarOperario validarEliminarOperario(Operario operario) throws PersistenciaException {
		//No hay validaciones hasta el momento
		return new ResultadoEliminarOperario();
	}
}
