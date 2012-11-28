/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pasosServer.model.Alarma;
import pasosServer.model.Protegido;

/**
 *
 * @author Juan Antonio
 */
@Stateless
public class AlarmaFacade extends AbstractFacade<Alarma> implements AlarmaFacadeRemote {
    @PersistenceContext(unitName = "PasosServerEnterpriseApplication-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public AlarmaFacade() {
        super(Alarma.class);
    }
    
    @Override
    public List<Alarma> findAlarmasByIdProtegido(Protegido protegido){
        return em.createQuery("select al from Alarma al where al.idProtegido=:protegido")
                .setParameter("protegido", protegido)
                .getResultList();
                
    }
    
    @Override
    public List findAlarmasGroupByMonth(){
        return em.createNativeQuery("select count(*) as cont, extract(month from al.fecha_hora) "
                                    + "from alarma al "
                                    + "group by extract(month from al.fecha_hora)"
                                    + "order by extract(month from al.fecha_hora)")
                .getResultList();
    }
    
    @Override
    public List findAlarmasGroupByMonth(String anio){
        return em.createNativeQuery("select count(*) as cont, extract(month from al.fecha_hora) "
                                    + "from alarma al "
                                    + "where extract(year from al.fecha_hora)="+anio
                                    + "group by extract(month from al.fecha_hora)"
                                    + "order by extract(month from al.fecha_hora)")
                .getResultList();
    }

  
    
}
