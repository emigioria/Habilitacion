package proy.gui.componentes;

import javafx.util.StringConverter;

public abstract class TableCellTextViewString<O> extends TableCellTextView<O, String> {

	public TableCellTextViewString(Class<? extends O> claseTabla) {
		super(claseTabla, new StringConverter<String>() {

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
