package org.inftel.socialwind.server.services;

import static com.beoui.geocell.GeocellManager.proximitySearch;
import static org.apache.commons.lang.StringUtils.capitalize;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.inftel.socialwind.server.domain.DomainContext;
import org.inftel.socialwind.server.domain.Spot;
import org.inftel.socialwind.server.domain.Surfer;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.GeocellQuery;
import com.beoui.geocell.model.Point;

public class SpotService {

    private static final Logger log = Logger.getLogger(SpotService.class.getName());

    private static final String SELECT_ALL = "select o from Spot o";
    private final static String SELECT_COUNT = "select count(o) from Spot o";
    private static final Integer HOTSPOT_SURFERS_LIMIT = 3;

    public static final EntityManager entityManager() {
        return DomainContext.get();
    }

    public static Long countSpots() {
        EntityManager em = entityManager();
        return ((Number) em.createQuery(SELECT_COUNT).getSingleResult()).longValue();
    }

    public static List<Spot> findAllSpots() {
        logRequest("buscando todos los spots");
        EntityManager em = entityManager();
        @SuppressWarnings("unchecked")
        List<Spot> SpotList = em.createQuery(SELECT_ALL).getResultList();
        SpotList.size(); // forzar materializar resultados
        return SpotList;
    }

    public static Spot findSpot(Long id) {
        if (id == null) {
            return null;
        }
        EntityManager em = entityManager();
        return em.find(Spot.class, id);
    }

    public static List<Spot> findSpotEntries(int firstResult, int maxResults) {
        logRequest("buscando la lista de spots");
        EntityManager em = entityManager();
        @SuppressWarnings("unchecked")
        List<Spot> resultList = em.createQuery(SELECT_ALL).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
        resultList.size(); // forzar materializar resultados
        return resultList;
    }

    public static void persist(Spot instance) {
        logRequest("guardando un " + instance);
        EntityManager em = entityManager();
        generateGeoCells(instance); // TODO actualizar geocells solo cuando se necesite
        em.persist(instance);
        em.refresh(instance);
    }

    private static void generateGeoCells(Spot instance) {
        Point location = new Point(instance.getLatitude(), instance.getLongitude());
        List<String> cells = GeocellManager.generateGeoCell(location);
        instance.setGeoCellsData(cells);
    }

    public static void remove(Spot instance) {
        logRequest("burrando un " + instance);
        EntityManager em = entityManager();
        Spot attached = em.find(Spot.class, instance.getId());
        em.remove(attached);
    }

    public static Spot findSpotByLocation(double latitude, double longitude) {
        logRequest("buscando un spot por su ubicacion " + latitude + ", " + longitude);
        EntityManager em = entityManager();
        Point center = new Point(latitude, longitude);
        GeocellQuery baseQuery = new GeocellQuery("select from Spot");
        List<Spot> spots = proximitySearch(center, 1, 500, Spot.class, baseQuery, em);
        if (spots.size() == 1) {
            return spots.get(0);
        } else {
            return null;
        }
    }

    public static List<Spot> findNearbySpots(double latitude, double longitude) {
        logRequest("buscando los spot cercanos a  " + latitude + ", " + longitude);
        EntityManager em = entityManager();
        Point center = new Point(latitude, longitude);
        GeocellQuery baseQuery = new GeocellQuery("select from Spot");
        List<Spot> spots = proximitySearch(center, 100, 0, Spot.class, baseQuery, em);
        spots.size(); // materializar resultados
        return spots;
    }

    public static List<Spot> findNearbyHotSpots(double latitude, double longitude) {
        logRequest("buscando los hotspot cercanos a  " + latitude + ", " + longitude);
        EntityManager em = entityManager();
        Point center = new Point(latitude, longitude);
        GeocellQuery baseQuery = new GeocellQuery("select from Spot where hot = true");
        List<Spot> spots = proximitySearch(center, 100, 0, Spot.class, baseQuery, em);
        spots.size(); // materializar resultados
        return spots;
    }

    /**
     * Se encarga de añadir surfer a la playa, acutaliza los contadores y el estado de playa
     * caliente.
     * 
     * @param spot
     *            playa donde se quiere añadir un surfero
     * @param surfer
     *            surfero al que se quiere añadir
     */
    static void addSurfer(Spot spot, Surfer surfer) {
        log.info("el surfer " + surfer.getEmail() + " ha entrado en la playa " + spot.getName());
        EntityManager em = entityManager();
        spot.setSurferCount(spot.getSurferCount() + 1);
        spot.setSurferCurrentCount(spot.getSurferCurrentCount() + 1);
        updateHotState(spot);
        em.persist(spot);
    }

    /**
     * Se encarga de eliminar un surfero de una playa, actualiza los contadores y el estado de playa
     * caliente.
     * 
     * @param spot
     *            playa donde se quiere eliminar un surfero
     * @param surfer
     *            surfero al que se quiere eliminar
     */
    static void removeSurfer(Spot spot, Surfer surfer) {
        log.info("el surfer " + surfer.getEmail() + " ha salido de la playa " + spot.getName());
        spot.setSurferCurrentCount(spot.getSurferCurrentCount() - 1);
        updateHotState(spot);
    }

    /**
     * @param spot
     */
    private static void updateHotState(Spot spot) {
        // Esta sorprendente situacion podria llegar a darse si no se usa un sistema transaccional
        // y de hecho, el sistema no tiene porque serlo ya que no es requisito y gasta recursos
        if (spot.getSurferCurrentCount() < 0) {
            spot.setSurferCurrentCount(0); // asi ningun usuario vera numero negativos
            log.warning("El contador del spot " + spot.getName() + " ha bajado de 0");
        }

        // Actualizar estado hotspot
        if (spot.getHot() && spot.getSurferCurrentCount() < HOTSPOT_SURFERS_LIMIT) {
            spot.setHot(false);
        } else if (!spot.getHot() && spot.getSurferCurrentCount() > HOTSPOT_SURFERS_LIMIT) {
            spot.setHot(true);
            EvenewsService.publish(capitalize(spot.getName()), spot.getLocation());
        }
    }

    private static void logRequest(String message) {
        if (log.isLoggable(Level.INFO)) {
            log.info("un usuario esta " + message);
        }
    }
}
