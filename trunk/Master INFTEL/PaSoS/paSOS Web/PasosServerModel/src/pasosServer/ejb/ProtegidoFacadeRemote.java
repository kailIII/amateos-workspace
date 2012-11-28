/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.ejb;

import java.util.List;
import javax.ejb.Remote;
import pasosServer.model.Protegido;

/**
 *
 * @author Juan Antonio
 */
@Remote
public interface ProtegidoFacadeRemote {

    void create(Protegido protegido);

    void edit(Protegido protegido);

    void remove(Protegido protegido);

    Protegido find(Object id);

    List<Protegido> findAll();

    List<Protegido> findRange(int[] range);

    int count();

    public pasosServer.model.Protegido findProtegidoByNombreAndApellidos(java.lang.String nombre, java.lang.String apellidos);

    public Protegido findByimei(String imei);

    public void updateProtegido(pasosServer.model.Protegido protegido);
    
}
