package org.inftel.socialwind.server.services;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.Point;

import static org.inftel.socialwind.server.services.SessionService.findActiveSession;

import org.inftel.socialwind.server.UserNotAuthenticatedException;
import org.inftel.socialwind.server.domain.Session;
import org.inftel.socialwind.server.domain.Spot;
import org.inftel.socialwind.server.domain.Surfer;
import org.inftel.socialwind.server.domain.DomainContext;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class SurferService {

    private static final Logger log = Logger.getLogger(SurferService.class.getName());

    private static final String SELECT_BY_EMAIL = "select o from Surfer o where o.email = :email";
    private static final String SELECT_ALL = "select o from Surfer o";
    private static final String SELECT_COUNT = "select count(o) from Surfer o";

    public static final EntityManager entityManager() {
        return DomainContext.get();
    }

    public static Long countSurfers() {
        EntityManager em = entityManager();
        return ((Number) em.createQuery(SELECT_COUNT).getSingleResult()).longValue();
    }

    public static List<Surfer> findAllSurfers() {
        log.info("intentando responder a findAllSurfers");
        EntityManager em = entityManager();
        @SuppressWarnings("unchecked")
        List<Surfer> surferList = em.createQuery(SELECT_ALL).getResultList();
        surferList.size(); // forzar materializar resultados
        log.info("respondiendo lista completa de surferos (total " + surferList.size() + ")");
        return surferList;
    }

    public static Surfer findSurfer(Long id) {
        if (id == null) {
            return null;
        }
        EntityManager em = entityManager();
        return em.find(Surfer.class, id);
    }

    public static List<Surfer> findSurferEntries(int firstResult, int maxResults) {
        EntityManager em = entityManager();
        @SuppressWarnings("unchecked")
        List<Surfer> resultList = em.createQuery(SELECT_ALL).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
        resultList.size(); // forzar materializar resultados
        return resultList;
    }

    public static void persist(Surfer instance) {
        if (instance == null) {
            throw new NullPointerException();
        }
        if (instance.getId() != null) {
            throw new RuntimeException("Id debe ser nulo, es decir, surfero nuevo");
        }
        if (instance.getEmail() == null || instance.getEmail().trim().length() == 0) {
            throw new RuntimeException("Un surfero debe crearse con email valido");
        }
        if (findSurferByEmail(instance.getEmail()) != null) {
            throw new RuntimeException("No pueden crearse dos surferos con el mismo mail");
        }
        EntityManager em = entityManager();
        em.persist(instance);
        em.refresh(instance);
    }

    public static void remove(Surfer instance) {
        EntityManager em = entityManager();
        Surfer attached = em.find(Surfer.class, instance.getId());
        em.remove(attached);
    }

    /**
     * Actualiza la posición del surfero. En caso de que el surfero entre o salga de una playa se
     * actualizara el estado de estas playas.
     * 
     * @param surfer
     * @param latitude
     * @param longitude
     */
    public static Session updateSurferLocation(double latitude, double longitude) {
        logRequest("actualizando posicion a " + latitude + ", " + longitude);
        Session session = null;

        // Usuario autenticado
        Surfer surfer = currentSurfer();

        // Location as a Point
        Point location = new Point(latitude, longitude);

        // Generate Geocells
        List<String> cells = GeocellManager.generateGeoCell(location);

        // Save instance
        surfer.setLatitude(latitude);
        surfer.setLongitude(longitude);
        surfer.setGeoCellsData(cells);

        // Se busca si la posicion forma parte de una playa
        Spot spot = SpotService.findSpotByLocation(latitude, longitude);
        Session actual = findActiveSession(surfer);

        // Esta el surfer en una playa?
        if (SessionService.hasActiveSession(surfer)) {
            // Ha salido o cambiado de playa el surfer?
            if (spot == null || !spot.getId().equals(actual.getSpotId())) {
                SessionService.endSession(surfer);
            }
        }

        // Ha cambiado o entrado en una nueva playa?
        if (spot != null && (actual == null || !spot.getId().equals(actual.getSpotId()))) {
            session = SessionService.beginSession(surfer, spot);
        }

        return session;
    }

    public static List<Session> findSessionsBySurfer(Surfer surfer) {
        return SessionService.findSessionsBySurfer(surfer);
    }

    public static List<Session> findSessions() {
        return SessionService.findSessionsBySurfer(currentSurfer());
    }

    /**
     * Devuelve el usuario asociado a la cuenta actual. En caso de ser un usuario nuevo, devuelve un
     * nuevo surfero.
     * 
     * FIXME es peligroso usar este metodo internamente! si se llama varias veces, no queda claro
     * cual de los surfer se hara persistente, y por tanto, es facil perder datos!
     * 
     * @return surfero asociado a la cuenta actual
     * @throws UserNotAuthenticatedException
     *             si no hay ningun usuario autenticado
     */
    public static Surfer currentSurfer() {
        logRequest("obteniendose asi mismo a traves de currentSurfer");
        // FIXME encapsular UserService y todo lo relacionado Usuarios - AppEngine
        User user = UserServiceFactory.getUserService().getCurrentUser();
        if (user == null) {
            throw new UserNotAuthenticatedException();
        }

        Surfer surfer = findSurferByEmail(user.getEmail());
        if (surfer == null) {
            log.info("Registrando nuevo usuario en el sistema con email (" + user.getEmail() + ")");
            surfer = new Surfer();
            surfer.setDisplayName(user.getNickname());
            surfer.setEmail(user.getEmail());
            surfer.setFullName(user.getNickname());
            persist(surfer);
        }
        return surfer;
    }

    /**
     * Busca el surfero asociado al correo pasado como parametro, en caso de no existir devuelve
     * <code>null</code>.
     * 
     * @param email
     *            correo del surfero que se quiere buscar
     * @return el surfero buscado o <code>null</code> si no existe ningun surfero con este correo
     */
    private static Surfer findSurferByEmail(String email) {
        if (email == null) {
            return null;
        }

        EntityManager em = entityManager();
        Query query = em.createQuery(SELECT_BY_EMAIL);
        query.setParameter("email", email);
        query.setMaxResults(1);
        @SuppressWarnings("unchecked")
        List<Surfer> resultList = query.getResultList();
        return (resultList.isEmpty()) ? null : resultList.get(0);
    }

    private static void logRequest(String message) {
        if (log.isLoggable(Level.INFO)) {
            log.info("un usuario esta " + message);
        }
    }
}
