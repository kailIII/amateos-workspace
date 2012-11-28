package org.inftel.socialwind.server.domain;

import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Gestiona el {@link EntityManager} asociado a las peticiones {@link RequestFactoryServlet}.
 * 
 * @author ibaca
 * 
 * @see DomainContext
 * 
 */
public class DomainFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
        DomainContext.initialize();
    }

    public void destroy() {
        DomainContext.destroy();
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        DomainContext.requestBegin();
        try {
            chain.doFilter(req, res);
            DomainContext.requestSuccess();
        } catch (Exception e) {
            DomainContext.requestFail();

        } finally {
            DomainContext.requestFinalize();
        }

    }
}