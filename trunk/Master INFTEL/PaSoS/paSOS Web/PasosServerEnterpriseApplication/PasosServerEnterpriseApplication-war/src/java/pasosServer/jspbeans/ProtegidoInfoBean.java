/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.jspbeans;

import java.awt.Image;
import pasosServer.model.Protegido;

/**
 *
 * @author Juan Antonio
 */
public class ProtegidoInfoBean {
    private Protegido protegido;
    
    public ProtegidoInfoBean(){
        
    }

    /**
     * @return the protegido
     */
    public Protegido getProtegido() {
        return protegido;
    }

    /**
     * @param protegido the protegido to set
     */
    public void setProtegido(Protegido protegido) {
        this.protegido = protegido;
    }


}
