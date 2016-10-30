/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.componentes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

/**
 * Esta clase se creó para usarse como celda de una TableColumn de Number.
 * En la edición el usuario solo pueda ingresar dígitos.
 * Se desaconseja su uso porque la entrada de numero se vuelve incómoda
 */
public abstract class TableCellTextViewNumber<O> extends TableCellTextView<O, Number> {

	public TableCellTextViewNumber(Class<? extends O> claseTabla, StringConverter<? extends Number> convertidor) {
		super(claseTabla, convertidor);
	}

	@Override
	protected void nuevoTextField() {
		
		textField = new TextField();
		
		textField.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	            if(newValue == null){
	            	return;
	            }
	            if(newValue.equals("-1")){
	            	textField.setText("");
	            	return;
	            }
	            
	        	if (!newValue.matches("\\d*")) {
	                textField.setText(newValue.replaceAll("[^\\d]", ""));
	            }
	        }
	    });
	}
}
