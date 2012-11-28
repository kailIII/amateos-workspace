/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pasosServer.model.Protegido;

/**
 *
 * @author Juan Antonio
 */
@Stateless
public class ProtegidoFacade extends AbstractFacade<Protegido> implements ProtegidoFacadeRemote {
    @PersistenceContext(unitName = "PasosServerEnterpriseApplication-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProtegidoFacade() {
        super(Protegido.class);
    }
    
    
    @Override
    public Protegido findProtegidoByNombreAndApellidos(String nombre, String apellidos){
        return (Protegido) em.createQuery("select p from Protegido p where p.nombre=:nombre and p.apellidos=:apellidos")
                .setParameter("nombre", nombre)
                .setParameter("apellidos", apellidos)
                .setMaxResults(1)
                .getSingleResult();
    }
    @Override
    public Protegido findByimei(String imei){
        return (Protegido) em.createQuery("SELECT p FROM Protegido p WHERE p.imei=:imei")
                .setParameter("imei", imei)
                .setMaxResults(1)
                .getSingleResult();
    }
    @Override
    public void updateProtegido(Protegido protegido){
       em.merge(protegido);             
    }    
    
}
