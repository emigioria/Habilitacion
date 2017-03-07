package proy.gui.controladores;

import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import proy.datos.entidades.Comentario;
import proy.datos.entidades.Operario;
import proy.datos.filtros.implementacion.FiltroOperario;
import proy.excepciones.PersistenciaException;
import proy.gui.ControladorDialogo;

public class NComentarioController extends ControladorDialogo {

	public static final String URL_VISTA = "/proy/gui/vistas/NComentario.fxml";

	private Comentario comentario;

	@FXML
	private ComboBox<Operario> cbOperario;

	@FXML
	private TextArea taComentarioStr;

	@FXML
	private void guardar() {
		comentario = new Comentario();
		comentario.setFechaComentario(new Date());
		comentario.setOperario(cbOperario.getValue());
		comentario.setTexto(taComentarioStr.getText().trim());
		salir();
	}

	@FXML
	private void salir() {
		stage.hide();
	}

	@Override
	public void inicializar() {
		stage.setTitle("Nuevo comentario");
		try{
			cbOperario.getItems().addAll(coordinador.listarOperarios(new FiltroOperario.Builder().build()));
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
		}
	}
}
