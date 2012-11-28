/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.ejb;

import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.inftel.scrum.model.Reuniones;

/**
 *
 * @author Fernando
 */
@Stateless
public class ReunionesFacade extends AbstractFacade<Reuniones> {
    @PersistenceContext(unitName = "ProyectoScrum-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReunionesFacade() {
        super(Reuniones.class);
    }

    public BigDecimal maxId() {
         try {
            BigDecimal id = (BigDecimal) em.createQuery("SELECT MAX(r.id) FROM Reuniones r").getSingleResult();
            if(id==null){
                id = BigDecimal.ZERO;
            }
            return id;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
    
}
