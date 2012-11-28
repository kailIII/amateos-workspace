/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.ejb;

import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.inftel.scrum.model.Historico;

/**
 *
 * @author empollica
 */
@Stateless
public class HistoricoFacade extends AbstractFacade<Historico> {
    @PersistenceContext(unitName = "ProyectoScrum-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public HistoricoFacade() {
        super(Historico.class);
    }

    public BigDecimal maxId() {
        try {
            return (BigDecimal) em.createQuery("SELECT MAX(h.id) FROM Historico h").getSingleResult() ;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
