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
import org.inftel.scrum.model.Grupo;
import org.inftel.scrum.model.Usuario;

/**
 *
 * @author Fernando
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {
    @PersistenceContext(unitName = "ProyectoScrumMovil-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }
    
    public Usuario findByName(String id) {
        try {
            return (Usuario) em.createQuery("SELECT u FROM Usuario u WHERE u.nombre LIKE :id").setParameter("id", id).getResultList().get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public Usuario findByMail(String id) {
        try {
            Usuario u=(Usuario) em.createQuery("SELECT u FROM Usuario u WHERE u.email LIKE :id").setParameter("id", id).getResultList().get(0);
            return u;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Usuario> findByGrupo(Grupo grupo) {
        try {
            return (List<Usuario>) em.createQuery("SELECT u FROM Usuario u WHERE u.grupoId = :grupo").setParameter("grupo", grupo).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    
    public BigDecimal maxId() {
        try {
            return (BigDecimal) em.createQuery("SELECT MAX(u.id) FROM Usuario u").getSingleResult();
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }

    }
    public List<Usuario> findByGrupoNull() {
        try {
            return (List<Usuario>) em.createQuery("SELECT u FROM Usuario u WHERE u.grupoId IS NULL").getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
