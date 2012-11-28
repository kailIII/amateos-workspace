package org.inftel.socialwind.server.services;

import static com.beoui.geocell.GeocellManager.proximitySearch;
import static org.inftel.socialwind.server.services.SurferService.currentSurfer;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.inftel.socialwind.server.domain.DomainContext;
import org.inftel.socialwind.server.domain.Evenews;
import org.inftel.socialwind.server.domain.Location;
import org.inftel.socialwind.server.domain.Surfer;

import com.beoui.geocell.model.GeocellQuery;
import com.beoui.geocell.model.Point;

/**
 * Servicio encargado de gestionar los <code>evenews</code>.
 * 
 * @author ibaca
 * @see Evenews
 * 
 */
public class EvenewsService {

    private static final Logger log = Logger.getLogger(EvenewsService.class.getName());

    private static final int DEFAULT_PUBLISH_RANGE = 50 * 1000;

    private static final String SELECT_SINCE = "select o from Evenews o where o.created > :fromDate and o.surferId = :surferId";

    private static final String SELECT_ALL = "select o from Evenews o";

    public static final EntityManager entityManager() {
        return DomainContext.get();
    }

    public static List<Evenews> findEvenewsSince(Date fromDate) {
        EntityManager em = entityManager();
        Query query = em.createQuery(SELECT_SINCE);
        query.setParameter("fromDate", fromDate);
        query.setParameter("surferId", currentSurfer().getId());

        @SuppressWarnings("unchecked")
        List<Evenews> resultList = query.getResultList();
        resultList.size(); // forzar materializar resultados
        return resultList;
    }

    public static List<Evenews> findEvenewsEntries(int firstResult, int maxResults) {
        // TODO falta implementarlo!
        return null;
    }

    public static List<Evenews> findAllEvenews() {
        EntityManager em = entityManager();
        @SuppressWarnings("unchecked")
        List<Evenews> list = em.createQuery(SELECT_ALL).getResultList();
        list.size(); // forzar materializar resultados
        return list;
    }

    public static Evenews findEvenews(Long id) {
        if (id == null) {
            return null;
        }
        EntityManager em = entityManager();
        return em.find(Evenews.class, id);
    }

    public static void persist(Evenews instance) {
        EntityManager em = entityManager();
        em.persist(instance);
        em.refresh(instance);
    }

    public static void remove(Evenews instance) {
        EntityManager em = entityManager();
        Evenews attached = em.find(Evenews.class, instance.getId());
        em.remove(attached);
    }

    /**
     * Publica una noticia no asociada a una posición, es decir, es una noticia que todos los
     * usuarios del sistema podrán ver.
     * 
     * @param message
     *            cadena con el mensaje que se quiere publicar
     */
    public static void publish(String message) {
        throw new UnsupportedOperationException("por ahora esto no es buena idea");
        // Sea quien sea el que implemente esto que no llame a los metodos de abajo!
        // NO puede guardarse un mensaje para cada usuario! debera tratarse de forma diferente!
    }

    /**
     * Publica una noticia en el area comprendida entre location y el rango por defecto.
     * 
     * @param message
     *            cadena con el mensaje que se quiere publicar
     * @param location
     * @see EvenewsService#publish(String, Location, Integer)
     * @see EvenewsService#DEFAULT_PUBLISH_RANGE
     */
    public static void publish(String message, Location location) {
        publish(message, location, DEFAULT_PUBLISH_RANGE);
    }

    /**
     * Publica una noticia
     * 
     * @param message
     *            cadena con el mensaje que se quiere publicar
     * @param location
     * @param range
     */
    public static void publish(String message, Location location, Integer range) {
        log.info("publicando mensaje " + message);
        // Se buscan los usuario que esta en rango
        EntityManager em = entityManager();
        Point center = new Point(location.getLatitude(), location.getLongitude());
        GeocellQuery baseQuery = new GeocellQuery("select from Surfer");
        List<Surfer> surfers = proximitySearch(center, 999, 0, Surfer.class, baseQuery, em);

        for (Surfer surfer : surfers) {
            log.info("publicando evenews para el usuario " + surfer.getId());
            // Se crea el evenews
            Evenews evenews = new Evenews();
            evenews.setLocation(location);
            evenews.setMessage(message);
            evenews.setSurferId(surfer.getId());
            persist(evenews);

            // Se notifica por el servicio de mensajes
            CloudToDeviceService.sendMessage(evenews);
        }
    }

}
