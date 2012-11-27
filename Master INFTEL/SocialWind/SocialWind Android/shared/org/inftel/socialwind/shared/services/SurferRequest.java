package org.inftel.socialwind.shared.services;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

import org.inftel.socialwind.shared.domain.SessionProxy;
import org.inftel.socialwind.shared.domain.SurferProxy;

import java.util.List;

/**
 * Permite obtener y gestionar los surferos registrados en el sistema.
 * 
 * @author ibaca
 * 
 */
@ServiceName("org.inftel.socialwind.services.SurferService")
public interface SurferRequest extends RequestContext {

    /**
     * Devuelve el numero de surferos registrado en el sistema.
     * 
     * @return un {@link Long} que representa el numero de surferos registados
     */
    Request<Long> countSurfers();

    /**
     * Devuelve el surfero que representa al usuario actual. El sistema obliga a estar registrado
     * para realizar cualquier peticion, por tanto siempre se devolvera un surfero valido. En caso
     * de llamar alta de usuario, se genera un surfero con los datos minimos.
     * 
     * @return surfero asociado a la cuenta de usuario actual
     */
    Request<SurferProxy> currentSurfer();

    /**
     * Devuelve la lista de todos los surferos registrados en el sistema.
     * 
     * @return lista de surferos
     */
    Request<List<SurferProxy>> findAllSurfers();

    /**
     * Busca la lista de sesiones del surfero que representa el usuario actual.
     * 
     * @return lista de sesiones del usuario actual
     */
    Request<List<SessionProxy>> findSessions();

    /**
     * Busca el listado de sesiones del surfero pasado como parametro.
     * 
     * @param surfer
     *            surfero del que se quieren obtener las sesiones
     * @return lista de sesiones del surfero
     */
    // TODO por ahora se queda comentado por
    // - si se quieren obtener sesiones, quizas seria mejor obtenerals desde su servicio
    // - obtener sesiones de usuarios diferentes al actual es inseguro!
    // Encambio, el metodo findSessions si es correcto por estar directamente relacionado con surfer
    // Request<List<SessionProxy>> findSessionsBySurfer(SurferProxy surfer);

    // El id de surfero no sale del server!
    // Request<SurferProxy> findSurfer(Long id);

    /**
     * Devuele la lista de todos los surferos pudiendo separarlas tramos.
     * 
     * @param firstResult
     *            numero del primer surfero que sera devuelto
     * @param maxResults
     *            nnumero total de surferos devueltos
     * @return lista de surferos empezando en <code>firstResult</code> y contieniendo
     *         <code>maxResults</code> elementos
     */
    Request<List<SurferProxy>> findSurferEntries(int firstResult, int maxResults);

    /**
     * Actualiza la posición del surfero, en caso de crear una nueva sesion debido a que el surfero
     * se haya movido a una playa, se devolverá la nueva sesion creada. En caso de que el surfero
     * este en una playa, se devolvera nulo tambien.
     * 
     * @param latitude
     * @param longitude
     * @return la nueva sesion creada, null en caso de no haber creaado una nueva sesion
     * 
     */
    Request<SessionProxy> updateSurferLocation(double latitude, double longitude);

}
