/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.componentes.tablecell;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn.CellDataFeatures;

public abstract class TableCellAbstract<O, T> extends TableCell<O, T> implements ChangeListener<O> {

	private String defaultStyle = getStyle();

	@SuppressWarnings("unchecked") //Tapa bug corregido en JDK 9
	public TableCellAbstract() {
		super();
		//Cuando la fila es seteada...
		this.tableRowProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue != null){
				//se agrega un listener a dicha fila para que escuche cuando cambia su contenido
				((javafx.scene.control.TableRow<O>) newValue).itemProperty().addListener(this);
			}
		});
	}

	protected String getEstilo(T item, boolean empty) {
		return defaultStyle;
	}

	protected void onEditCommit(O object, T newValue) {
		commitEdit(newValue);

		if(newValue != null){
			onEdit(object, newValue);
		}

		//Truco para que se llame a Cell.updateItem() para que formatee el valor ingresado.
		getTableColumn().setVisible(false);
		getTableColumn().setVisible(true);
	}

	public abstract void onEdit(O object, T newValue);

	@Override
	public void changed(ObservableValue<? extends O> observable, O oldValue, O newValue) {
		if(this.getTableRow() != null && newValue != null){
			this.setEditable(esEditable(newValue));
		}
		else{
			this.setEditable(false);
		}
	}

	protected Boolean esEditable(O newValue) {
		return getTableView().isEditable();
	}

	protected String getString() {
		O objeto = null;
		try{
			objeto = getTableView().getItems().get(getIndex());
		} catch(Exception e){
		}

		if(objeto != null){
			if(getTableColumn().getCellValueFactory() != null){
				T valor = getTableColumn().getCellValueFactory().call(new CellDataFeatures<>(getTableView(), getTableColumn(), objeto)).getValue();
				if(valor != null){
					return valor.toString();
				}
			}
		}
		return getItem() == null ? "" : getItem().toString();
	}
}
