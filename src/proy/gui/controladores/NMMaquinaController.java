/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.controladores;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.IntegerStringConverter;
import proy.datos.entidades.Maquina;
import proy.datos.entidades.Material;
import proy.datos.entidades.Parte;
import proy.datos.entidades.Pieza;
import proy.datos.entidades.Proceso;
import proy.datos.filtros.implementacion.FiltroMaterial;
import proy.datos.filtros.implementacion.FiltroParte;
import proy.datos.filtros.implementacion.FiltroPieza;
import proy.datos.filtros.implementacion.FiltroProceso;
import proy.excepciones.PersistenciaException;
import proy.gui.ControladorRomano;
import proy.gui.componentes.TableCellTextView;
import proy.gui.componentes.TableCellTextViewString;
import proy.gui.componentes.ventanas.VentanaConfirmacion;
import proy.gui.controladores.errores.TratamientoDeErroresCrearMaquina;
import proy.gui.controladores.errores.TratamientoDeErroresModificarMaquina;
import proy.logica.gestores.resultados.ResultadoCrearMaquina;
import proy.logica.gestores.resultados.ResultadoEliminarParte;
import proy.logica.gestores.resultados.ResultadoEliminarPieza;
import proy.logica.gestores.resultados.ResultadoModificarMaquina;

public class NMMaquinaController extends ControladorRomano {

	public static final String URL_VISTA = "/proy/gui/vistas/NMMaquina.fxml";

	private String titulo;

	@FXML
	private TextField nombreMaquina;

	@FXML
	private TextField nombreParte;

	@FXML
	private TextField nombrePieza;

	@FXML
	private TableView<Parte> tablaPartes;

	@FXML
	private TableColumn<Parte, String> columnaNombreParte;

	@FXML
	private TableColumn<Parte, Number> columnaCantidadParte;

	@FXML
	private TableView<Pieza> tablaPiezas;

	@FXML
	private TableColumn<Pieza, String> columnaNombrePieza;

	@FXML
	private TableColumn<Pieza, Number> columnaCantidadPieza;

	@FXML
	private TableColumn<Pieza, Material> columnaMaterialPieza;

	@FXML
	private TableColumn<Pieza, String> columnaCodigoPlanoPieza;

	@FXML
	private Label lbNMMaquina;

	private Maquina maquina;

	private ArrayList<Parte> partesAGuardar = new ArrayList<>(); //Partes nuevas no persistidas
	private Map<Parte, ArrayList<Pieza>> piezasAGuardar = new IdentityHashMap<>(); //Piezas nuevas no persistidas

	//Traducen los resultados a string
	private TratamientoDeErroresCrearMaquina tratamientoDeErroresCrearMaquina = new TratamientoDeErroresCrearMaquina();

	private TratamientoDeErroresModificarMaquina tratamientoDeErroresModificarMaquina = new TratamientoDeErroresModificarMaquina();

