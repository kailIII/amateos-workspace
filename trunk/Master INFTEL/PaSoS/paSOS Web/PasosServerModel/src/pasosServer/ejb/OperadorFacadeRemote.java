/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.ejb;

import java.util.List;
import javax.ejb.Remote;
import pasosServer.model.Operador;

/**
 *
 * @author Juan Antonio
 */
@Remote
public interface OperadorFacadeRemote {

    void create(Operador operador);

    void edit(Operador operador);

    void remove(Operador operador);

    Operador find(Object id);
    
    Operador findByLoginAndPassword(String login, String pass);
    
    List<Operador> findAll();

    List<Operador> findRange(int[] range);

    int count();
    
}
