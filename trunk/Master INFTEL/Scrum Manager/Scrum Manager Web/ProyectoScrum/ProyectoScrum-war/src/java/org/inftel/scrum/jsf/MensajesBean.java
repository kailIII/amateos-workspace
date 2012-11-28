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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.inftel.scrum.ejb.MensajesFacade;
import org.inftel.scrum.ejb.UsuarioFacade;
import org.inftel.scrum.model.Mensajes;
import org.inftel.scrum.model.Usuario;
import org.primefaces.model.DualListModel;

/**
 *
 * @author empollica
 */
@ManagedBean
@ViewScoped
public class MensajesBean implements Serializable {

    private final static Logger LOGGER = Logger.getLogger(MensajesBean.class.getName());
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private MensajesFacade mensajesFacade;
    private Boolean vistaRedactar;
    private Boolean vistaRecibidos;
    private Boolean vistaEnviados;
    private Usuario usuario;
    private List<Usuario> todosUsuarios;
    private List<Mensajes> recibidos;
    private List<Mensajes> enviados;
    private Mensajes mensajeRecibidoSeleccionado;
    private Mensajes mensajeEnviadoSeleccionado;
    private String destinatarioRedactar;
    private String asuntoRedactar;
    private String mensajeRedactar;
    private UsuarioController usuarioController;
    private DualListModel<String> usuarios;

    /** Creates a new instance of PruebaBean */
    public MensajesBean() {
    }

    @PostConstruct
    public void init() {
        // Booleanos para visualizacion
        vistaRedactar = false;
        vistaRecibidos = true;
        vistaEnviados = false;

        // OBTENCION DEL USUARIO
        usuarioController = (UsuarioController) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioController");
        usuario = usuarioFacade.findByMail(usuarioController.getEmail());

        // LISTA DROPDOWN PARA SELECCIÓN DE DESTINATARIO
        todosUsuarios = usuarioFacade.findAll();
        List<String> usuariosSource = new ArrayList<String>();
        List<String> usuariosTarget = new ArrayList<String>();

        for (Usuario u : todosUsuarios) {
            usuariosSource.add(u.getEmail());
        }
        usuarios = new DualListModel<String>(usuariosSource, usuariosTarget);
    }

    public DualListModel<String> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(DualListModel<String> usuarios) {
        this.usuarios = usuarios;
    }

    public Boolean getVistaEnviados() {
        return vistaEnviados;
    }

    public void setVistaEnviados(Boolean vistaEnviados) {
        this.vistaEnviados = vistaEnviados;
    }

    public Boolean getVistaRecibidos() {
        return vistaRecibidos;
    }

    public void setVistaRecibidos(Boolean vistaRecibidos) {
        this.vistaRecibidos = vistaRecibidos;
    }

    public Boolean getVistaRedactar() {
        return vistaRedactar;
    }

    public void setVistaRedactar(Boolean vistaRedactar) {
        this.vistaRedactar = vistaRedactar;
    }

    public String getAsuntoRedactar() {
        return asuntoRedactar;
    }

    public void setAsuntoRedactar(String asuntoRedactar) {
        this.asuntoRedactar = asuntoRedactar;
    }

    public String getDestinatarioRedactar() {
        return destinatarioRedactar;
    }

    public void setDestinatarioRedactar(String destinatarioRedactar) {
        this.destinatarioRedactar = destinatarioRedactar;
    }

    public List<Mensajes> getEnviados() {
        enviados = mensajesFacade.findEnviadosByUsuario(usuario);
        return enviados;
    }

    public void setEnviados(List<Mensajes> enviados) {
        this.enviados = enviados;
    }

    public String getMensajeRedactar() {
        return mensajeRedactar;
    }

    public void setMensajeRedactar(String mensajeRedactar) {
        this.mensajeRedactar = mensajeRedactar;
    }

    public Mensajes getMensajeRecibidoSeleccionado() {
        return mensajeRecibidoSeleccionado;
    }

    public void setMensajeRecibidoSeleccionado(Mensajes mensajeRecibidoSeleccionado) {
        this.mensajeRecibidoSeleccionado = mensajeRecibidoSeleccionado;
        LOGGER.info("MENSAJE RECIBIDO SELECCIONADO ->" + this.mensajeRecibidoSeleccionado);
    }

