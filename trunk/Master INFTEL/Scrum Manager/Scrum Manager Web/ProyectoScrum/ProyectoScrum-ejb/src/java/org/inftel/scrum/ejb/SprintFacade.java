/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.ejb;

import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.inftel.scrum.model.Proyecto;
import org.inftel.scrum.model.Sprint;

/**
 *
 * @author Fernando
 */
@Stateless
public class SprintFacade extends AbstractFacade<Sprint> {

    @PersistenceContext(unitName = "ProyectoScrum-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SprintFacade() {
        super(Sprint.class);
    }

    public Sprint find(BigDecimal id) {
        return (Sprint) em.createQuery("SELECT s FROM Sprint s WHERE s.id = :id").setParameter("id", id).getResultList().get(0);
    }
    
    public List<Sprint> findByProyecto(Proyecto proyecto) {
        try{
            return (List<Sprint>) em.createQuery("SELECT s FROM Sprint s WHERE s.proyectoId = :proyecto").setParameter("proyecto", proyecto).getResultList();
        }catch(Exception e){
            return null;
        }
    }

    public BigDecimal maxId() {
        try {
            BigDecimal id = (BigDecimal) em.createQuery("SELECT MAX(s.id) FROM Sprint s").getSingleResult();
            return id;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
