/**
 * Copyright (c) 2016, Andres Leonel Rico - Emiliano Gioria - Marina Ludi
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package proy.gui;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Encargada de la encriptación de contraseñas
 */
public abstract class Contra {

	private final static Integer ITERATIONS = 10000;
	private final static Integer KEY_LENGTH = 256;

	/**
	 * Encripta un String con el algoritmo MD5.
	 *
	 * @param palabra
	 *            palabra a encriptar, se borrará al terminar.
	 * @param sal
	 *            sal para ocultar la palabra.
	 * @return String
	 */
	public static String encriptarMD5(char[] palabra, String sal) {
		return new String(hashPassword(palabra, sal.getBytes(), ITERATIONS, KEY_LENGTH));
	}

	private static byte[] hashPassword(final char[] password, final byte[] salt, final int iterations, final int keyLength) {
		try{
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
			PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
			SecretKey key = skf.generateSecret(spec);
			byte[] res = key.getEncoded();
			Arrays.fill(password, '\u0000'); // clear sensitive data
			return res;
		} catch(NoSuchAlgorithmException | InvalidKeySpecException e){
			throw new RuntimeException(e);
		}
	}

	public static String generarSal() {
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[20];
		random.nextBytes(bytes);
		return new String(bytes);
	}
}