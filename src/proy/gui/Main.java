package proy.gui;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import proy.gui.componentes.IconoAplicacion;
import proy.gui.componentes.VentanaErrorExcepcionInesperada;
import proy.gui.componentes.VentanaEsperaBaseDeDatos;
import proy.gui.controladores.ControladorRomano;
import proy.gui.controladores.VTareasController;
import proy.logica.gestores.CoordinadorJavaFX;

public class Main extends Application {

	private PilaScene apilador;
	private CoordinadorJavaFX coordinador;
	private Stage stagePrincipal;
	private ApplicationContext appContext;
	private ControladorRomano controladorVentanaInicio;

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
		stagePrincipal = primaryStage;
		apilador = new PilaScene(primaryStage);

		//Setear icono y titulo de aplicacion
		primaryStage.getIcons().add(new IconoAplicacion());
		primaryStage.setTitle("Aplicacion Romano");

		//Setear acción de cierre
		primaryStage.setOnCloseRequest((WindowEvent e) -> {
			apilador.desapilarScene();
			if(!apilador.isEmpty()){
				e.consume();
			}
			else{
				SessionFactory sessionFact = (SessionFactory) appContext.getBean("sessionFactory");
				sessionFact.close();
			}
		});

		iniciarHibernate();

		//Crear primera ventana
		controladorVentanaInicio = ControladorRomano.nuevaScene(VTareasController.URLVista, apilador, coordinador);
	}

	private void iniciarHibernate() {
		//Crear ventana de espera
		VentanaEsperaBaseDeDatos ventanaEspera = new VentanaEsperaBaseDeDatos(stagePrincipal.getOwner());

		//Crear tarea para iniciar hibernate y el coordinador de la aplicacion
		Task<Boolean> task = new Task<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
				coordinador = appContext.getBean(CoordinadorJavaFX.class);
				if(controladorVentanaInicio != null){
					controladorVentanaInicio.setCoordinador(coordinador);
				}
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
					ventanaEspera.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
					ventanaEspera.hide();
				});

		//Si falla, informa al usuario del error y cierra la aplicacion
		task.setOnFailed(
				(event) -> {
					try{
						throw task.getException();
					} catch(Throwable e){
						e.printStackTrace();
						if(appContext != null){
							SessionFactory sessionFact = (SessionFactory) appContext.getBean("sessionFactory");
							sessionFact.close();
						}
						new VentanaErrorExcepcionInesperada(stagePrincipal);
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
