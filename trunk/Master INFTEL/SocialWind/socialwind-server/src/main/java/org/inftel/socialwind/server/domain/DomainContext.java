package org.inftel.socialwind.server.domain;

import com.google.web.bindery.requestfactory.server.SimpleRequestProcessor;
import com.google.web.bindery.requestfactory.shared.Locator;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Factoria para crear EntityManager de JPA.<br>
 * 
 * Basada en los comentarios de Caffeine Coma en Stackoverflow <a href=
 * "http://stackoverflow.com/questions/4988397/gwt-requestfactory-how-to-use-single-entitymanager-per-request"
 * >gwt-requestfactory-how-to-use-single-entitymanager-per-request</a><br>
 * 
 * TODO: Falta añadir la posibilidad de realizar un ciclo transaccional, de forma que asegure que
 * toda la peticion se realize sobre una transaccion, y que en caso de fallo se cancele.
 * 
 * TODO Tambien se puede usar los {@link Locator} para interceptar las peticiones y guardar la
 * referencia en el contexto
 * <code>RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext()</code>. Este
 * metodo es mejor para hacer test unitarios cuando se usa {@link SimpleRequestProcessor}, ya que al
 * no llamar a través de servlet, la gestion de esta clase se debe hacer manualmente.
 * 
 * @see DomainFilter
 */
public final class DomainContext {

    private static ThreadLocal<EntityManager> holder = new ThreadLocal<EntityManager>() {
        @Override
        protected EntityManager initialValue() {
            throw new IllegalStateException(DomainContext.class.getName()
                    + " no esta inicializado");
        }
    };

    private static final Logger log = Logger.getLogger(DomainContext.class.getName());

    private static EntityManagerFactory factory;

    private DomainContext() {
        // no instanciable
    }

    /**
     * Obtiene el {@link EntityManager} en curso
     * 
     * @return
     */
    public static EntityManager get() {
        return holder.get();
    }

    /**
     * Establece el {@link EntityManager} en curso
     * 
     * @param em
     */
    public static void set(EntityManager em) {
        if (em == null) {
            holder.remove();
        } else {
            holder.set(em);
        }
    }

    public static void initialize() {
    	log.info("inicializando contexto de dominio");
        // TODO por defecto appEngine no permite crear varios DataStore, si
        // se deja esto activo da error al lanzar los test, por ahora se desactvia
        // if (factory != null) {
        // throw new IllegalStateException(ThreadLocalEntityManager.class.getName()
        // + " ya esta inicializado");
        // }
        if (factory == null) {
            factory = Persistence.createEntityManagerFactory("transactions-optional");
            if (factory == null) {
            	throw new RuntimeException("no se ha podido inicializar EntityManager");
            }
        }
    }

    public static void destroy() {
        // factory.close();
        // factory = null;
    }

    /**
     * Crea un nueva instancia {@link EntityManager}
     */
    public static void requestBegin() {
        EntityManager em = factory.createEntityManager();
        set(em);
        log.info("iniciado gestor de entidades " + em);
        // EntityTransaction tx = em.getTransaction();
        // tx.begin();

    }

    /**
     * En caso de ciclo transaccional, finaliza con exito la transaccion
     */
    public static void requestSuccess() {
        // tx.commit();
    }

    /**
     * En caso de ciclo transaccional, cancela la transaccion en curso
     */
    public static void requestFail() {
        // tx.rollback();
    }

    /**
     * Libera los recursos de la instancia {@link EntityManager}
     */
    public static void requestFinalize() {
        EntityManager em = get();
        log.info("cerrando gestor de entidades " + em);
        em.close();
        set(null);
    }

}
