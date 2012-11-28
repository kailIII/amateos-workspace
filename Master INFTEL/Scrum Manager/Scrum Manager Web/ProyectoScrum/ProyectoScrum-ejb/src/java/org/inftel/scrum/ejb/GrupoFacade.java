/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.ejb;

import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import org.inftel.scrum.model.Grupo;

/**
 *
 * @author migueqm
 */
@Stateless
public class GrupoFacade extends AbstractFacade<Grupo> {
    @PersistenceContext(unitName = "ProyectoScrum-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GrupoFacade() {
        super(Grupo.class);
    }
    public List<Grupo> findByGrupoId(String id) {
        
            
             List<Grupo> lista=(List<Grupo>) em.createQuery("SELECT g FROM Grupo g WHERE g.id LIKE :id")
                                    .setParameter("id", id)
                                    .getResultList();
             return lista;
             
        
    }
    public BigDecimal maxId() {
        try {
            return (BigDecimal) em.createQuery("SELECT MAX(g.id) FROM Grupo g").getSingleResult() ;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }

    }
      public long findByGrupoRepetido(String nombre) {

          try{
            return (Long) em.createQuery("SELECT count(g.nombre) FROM Grupo g WHERE g.nombre = :nombre")
                                    .setParameter("nombre", nombre)
                                    .getSingleResult();
          }
          catch(Exception e){
             
            return -1;
          }


    }
    
}
