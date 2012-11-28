/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.ejb;

import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.inftel.scrum.model.Proyecto;

/**
 *
 * @author Fernando
 */
@Stateless
public class ProyectoFacade extends AbstractFacade<Proyecto> {

    @PersistenceContext(unitName = "ProyectoScrum-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProyectoFacade() {
        super(Proyecto.class);
    }

    public Proyecto findByNombre(String nombre) {
        try {
            return (Proyecto) em.createQuery("SELECT p FROM Proyecto p WHERE p.nombre = :nombre").setParameter("nombre", nombre).getSingleResult();
        } catch (Exception e) {
            return null;
        }
//        return getEntityManager().find(entityClass, id);
    }
    
    public BigDecimal maxId() {
        try {
            return (BigDecimal) em.createQuery("SELECT MAX(p.id) FROM Proyecto p").getSingleResult() ;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
    
    public long findByProyectoRepetido(String nombre) {
          try{
            return (Long) em.createQuery("SELECT count(p.nombre) FROM Proyecto p WHERE p.nombre = :nombre")
                                    .setParameter("nombre", nombre)
                                    .getSingleResult();
          }
          catch(Exception e){
             
            return -1;
          }
    }
    
    public Proyecto findProyectoById(BigDecimal id){
        try {
            return (Proyecto) em.createQuery("SELECT p FROM Proyecto p WHERE p.id = :id").setParameter("id",id).getSingleResult() ;
        } catch (Exception e) {
            return null;
        }
    }
    
}
