/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.internacionalizacion;

import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author empollica
 */
@ManagedBean
@SessionScoped
public class LanguageBeam {

    private Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
    
    /** Creates a new instance of LanguageBeam */
    public LanguageBeam() {
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }


    public void changeLanguage(String language) {
        locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }

}
