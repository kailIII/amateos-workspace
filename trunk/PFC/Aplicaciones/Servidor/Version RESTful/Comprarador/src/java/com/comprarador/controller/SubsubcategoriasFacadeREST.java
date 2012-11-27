/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comprarador.controller;

import com.comprarador.entities.Subsubcategorias;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author albertomateos
 */
@Stateless
@Path("subsubcategorias")
public class SubsubcategoriasFacadeREST extends AbstractFacade<Subsubcategorias> {
    
    private final static Logger LOGGER = Logger.getLogger(SubsubcategoriasFacadeREST.class .getName());

    @PersistenceContext(unitName = "iCompraPU")
    private EntityManager em;

    public SubsubcategoriasFacadeREST() {
        super(Subsubcategorias.class);
    }

    @POST
    @Consumes({"application/xml"})
    @Produces({"text/plain"})
    public String crear(Subsubcategorias entity) {
        
        Subsubcategorias ssc = null;

        // Se busca verifica si el producto se encuentra en la base de datos
        try {
            ssc = (Subsubcategorias) getEntityManager().createQuery("SELECT ssc FROM Subsubcategorias ssc WHERE ssc.nombre = :nombre AND ssc.categoria = :categoria AND ssc.subcategoria = :subcategoria").setParameter("nombre", entity.getNombre()).setParameter("categoria", entity.getCategoria()).setParameter("subcategoria", entity.getSubcategoria()).getSingleResult();
        } catch (Exception e) {
        }


        if (ssc != null && entity.getNombre().equals(ssc.getNombre()) && entity.getCategoria()==ssc.getCategoria() && entity.getSubcategoria()==ssc.getSubcategoria()) {

            // LA CATEGORIA YA SE ENCUENTRA EN LA BASE DE DATOS
            if (ssc.getPosicion() != entity.getPosicion()) { // LA POSICIÓN ES DIFERENTE -> ACTUALIZAR
                ssc.setPosicion(entity.getPosicion());
                super.edit(ssc);

                LOGGER.info("SUBSUBCATEGORIAS - POST: actualizada posición de subsubcategoría");
                return "actualizada posicion";
            }else{
                LOGGER.info("SUBSUBCATEGORIAS - POST: subsubcategoría no modificada");
                return "no modificada";
            }

        } else {

            // LA CATEGORIA NO SE ENCUENTRA EN LA BASE DE DATOS -> AGREGAR A BASE DE DATOS
            super.create(entity);
            LOGGER.info("SUBSUBCATEGORIAS - POST: insertada nueva subsubcategoría");
            return "insertada";
        }
    }
    
    @GET
    @Path("{cat}/{subcat}")
    @Produces({"application/xml"})
    public List<Subsubcategorias> findBySubcategoria(@PathParam("cat") Integer cat, @PathParam("subcat") Integer subcat) {
        
        LOGGER.info("SUBSUBCATEGORIAS - GET: subsubcategorías de categoría "+cat+" & subcategoría "+subcat);

        try {
            return getEntityManager().createQuery("SELECT s FROM Subsubcategorias s WHERE s.categoria = :cat AND s.subcategoria = :subcat").setParameter("cat", cat).setParameter("subcat", subcat).getResultList();
        } catch (Exception e) {
            return null;
        }    
    }
    

    @java.lang.Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
