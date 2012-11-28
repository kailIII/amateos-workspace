/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.converter;

import java.math.BigInteger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author empollica
 */
public class PrioridadConverter implements Converter{

    public PrioridadConverter(){
        
    }
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String conversion = "";
        try{
            BigInteger prioridad = (BigInteger) value;

            if(prioridad == BigInteger.ONE){
                conversion = "Alta";
            }else{
                conversion = "Baja";
            }
            
        }catch(Exception e){
        }
        return conversion;
    }
    
}
