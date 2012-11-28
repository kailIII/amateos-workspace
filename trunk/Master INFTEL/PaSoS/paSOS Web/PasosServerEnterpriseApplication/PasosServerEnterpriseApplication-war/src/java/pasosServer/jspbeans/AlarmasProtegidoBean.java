/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.jspbeans;

import java.util.List;
import pasosServer.model.Alarma;

/**
 *
 * @author Juan Antonio
 */
public class AlarmasProtegidoBean {

    private List<Alarma> alarmas;

    /**
     * @return the alarmas
     */
    public List<Alarma> getAlarmas() {
        return alarmas;
    }

    /**
     * @param alarmas the alarmas to set
     */
    public void setAlarmas(List<Alarma> alarmas) {
        this.alarmas = alarmas;
    }
}
