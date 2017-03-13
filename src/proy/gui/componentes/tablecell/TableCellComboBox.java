/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.componentes.tablecell;

import java.util.Collection;

import javafx.scene.control.ComboBox;

public abstract class TableCellComboBox<O, T> extends TableCellAbstract<O, T> {

	protected ComboBox<T> comboBox;
	private Collection<? extends T> opciones;

	public TableCellComboBox(Collection<? extends T> opciones) {
		super();
		this.opciones = opciones;
	}

	@Override
	public void startEdit() {
		if(this.isEditable()){
			super.startEdit();

			if(comboBox == null){
				createComboBox();
			}
			setText(null);
			comboBox.getSelectionModel().select(getItem());
			setGraphic(comboBox);

			comboBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
				if(!newValue){
					guardarCambios();
				}
			});
		}
	}

	private void createComboBox() {
		nuevoComboBox();
		setComboBoxProperties();
		comboBox.getItems().addAll(opciones);
	}

	protected void nuevoComboBox() {
		comboBox = new ComboBox<>();
	}

	protected void setComboBoxProperties() {
		comboBox.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
		comboBox.setOnKeyReleased((t) -> {
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
			onEditCommit(objeto, comboBox.getValue());
		} catch(Exception e){
			onEditCommit(objeto, null);
		}
		cancelEdit();
	}
}
