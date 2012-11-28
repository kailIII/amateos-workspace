package org.inftel.socialwind.server.test;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Utilidades varias para test
 * 
 * @author ibaca
 * 
 */
public final class Utils {

    private static final SecureRandom random = new SecureRandom();

    public Utils() {
        // no instanciable pero debe de dejarse q se intancie porque
        // como se llama Test... el framework la intenta ejecutar
    };

    public static String randomString() {
        return new BigInteger(130, random).toString(32);
    }

}
