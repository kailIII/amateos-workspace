package org.inftel.socialwind.server.services;

import static org.inftel.socialwind.server.domain.DomainContext.requestBegin;
import static org.inftel.socialwind.server.domain.DomainContext.requestFinalize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.inftel.socialwind.server.UserNotAuthenticatedException;
import org.inftel.socialwind.server.domain.DomainContext;
import org.inftel.socialwind.server.domain.Session;
import org.inftel.socialwind.server.domain.Spot;
import org.inftel.socialwind.server.domain.Surfer;
import org.inftel.socialwind.server.test.Utils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class SurferServiceTest {

    /** Helper para AppEngine Local Services **/
    static LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig());

    @BeforeClass
    public static void setUpClass() throws Exception {
        helper.setUp();
        helper.setEnvIsLoggedIn(true);
        helper.setEnvAuthDomain("google.com");
        DomainContext.initialize();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        DomainContext.destroy();
        helper.tearDown();
    }

    @Before
    public void setUp() throws Exception {
        DomainContext.requestBegin();
    }

    @After
    public void tearDown() throws Exception {
        DomainContext.requestFinalize();
    }

    @Test
    public final void testEntityManager() {
        assertNotNull(SurferService.entityManager());
    }
    
    @Test(expected = UserNotAuthenticatedException.class)
    public final void testCurrentUserNotAuthenticated() {
        helper.setEnvIsLoggedIn(false);
        @SuppressWarnings("unused")
        Surfer current = SurferService.currentSurfer();
    }
    
    @Test
    public final void testCurrentUserAuthenticated() {
        helper.setEnvIsLoggedIn(true);
        helper.setEnvEmail("test@gmail.com");
        
        Surfer current = SurferService.currentSurfer();
        assertNotNull(current);
        assertEquals("test@gmail.com", current.getEmail());
    }
    
    @Test
    public final void testCountSurfers() {
        Integer expected = SurferService.findAllSurfers().size();
        Integer actual = SurferService.countSurfers().intValue();
        assertEquals(expected, actual);
    }

    @Test
    public final void testFindAllSurfers() {
        // el anterior es suficiente por ahora
    }

    @Test
    public final void testFindSurfer() {
        // fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testFindSurferEntries() {
        // fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testPersist() {
        // Se comprueba que las fechas update y create se comporten correctamente

        // Spot Creation
        Surfer surfer = new Surfer();
        surfer.setEmail("test-persist-user@gmail.com");
        surfer.setDisplayName("test-persist-user");

        // First save
        SurferService.persist(surfer);
        
        requestFinalize();
        requestBegin();

        Date firstCreated = surfer.getCreated();
        assertNotNull(firstCreated);
        Date firstUpdated = surfer.getUpdated();
        assertNotNull(firstUpdated);

        Date secondReference = new Date();

        // Second save
        surfer = SurferService.findSurfer(surfer.getId());
        surfer.setFullName("second_name");
        //SurferService.persist(surfer);

        requestFinalize();
        requestBegin();
        
        Date secondCreated = surfer.getCreated();
        assertNotNull(secondCreated);
        Date secondUpdated = surfer.getUpdated();
        assertNotNull(secondUpdated);

        // Check dates diff
        assertEquals(firstCreated, secondCreated);
        assertNotSame(firstUpdated, secondUpdated);
        assertTrue(secondReference.before(secondUpdated));
    }

    @Test
    public final void testRemove() {
        // fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testUpdateSurferLocationBeginSession() {
        String email = Utils.randomString() + "@gmail.com";
        Surfer surfer = new Surfer();
        surfer.setEmail(email);
        SurferService.persist(surfer);

        // Malaga
        Double latitude = 36.723;
        Double longitude = -4.415;

        Spot spot = new Spot();
        spot.setName("malaga");
        spot.setLatitude(latitude);
        spot.setLongitude(longitude);
        SpotService.persist(spot);

        requestFinalize();
        requestBegin();

        //surfer = SurferService.findSurfer(surfer.getId());
        //assertFalse(SessionService.hasActiveSession(surfer));

        // Actualizar la posición debe
        // * Cambiar la posicion del surfero
        // * Añadirlo a la playa malaga
        // * Actualizar los contadores de playa
        helper.setEnvEmail(email); // para que current user sea el que queremos
        Session session = SurferService.updateSurferLocation(latitude, longitude);

        requestFinalize();
        requestBegin();

        surfer = SurferService.findSurfer(surfer.getId());
        assertTrue(SessionService.hasActiveSession(surfer));
        assertNotNull(session);

        assertEquals(1, session.getSpot().getSurferCount().intValue());
        assertEquals(1, session.getSpot().getSurferCurrentCount().intValue());
    }

    @Test
    public final void testUpdateSurferLocationEndSession() {
        String email = Utils.randomString() + "@gmail.com";
        Surfer surfer = new Surfer();
        surfer.setEmail(email);
        SurferService.persist(surfer);

        // Malaga
        Double latitude = 36.723;
        Double longitude = -4.415;

        Spot spot = new Spot();
        spot.setName("malaga");
        spot.setLatitude(latitude);
        spot.setLongitude(longitude);
        SpotService.persist(spot);

        requestFinalize();
        requestBegin();

        surfer = SurferService.findSurfer(surfer.getId());
        helper.setEnvEmail(email); // para que current user sea el que queremos
        Session session = SurferService.updateSurferLocation(latitude, longitude);

        // Playamar
        double playamarLatitude = 36.6338;
        double playamarLongitude = -4.4859;

        requestFinalize();
        requestBegin();

        surfer = SurferService.findSurfer(surfer.getId());
        session = SessionService.findSession(session.getId());
        assertTrue(SessionService.hasActiveSession(surfer));

        // Datos para comparar
        spot = session.getSpot(); // Esto recarga los datos del spot
        Integer surferCount = spot.getSurferCount();
        Integer surferCurrentCount = spot.getSurferCurrentCount();

        // Actualizar la posicion fuera de la playa implica
        // * Cambiar la posicion del surfero
        // * Finalizar la sesion, fecha de fin
        // * Eliminar la sesion activa del surfero
        // * Actualizar contador de surferos en la playa anterior
        helper.setEnvEmail(email); // para que current user sea el que queremos
        SurferService.updateSurferLocation(playamarLatitude, playamarLongitude);

        requestFinalize();
        requestBegin();

        // Recarga de datos
        session = SessionService.findSession(session.getId());
        surfer = session.getSurfer();
        spot = session.getSpot();

        // Comprobaciones
        assertEquals(playamarLatitude, surfer.getLatitude(), 0.0001);
        assertEquals(playamarLongitude, surfer.getLongitude(), 0.0001);
        assertNotNull(session.getEnd());
        assertTrue(session.getStart().before(session.getEnd()));
        assertFalse(SessionService.hasActiveSession(surfer));
        assertEquals(surferCount, spot.getSurferCount());
        assertEquals(surferCurrentCount - 1, spot.getSurferCurrentCount().intValue());
    }
    
}
