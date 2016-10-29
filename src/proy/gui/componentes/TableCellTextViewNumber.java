package proy.gui.componentes;

import javafx.util.StringConverter;

public abstract class TableCellTextViewNumber<O> extends TableCellTextView<O, Number> {
	
	public TableCellTextViewNumber(Class<? extends O> claseTabla) {
		super(claseTabla, new StringConverter<Number>() {

			@Override
			public Number fromString(String string) {
				return new Integer(Integer.parseInt(string));
			}

			@Override
			public String toString(Number object) {
				return String.valueOf(object);
			}
		});
	}
}
