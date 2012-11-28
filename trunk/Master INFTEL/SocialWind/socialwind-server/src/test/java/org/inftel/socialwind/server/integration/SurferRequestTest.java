package org.inftel.socialwind.server.integration;

import static org.inftel.socialwind.server.RequestFactoryHelper.createSimpleFactory;
import static org.inftel.socialwind.server.test.Utils.randomString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.inftel.socialwind.server.domain.DomainContext;
import org.inftel.socialwind.shared.domain.SurferProxy;
import org.inftel.socialwind.shared.services.SocialwindRequestFactory;
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
public class SurferRequestTest {

    // private static SocialwindRequestFactory factory;
    // private static SurferRequest service;

    /** Helper para AppEngine Local Services **/
    static LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig());

    @BeforeClass
    public static void setUpClass() throws Exception {
        helper.setUp();
        DomainContext.initialize();

        // Factoría y servicios con back-end simulado
        // factory = RequestFactoryHelper.create(SocialwindRequestFactory.class);
        // service = RequestFactoryHelper.getService(SurferRequest.class);
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

    @Test(expected = Exception.class)
    public void testSurferRequestCurrentUserAuthenticationException() {
        SocialwindRequestFactory swrf = createSimpleFactory(SocialwindRequestFactory.class);

        SurferRequest request = swrf.surferRequest();
        request.currentSurfer().fire(new Receiver<SurferProxy>() {
            @Override
            public void onSuccess(SurferProxy response) {
                fail("Debe fallar por usuario no atenticado");
            }
        });
    }

    @Test
    public void testSurferRequestCurrentUser() {
        SocialwindRequestFactory swrf = createSimpleFactory(SocialwindRequestFactory.class);

        // Usuario valido
        final String email = randomString() + "@gmail.com";
        helper.setEnvIsLoggedIn(true);
        helper.setEnvAuthDomain("google.com");
        helper.setEnvEmail(email);

        SurferRequest request = swrf.surferRequest();
        final List<SurferProxy> container = new ArrayList<SurferProxy>();
        request.currentSurfer().fire(new Receiver<SurferProxy>() {
            @Override
            public void onSuccess(SurferProxy response) {
                assertNotNull(response);
                container.add(response);
            }

            @Override
            public void onFailure(ServerFailure error) {
                fail(error.getMessage());
            }
        });
        final SurferProxy surfer = container.get(0);

        assertNotNull(surfer);
        assertEquals(email, surfer.getEmail());

        request = swrf.surferRequest();

        // Intento de buscar la entidad guardada
        request.find(surfer.stableId()).fire(new Receiver<SurferProxy>() {
            @Override
            public void onSuccess(SurferProxy response) {
                assertNotNull(response);
                assertEquals(email, response.getEmail());
                assertEquals(surfer.stableId(), response.stableId());
            }
        });

        request = swrf.surferRequest();

        // Intento de buscar todos los surfers del sistema
        request.findAllSurfers().fire(new Receiver<List<SurferProxy>>() {
            @Override
            public void onSuccess(List<SurferProxy> response) {
                assertTrue(response.size() > 0);
            }
        });

    }

    @Test
    public void testSurferEdit() {
        SocialwindRequestFactory swrf = createSimpleFactory(SocialwindRequestFactory.class);

        // Usuario valido
        final String email = randomString() + "@gmail.com";
        helper.setEnvIsLoggedIn(true);
        helper.setEnvAuthDomain("google.com");
        helper.setEnvEmail(email);

        SurferRequest request = swrf.surferRequest();
        final List<SurferProxy> container = new ArrayList<SurferProxy>();
        request.currentSurfer().fire(new Receiver<SurferProxy>() {
            @Override
            public void onSuccess(SurferProxy response) {
                container.add(response);
            }
        });
        SurferProxy surfer = container.get(0);

        DomainContext.requestFinalize();
        DomainContext.requestBegin();

        request = swrf.surferRequest();
        surfer = request.edit(surfer);
        final String displayName = randomString() + "_modified";
        surfer.setDisplayName(displayName);
        request.fire();

        DomainContext.requestFinalize();
        DomainContext.requestBegin();

        // Comprobar que se ha guardado
        request = swrf.surferRequest();
        request.currentSurfer().fire(new Receiver<SurferProxy>() {
            @Override
            public void onSuccess(SurferProxy response) {
                assertEquals(displayName, response.getDisplayName());
            }
        });

    }

    /**
     * TODO Intneto de sumulacion del back-end
     */
    @Test
    public void testCountSurfers() {
        // Crear resultados del back-end
        // Long count = Long.valueOf(42);
        // Instrumentar la simulacion del back-end
        // Mockito.when(service.countSurfers()).thenReturn(count);
    }

    @Test
    public void testCurrentUser() {
        helper.setEnvIsLoggedIn(true);
        helper.setEnvAuthDomain("google.com");
        helper.setEnvEmail("test@gmail.com");

        SocialwindRequestFactory swrf = createSimpleFactory(SocialwindRequestFactory.class);
        SurferRequest request = swrf.surferRequest();

        final List<SurferProxy> container = new ArrayList<SurferProxy>();
        request.currentSurfer().fire(new Receiver<SurferProxy>() {
            @Override
            public void onSuccess(SurferProxy response) {
                container.add(response);
            }
        });
    }

}
