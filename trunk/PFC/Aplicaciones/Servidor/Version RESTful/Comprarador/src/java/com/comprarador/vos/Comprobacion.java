/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comprarador.vos;

import com.comprarador.entities.Productos;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author albertomateos
 */
@XmlRootElement
public class Comprobacion {
    private double totalMercadona;
    private double totalHipercor;
    private double totalCarrefour;
    private double totalCorteIngles;
    
    private int aciertosMercadona;
    private int aciertosHipercor;
    private int aciertosCarrefour;
    private int aciertosCorteIngles;
    
    public Comprobacion(){
        aciertosMercadona=0;
        aciertosHipercor=0;
        aciertosCarrefour=0;
        aciertosCorteIngles=0;
        totalMercadona=0.00;
        totalHipercor=0.00;
        totalCarrefour=0.00;
        totalCorteIngles=0.00;
    }
    
    public void agregarProducto(Productos p){    
        
        if(p.getPrecioMercadona() != null && p.getPrecioMercadona().doubleValue() != 0.00){
            sumarAciertoMercadona(p.getPrecioMercadona());
        }
        
        if(p.getPrecioCarrefour() != null && p.getPrecioCarrefour().doubleValue() != 0.00){
            sumarAciertoCarrefour(p.getPrecioCarrefour());
        }
        
        if(p.getPrecioHipercor() != null && p.getPrecioHipercor().doubleValue() != 0.00){
            sumarAciertoHipercor(p.getPrecioHipercor());
        }
        
        if(p.getPreciocorteIngles() != null && p.getPreciocorteIngles().doubleValue() != 0.00){
            sumarAciertoCorteIngles(p.getPreciocorteIngles());
        }
    }
    
    public void sumarAciertoMercadona(BigDecimal precio){
        totalMercadona += precio.doubleValue();
        aciertosMercadona++;
    }
    
    public void sumarAciertoHipercor(BigDecimal precio){
        totalHipercor += precio.doubleValue();
        aciertosHipercor++;
    }
    
    public void sumarAciertoCarrefour(BigDecimal precio){
        totalCarrefour += precio.doubleValue();
        aciertosCarrefour++;
    }
    
    public void sumarAciertoCorteIngles(BigDecimal precio){
        totalCorteIngles += precio.doubleValue();
        aciertosCorteIngles++;
    }

    public int getAciertosCarrefour() {
        return aciertosCarrefour;
    }

    public void setAciertosCarrefour(int aciertosCarrefour) {
        this.aciertosCarrefour = aciertosCarrefour;
    }

    public int getAciertosCorteIngles() {
        return aciertosCorteIngles;
    }

    public void setAciertosCorteIngles(int aciertosCorteIngles) {
        this.aciertosCorteIngles = aciertosCorteIngles;
    }

    public int getAciertosHipercor() {
        return aciertosHipercor;
    }

    public void setAciertosHipercor(int aciertosHipercor) {
        this.aciertosHipercor = aciertosHipercor;
    }

    public int getAciertosMercadona() {
        return aciertosMercadona;
    }

    public void setAciertosMercadona(int aciertosMercadona) {
        this.aciertosMercadona = aciertosMercadona;
    }

    public double getTotalCarrefour() {
        return totalCarrefour;
    }

    public void setTotalCarrefour(double totalCarrefour) {
        this.totalCarrefour = totalCarrefour;
    }

    public double getTotalCorteIngles() {
        return totalCorteIngles;
    }

    public void setTotalCorteIngles(double totalCorteIngles) {
        this.totalCorteIngles = totalCorteIngles;
    }

    public double getTotalHipercor() {
        return totalHipercor;
    }

    public void setTotalHipercor(double totalHipercor) {
        this.totalHipercor = totalHipercor;
    }

    public double getTotalMercadona() {
        return totalMercadona;
    }

    public void setTotalMercadona(double totalMercadona) {
        this.totalMercadona = totalMercadona;
    }
    
    
}