	@Override
	protected void inicializar() {
		tablaPartes.setEditable(true);

		//Inicialización de la columna PARTE->NOMBRE
		{
			//Seteamos el Cell Value Factory
			columnaNombreParte.setCellValueFactory(param -> {
				if(param.getValue() != null){
					if(param.getValue().getNombre() != null){
						return new SimpleStringProperty(formateadorString.primeraMayuscula(param.getValue().getNombre()));
					}
				}
				return new SimpleStringProperty("<Sin nombre>");
			});
			//Seteamos el Cell Factory para permitir edición
			columnaNombreParte.setCellFactory(col -> {
				return new TableCellTextViewString<Parte>(Parte.class) {

					@Override
					public void changed(ObservableValue<? extends Parte> observable, Parte oldValue, Parte newValue) {

					}

					//Al terminar de editar, se guarda el valor
					@Override
					public void onEdit(Parte object, String newValue) {
						object.setNombre(newValue.toLowerCase().trim());
					}
				};
			});
		}

		//Inicialización de la columna PARTE->CANTIDAD
		{
			//Seteamos el Cell Value Factory
			columnaCantidadParte.setCellValueFactory(param -> {
				if(param.getValue() != null){
					if(param.getValue().getCantidad() != null){
						return new SimpleIntegerProperty(param.getValue().getCantidad());
					}
				}
				return new SimpleIntegerProperty(0);
			});
			//Seteamos el Cell Factory para permitir edición
			columnaCantidadParte.setCellFactory(col -> {
				return new TableCellTextView<Parte, Number>(Parte.class, new IntegerStringConverter()) {

					@Override
					public void changed(ObservableValue<? extends Parte> observable, Parte oldValue, Parte newValue) {

					}

					//Al terminar de editar, se guarda el valor.
					@Override
					public void onEdit(Parte object, Number newValue) {
						if(newValue != null){
							object.setCantidad((Integer) newValue);
						}
					}
				};
			});
		}

		//Inicialización de la columna PIEZA->NOMBRE
		{
			//Seteamos el Cell Value Factory
			columnaNombrePieza.setCellValueFactory(param -> {
				if(param.getValue() != null){
					if(param.getValue().getNombre() != null){
						return new SimpleStringProperty(formateadorString.primeraMayuscula(param.getValue().getNombre()));
					}
				}
				return new SimpleStringProperty("<Sin nombre>");
			});
			//Seteamos el Cell Factory para permitir edición
			columnaNombrePieza.setCellFactory(col -> {
				return new TableCellTextViewString<Pieza>(Pieza.class) {

					@Override
					public void changed(ObservableValue<? extends Pieza> observable, Pieza oldValue, Pieza newValue) {
						esEditablePieza(this, newValue);
					}

					//Al terminar de editar, se guarda el valor
					@Override
					public void onEdit(Pieza object, String newValue) {
						object.setNombre(newValue.toLowerCase().trim());
					}
				};
			});
		}

		//Inicialización de la columna PIEZA->CANTIDAD
		{
			//Seteamos el Cell Value Factory
			columnaCantidadPieza.setCellValueFactory(param -> {
				if(param.getValue() != null){
					if(param.getValue().getCantidad() != null){
						return new SimpleIntegerProperty(param.getValue().getCantidad());
					}
				}
				return new SimpleIntegerProperty(0);
			});
			//Seteamos el Cell Factory para permitir edición
			columnaCantidadPieza.setCellFactory(col -> {
				return new TableCellTextView<Pieza, Number>(Pieza.class, new IntegerStringConverter()) {

					@Override
					public void changed(ObservableValue<? extends Pieza> observable, Pieza oldValue, Pieza newValue) {
						esEditablePieza(this, newValue);
					}

					//Al terminar de editar, se guarda el valor.
					@Override
					public void onEdit(Pieza object, Number newValue) {
						if(newValue != null){
							object.setCantidad((Integer) newValue);
						}
					}
				};
			});
		}

		//Inicialización de la columna PIEZA->MATERIAL
		{
			//Seteamos el Cell Value Factory
			columnaMaterialPieza.setCellValueFactory(new PropertyValueFactory<Pieza, Material>("material"));

			//Seteamos el Cell Factory para permitir edición
			try{
				columnaMaterialPieza.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(coordinador.listarMateriales(new FiltroMaterial.Builder().build()))));
			} catch(PersistenciaException e){
				presentadorVentanas.presentarExcepcion(e, stage);
			}
			//Al terminar de editar, se guarda el valor.
			columnaMaterialPieza.setOnEditCommit(new EventHandler<CellEditEvent<Pieza, Material>>() {

				@Override
				public void handle(CellEditEvent<Pieza, Material> t) {
					if(t.getNewValue() != null){
						Pieza pieza = t.getRowValue();
						Parte parteSeleccionada = tablaPartes.getSelectionModel().getSelectedItem();
						if(parteSeleccionada == null){
							return;
						}
						if(piezasAGuardar.get(parteSeleccionada) != null && !piezasAGuardar.get(parteSeleccionada).isEmpty()){
							if(piezasAGuardar.get(parteSeleccionada).contains(pieza)){
								pieza.setMaterial(t.getNewValue());
							}
						}
					}
					//Truco para que se llame a Cell.updateItem() para que formatee el valor ingresado.
					t.getTableColumn().setVisible(false);
					t.getTableColumn().setVisible(true);
				};
			});
		}

		//Inicialización de la columna PIEZA->CODIGO_PLANO
		{
			//Seteamos el Cell Value Factory
			columnaCodigoPlanoPieza.setCellValueFactory(param -> {
				if(param.getValue() != null){
					if(param.getValue().getCodigoPlano() != null){
						return new SimpleStringProperty(param.getValue().getCodigoPlano());
					}
				}
				return new SimpleStringProperty("<Sin nombre>");
			});
			//Seteamos el Cell Factory para permitir edición
			columnaCodigoPlanoPieza.setCellFactory(col -> {
				return new TableCellTextViewString<Pieza>(Pieza.class) {

					@Override
					public void changed(ObservableValue<? extends Pieza> observable, Pieza oldValue, Pieza newValue) {
						esEditablePieza(this, newValue);
					}

					//Al terminar de editar, se guarda el valor
					@Override
					public void onEdit(Pieza object, String newValue) {
						object.setCodigoPlano(newValue.trim());
					}
				};
			});
		}

		//Cuando cambia la parte seleccionada, cargamos sus piezas
		tablaPartes.getSelectionModel().selectedIndexProperty().addListener((ovs, viejo, nuevo) -> {
			Platform.runLater(() -> {
				tablaPiezas.getItems().clear();
				try{
					Parte nueva = tablaPartes.getSelectionModel().getSelectedItem();
					if(nueva != null){
						if(!partesAGuardar.contains(nueva)){
							tablaPiezas.getItems().addAll(coordinador.listarPiezas(new FiltroPieza.Builder().parte(nueva).build()));
						}
						if(piezasAGuardar.get(nueva) != null && !piezasAGuardar.get(nueva).isEmpty()){
							tablaPiezas.getItems().addAll(piezasAGuardar.get(nueva));
						}
					}
				} catch(PersistenciaException e){
					presentadorVentanas.presentarExcepcion(e, stage);
				}
			});
		});

		actualizar();
	}

	private void esEditablePieza(TableCell<?, ?> tableCellTextViewString, Pieza newValue) {
		tableCellTextViewString.setEditable(false);
		if(tableCellTextViewString.getTableRow() == null || newValue == null){
			return;
		}
		Parte parteSeleccionada = tablaPartes.getSelectionModel().getSelectedItem();
		if(parteSeleccionada == null){
			return;
		}
		if(piezasAGuardar.get(parteSeleccionada) != null && !piezasAGuardar.get(parteSeleccionada).isEmpty()){
			tableCellTextViewString.setEditable(piezasAGuardar.get(parteSeleccionada).contains(newValue));
		}
	}

	@FXML
	private void nuevaParte() {
		Parte nuevaParte = new Parte();
		partesAGuardar.add(nuevaParte);
		tablaPartes.getItems().add(0, nuevaParte);
		tablaPartes.getSelectionModel().select(null);
	}

	@FXML
	private void nuevaPieza() {
		Parte parteDePiezaAGuardar = tablaPartes.getSelectionModel().getSelectedItem();
		if(parteDePiezaAGuardar == null){
			return;
		}
		Pieza nuevaPieza = new Pieza();
		if(piezasAGuardar.get(parteDePiezaAGuardar) == null || piezasAGuardar.get(parteDePiezaAGuardar).isEmpty()){
			piezasAGuardar.put(parteDePiezaAGuardar, new ArrayList<>());
		}
		piezasAGuardar.get(parteDePiezaAGuardar).add(nuevaPieza);
		tablaPiezas.getItems().add(0, nuevaPieza);
		if(!tablaPiezas.isEditable()){
			tablaPiezas.setEditable(true);
		}
	}

	@FXML
	private void eliminarParte() {
		//TODO ver si hay que sacar la parte de la máquina
		ResultadoEliminarParte resultadoEliminarParte;

		//Toma de datos de la vista
		Parte parteAEliminar = tablaPartes.getSelectionModel().getSelectedItem();
		if(parteAEliminar == null){
			return;
		}

		//Si no fue guardada previamente se elimina sin ir al gestor
		if(partesAGuardar.contains(parteAEliminar)){
			partesAGuardar.remove(parteAEliminar);
			piezasAGuardar.remove(parteAEliminar);
			tablaPartes.getItems().remove(parteAEliminar);
			tablaPartes.getSelectionModel().select(null);
			return;
		}

		//Se buscan procesos asociados
		ArrayList<Proceso> procesosAsociados = new ArrayList<>();
		try{
			procesosAsociados = coordinador.listarProcesos(new FiltroProceso.Builder().parte(parteAEliminar).build());
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}

		//Se pregunta si quiere dar de baja
		VentanaConfirmacion vc;
		if(!procesosAsociados.isEmpty()){ //hay procesos usando esa parte, los elimino?
			vc = presentadorVentanas.presentarConfirmacion("Confirmar eliminar parte",
					"Al eliminar la parte <" + parteAEliminar + "> se eliminarán los siguientes procesos: " + procesosAsociados + "\n¿Está seguro que desea eliminarla?", stage);
			if(!vc.acepta()){
				return;
			}
		}
		else{
			vc = presentadorVentanas.presentarConfirmacion("Confirmar eliminar parte",
					"Se eliminará la parte <" + parteAEliminar + "> de forma permanente.\n" +
							"¿Está seguro de que desea continuar?",
					stage);
			if(!vc.acepta()){
				return;
			}
		}

		if(!vc.acepta()){
			return;
		}

		//Si acepta dar de baja se verifica que la parte a eliminar no tiene tareas no terminadas asociadas
		Boolean tieneTareasNoTerminadasAsociadas = false;
		try{
			tieneTareasNoTerminadasAsociadas = coordinador.tieneTareasNoTerminadasAsociadas(parteAEliminar);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}

		//Se pregunta si quiere dar de baja estas tareas asociadas
		if(tieneTareasNoTerminadasAsociadas){
			vc = presentadorVentanas.presentarConfirmacion("Confirmar eliminar parte",
					"La parte <" + parteAEliminar + "> tiene tareas no terminadas asociadas\nSi continúa, esas tareas se eliminarán\n¿Está seguro que desea eliminarla?",
					stage);
			if(!vc.acepta()){
				return;
			}
		}

		//Inicio transacciones al gestor
		try{
			resultadoEliminarParte = coordinador.eliminarParte(parteAEliminar);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}

		//Tratamiento de errores
		if(resultadoEliminarParte.hayErrores()){
			String errores = tratamientoDeErroresModificarMaquina.tratarErroresEliminarParte(resultadoEliminarParte); //TODO unificar todos los tratamientos de errores
			if(!errores.isEmpty()){
				presentadorVentanas.presentarError("Error al eliminar parte", errores, stage);
			}
		}
		else{
			piezasAGuardar.remove(parteAEliminar);
			tablaPartes.getItems().remove(parteAEliminar);
			tablaPartes.getSelectionModel().select(null);

			presentadorVentanas.presentarToast("Se ha eliminado correctamente la parte", stage);
		}
	}

	@FXML
	private void eliminarPieza() {
		//TODO ver si hay que sacar la pieza de la parte
		ResultadoEliminarPieza resultadoEliminarPieza;

		//Toma de datos de la vista
		Parte parteDePiezaAEliminar = tablaPartes.getSelectionModel().getSelectedItem();
		Pieza piezaAEliminar = tablaPiezas.getSelectionModel().getSelectedItem();
		if(parteDePiezaAEliminar == null || piezaAEliminar == null){
			return;
		}

		//Si no fue guardada previamente se elimina sin ir al gestor
		if(piezasAGuardar.get(parteDePiezaAEliminar).contains(piezaAEliminar)){
			piezasAGuardar.get(parteDePiezaAEliminar).remove(piezaAEliminar);
			if(piezasAGuardar.get(parteDePiezaAEliminar).isEmpty()){
				piezasAGuardar.remove(parteDePiezaAEliminar);
			}
			tablaPiezas.getItems().remove(piezaAEliminar);
			return;
		}

		//Se buscan procesos asociados
		ArrayList<Proceso> procesosAsociados = new ArrayList<>();
		try{
			procesosAsociados = coordinador.listarProcesos(new FiltroProceso.Builder().pieza(piezaAEliminar).build());
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}

		//Se pregunta si quiere dar de baja
		VentanaConfirmacion vc;
		if(!procesosAsociados.isEmpty()){ //hay procesos usando esa pieza, los elimino?
			vc = presentadorVentanas.presentarConfirmacion("Confirmar eliminar pieza",
					"Al eliminar la pieza <" + piezaAEliminar + "> se eliminarán los siguientes procesos: " + procesosAsociados + "\n¿Está seguro que desea eliminarla?", stage);
			if(!vc.acepta()){
				return;
			}
		}
		else{
			vc = presentadorVentanas.presentarConfirmacion("Confirmar eliminar pieza",
					"Se eliminará la pieza <" + piezaAEliminar + "> de forma permanente.\n" +
							"¿Está seguro de que desea continuar?",
					stage);
			if(!vc.acepta()){
				return;
			}
		}

		if(!vc.acepta()){
			return;
		}

		//Si acepta dar de baja se verifica que la pieza a eliminar no tiene tareas no terminadas asociadas
		Boolean tieneTareasNoTerminadasAsociadas = false;
		try{
			if(piezasAGuardar.get(parteDePiezaAEliminar) == null || !piezasAGuardar.get(parteDePiezaAEliminar).contains(piezaAEliminar)){
				tieneTareasNoTerminadasAsociadas = coordinador.tieneTareasNoTerminadasAsociadas(piezaAEliminar);
			}
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}

		//Se pregunta si quiere dar de baja estas tareas asociadas
		if(tieneTareasNoTerminadasAsociadas){
			vc = presentadorVentanas.presentarConfirmacion("Confirmar eliminar pieza",
					"La pieza <" + piezaAEliminar + "> corresponde a una parte que tiene tareas no terminadas asociadas\nSi continúa, esas tareas se eliminarán\n¿Está seguro que desea eliminar la pieza?",
					stage);
			if(!vc.acepta()){
				return;
			}
		}

		//Inicio transacciones al gestor
		try{
			resultadoEliminarPieza = coordinador.eliminarPieza(piezaAEliminar);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return;
		}

		//Tratamiento de errores
		if(resultadoEliminarPieza.hayErrores()){
			String errores = tratamientoDeErroresModificarMaquina.tratarErroresEliminarPieza(resultadoEliminarPieza);
			if(!errores.isEmpty()){
				presentadorVentanas.presentarError("Error al eliminar pieza", errores, stage);
			}
		}
		else{
			tablaPiezas.getItems().remove(piezaAEliminar);
			presentadorVentanas.presentarToast("Se ha eliminado correctamente la pieza", stage);
		}
	}

	@FXML
	private void guardar() {
		Boolean hayErrores = true;
		if(maquina == null){
			hayErrores = crearMaquina();
		}
		else{
			hayErrores = modificarMaquina();
		}
		if(!hayErrores){
			salir();
		}
	}

	private Boolean crearMaquina() {
		ResultadoCrearMaquina resultado;
		Maquina maquina = new Maquina();

		//Toma de datos de la vista
		maquina.setNombre(nombreMaquina.getText().toLowerCase().trim());
		maquina.getPartes().clear();
		maquina.getPartes().addAll(partesAGuardar);
		for(Parte parte: partesAGuardar){
			parte.setMaquina(maquina);
			if(piezasAGuardar.get(parte) != null && !piezasAGuardar.get(parte).isEmpty()){
				parte.getPiezas().clear();
				parte.getPiezas().addAll(piezasAGuardar.get(parte));
				for(Pieza pieza: parte.getPiezas()){
					pieza.setParte(parte);
				}
			}
		}

		//Inicio transacciones al gestor
		try{
			resultado = coordinador.crearMaquina(maquina);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return true;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return true;
		}

		//Tratamiento de errores
		if(resultado.hayErrores()){
			String errores = tratamientoDeErroresCrearMaquina.tratarErroresCrearMaquina(resultado);
			if(!errores.isEmpty()){
				presentadorVentanas.presentarError("Error al crear la máquina", errores, stage);
			}
			return true;
		}
		else{
			presentadorVentanas.presentarInformacion("Operación exitosa", "Se ha creado la máquina con éxito", stage);
			return false;
		}
	}

	private Boolean modificarMaquina() {
		ResultadoModificarMaquina resultadoModificarMaquina;

		//Toma de datos de la vista
		maquina.setNombre(nombreMaquina.getText().toLowerCase().trim());

		maquina.getPartes().addAll(partesAGuardar);
		maquina.getPartes().removeAll(tablaPartes.getItems());
		maquina.getPartes().addAll(tablaPartes.getItems());

		for(Parte parte: maquina.getPartes()){
			if(piezasAGuardar.get(parte) != null && !piezasAGuardar.get(parte).isEmpty()){
				parte.getPiezas().addAll(piezasAGuardar.get(parte));
			}
		}

		//Inicio transacciones al gestor
		try{
			resultadoModificarMaquina = coordinador.modificarMaquina(maquina);
		} catch(PersistenciaException e){
			presentadorVentanas.presentarExcepcion(e, stage);
			return true;
		} catch(Exception e){
			presentadorVentanas.presentarExcepcionInesperada(e, stage);
			return true;
		}

		//Tratamiento de errores
		if(resultadoModificarMaquina.hayErrores()){
			String errores = tratamientoDeErroresModificarMaquina.tratarErroresModificarMaquina(resultadoModificarMaquina);
			if(!errores.isEmpty()){
				presentadorVentanas.presentarError("Error al modificar la máquina", errores, stage);
			}
			return true;
		}
		else{
			presentadorVentanas.presentarInformacion("Operación exitosa", "Se ha modificado la máquina con éxito", stage);
			return false;
		}
	}

	public void formatearNuevaMaquina() {
		titulo = "Nueva máquina";
		lbNMMaquina.setText(titulo);
	}

	public void formatearModificarMaquina(Maquina maquina) {
		titulo = "Modificar máquina";
		lbNMMaquina.setText(titulo);
		this.maquina = maquina;
		nombreMaquina.setText(maquina.getNombre());
		actualizar();
	}

	@Override
	public void actualizar() {
		Platform.runLater(() -> {
			tablaPartes.getItems().clear();
			tablaPiezas.getItems().clear();
			tablaPartes.getItems().addAll(partesAGuardar);
			try{
				if(maquina != null){
					tablaPartes.getItems().addAll(coordinador.listarPartes(new FiltroParte.Builder().maquina(maquina).build()));
				}
			} catch(PersistenciaException e){
				presentadorVentanas.presentarExcepcion(e, stage);
			}

			stage.setTitle(titulo);
		});
	}
}
