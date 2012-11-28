/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author empollica
 */
public class ConectadoConverter implements Converter{

    public ConectadoConverter(){
        
    }
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String conversion = "";
        try{
            char conectado = (Character) value;
            if(conectado == '0'){
                conversion = "No conectado";
            }else if(conectado == '2'){
                conversion = "Conectado (móvil)";
            }else{
                conversion = "Conectado";
            }
            
        }catch(Exception e){
            
        }
        return conversion;
    }
    
}
