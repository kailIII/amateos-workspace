/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.jsf;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.inftel.scrum.ejb.HistoricoFacade;
import org.inftel.scrum.ejb.ProyectoFacade;
import org.inftel.scrum.ejb.UsuarioFacade;
import org.inftel.scrum.model.Grupo;
import org.inftel.scrum.ejb.GrupoFacade;
import org.inftel.scrum.model.Historico;
import org.inftel.scrum.model.Proyecto;
import org.inftel.scrum.model.Usuario;
import org.primefaces.model.DualListModel;

/**
 *
 * @author inftel
 */
@ManagedBean
@ViewScoped
public class GrupoBean implements Serializable {

    @EJB
    private HistoricoFacade historicoFacade;
    @EJB
    private ProyectoFacade proyectoFacade;
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private GrupoFacade grupoFacade;
    private final static Logger LOGGER = Logger.getLogger(GrupoBean.class.getName());
    private List<Grupo> usuariosGrupo;
    private Grupo nuevoGrupo;
    private boolean renderNuevo;
    private boolean renderVer;
    private List<String> proyectos;
    private String proyectoSeleccionado;
    private Grupo grupoSeleccionado;
    private DualListModel<String> usuariosPickList;
    private DualListModel<String> usuariosEditadosPickList;

    /**
     * Creates a new instance of GrupoBean
     */
    public GrupoBean() {
        LOGGER.info("Creando GrupoBean ...");
    }

    @PostConstruct
    public void init() {
        LOGGER.info("Inicializando GrupoBean ... " + this.hashCode());
        iniciaUsuariosGrupo();
        nuevoGrupo = new Grupo();
        cargaComboProyectos();
        añadirUsuariosPickList();
        usuariosEditadosPickList = new DualListModel<String>();
    }

    public List<Grupo> getUsuariosGrupo() {
        return usuariosGrupo;
    }

    public void setUsuariosGrupo(List<Grupo> usuariosGrupo) {
        this.usuariosGrupo = usuariosGrupo;
    }

    public void iniciaUsuariosGrupo() {

        usuariosGrupo = new ArrayList<Grupo>();

        Usuario usuario = getUsuarioActual();
        LOGGER.info("OBTENIDO USUARIO " + usuario);
        if (usuario.getGrupoId() != null) {
            LOGGER.info("OBTENIDO GRUPO USUARIO " + usuario.getGrupoId().getId());
            usuariosGrupo = grupoFacade.findByGrupoId(usuario.getGrupoId().getId().toString());
            LOGGER.info("OBTENIDO GRUPOS " + usuariosGrupo);

        } else {

            FacesMessage msg = new FacesMessage("FAIL: ", "El usuario " + usuario.getEmail() + " no tiene grupo asociado!");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        }
    }

    public Grupo getNuevoGrupo() {
        return nuevoGrupo;
    }

    public void setNuevoGrupo(Grupo nuevoGrupo) {
        this.nuevoGrupo = nuevoGrupo;
    }

