package org.inftel.socialwind.server;

/**
 * Excepcion que indica que se esta intentando acceder a los servicios, pero no se ha autenticado
 * ningun usuario valido.
 * 
 * @author ibaca
 * 
 */
public class UserNotAuthenticatedException extends RuntimeException {
    private static final long serialVersionUID = -8733695180781376521L;
    
    public UserNotAuthenticatedException() {
        super("El usuario debe estar autenticado");
    }
}
