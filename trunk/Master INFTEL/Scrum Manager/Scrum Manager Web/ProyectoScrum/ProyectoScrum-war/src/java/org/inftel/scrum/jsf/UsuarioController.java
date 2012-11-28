package org.inftel.scrum.jsf;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.inftel.scrum.ejb.LogFacade;
import org.inftel.scrum.ejb.UsuarioFacade;
import org.inftel.scrum.jsf.util.PaginationHelper;
import org.inftel.scrum.model.Log;
import org.inftel.scrum.model.Usuario;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Fernando
 */
@ManagedBean(name = "usuarioController")
@SessionScoped
public class UsuarioController implements Serializable {

    @EJB
    private LogFacade logFacade;
    private Log newLog;
    private final static Logger LOGGER = Logger.getLogger(LogBean.class.getName());
    private Usuario user;
    private DataModel items = null;
    @EJB
    private org.inftel.scrum.ejb.UsuarioFacade userFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private String email;
    private String pass;
    private String error = "";
    private String nombre;
    private String apellidos;
    private Date fecha;
    private String repass;
    private boolean edita = true;
    private boolean lista = false;
    private boolean elimina = false;
    private boolean loggedIn = false;
    // booleanos para visualización de opciones de menu
    private boolean menuLog;
    private boolean menuChat;
    private boolean menuEstadisticas;
    private boolean menuSprints;

    public UsuarioController() {
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
    }

    public boolean isEdita() {
        return edita;
    }

    public void setEdita(boolean edita) {
        this.edita = edita;
        lista = false;
        elimina = false;
    }

    public boolean isMenuChat() {
        return menuChat;
    }

    public void setMenuChat(boolean menuChat) {
        this.menuChat = menuChat;
    }

    public boolean isMenuEstadisticas() {
        return menuEstadisticas;
    }

    public void setMenuEstadisticas(boolean menuEstadisticas) {
        this.menuEstadisticas = menuEstadisticas;
    }

    public boolean isMenuLog() {
        return menuLog;
    }

    public void setMenuLog(boolean menuLog) {
        this.menuLog = menuLog;
    }

    public boolean isElimina() {
        return elimina;
    }

    public void setElimina(boolean elimina) {
        this.elimina = elimina;
        lista = false;
        edita = false;
    }

    public boolean isLista() {
        return lista;
    }

