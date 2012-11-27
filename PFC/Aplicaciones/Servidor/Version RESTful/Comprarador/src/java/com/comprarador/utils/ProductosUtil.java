/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comprarador.utils;

import com.comprarador.entities.Productos;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author albertomateos
 */
public class ProductosUtil {

    public static Productos actualizarProducto(Productos anterior, Productos nuevo) {

        Productos p = new Productos();

        // Se copian los datos que no varían
        p.setId(anterior.getId());
        p.setMarca(anterior.getMarca());
        p.setDescripcion(anterior.getDescripcion());
        p.setFormato(anterior.getFormato());
        p.setCodigoBarras(anterior.getCodigoBarras());
        p.setCodigoCreado(anterior.getCodigoCreado());
        p.setImagenSrc(anterior.getImagenSrc());

        p.setCreado(new Date());

        p.setCategoria(nuevo.getCategoria());
        p.setSubcategoria(nuevo.getSubcategoria());
        p.setSubsubcategoria(nuevo.getSubsubcategoria());

        if (nuevo.getPrecioCarrefour() != null) {
            p.setPrecioCarrefour(nuevo.getPrecioCarrefour());
        } else {
            p.setPrecioCarrefour(BigDecimal.ZERO);
        }

        if (nuevo.getPrecioHipercor() != null) {
            p.setPrecioHipercor(nuevo.getPrecioHipercor());
        } else {
            p.setPrecioHipercor(BigDecimal.ZERO);
        }

        if (nuevo.getPrecioMercadona() != null) {
            p.setPrecioMercadona(nuevo.getPrecioMercadona());
        } else {
            p.setPrecioMercadona(BigDecimal.ZERO);
        }
        if (nuevo.getPreciocorteIngles() != null) {
            p.setPreciocorteIngles(nuevo.getPreciocorteIngles());
        } else {
            p.setPreciocorteIngles(BigDecimal.ZERO);
        }

        p.setPrecioRelativoCarrefour(nuevo.getPrecioRelativoCarrefour());
        p.setPrecioRelativoHipercor(nuevo.getPrecioRelativoHipercor());
        p.setPrecioRelativoMercadona(nuevo.getPrecioRelativoMercadona());
        p.setPreciorelativocorteIngles(nuevo.getPreciorelativocorteIngles());

        p.setOfertaCarrefour(nuevo.getOfertaCarrefour());
        p.setOfertaHipercor(nuevo.getOfertaHipercor());
        p.setOfertaMercadona(nuevo.getOfertaMercadona());
        p.setOfertacorteIngles(nuevo.getOfertacorteIngles());

        return p;

    }

    public static void descargarImagen(final String src, final Long id) {

        Thread t = new Thread() {

            @Override
            public void run() {

                super.run();

                try {
                    URL url = new URL(src);
                    InputStream in = url.openStream();
                    OutputStream out = new BufferedOutputStream(new FileOutputStream("/Users/albertomateos/Teleco/PFC/Desarrollo/imagenes_productos/" + id + ".gif"));
                    for (int b; (b = in.read()) != -1;) {
                        out.write(b);
                    }
                    out.close();
                    in.close();
                } catch (MalformedURLException ex) {
                    Logger.getLogger(ProductosUtil.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ProductosUtil.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ProductosUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        t.start();
    }
}
