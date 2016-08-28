package proy.gui;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Encargada de la encriptación de contraseñas
 *
 */
public abstract class Contra {

	/**
	 * Encripta un String con el algoritmo MD5.
	 *
	 * @param palabra
	 *            palabra a encriptar, se borrará al terminar.
	 * @return String
	 */
	public static String encriptarMD5(char[] palabra) {
		String pe = "";
		pe = hash(palabra);
		return pe;
	}

	private static String hash(char[] clear) {
		String hash = "";
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] b = md.digest(toBytes(clear));
			int size = b.length;
			StringBuffer h = new StringBuffer(size);
			for(int i = 0; i < size; i++){
				int u = b[i] & 255;
				if(u < 16){
					h.append("0" + Integer.toHexString(u));
				}
				else{
					h.append(Integer.toHexString(u));
				}
			}
			hash = h.toString();
		} catch(NoSuchAlgorithmException e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return hash;
	}

	private static byte[] toBytes(char[] chars) {
		CharBuffer charBuffer = CharBuffer.wrap(chars);
		ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
		byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
		Arrays.fill(chars, '\u0000'); // clear sensitive data
		Arrays.fill(charBuffer.array(), '\u0000'); // clear sensitive data
		Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
		return bytes;
	}
}