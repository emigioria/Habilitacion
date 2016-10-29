/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui.componentes;

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
		textField = new TextField() {

			@Override
			public void replaceText(int start, int end, String text) {
				String textoOriginal = this.getText();
				super.replaceText(start, end, text);
				if(!validate(this.getText())){
					this.setText(textoOriginal);
				}
			}

			@Override
			public void replaceSelection(String text) {
				String textoOriginal = this.getText();
				super.replaceSelection(text);
				if(!validate(this.getText())){
					this.setText(textoOriginal);
				}
			}

			private boolean validate(String text) {
				if(text == null || text.isEmpty()){
					return false;
				}
				return text.matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?");
			}
		};

	}
}
