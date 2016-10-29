/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.componentes;

import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

public abstract class TableCellTextView<O, T> extends TableCell<O, T> implements ChangeListener<O> {

	protected TextField textField;
	protected StringConverter<? extends T> convertidor;
	private Class<? extends O> claseTabla;
	private O objeto = null;

	@SuppressWarnings("unchecked")
	public TableCellTextView(Class<? extends O> claseTabla, StringConverter<? extends T> convertidor) {
		super();
		this.claseTabla = claseTabla;
		this.convertidor = convertidor;
		//Cuando la fila es seteada...
		this.tableRowProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue != null){
				//se agrega un listener a dicha fila para que escuche cuando cambia su contenido
				((javafx.scene.control.TableRow<O>) newValue).itemProperty().addListener(this);
			}
		});
	}

	@Override
	public void startEdit() {
		if(this.isEditable()){
			super.startEdit();

			if(textField == null){
				createTextField();
			}
			setText(null);
			textField.setText(getString());
			setGraphic(textField);
		}
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
		setText(getString());
		textField.setText(null);
		setGraphic(null);
	}

	@Override
	public void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		if(empty){
			setText(null);
			setGraphic(null);
		}
		else{
			setText(getString());
		}
	}

	private void createTextField() {
		nuevoTextField();
		setTextFieldProperties();
	}

	protected void nuevoTextField() {
		textField = new TextField();
	}

	protected void setTextFieldProperties() {
		textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
		textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				if(t.getCode() == KeyCode.ENTER){
					try{
						commitEdit(convertidor.fromString(textField.getText()));
					} catch(Exception e){
						commitEdit(null);
					}
					cancelEdit();
				}
				else if(t.getCode() == KeyCode.ESCAPE){
					cancelEdit();
				}
			}
		});
	}

	private String getString() {
		Optional.ofNullable(getTableRow().getItem()).filter(claseTabla::isInstance).map(claseTabla::cast).ifPresent(n -> objeto = n);
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
