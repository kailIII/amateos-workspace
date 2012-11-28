/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.jsf;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.inftel.scrum.ejb.*;
import org.inftel.scrum.model.*;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Fernando
 */
@ManagedBean(name = "gestionMovil")
@SessionScoped
public class GestionMovil implements Serializable {

    @EJB
    private UsuarioFacade userFacade;
    @EJB
    private GrupoFacade grupoFacade;
    private Usuario user = new Usuario();
    private Proyecto proyecto = new Proyecto();
    private Sprint sprint = new Sprint();
    private Grupo grupo = new Grupo();
    private String email = "";
    private String pass = "";
    private boolean loggedIn = false;
    private String error = "";
    private String nombre = "";
    private String apellidos = "";
    private Date fecha;

    /** Creates a new instance of GestionMovil */
    public GestionMovil() {
    }

    public Grupo getGrupo() {
        List<Grupo> grupos = grupoFacade.findAll();
        grupo = null;
        for (Grupo g : grupos) {
            for (Usuario u : g.getUsuarioList()) {
                if (email.compareTo(u.getEmail()) == 0) {
                    grupo = g;
                }
            }
        }
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Proyecto getProyecto() {
        proyecto = null;
        getGrupo();
        proyecto = grupo.getProyectoId();
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Sprint getSprint() {
        getGrupo();
        for (Sprint s : grupo.getProyectoId().getSprintList()) {
            if (s.getFin().after(new Date())) {
                sprint = s;
                break;
            }
        }
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String doLoginMovil() {
        if (email.contains("@")) {
            List<Usuario> j = userFacade.findAll();
            this.user = userFacade.findByMail(email);
            if (user != null) {
                if (user.getPassword().compareTo(pass) == 0) {
                    loggedIn = true;
                    nombre = user.getNombre();
                    apellidos = user.getApellidos();
                    pass = user.getPassword();
                    fecha = user.getFechanac();
                    email = user.getEmail();
                    user.setLoggedin('2');
                    userFacade.edit(user);
                    return "main3";
                } else {
                    error = "Contraseña invalida";
                    FacesMessage msg = new FacesMessage("ERROR: ", error);
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                }
            } else {
                error = "Usuario no encontrado";
                FacesMessage msg = new FacesMessage("ERROR: ", error);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } else {
            error = "El email no esta en formato correcto";
            FacesMessage msg = new FacesMessage("ERROR: ", error);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return "main";
    }

    public void logout() {
        ExternalContext ctx =FacesContext.getCurrentInstance().getExternalContext();
        String ctxPath = ((ServletContext) ctx.getContext()).getContextPath();
        this.user.setLoggedin('0');
        userFacade.edit(user);
        try {
            ((HttpSession) ctx.getSession(false)).invalidate();
            ctx.redirect(ctxPath + "/faces/main.xhtml");
        } catch (IOException ex) {
        }
    }

    @PreDestroy
    public void close() {
        this.user.setLoggedin('0');
        userFacade.edit(user);
    }
}
