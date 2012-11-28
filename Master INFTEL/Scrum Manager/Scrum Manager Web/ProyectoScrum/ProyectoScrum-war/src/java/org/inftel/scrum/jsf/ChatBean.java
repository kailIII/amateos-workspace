/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.inftel.scrum.ejb.UsuarioFacade;
import org.inftel.scrum.model.Usuario;
import org.primefaces.context.RequestContext;

/**
 *
 * @author migueqm
 */
@ManagedBean
@ViewScoped
public class ChatBean implements Serializable {

    @EJB
    private UsuarioFacade usuarioFacade;
    private final static String CHANNEL = "chat";
    private String message;
    private String username;
    private List<Usuario> listUsers;
    private Set<String> users = new HashSet<String>();

    public ChatBean() {
    }

    @PostConstruct
    public void refreshUsers() {
        FacesContext context = FacesContext.getCurrentInstance();
        UsuarioController usuarioController = (UsuarioController) context.getApplication().evaluateExpressionGet(context, "#{usuarioController}", UsuarioController.class);
        Usuario user = usuarioFacade.findByMail(usuarioController.getEmail());
        List<Usuario> listUsersAux = new ArrayList<Usuario>();
        listUsers = new ArrayList<Usuario>();
        listUsersAux = usuarioFacade.findByGrupo(user.getGrupoId()); 
        for(Usuario usuario : listUsersAux){
            if( (usuario.getLoggedin() != '0') && (!usuario.equals(user) ) )    listUsers.add(usuario);
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Usuario> getListUsers() {
        return listUsers;
    }

    public void setListUsers(List<Usuario> listUsers) {
        this.listUsers = listUsers;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }

    public void send() {
        RequestContext.getCurrentInstance().push(CHANNEL, username + ": " + message);
        message = null;
    }

    public void login() {
        FacesContext context = FacesContext.getCurrentInstance();
        UsuarioController usuarioController = (UsuarioController) context.getApplication().evaluateExpressionGet(context, "#{usuarioController}", UsuarioController.class);
        username = usuarioController.getEmail();
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.push(CHANNEL, username + " joined the channel.");        
    }

    public void disconnect() {
        RequestContext.getCurrentInstance().push(CHANNEL, username + " has left the channel.");
        username = null;
    }
}
