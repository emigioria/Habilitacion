package proy.gui;

import java.util.List;
import java.util.logging.Level;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import proy.datos.servicios.implementacion.HibernateUtil;
import proy.excepciones.ConnectionException;
import proy.gui.componentes.IconoAplicacion;
import proy.gui.componentes.VentanaErrorExcepcion;
import proy.gui.componentes.VentanaErrorExcepcionInesperada;
import proy.gui.componentes.VentanaEsperaBaseDeDatos;
import proy.logica.gestores.Coordinador;

public class Main extends Application {

	private PilaScene apilador;
	private Coordinador coordinador;
	private Stage stagePrincipal;

	public static void main(String[] args) {
		java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.WARNING);
		java.util.logging.Logger.getLogger("com.mchange.v2.c3p0").setLevel(Level.WARNING);
		java.util.logging.Logger.getLogger("com.mchange.v2.log.MLog").setLevel(Level.WARNING);
		java.util.logging.Logger.getLogger("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL").setLevel(Level.WARNING);
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		verParametros(getParameters().getRaw());
		stagePrincipal = primaryStage;
		apilador = new PilaScene(primaryStage);
		coordinador = new Coordinador() {
		}; //TODO cambiar en el futuro
		primaryStage.getIcons().add(new IconoAplicacion());
		primaryStage.setOnCloseRequest((WindowEvent e) -> {
			apilador.desapilarScene();
			if(!apilador.isEmpty()){
				e.consume();
			}
			else{
				HibernateUtil.close();
			}
		});
		iniciarHibernate();
		//TODO cambiar en el futuro
		//		ControladorIva.nuevaSceneIva(InicioController.URLVista, apilador, coordinador);
	}

	private void iniciarHibernate() {
		VentanaEsperaBaseDeDatos ventanaEspera = new VentanaEsperaBaseDeDatos(stagePrincipal.getOwner());
		Task<Boolean> task = new Task<Boolean>() {
			@Override
			public Boolean call() throws ConnectionException {
				HibernateUtil.getSessionFactory();
				return true;
			}
		};

		task.setOnRunning(
				(event) -> {
					ventanaEspera.showAndWait();
				});

		task.setOnSucceeded(
				(event) -> {
					ventanaEspera.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
					ventanaEspera.hide();
				});

		task.setOnFailed(
				(event) -> {
					try{
						throw task.getException();
					} catch(ConnectionException e){
						HibernateUtil.close();
						new VentanaErrorExcepcion(e.getMessage(), stagePrincipal);
						System.exit(1);
					} catch(Throwable e){
						HibernateUtil.close();
						new VentanaErrorExcepcionInesperada(stagePrincipal);
						System.exit(1);
					}
				});

		Thread hiloHibernate = new Thread(task);
		hiloHibernate.setDaemon(false);
		hiloHibernate.start();
	}

	private void verParametros(List<String> raw) {
		//Analizar parámetros
	}
}
