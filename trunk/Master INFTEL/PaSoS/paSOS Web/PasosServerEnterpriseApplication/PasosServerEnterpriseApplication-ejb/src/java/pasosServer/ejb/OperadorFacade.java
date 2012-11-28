/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pasosServer.model.Operador;

/**
 *
 * @author Juan Antonio
 */
@Stateless
public class OperadorFacade extends AbstractFacade<Operador> implements OperadorFacadeRemote {
    @PersistenceContext(unitName = "PasosServerEnterpriseApplication-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public OperadorFacade() {
        super(Operador.class);
    }

    @Override
    public Operador findByLoginAndPassword(String login, String pass) {
        return (Operador) em.createQuery("SELECT o FROM Operador o WHERE o.login = :login AND o.password = :pass")
                .setParameter("login", login).setParameter("pass", pass)
                .getSingleResult();
    }
    
}
