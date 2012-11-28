/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pasosServer.model.CentroAyuda;

/**
 *
 * @author Juan Antonio
 */
@Stateless
public class CentroAyudaFacade extends AbstractFacade<CentroAyuda> implements CentroAyudaFacadeRemote {
    @PersistenceContext(unitName = "PasosServerEnterpriseApplication-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public CentroAyudaFacade() {
        super(CentroAyuda.class);
    }
    
}
