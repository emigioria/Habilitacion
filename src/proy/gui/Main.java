/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import proy.excepciones.ErrorInicioException;
import proy.gui.componentes.IconoAplicacion;
import proy.gui.componentes.ventanas.PresentadorVentanas;
import proy.gui.componentes.ventanas.VentanaEsperaBaseDeDatos;
import proy.gui.controladores.VTareasController;
import proy.logica.CoordinadorJavaFX;

public class Main extends Application {

	private CoordinadorJavaFX coordinador;
	private Stage primaryStage;
	private ApplicationContext appContext;
	private PresentadorVentanas presentador;
	private PilaScene apilador;

	public static void main(String[] args) {
		//Ocultar logs
		java.util.Enumeration<String> loggers = java.util.logging.LogManager.getLogManager().getLoggerNames();
		while(loggers.hasMoreElements()){
			String log = loggers.nextElement();
			java.util.logging.Logger.getLogger(log).setLevel(java.util.logging.Level.WARNING);
		}
		//Iniciar aplicacion
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		//Analizar parámetros de entrada
		verParametros(getParameters().getRaw());

		//Inicializar parametros
		this.primaryStage = primaryStage;
		presentador = new PresentadorVentanas();

		//Iniciar el stage en el centro de la pantalla
		primaryStage.centerOnScreen();

		//Setear icono y titulo de aplicacion
		primaryStage.getIcons().add(new IconoAplicacion());
		primaryStage.setTitle("Aplicacion Romano");

		//Setear acción de cierre
		primaryStage.setOnCloseRequest((e) -> {
			if(!apilador.sePuedeSalir()){
				e.consume();
			}
			else{
				SessionFactory sessionFact = (SessionFactory) appContext.getBean("sessionFactory");
				sessionFact.close();
			}
		});

		iniciarHibernate();
	}

	private void iniciarHibernate() {
		//Crear ventana de espera
		VentanaEsperaBaseDeDatos ventanaEspera = presentador.presentarEsperaBaseDeDatos(primaryStage);

		//Crear tarea para iniciar hibernate y el coordinador de la aplicacion
		Task<Boolean> task = new Task<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
				coordinador = appContext.getBean(CoordinadorJavaFX.class);
				return true;
			}
		};

		//mientras se muestra una ventana de espera
		task.setOnRunning(
				(event) -> {
					ventanaEspera.showAndWait();
				});

		//que se cierra al terminar.
		task.setOnSucceeded(
				(event) -> {
					ventanaEspera.close();
					apilador = ControladorRomano.crearYMostrarPrimeraVentana(coordinador, primaryStage, VTareasController.URL_VISTA);
				});

		//Si falla, informa al usuario del error y cierra la aplicacion
		task.setOnFailed(
				(event) -> {
					try{
						throw task.getException();
					} catch(Throwable e){
						System.out.println(e.getClass());
						if(appContext != null){
							SessionFactory sessionFact = (SessionFactory) appContext.getBean("sessionFactory");
							sessionFact.close();
						}
						presentador.presentarExcepcion(new ErrorInicioException(e), ventanaEspera);
						System.exit(1);
					}
				});

		//Iniciar tarea
		Thread hiloHibernate = new Thread(task);
		hiloHibernate.setDaemon(false);
		hiloHibernate.start();
	}

	private void verParametros(List<String> raw) {
		//Analizar parámetros
	}
}
