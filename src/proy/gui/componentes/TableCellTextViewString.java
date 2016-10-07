package proy.gui.componentes;

import javafx.util.StringConverter;

public abstract class TableCellTextViewString<O> extends TableCellTextView<O, String> {

	public TableCellTextViewString() {
		super(new StringConverter<String>() {

			@Override
			public String fromString(String string) {
				return string;
			}

			@Override
			public String toString(String object) {
				return object;
			}
		});
	}
}
