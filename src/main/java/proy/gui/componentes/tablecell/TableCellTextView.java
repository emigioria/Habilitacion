/**
 * Copyright (c) 2016, Emiliano Gioria - Andres Leonel Rico
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.componentes.tablecell;

import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public abstract class TableCellTextView<O, T> extends TableCellAbstract<O, T> {

	private TextField textField;
	private StringConverter<? extends T> convertidor;

	public TableCellTextView(StringConverter<? extends T> convertidor) {
		super();
		this.convertidor = convertidor;
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

			textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
				if(!newValue){
					guardarCambios();
				}
			});
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
		textField.setOnKeyReleased((t) -> {
			switch(t.getCode()) {
			case ENTER:
				guardarCambios();
				break;

			case ESCAPE:
				cancelEdit();
				break;
			default:
				break;
			}
		});
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
		setText(getString());
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
		setStyle(getEstilo(item, empty));
	}

	private void guardarCambios() {
		O objeto = getTableView().getItems().get(getIndex());
		try{
			onEditCommit(objeto, convertidor.fromString(textField.getText()));
		} catch(Exception e){
			onEditCommit(objeto, null);
		}
		cancelEdit();
	}
}
