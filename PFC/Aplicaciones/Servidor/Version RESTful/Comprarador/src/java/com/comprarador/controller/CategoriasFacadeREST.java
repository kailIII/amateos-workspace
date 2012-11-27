/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comprarador.controller;

import com.comprarador.entities.Categorias;
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
@Path("categorias")
public class CategoriasFacadeREST extends AbstractFacade<Categorias> {

    private final static Logger LOGGER = Logger.getLogger(CategoriasFacadeREST.class .getName());
    
    
    @PersistenceContext(unitName = "iCompraPU")
    private EntityManager em;

    public CategoriasFacadeREST() {
        super(Categorias.class);
    }

    @POST
    @Consumes({"application/xml"})
    @Produces({"text/plain"})
    public String crear(Categorias entity) {
                
        Categorias c = null;

        // Se busca verifica si el producto se encuentra en la base de datos
        try {
            c = (Categorias) getEntityManager().createQuery("SELECT c FROM Categorias c WHERE c.nombre = :nombre").setParameter("nombre", entity.getNombre()).getSingleResult();
        } catch (Exception e) {
        }


        if (c != null && entity.getNombre().equals(c.getNombre())) {

            // LA CATEGORIA YA SE ENCUENTRA EN LA BASE DE DATOS
            if (c.getPosicion() != entity.getPosicion()) { // LA POSICIÓN ES DIFERENTE -> ACTUALIZAR
                c.setPosicion(entity.getPosicion());
                super.edit(c);
                LOGGER.info("CATEGORIAS - POST: actualizada posición de categoría");
                return "actualizada posicion";
            }else{
                LOGGER.info("CATEGORIAS - POST: categoría no modificada");
                return "no modificada";
            }

        } else {

            // LA CATEGORIA NO SE ENCUENTRA EN LA BASE DE DATOS -> AGREGAR A BASE DE DATOS
            super.create(entity);
            LOGGER.info("CATEGORIAS - POST: insertada nueva categoría");
            return "insertada";
        }

    }

    @GET
    @Path("{id}")
    @Produces({"application/xml"})
    public Categorias find(@PathParam("id") Integer id) {
        LOGGER.info("CATEGORIAS - GET: categoria id="+id);
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml"})
    public List<Categorias> findAll() {
        LOGGER.info("CATEGORIAS - GET: todas las categorías");
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml"})
    public List<Categorias> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        LOGGER.info("CATEGORIAS - GET: categorías rango "+from+" - "+to);
        return super.findRange(new int[]{from, to});
    }

    @java.lang.Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
