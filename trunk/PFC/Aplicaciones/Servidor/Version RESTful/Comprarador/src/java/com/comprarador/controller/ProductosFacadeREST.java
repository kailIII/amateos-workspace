/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comprarador.controller;

import com.comprarador.entities.Productos;
import com.comprarador.utils.ProductosUtil;
import com.comprarador.vos.Comprobacion;
import java.util.ArrayList;
import java.util.Date;
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
@Path("productos")
public class ProductosFacadeREST extends AbstractFacade<Productos> {
    
    private final static Logger LOGGER = Logger.getLogger(ProductosFacadeREST.class .getName());

    
    @PersistenceContext(unitName = "iCompraPU")
    private EntityManager em;

    public ProductosFacadeREST() {
        super(Productos.class);
    }

    @POST
    @Consumes({"application/xml"})
    @Produces({"text/plain"})
    public String crear(Productos entity) {
        
        Productos p = null;

        // Se busca verifica si el producto se encuentra en la base de datos
        try{
            if(entity.getImagenSrc() != null){
                p = (Productos) getEntityManager().createQuery("SELECT p FROM Productos p WHERE p.marca = :marca AND p.descripcion = :descripcion AND p.formato = :formato AND p.imagenSrc = :imagen").setParameter("descripcion", entity.getDescripcion()).setParameter("imagen", entity.getImagenSrc()).setParameter("marca", entity.getMarca()).setParameter("formato", entity.getFormato()).getSingleResult();   
            }else{
                p = (Productos) getEntityManager().createQuery("SELECT p FROM Productos p WHERE p.marca = :marca AND p.descripcion = :descripcion AND p.formato = :formato").setParameter("descripcion", entity.getDescripcion()).setParameter("marca", entity.getMarca()).setParameter("formato", entity.getFormato()).getSingleResult();
            }
        }catch (Exception e){
            System.out.println("EXCEPCION AL HACER BUSQUEDA -> "+e.getMessage());
        }

        
        if(entity.equals(p)){ 
            
            // EL PRODUCTO YA SE ENCUENTRA EN LA BASE DE DATOS -> ACTUALIZAR VALORES
            Productos actualizado = ProductosUtil.actualizarProducto(p, entity);
            super.edit(actualizado);
         
            LOGGER.info("PRODUCTOS - POST: actualizado producto");

            return "actualizado";
        }else{
            
            // EL PRODUCTO NO SE ENCUENTRA EN LA BASE DE DATOS -> AGREGAR A BASE DE DATOS
            super.create(entity);

            if(entity.getImagenSrc() != null){
                Long id = (Long) getEntityManager().createQuery("SELECT p.id FROM Productos p WHERE p.marca = :marca AND p.descripcion = :descripcion AND p.formato = :formato AND p.imagenSrc = :imagen").setParameter("descripcion", entity.getDescripcion()).setParameter("imagen", entity.getImagenSrc()).setParameter("marca", entity.getMarca()).setParameter("formato", entity.getFormato()).getSingleResult();
                // Se descarga la imagen del producto
                ProductosUtil.descargarImagen(entity.getImagenSrc(), id);
            }
            
            
            LOGGER.info("PRODUCTOS - POST: insertado producto");
            
            return "insertado";
        }
        
        
        
    }

    
    @GET
    @Path("{id}")
    @Produces({"application/xml"})
    public Productos find(@PathParam("id") Long id) {
        LOGGER.info("PRODUCTOS - GET: producto id="+id);
        Productos p=null;
        try{
            p = (Productos) getEntityManager().createQuery("SELECT p FROM Productos p WHERE p.id = :id").setParameter("id", id).getSingleResult();
            System.out.println("PRODUCTO ENCONTRADO -> "+p.getDescripcion());
        }catch (Exception e){
            System.out.println("EXCEPCION AL HACER BUSQUEDA -> "+e.getMessage());
        }
        return p;
        //return super.find(id);
    }
    
    @GET
    @Path("query/{texto}")
    @Produces({"application/xml"})
    public List<Productos> findByTexto(@PathParam("texto") String texto) {
        
        LOGGER.info("PRODUCTOS - GET: búsqueda producto por texto");

        try {
            return getEntityManager().createQuery("SELECT p FROM Productos p WHERE p.descripcion LIKE :texto OR p.marca LIKE :texto").setParameter("texto", "%"+texto+"%").getResultList();
        } catch (Exception e) {
            return new ArrayList<Productos>();
        }
    }
    
    @GET
    @Path("check/{param}")
    @Produces({"application/xml"})
    public Comprobacion comprobar(@PathParam("param") String param) {
        
        // Se obtienen los ids
        String[] ids = param.split(",");
                
        Comprobacion comprobacion = new Comprobacion();
        Productos p;
        
        for(String id:ids){
            try{
                long i = Long.parseLong(id);
                p = (Productos) getEntityManager().createQuery("SELECT p FROM Productos p WHERE p.id = :i").setParameter("i", i).getSingleResult();
                comprobacion.agregarProducto(p);
            } catch (Exception e){
            }
        }
        
        LOGGER.info("PRODUCTOS - GET: comprobación de productos");
        
        return comprobacion;
            
    }
    
    @GET
    @Path("code/{codigo}")
    @Produces({"application/xml"})
    public Productos findByCodigoBarras(@PathParam("codigo") String codigo) {
        
        LOGGER.info("PRODUCTOS - GET: búsqueda producto por código de barras");
        
        try {
            Productos p = (Productos) getEntityManager().createQuery("SELECT p FROM Productos p WHERE p.codigoBarras = :codigo").setParameter("codigo", codigo).getSingleResult();
            return p;
        } catch (Exception e) {
            return new Productos();
        }
    }

    
    @GET
    @Path("{cat}/{subcat}/{subsubcat}")
    @Produces({"application/xml"})
    public List<Productos> findByCategorias(@PathParam("cat") Integer cat, @PathParam("subcat") Integer subcat, @PathParam("subsubcat") Integer subsubcat) {
       
        LOGGER.info("PRODUCTOS - GET: búsqueda producto cat/subcat/subsubcat");

        try {
            return getEntityManager().createQuery("SELECT p FROM Productos p WHERE p.categoria = :cat AND p.subcategoria = :subcat AND p.subsubcategoria = :subsubcat").setParameter("cat", cat).setParameter("subcat", subcat).setParameter("subsubcat", subsubcat).getResultList();
        } catch (Exception e) {
            return new ArrayList<Productos>();
        }
        
    }


    @java.lang.Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