    public Mensajes getMensajeEnviadoSeleccionado() {
        return mensajeEnviadoSeleccionado;
    }

    public void setMensajeEnviadoSeleccionado(Mensajes mensajeEnviadoSeleccionado) {
        this.mensajeEnviadoSeleccionado = mensajeEnviadoSeleccionado;
        LOGGER.info("MENSAJE ENVIADO SELECCIONADO ->" + this.mensajeEnviadoSeleccionado);
    }

    public List<Mensajes> getRecibidos() {
        recibidos = mensajesFacade.findRecibidosByUsuario(usuario);
        return recibidos;
    }

    public void setRecibidos(List<Mensajes> recibidos) {
        this.recibidos = recibidos;
    }

    public void pickDestinatario() {
        LOGGER.info("PICK DESTINATARIO -> " + usuarios.getTarget().get(0));
        this.destinatarioRedactar = usuarios.getTarget().get(0);
        for (String u : usuarios.getTarget().subList(1, usuarios.getTarget().size())) {
            this.destinatarioRedactar += "," + u;
        }
        LOGGER.info("DESTINATARIOS -> " + destinatarioRedactar);
    }

    public void toggleVista(int vista) {
        switch (vista) {
            case 1:
                vistaRedactar = true;
                vistaRecibidos = false;
                vistaEnviados = false;
                break;
            case 2:
                vistaRedactar = false;
                vistaRecibidos = true;
                vistaEnviados = false;
                break;
            case 3:
                vistaRedactar = false;
                vistaRecibidos = false;
                vistaEnviados = true;
                break;
            default:
                vistaRedactar = false;
                vistaRecibidos = true;
                vistaEnviados = false;
                break;
        }
    }

    public void eliminarMensajeRecibido() {
        mensajesFacade.remove(mensajeRecibidoSeleccionado);
        // Notificación growl
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Mensaje eliminado.", "El mensaje ha sido eliminado con éxito"));
        recibidos = mensajesFacade.findRecibidosByUsuario(usuario);
    }

    public void eliminarMensajeEnviado() {
        mensajesFacade.remove(mensajeEnviadoSeleccionado);
        // Notificación growl
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Mensaje eliminado.", "El mensaje ha sido eliminado con éxito"));
        enviados = mensajesFacade.findEnviadosByUsuario(usuario);
    }

    public void marcarLeido() {
        LOGGER.info("LEIDO MENSAJE");
        char leido = '1';
        mensajeRecibidoSeleccionado.setLeido(leido);
        mensajesFacade.edit(mensajeRecibidoSeleccionado);
    }

    public void enviarMensaje() {
        String[] destinatarios = destinatarioRedactar.split(",");
        Mensajes m;
        for (String destinatario : destinatarios) {
            m = new Mensajes();
            m.setAsunto(asuntoRedactar);
            m.setDestino(usuarioFacade.findByMail(destinatario));
            m.setOrigen(usuario);
            m.setMensaje(mensajeRedactar);
            try {
                m.setId(mensajesFacade.maxId().add(BigDecimal.ONE));
            } catch (Exception e) {
                m.setId(BigDecimal.ONE);
            }
            char leido = '0';
            m.setLeido(leido);
            m.setFecha(new Date());
            LOGGER.info("SE VA A ENVIAR EL MENSAJE -> " + m);

            mensajesFacade.create(m);
        }

        // Notificación growl
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Mensaje enviado.", "El mensaje ha sido enviado con éxito"));

        // Se reinicializan los campos y picklist
        asuntoRedactar = "";
        mensajeRedactar = "";
        destinatarioRedactar = "";
        List<String> usuariosSource = new ArrayList<String>();
        List<String> usuariosTarget = new ArrayList<String>();

        for (Usuario u : todosUsuarios) {
            usuariosSource.add(u.getEmail());
        }
        LOGGER.info("REINICIADO PICKLIST");
        LOGGER.info("SOURCE -> " + usuariosSource);
        LOGGER.info("TARGET -> " + usuariosTarget);
        usuarios.setSource(usuariosSource);
        usuarios.setTarget(usuariosTarget);
        LOGGER.info("USUARIOS PICKLIST-> " + usuarios);
    }
}