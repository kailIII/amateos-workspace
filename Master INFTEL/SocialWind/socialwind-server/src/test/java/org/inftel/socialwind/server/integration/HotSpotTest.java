package org.inftel.socialwind.server.integration;

import static org.inftel.socialwind.server.RequestFactoryHelper.createSimpleFactory;
import static org.inftel.socialwind.server.test.Utils.randomString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.inftel.socialwind.server.domain.DomainContext;
import org.inftel.socialwind.server.domain.Location;
import org.inftel.socialwind.shared.domain.LocationProxy;
import org.inftel.socialwind.shared.domain.SessionProxy;
import org.inftel.socialwind.shared.domain.SpotProxy;
import org.inftel.socialwind.shared.domain.SurferProxy;
import org.inftel.socialwind.shared.services.SocialwindRequestFactory;
import org.inftel.socialwind.shared.services.SpotRequest;
import org.inftel.socialwind.shared.services.SurferRequest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

/**
 * Para realizar una simulacion del back-end de persistencia esta guía es un buen punto de partida
 * http://cleancodematters.wordpress.com/2011/06/18/tutorial-gwt-request-factory-%E2%80%93-part-ii/
 * 
 * @author ibaca
 * 
 */
public class HotSpotTest {

    /** Helper para AppEngine Local Services **/
    static LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig());

    static SocialwindRequestFactory factory;

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

    @BeforeClass
    public static void setUpClass() throws Exception {
        helper.setUp();
        helper.setEnvIsLoggedIn(true);
        helper.setEnvAuthDomain("google.com");
        helper.setEnvEmail("debe_modificarse_para_cada_test");
        DomainContext.initialize();
        factory = createSimpleFactory(SocialwindRequestFactory.class);
    }

    static Map<String, SurferProxy> surfers = new HashMap<String, SurferProxy>();
    static {
        surfers.put("juan", null);
        surfers.put("ignacio", null);
        surfers.put("carlos", null);
        surfers.put("jose", null);
        surfers.put("alberto", null);
        surfers.put("guillermo", null);
        surfers.put("gonzalo", null);
        surfers.put("adrian", null);
        surfers.put("angel", null);
        surfers.put("manuel", null);
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
    public void testPopulate() {
        // Este test no deberia fallar!

        // Este usuario se usa para añadir spots
        helper.setEnvEmail(randomString() + "_spot_add@gmail.com");

        // Un spot en cada punto
        for (L location : locations) {
            SpotRequest request = factory.spotRequest();

            LocationProxy lp = request.create(LocationProxy.class);
            lp.setLatitude(location.getLatitude());
            lp.setLongitude(location.getLongitude());

            SpotProxy spot = request.create(SpotProxy.class);
            spot.setName(location.name);
            spot.setLocation(lp);
            request.persist(spot).fire();

            // Restart domain context
            DomainContext.requestFinalize();
            DomainContext.requestBegin();
        }

        // Se dan de alta los surfers
        for (final String name : surfers.keySet()) {
            // Usuario valido
            helper.setEnvEmail(name + "@gmail.com");

            SurferRequest request = factory.surferRequest();
            request.currentSurfer().fire(new Receiver<SurferProxy>() {
                @Override
                public void onSuccess(SurferProxy response) {
                    surfers.put(name, response);
                }
            });

            // Restart domain context
            DomainContext.requestFinalize();
            DomainContext.requestBegin();
        }

    }

    /**
     * Asegura que se devuelvan los spots creados, y que estos contengan la localizacion. Sirve de
     * ejemplo de como obtener proxies anidados.
     */
    @Test
    public void testFindSpots() {

        SpotRequest request = factory.spotRequest();
        request.findAllSpots().with("location").fire(new Receiver<List<SpotProxy>>() {
            @Override
            public void onSuccess(List<SpotProxy> result) {
                List<SpotProxy> spots = result;
                assertEquals(4, spots.size());
                assertNotNull(result.get(0).getLocation());
            }
        });
    }

    //@Test
    public void testNewHotSpot() {

        for (final String name : surfers.keySet()) {
            // Usuario valido
            helper.setEnvEmail(name + "@gmail.com");
            double lat = locations[0].getLatitude();
            double lon = locations[0].getLongitude();

            SurferRequest request = factory.surferRequest();
            request.updateSurferLocation(lat, lon).fire(new Receiver<SessionProxy>() {
                public void onFailure(ServerFailure error) {
                    fail(error.getMessage());
                }

                public void onSuccess(SessionProxy response) {
                    assertNotNull(response);
                }
            });

            // Restart domain context
            DomainContext.requestFinalize();
            DomainContext.requestBegin();
        }

        SpotRequest sr = factory.spotRequest();
        sr.findAllSpots().fire(new Receiver<List<SpotProxy>>() {
            @Override
            public void onSuccess(List<SpotProxy> response) {
                // solo por asegurar, ¿estan todos los spots?
                assertEquals(locations.length, response.size());
                for (SpotProxy spotProxy : response) {
                    if (spotProxy.getName().equals("malaga")) {
                        // En malaga deben de estar todos y ser hotspot!
                        assertEquals(surfers.size(), spotProxy.getSurferCurrentCount().intValue());
                        assertTrue(spotProxy.getHot());
                    } else {
                        // En el resto no debe haber nadie y no ser hotspot!
                        assertEquals(0, spotProxy.getSurferCurrentCount().intValue());
                        assertFalse(spotProxy.getHot());
                    }
                }

            }
        });

    }

}
