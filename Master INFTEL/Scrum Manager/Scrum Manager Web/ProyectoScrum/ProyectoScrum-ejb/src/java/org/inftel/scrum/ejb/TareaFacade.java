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
import org.inftel.scrum.model.Tarea;
import org.inftel.scrum.model.Usuario;

/**
 *
 * @author migueqm
 */
@Stateless
public class TareaFacade extends AbstractFacade<Tarea> {

    @PersistenceContext(unitName = "ProyectoScrum-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TareaFacade() {
        super(Tarea.class);
    }

    public List<Tarea> findByName(String id) {
        try {
            return em.createQuery("SELECT t FROM Tarea t WHERE t.descripcion LIKE :id").setParameter("id", id).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
     public Tarea findByOneName(String id) {
        try {
            return (Tarea) em.createQuery("SELECT t FROM Tarea t WHERE t.descripcion LIKE :id").setParameter("id", id).getResultList().get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Tarea> findByUser(Usuario u) {
        try {
            return em.createNamedQuery("Tarea.findByUser").setParameter("usuario", u).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Tarea> findTodo() {
        return em.createNamedQuery("Tarea.findByDone").setParameter("done", '0').getResultList();
    }

    public List<Tarea> findDoing() {
        return em.createNamedQuery("Tarea.findByDone").setParameter("done", '1').getResultList();
    }

    public List<Tarea> findDone() {
        return em.createNamedQuery("Tarea.findByDone").setParameter("done", '2').getResultList();
    }
    
    public void create(Tarea entity) {
        em.persist(entity);
    }
    
    public BigDecimal maxId() {
        try {
            return (BigDecimal) em.createQuery("SELECT MAX(t.id) FROM Tarea t").getSingleResult() ;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
    
    public long findByTareaRepetida(String descripcion, Proyecto proyectoId) {
          try{
            Long c = (Long) em.createQuery("SELECT count(t.descripcion) FROM Tarea t WHERE t.descripcion = :descripcion AND t.proyectoId = :proyectoId")
                                    .setParameter("descripcion", descripcion)
                                    .setParameter("proyectoId", proyectoId)
                                    .getSingleResult();
            return c;
          }
          catch(Exception e){
            return -1;
          }
    }
    
    public Tarea findById(BigDecimal id) {
        try {
            return (Tarea) em.createQuery("SELECT t FROM Tarea t WHERE t.id = :id").setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
