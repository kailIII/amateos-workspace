/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.ejb;

import java.util.List;
import javax.ejb.Remote;
import pasosServer.model.Localizacion;

/**
 *
 * @author Juan Antonio
 */
@Remote
public interface LocalizacionFacadeRemote {

    void create(Localizacion localizacion);

    void edit(Localizacion localizacion);

    void remove(Localizacion localizacion);

    Localizacion find(Object id);

    List<Localizacion> findAll();

    List<Localizacion> findRange(int[] range);

    int count();
    
}
