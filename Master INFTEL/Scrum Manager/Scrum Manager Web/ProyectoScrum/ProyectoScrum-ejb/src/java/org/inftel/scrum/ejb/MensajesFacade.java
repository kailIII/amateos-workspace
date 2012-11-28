/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.ejb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.inftel.scrum.model.Mensajes;
import org.inftel.scrum.model.Usuario;

/**
 *
 * @author eumigue
 */
@Stateless
public class MensajesFacade extends AbstractFacade<Mensajes> {

    private final static Logger LOGGER = Logger.getLogger(MensajesFacade.class.getName());
    @PersistenceContext(unitName = "ProyectoScrum-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MensajesFacade() {
        super(Mensajes.class);
    }

    public BigDecimal maxId() {
        try {
            BigDecimal id = (BigDecimal) em.createQuery("SELECT MAX(m.id) FROM Mensajes m").getSingleResult();
            if(id==null){
                id = BigDecimal.ZERO;
            }
            return id;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    public List<Mensajes> findEnviadosByUsuario(Usuario usuario) {
        try {
            return (List<Mensajes>) em.createQuery("SELECT m FROM Mensajes m WHERE m.origen=:usuario ORDER BY m.fecha DESC").setParameter("usuario", usuario).getResultList();
        } catch (Exception e) {
            return new ArrayList<Mensajes>();
        }
    }

    public List<Mensajes> findRecibidosByUsuario(Usuario usuario) {
        try {
            return (List<Mensajes>) em.createQuery("SELECT m FROM Mensajes m WHERE m.destino=:usuario ORDER BY m.fecha DESC").setParameter("usuario", usuario).getResultList();
        } catch (Exception e) {
            return new ArrayList<Mensajes>();
        }
    }
    
    public Mensajes findMensajeById(String iden) {
        try {
            return (Mensajes) em.createQuery("SELECT m FROM Mensajes m WHERE m.id=:iden").setParameter("iden",new BigDecimal (iden)).getSingleResult();
        } catch (Exception e) {
            return new Mensajes();
        }
    }
    
    public long findNumeroMensajesNuevosByUsuario(Usuario usuario) {
        try {
            return (Long) em.createQuery("SELECT COUNT(m.id) FROM Mensajes m WHERE m.destino=:usuario AND m.leido='0'").setParameter("usuario", usuario).getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }
}