/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.ejb;

import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pasosServer.model.Maltratador;
import pasosServer.model.Protegido;

/**
 *
 * @author Juan Antonio
 */
@Stateless
public class MaltratadorFacade extends AbstractFacade<Maltratador> implements MaltratadorFacadeRemote {
    @EJB
    private ProtegidoFacadeRemote protegidoFacade;
    @PersistenceContext(unitName = "PasosServerEnterpriseApplication-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public MaltratadorFacade() {
        super(Maltratador.class);
    }
    
   
    @Override
    public Maltratador findByimei(String imei){
        return (Maltratador) em.createQuery("SELECT m FROM Maltratador m WHERE m.imei = :imei")
                .setParameter("imei", imei)
                .getSingleResult();
    }  
    
    @Override
     public void createMaltratador(Maltratador maltratador, BigDecimal idProtegido){
        
        Protegido p = this.protegidoFacade.find(idProtegido);
        em.persist(maltratador);
        maltratador.setIdProtegido(p);
        
    }
}
