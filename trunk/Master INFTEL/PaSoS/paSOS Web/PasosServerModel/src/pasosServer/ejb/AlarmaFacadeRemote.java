/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.ejb;

import java.util.List;
import javax.ejb.Remote;
import pasosServer.model.Alarma;
import pasosServer.model.Protegido;

/**
 *
 * @author Juan Antonio
 */
@Remote
public interface AlarmaFacadeRemote {

    void create(Alarma alarma);

    void edit(Alarma alarma);

    void remove(Alarma alarma);

    Alarma find(Object id);

    List<Alarma> findAll();

    List<Alarma> findRange(int[] range);

    int count();

    public List<Alarma> findAlarmasByIdProtegido(Protegido idprotegido);

    public List findAlarmasGroupByMonth();

    public List findAlarmasGroupByMonth(String anio);

}
