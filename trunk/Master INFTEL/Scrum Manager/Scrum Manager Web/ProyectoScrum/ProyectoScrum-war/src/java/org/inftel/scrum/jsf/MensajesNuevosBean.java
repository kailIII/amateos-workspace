/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.jsf;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.inftel.scrum.ejb.MensajesFacade;
import org.inftel.scrum.ejb.UsuarioFacade;
import org.inftel.scrum.model.Usuario;

/**
 *
 * @author empollica
 */
@ManagedBean
@RequestScoped
public class MensajesNuevosBean implements Serializable{

    private final static Logger LOGGER = Logger.getLogger(MensajesNuevosBean.class.getName());
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private MensajesFacade mensajesFacade;
    private Usuario usuario;
    private UsuarioController usuarioController;
    private long mensajesNuevos;

    /** Creates a new instance of PruebaBean */
    public MensajesNuevosBean() {
    }

    @PostConstruct
    public void init() {

        // OBTENCION DEL USUARIO
        usuarioController = (UsuarioController) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioController");
        usuario = usuarioFacade.findByMail(usuarioController.getEmail());
        mensajesNuevos = mensajesFacade.findNumeroMensajesNuevosByUsuario(usuario);
    }

    public long getMensajesNuevos() {
        return mensajesNuevos;
    }

    public void setMensajesNuevos(long mensajesNuevos) {
        this.mensajesNuevos = mensajesNuevos;
    }

    public void mensajesNuevos() {
        mensajesNuevos = mensajesFacade.findNumeroMensajesNuevosByUsuario(usuario);
    }
}