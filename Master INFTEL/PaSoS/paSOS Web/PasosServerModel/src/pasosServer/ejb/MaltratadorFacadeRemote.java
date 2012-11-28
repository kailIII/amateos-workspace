/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.ejb;

import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Remote;
import pasosServer.model.Maltratador;

/**
 *
 * @author Juan Antonio
 */
@Remote
public interface MaltratadorFacadeRemote {

    void create(Maltratador maltratador);

    void edit(Maltratador maltratador);

    void remove(Maltratador maltratador);

    Maltratador find(Object id);

    List<Maltratador> findAll();

    List<Maltratador> findRange(int[] range);

    int count();

    public pasosServer.model.Maltratador findByimei(java.lang.String imei);   
    
    public void createMaltratador(Maltratador maltratador, BigDecimal idProtegido);
}
