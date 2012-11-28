/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.ejb;

import java.util.List;
import javax.ejb.Remote;
import pasosServer.model.CentroAyuda;

/**
 *
 * @author Juan Antonio
 */
@Remote
public interface CentroAyudaFacadeRemote {

    void create(CentroAyuda centroAyuda);

    void edit(CentroAyuda centroAyuda);

    void remove(CentroAyuda centroAyuda);

    CentroAyuda find(Object id);

    List<CentroAyuda> findAll();

    List<CentroAyuda> findRange(int[] range);

    int count();
    
}