    public void crearNuevo() {

        if (nuevoGrupo != null) {

            LOGGER.info("grupos: " + grupoFacade.findByGrupoRepetido(proyectoSeleccionado));
            if (grupoFacade.findByGrupoRepetido(proyectoSeleccionado) == 0) {
                if (compruebaMaster(usuariosPickList)) {
                    try {
                        nuevoGrupo.setId(grupoFacade.maxId().add(BigDecimal.ONE));
                    } catch (Exception e) {
                        nuevoGrupo.setId(BigDecimal.ONE);
                    }
                    nuevoGrupo.setProyectoId(proyectoFacade.findByNombre(proyectoSeleccionado));
                    LOGGER.info("ID Proyecto: " + proyectoFacade.findByNombre(proyectoSeleccionado));
                    LOGGER.info("nuevoGrupo = " + nuevoGrupo);

                    Historico h = new Historico();

                    h.setGrupo(nuevoGrupo);

                    grupoFacade.create(nuevoGrupo);

                    List<String> lista = usuariosPickList.getTarget();
                    LOGGER.info("Lista de Usuarios: " + lista);
                    LOGGER.info("Se van a recorrer todos los usuarios!!");

                    for (String s : lista) {
                        LOGGER.info("Email de usuario: " + s);
                        Usuario u = usuarioFacade.findByMail(s);
                        try{
                            h.setId(historicoFacade.maxId().add(BigDecimal.ONE));
                        }catch(Exception e){
                            h.setId(BigDecimal.ONE);
                        }
                        h.setUsuario(u);
                        LOGGER.info("Nuevo GrupoID : " + nuevoGrupo.getId());
                        u.setGrupoId(nuevoGrupo);
                        LOGGER.info("Nuevo GrupoID Usuario: " + u.getGrupoId().getId());
                        usuarioFacade.edit(u);
                        historicoFacade.create(h);
                    }


                    LOGGER.info("nuevoGrupo2 = " + nuevoGrupo);

                    FacesMessage msg = new FacesMessage("SUCCESS: ", "Grupo creado!");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                    nuevoGrupo = new Grupo();
                    proyectoSeleccionado = null;
                    añadirUsuariosPickList();


                } else {
                    FacesMessage msg = new FacesMessage("FAIL: ", "Grupo no creado!");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                }

            } else {
                FacesMessage msg = new FacesMessage("FAIL: ", "Nombre de Grupo repetido!");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } else {
            LOGGER.log(Level.WARNING, "Error al crear grupo nuevo,no puede ser null.");
        }


    }

    public boolean isRenderNuevo() {
        return renderNuevo;
    }

    public void setRenderNuevo(boolean renderNuevo) {
        this.renderNuevo = renderNuevo;
    }

    public boolean isRenderVer() {
        return renderVer;
    }

    public void setRenderVer(boolean renderVer) {
        this.renderVer = renderVer;
    }

    public String redirigir(int estado) {
        if (estado == 1) {
            renderNuevo = true;
            renderVer = false;
        } else if (estado == 2) {

            if (getUsuarioActual().getGrupoId() != null) {
                renderNuevo = false;
                renderVer = true;
            } else {
                renderVer = false;
            }
        }
        return "grupo";
    }

    public void cargaComboProyectos() {

        proyectos = new ArrayList<String>();
        LOGGER.info("Cargando proyectos en el combo...");
        List<Proyecto> proy = proyectoFacade.findAll();
        LOGGER.info("proyectos: " + proy);
        for (Proyecto p : proy) {
            LOGGER.info("proyectos que se trae: " + p);
            String nombre = p.getNombre();
            LOGGER.info("nombre del proyecto: " + nombre);
            proyectos.add(nombre);
            LOGGER.info("proyectos: " + proyectos);
        }
        LOGGER.info("proyectos: " + proyectos);
    }

    public String getProyectoSeleccionado() {
        return proyectoSeleccionado;
    }

    public void setProyectoSeleccionado(String proyectoSeleccionado) {
        this.proyectoSeleccionado = proyectoSeleccionado;
    }

    public List<String> getProyectos() {
        return proyectos;
    }

    public void setProyectos(List<String> proyectos) {
        this.proyectos = proyectos;
    }

    public Grupo getGrupoSeleccionado() {
        añadirUsuariosEditadosPickList();
        return grupoSeleccionado;
    }

    public void setGrupoSeleccionado(Grupo grupoSeleccionado) {
        this.grupoSeleccionado = grupoSeleccionado;
    }

    public DualListModel<String> getUsuariosPickList() {
        return usuariosPickList;
    }

    public void setUsuariosPickList(DualListModel<String> usuariosPickList) {
        this.usuariosPickList = usuariosPickList;
    }

    public DualListModel<String> getUsuariosEditadosPickList() {
        return usuariosEditadosPickList;
    }

    public void setUsuariosEditadosPickList(DualListModel<String> usuariosEditadosPickList) {
        this.usuariosEditadosPickList = usuariosEditadosPickList;
    }

    public void editarGrupo() {

        if (compruebaMaster(usuariosEditadosPickList)) {

            LOGGER.info("Editando grupo ...");
            LOGGER.info("grupoSeleccionado = " + grupoSeleccionado.getId());
            List<Usuario> lista = crearListaUsuariosEditados();
            LOGGER.info("lista editados: " + lista);
            grupoSeleccionado.setUsuarioList(lista);
            LOGGER.info("usuarioList editados: " + grupoSeleccionado.getUsuarioList());
            for (Usuario u : lista) {
                LOGGER.info("GrupoId " + u.getGrupoId() + " del Usuario " + u.getEmail());
            }
            grupoSeleccionado.setProyectoId(proyectoFacade.findByNombre(grupoSeleccionado.getProyectoId().getNombre()));
            LOGGER.info("Proyecto seleccionado: " + proyectoFacade.findByNombre(grupoSeleccionado.getProyectoId().getNombre()));
            grupoFacade.edit(grupoSeleccionado);

            usuariosPickList.setSource(usuariosEditadosPickList.getSource());

            FacesMessage msg = new FacesMessage("SUCCESS: ", "Grupo editado!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    public boolean isMaster() {

        Usuario u = getUsuarioActual();
        String rol = "";
        if (u != null && u.getRol() != null) {
            rol = getUsuarioActual().getRol();
        }
        if (rol.equalsIgnoreCase("Scrum Master")) {

            return true;
        } else {
            return false;
        }

    }

    public void añadirUsuariosPickList() {

        LOGGER.info("Añadiendo usuarios al picklist ...");
        List<Usuario> usuarios = usuarioFacade.findByGrupoNull();
        List<String> source = new ArrayList<String>();
        List<String> target = new ArrayList<String>();

        for (Usuario u : usuarios) {
            LOGGER.info("Usuario picklist por email: " + u.getEmail());
            source.add(u.getEmail());
        }

        target.add(getUsuarioActual().getEmail());
        usuariosPickList = new DualListModel<String>(source, target);
        LOGGER.info("usuariosPickList = " + usuariosPickList);

    }

    public void añadirUsuariosEditadosPickList() {

        LOGGER.info("Añadiendo usuarios al picklist de editar grupo ...");
        List<Usuario> usuarios = usuarioFacade.findByGrupoNull();
        List<Usuario> listaTarget = usuarioFacade.findByGrupo(grupoSeleccionado);
        LOGGER.info("listaTarget: " + listaTarget);

        List<String> source = new ArrayList<String>();
        List<String> target = new ArrayList<String>();

        for (Usuario u : usuarios) {
            source.add(u.getEmail());
        }
        for (Usuario u2 : listaTarget) {
            LOGGER.info("Añadiendo usuario: " + u2.getEmail());
            target.add(u2.getEmail());
        }

        usuariosEditadosPickList.setSource(source);
        usuariosEditadosPickList.setTarget(target);
        LOGGER.info("usuariosEditadosPickList = " + usuariosEditadosPickList);

    }

    private List<Usuario> crearListaUsuariosEditados() {
        List<Usuario> l = new ArrayList<Usuario>();

        for (int i = 0; i < usuariosEditadosPickList.getTarget().size(); i++) {

            Usuario u = usuarioFacade.findByMail(usuariosEditadosPickList.getTarget().get(i));
            LOGGER.info("Usuario editado:  " + u);
            LOGGER.info("ID de grupo Seleccionado: " + grupoSeleccionado.getId());
            u.setGrupoId(grupoSeleccionado);
            usuarioFacade.edit(u);
            l.add(u);
        }

        for (int i = 0; i < usuariosEditadosPickList.getSource().size(); i++) {
            Usuario user = usuarioFacade.findByMail(usuariosEditadosPickList.getSource().get(i));
            user.setGrupoId(null);
            usuarioFacade.edit(user);
        }

        return l;
    }

    public Usuario getUsuarioActual() {

        UsuarioController usuarioController = (UsuarioController) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioController");
        Usuario u = usuarioFacade.findByMail(usuarioController.getEmail());
        return u;
    }

    public boolean compruebaMaster(DualListModel<String> lista) {

        boolean b = true;
        Usuario u = new Usuario();
        for (int i = 0; i < lista.getSource().size() && b; i++) {
            u = usuarioFacade.findByMail(lista.getSource().get(i));
            String rol = u.getRol();
            LOGGER.info("El usuario tiene el rol:  " + rol);
            LOGGER.info(getUsuarioActual().getRol());
            if (rol.equals(getUsuarioActual().getRol())) {
                b = false;
            }
        }
        if (b == false) {
            FacesMessage msg = new FacesMessage("FAIL: ", "Imposible quitar " + u.getEmail() + " es el Scrum Master del grupo.Añadalo de nuevo");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        LOGGER.info("Valor de b: " + b);
        return b;
    }
}
