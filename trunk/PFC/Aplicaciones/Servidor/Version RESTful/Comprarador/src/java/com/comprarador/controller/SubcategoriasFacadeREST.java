/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comprarador.controller;

import com.comprarador.entities.Subcategorias;
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
@Path("subcategorias")
public class SubcategoriasFacadeREST extends AbstractFacade<Subcategorias> {
    
    private final static Logger LOGGER = Logger.getLogger(SubcategoriasFacadeREST.class .getName());

    @PersistenceContext(unitName = "iCompraPU")
    private EntityManager em;

    public SubcategoriasFacadeREST() {
        super(Subcategorias.class);
    }

    @POST
    @Consumes({"application/xml"})
    @Produces({"text/plain"})
    public String crear(Subcategorias entity) {
        
        Subcategorias sc = null;

        // Se busca verifica si el producto se encuentra en la base de datos
        try {
            sc = (Subcategorias) getEntityManager().createQuery("SELECT sc FROM Subcategorias sc WHERE sc.nombre = :nombre AND sc.categoria = :categoria").setParameter("nombre", entity.getNombre()).setParameter("categoria", entity.getCategoria()).getSingleResult();
        } catch (Exception e) {
        }


        if (sc != null && entity.getNombre().equals(sc.getNombre())&& entity.getCategoria()==sc.getCategoria()) {

            // LA CATEGORIA YA SE ENCUENTRA EN LA BASE DE DATOS
            if (sc.getPosicion() != entity.getPosicion()) { // LA POSICIÓN ES DIFERENTE -> ACTUALIZAR
                sc.setPosicion(entity.getPosicion());
                super.edit(sc);
                
                LOGGER.info("SUBCATEGORIAS - POST: actualizada posición de subcategoría");
                return "actualizada posicion";

            }else{
                LOGGER.info("SUBCATEGORIAS - POST: subcategoría no modificada");
                return "no modificada";
            }

        } else {

            // LA CATEGORIA NO SE ENCUENTRA EN LA BASE DE DATOS -> AGREGAR A BASE DE DATOS
            super.create(entity);
            LOGGER.info("SUBCATEGORIAS - POST: insertada nueva subcategoría");
            return "insertada";
        }
    }


    @GET
    @Path("{cat}")
    @Produces({"application/xml"})
    public List<Subcategorias> findByCategoria(@PathParam("cat") Integer cat) {
       
        LOGGER.info("SUBCATEGORIAS - GET: subcategorías de categoría "+cat);
        
        try {
            return getEntityManager().createQuery("SELECT s FROM Subcategorias s WHERE s.categoria = :cat").setParameter("cat", cat).getResultList();
        } catch (Exception e) {
            return null;
        }
        
    }
    
    
    @java.lang.Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
