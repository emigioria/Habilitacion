package proy.gui.componentes;

import java.lang.reflect.Field;

import javafx.scene.control.PasswordField;

public class SafePasswordField extends PasswordField {

	public final char[] getPassword() {
		try{
			Content c = getContent();

			Field fld = c.getClass().getDeclaredField("characters");
			fld.setAccessible(true);

			StringBuilder sb = (StringBuilder) fld.get(c);
			char[] result = new char[sb.length()];
			sb.getChars(0, sb.length(), result, 0);

			return result;
		} catch(Exception e){
			e.printStackTrace();
			return new char[] {};
		}
	}

}
