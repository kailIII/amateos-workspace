package org.inftel.socialwind.server.domain;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import static org.inftel.socialwind.server.test.Utils.randomString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.inftel.socialwind.server.domain.Session;
import org.inftel.socialwind.server.domain.Spot;
import org.inftel.socialwind.server.domain.Surfer;
import org.inftel.socialwind.server.domain.DomainContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;

import javax.persistence.EntityManager;

/**
 * Test para probar que la definicion de las entidades JPA es correcta.
 * 
 * @author ibaca
 * 
 */
public class PersistenceTestCase {

    /** Helper para AppEngine Local Services **/
    static LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig());

    @BeforeClass
    public static void setUpClass() throws Exception {
        helper.setUp();
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
    public void testSpotCreationAndFind() {
        EntityManager em = DomainContext.get();

        // Spot Creation
        Spot createdSpot = new Spot();
        String randomName = randomString();
        createdSpot.setName(randomName);

        // Spot save
        em.persist(createdSpot);
        em.refresh(createdSpot);

        // Spot Find
        Spot findedSpot = em.find(Spot.class, createdSpot.getId());
        assertEquals(createdSpot.getName(), findedSpot.getName());
    }

    @Test
    public void testSurferCreationAndFind() {
        EntityManager em = DomainContext.get();

        // Spot Creation
        Surfer createdSurfer = new Surfer();
        String randomName = randomString();
        createdSurfer.setDisplayName(randomName);

        // Spot save
        em.persist(createdSurfer);
        em.refresh(createdSurfer);

        // Spot Find
        Surfer findedSpot = em.find(Surfer.class, createdSurfer.getId());
        assertEquals(createdSurfer.getDisplayName(), findedSpot.getDisplayName());
    }

    @Test
    public void testSurferVersioning() {
        EntityManager em = DomainContext.get();

        // Spot Creation
        Surfer surfer = new Surfer();
        String randomName = randomString();
        surfer.setDisplayName(randomName);
        surfer.setFullName("first_name");

        // First save
        em.getTransaction().begin();
        Surfer createdSurfer = em.merge(surfer);
        em.getTransaction().commit();

        // Second save
        em.getTransaction().begin();
        surfer.setFullName("second_name");
        Surfer mergedSurfer = em.merge(surfer);
        em.getTransaction().commit();

        // Check version diff
        assertNotSame(createdSurfer.getVersion(), mergedSurfer.getVersion());
    }

    @Test
    public void testSurferAutoDates() {
        EntityManager em = DomainContext.get();

        // Spot Creation
        Surfer surfer = new Surfer();
        String randomName = randomString();
        surfer.setDisplayName(randomName);

        // First save
        em.getTransaction().begin();
        Surfer createdSurfer = em.merge(surfer);
        em.getTransaction().commit();

        Date firstCreated = createdSurfer.getCreated();
        assertNotNull(firstCreated);
        Date firstUpdated = createdSurfer.getUpdated();
        assertNotNull(firstUpdated);

        Date secondReference = new Date();

        // Second save
        em.getTransaction().begin();
        surfer.setFullName("second_name");
        Surfer mergedSurfer = em.merge(surfer);
        em.getTransaction().commit();

        Date secondCreated = mergedSurfer.getCreated();
        assertNotNull(secondCreated);
        Date secondUpdated = mergedSurfer.getUpdated();
        assertNotNull(secondUpdated);

        // Check dates diff
        assertEquals(firstCreated, secondCreated);
        assertNotSame(firstUpdated, secondUpdated);
        assertTrue(secondReference.before(secondUpdated));
    }

    @Test
    public void testSurferAutoDating() {
        EntityManager em = DomainContext.get();

        // Spot Creation
        Surfer surfer = new Surfer();
        String randomName = randomString();
        surfer.setDisplayName(randomName);
        surfer.setFullName("first_name");

        // First save
        em.getTransaction().begin();
        Surfer createdSurfer = em.merge(surfer);
        em.getTransaction().commit();

        // Check initial values
        assertNotNull(createdSurfer.getCreated());
        assertNotNull(createdSurfer.getUpdated());
        assertTrue(createdSurfer.getCreated().equals(createdSurfer.getUpdated()));

        // Second save
        em.getTransaction().begin();
        surfer.setFullName("second_name");
        Surfer updatedSurfer = em.merge(surfer);
        em.getTransaction().commit();

        // Check Updated
        assertFalse(createdSurfer.getUpdated().equals(updatedSurfer.getUpdated()));
    }

    @Test
    public void testSession() {
        EntityManager em = DomainContext.get();

        // Crar un surfer y una playa
        Surfer surfer = new Surfer();
        Spot spot = new Spot();

        em.getTransaction().begin();
        em.persist(surfer);
        em.getTransaction().commit();

        em.getTransaction().begin();
        em.persist(spot);
        em.getTransaction().commit();

        // Crear la sesion asociada
        Session session = new Session();
        session.setSpot(spot);
        session.setSurfer(surfer);

        em.getTransaction().begin();
        em.persist(session);
        em.getTransaction().commit();

        // Comprobar resultados
        Session result = em.find(Session.class, session.getId());
        assertEquals(surfer.getId(), result.getSurferId());
        // assertNotNull(result.getSurfer());
        assertEquals(spot.getId(), result.getSpotId());
        // assertNotNull(result.getSpot());
    }
}
