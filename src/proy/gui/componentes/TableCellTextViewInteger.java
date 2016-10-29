package proy.gui.componentes;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


/**
 * Esta clase se creó para usarse como celda de una TableColumn de enteros.
 * Sobreescribe el método createTextField() para que durante la edición,
 * el usuario solo pueda ingresar dígitos.
 * 
 * @author andrés
 *
 * @param <O>
 */
public abstract class TableCellTextViewInteger<O> extends TableCellTextViewNumber<O>{

	public TableCellTextViewInteger(Class<? extends O> claseTabla) {
		super(claseTabla);
	}
	
	@Override
	protected void createTextField() {
		textField = new TextField(){
			
			@Override
		    public void replaceText(int start, int end, String text)
		    {
		        if (validate(text))
		        {
		            super.replaceText(start, end, text);
		        }
		    }

		    @Override
		    public void replaceSelection(String text)
		    {
		        if (validate(text))
		        {
		            super.replaceSelection(text);
		        }
		    }

		    private boolean validate(String text)
		    {
		        return text.matches("[0-9]*");
		    }
		};
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
}
