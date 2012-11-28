package org.inftel.socialwind.server.services;

import static org.inftel.socialwind.server.domain.DomainContext.requestBegin;
import static org.inftel.socialwind.server.domain.DomainContext.requestFinalize;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.inftel.socialwind.server.domain.DomainContext;
import org.inftel.socialwind.server.domain.Location;
import org.inftel.socialwind.server.domain.Spot;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class SpotServiceTest {

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
    
    static class L extends Location {
        public String name;

        public L(String name, double lat, double lon) {
            super(lat, lon);
            this.name = name;
        }
    }

    static L[] locations;

    static {
        // Puntos varios para añadir y validar
        L malaga = new L("malaga", 36.723, -4.415);
        L playamar = new L("playamar", 36.6332, -4.4869);
        L elpalo = new L("elpalo", 36.7177, -4.3492);
        L almeria = new L("almeria", 36.836, -2.403);
        locations = new L[] { malaga, playamar, elpalo, almeria };
    }

    @Test
    public final void testPopulate() {
        helper.setEnvIsLoggedIn(true);
        helper.setEnvEmail("spot_service_test@gmail.com");

        SurferService.currentSurfer();
        
        // Un spot en cada punto
        for (L location : locations) {
            Spot spot = new Spot();
            spot.setName(location.name);
            spot.setLocation(new Location(location.getLatitude(), location.getLongitude()));
            SpotService.persist(spot);
            
            // Restart domain context
            requestFinalize();
            requestBegin();
        }
    }

    @Test
    public final void testFindNearbySpots() {
        // Malaga
        Double latitude = 36.723;
        Double longitude = -4.415;

        List<Spot> spots = SpotService.findNearbySpots(latitude, longitude);
        assertTrue(spots.size() > 0);

        List<Spot> hotSpots = SpotService.findNearbyHotSpots(latitude, longitude);
        assertTrue(hotSpots.size() == 0);
    }
}
