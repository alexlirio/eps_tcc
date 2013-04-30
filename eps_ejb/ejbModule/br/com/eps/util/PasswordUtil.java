package br.com.eps.util;

import org.jboss.security.auth.spi.Util;

/**
 * Classe usada para testar a criptografia de Strings
 * @author Alex Lirio
 *
 */
public class PasswordUtil {
	
	public static void main(String[] args) {

		String hashedPassword = Util.createPasswordHash("SHA-256", "BASE64", null, null, "123456");
		System.out.println(hashedPassword);
		
	}
}