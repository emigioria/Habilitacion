package proy.gui.componentes;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

public abstract class TableCellTextView<O, T> extends TableCell<O, T> implements ChangeListener<O> {

	private TextField textField;
	private StringConverter<T> convertidor;

	@SuppressWarnings("unchecked")
	public TableCellTextView(StringConverter<T> convertidor) {
		super();
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
		textField = new TextField();
		textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
		textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				if(t.getCode() == KeyCode.ENTER){
					commitEdit(convertidor.fromString(textField.getText()));
					cancelEdit();
				}
				else if(t.getCode() == KeyCode.ESCAPE){
					cancelEdit();
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private String getString() {
		if(getTableColumn().getCellValueFactory() != null){
			T valor = getTableColumn().getCellValueFactory().call(new CellDataFeatures<>(getTableView(), getTableColumn(), (O) getTableRow().getItem())).getValue();
			if(valor != null){
				return valor.toString();
			}
		}
		return getItem() == null ? "" : getItem().toString();
	}
}
