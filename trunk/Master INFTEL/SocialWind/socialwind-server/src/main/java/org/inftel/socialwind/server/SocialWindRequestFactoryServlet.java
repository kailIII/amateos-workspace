package org.inftel.socialwind.server;

import com.google.web.bindery.requestfactory.server.DefaultExceptionHandler;
import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import static java.util.logging.Level.WARNING;

import java.util.logging.Logger;

/**
 * Wrapper de {@link RequestFactoryServlet} para realizar log de los errores producidos en los
 * servicios solicitados por los clientes a traves de <code>RequestFactory</code>.
 * 
 * @author ibaca
 * 
 */
@SuppressWarnings("serial")
public class SocialWindRequestFactoryServlet extends RequestFactoryServlet {

    private static final Logger log = Logger.getLogger(SocialWindRequestFactoryServlet.class
            .getName());

    /**
     * {@link ExceptionHandler} que registra los errores en el log.
     * 
     * @author ibaca
     * 
     */
    public static final class SocialWindExceptionHandler extends DefaultExceptionHandler {
        @Override
        public ServerFailure createServerFailure(Throwable throwable) {
            log.log(WARNING, "Service error: " + throwable.getMessage(), throwable);
            return super.createServerFailure(throwable);
        }
    }

    public SocialWindRequestFactoryServlet() {
        super(new SocialWindExceptionHandler());
        log.info("request factory servlet inicializado: " + this);
    }

}