    public void setLista(boolean lista) {
        this.lista = lista;
        edita = false;
        elimina = false;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRepass() {
        return repass;
    }

    public void setRepass(String repass) {
        this.repass = repass;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
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

    public boolean isMenuSprints() {
        return menuSprints;
    }

    public void setMenuSprints(boolean menuSprints) {
        this.menuSprints = menuSprints;
    }

    public void doLogin() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (context == null) {
            initEJB();
        }
        if (email.contains("@")) {
            this.user = userFacade.findByMail(email);
            if (user != null) {
                if (user.getPassword().compareTo(pass) == 0) {
                    LOGGER.log(Level.INFO, "USUARIO: {0}{1}", new Object[]{user.getEmail(), user.getPassword()});
                    loggedIn = true;
                    nombre = user.getNombre();
                    apellidos = user.getApellidos();
                    pass = user.getPassword();
                    fecha = user.getFechanac();
                    email = user.getEmail();
                    user.setLoggedin('1');
                    userFacade.edit(user);
                    if (user.getGrupoId() != null) {
                        LOGGER.info("GRUPO NO ES NULL");
                        menuLog = true;
                        menuChat = true;
                        menuEstadisticas = true;
                        menuSprints = true;
                    } else {
                        LOGGER.info("GRUPO NULL");
                        menuLog = false;
                        menuChat = false;
                        menuEstadisticas = false;
                        menuSprints = false;
                    }

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
        if (context != null) {
            context.addCallbackParam("loggedIn", loggedIn);
        }
    }

    public void initEJB() {

        try {
            InitialContext initialContext = new InitialContext();
            java.lang.Object ejbHome = initialContext.lookup("java:global/ProyectoScrum/ProyectoScrum-ejb/UsuarioFacade");
            this.userFacade = (UsuarioFacade) javax.rmi.PortableRemoteObject.narrow(ejbHome, UsuarioFacade.class);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public String doLoginMovil() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (email.contains("@")) {
            this.user = userFacade.findByMail(email);
            if (user != null) {
                if (user.getPassword().compareTo(pass) == 0) {
                    LOGGER.log(Level.INFO, "USUARIO: {0}{1}", new Object[]{user.getEmail(), user.getPassword()});
                    loggedIn = true;
                    nombre = user.getNombre();
                    apellidos = user.getApellidos();
                    pass = user.getPassword();
                    fecha = user.getFechanac();
                    email = user.getEmail();
                    user.setLoggedin('1');
                    userFacade.edit(user);
                    return "index";
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
        context.addCallbackParam("loggedIn", loggedIn);
        return "";
    }

    public String registrarse() {
        this.email = "";
        this.pass = "";
        return "registroUsuario";
    }

    private UsuarioFacade getFacade() {
        return userFacade;
    }

    public String create() {
        Usuario us = userFacade.findByMail(email);
        if (us == null) {
            Usuario u = new Usuario();
            BigDecimal nUsuarios;
            try {
                nUsuarios = userFacade.maxId().add(BigDecimal.ONE);
            } catch (Exception e) {
                nUsuarios = BigDecimal.ONE;
            }
            u.setApellidos(apellidos);
            u.setEmail(email);
            u.setNombre(nombre);
            u.setFechanac(fecha);
            u.setPassword(pass);
            u.setLoggedin('0');
            u.setId(nUsuarios);
            LOGGER.log(Level.INFO, "apellidos: {0}", apellidos);
            LOGGER.log(Level.INFO, "email: {0}", email);
            LOGGER.log(Level.INFO, "nombre: {0}", nombre);
            LOGGER.log(Level.INFO, "fecha: {0}", fecha);
            LOGGER.log(Level.INFO, "pass: {0}", pass);
            LOGGER.log(Level.INFO, "pass: {0}", pass);
            userFacade.create(u);
            LOGGER.log(Level.INFO, "He creado un usuario");
            newLog = new Log();
            try {
                newLog.setProyectoId(null);
            } catch (Exception uno) {
            }
            try {
                newLog.setId(logFacade.maxId().add(BigDecimal.ONE));
            } catch (Exception dos) {
                newLog.setId(BigDecimal.ONE);
            }
            try {
                newLog.setDescripcion("Creación de Usuario " + nombre);
            } catch (Exception tres) {
            }
            try {
                newLog.setFecha(new Date());
            } catch (Exception cuatro) {
            }
            try {
                newLog.setUsuarioId(u);
            } catch (Exception cinco) {
            }

            logFacade.create(newLog);


            return "index";
        } else {
            FacesMessage msg = new FacesMessage("Error ", "Correo existente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return "";
    }

    public String editar(int estado) {
        if (estado == 1) {
            edita = true;
            elimina = false;
            lista = false;
        } else if (estado == 2) {
            lista = true;
            edita = false;
            elimina = false;
        }
        return "editar";
    }

    public void logout() {
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        String ctxPath = ((ServletContext) ctx.getContext()).getContextPath();
        this.user.setLoggedin('0');
        userFacade.edit(user);
        try {
            ((HttpSession) ctx.getSession(false)).invalidate();
            ctx.redirect(ctxPath + "/faces/index.xhtml");
        } catch (IOException ex) {
        }
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public void comprobarSesion() {
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        String ctxPath = ((ServletContext) ctx.getContext()).getContextPath();
        LOGGER.info(nombre);
        try {
            if (this.nombre == null || nombre.compareTo("") == 0) {
                LOGGER.info("Creando usuario controller beangdghdff");
                ctx.redirect(ctxPath + "/faces/index.xhtml");
            }
        } catch (IOException ex) {
            LOGGER.info("excepxion");
        }
    }

    @PreDestroy
    public void close() {
        if (this.user != null) {
            this.user.setLoggedin('0');
            userFacade.edit(user);
        }
    }

    @Override
    public String toString() {
        return "UsuarioController{" + "email=" + email + ", pass=" + pass + ", nombre=" + nombre + ", apellidos=" + apellidos + ", loggedIn=" + loggedIn + '}';
    }
}
