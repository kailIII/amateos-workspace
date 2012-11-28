/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.ejb;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.inftel.scrum.model.Log;
import org.inftel.scrum.model.Proyecto;

/**
 *
 * @author migueqm
 */
@Stateless
public class LogFacade extends AbstractFacade<Log> {

    private final static Logger LOGGER = Logger.getLogger(LogFacade.class.getName());
    @PersistenceContext(unitName = "ProyectoScrum-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LogFacade() {
        super(Log.class);
    }

    public List<Log> findByProyecto(Proyecto proyecto) {
        LOGGER.info("BUSCANDO LOGS PARA PROYECTO -> " + proyecto);
        try {
            List<Log> logs = (List<Log>) em.createQuery("SELECT l FROM Log l WHERE l.proyectoId = :proyecto").setParameter("proyecto", proyecto).getResultList();
            LOGGER.info("ENCONTRADOS LOGS -> "+logs);
            return logs;
        } catch (Exception e) {
            return null;
        }
    }

    public BigDecimal maxId() {
        try {
            return (BigDecimal) em.createQuery("SELECT MAX(lo.id) FROM Log lo").getSingleResult();
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }

    }
}
