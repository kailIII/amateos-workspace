/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.inftel.scrum.model.Sprint;

/**
 *
 * @author Fernando
 */
@Stateless
public class SprintFacade extends AbstractFacade<Sprint> {
    @PersistenceContext(unitName = "ProyectoScrumMovil-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SprintFacade() {
        super(Sprint.class);
    }
    
}
