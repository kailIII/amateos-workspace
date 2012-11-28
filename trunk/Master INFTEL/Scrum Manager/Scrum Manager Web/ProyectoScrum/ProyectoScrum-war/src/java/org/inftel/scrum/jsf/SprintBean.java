/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.jsf;

import org.inftel.scrum.ejb.MensajesFacade;
import org.inftel.scrum.ejb.TareaFacade;
import org.inftel.scrum.ejb.GrupoFacade;
import org.inftel.scrum.ejb.ProyectoFacade;
import org.inftel.scrum.jsf.util.JsfUtil;
import org.inftel.scrum.jsf.util.PaginationHelper;
import org.inftel.scrum.ejb.SprintFacade;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.inftel.scrum.ejb.LogFacade;
import org.inftel.scrum.ejb.UsuarioFacade;
import org.inftel.scrum.model.*;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Fernando
 */
@ManagedBean
@ViewScoped
public class SprintBean implements Serializable {
    
    @EJB
    private MensajesFacade mensajesFacade;
    @EJB
    private TareaFacade tareaFacade;
    @EJB
    private SprintFacade ejbFacade;
    @EJB
    private LogFacade logFacade;
    private Log newLog;
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private ProyectoFacade proyectoFacade;
    @EJB
    private GrupoFacade grupoFacade;
    private Sprint current;
    private DataModel items = null;
    DualListModel<Tarea> ta;
    DualListModel<Sprint> sprintToProyecto;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private boolean nuevo;
    private boolean editar;
    private boolean reunion;
    private boolean tablon;
    private Date finicio;
    private Date ffin;
    private Date fechaReunion;
    private String mensajeReunion;
    private String asuntoReunion;
    private List<Proyecto> proyectos;
    private Proyecto proyecto;
    private Sprint newSprint;
    private List<Grupo> grupos;
    private Grupo grupoUnico;
    private String nombreGrupo;
    private Sprint editSprint;
    private Proyecto sprintAProyecto;
    private boolean tareaNueva;
    private boolean tareas;
    private boolean nuevoMenu;
    private boolean editarMenu;
    private boolean reunionMenu;
    private List<Tarea> dest = new ArrayList<Tarea>();
    private List<Sprint> destino = new ArrayList<Sprint>();
    private List<Tarea> tareasProyecto = new ArrayList<Tarea>();
    private Tarea tareaSeleccionada = new Tarea();
    private static final Logger log = Logger.getLogger("org.inftel.scrum.jsf.SprintBean");
    private Mensajes nuevosMensajes;
    private List<Usuario> usuarios;
    private Usuario usuario;
    
    public SprintBean() {
    }
    
    @PostConstruct
    public void init() {
        UsuarioController usuarioController = (UsuarioController) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioController");
        usuario = usuarioFacade.findByMail(usuarioController.getEmail());
        log.info("OBTENIDO USUARIO " + usuario);
        if (usuario.getGrupoId() != null) {
            proyecto = usuario.getGrupoId().getProyectoId();
        }
        
        if (usuario.getRol() != null) {
            nuevo = false;
            editar = false;
            reunion = false;
            tablon = true;
            
            if (usuario.getRol().equalsIgnoreCase("scrum master")) {
                nuevoMenu = true;
                editarMenu = true;
                reunionMenu = true;
            } else {
                nuevoMenu = false;
                editarMenu = false;
                reunionMenu = false;
            }
        } else {
            nuevo = false;
            editar = false;
            reunion = false;
            tablon = false;
        }
        
        
    }
    
    public boolean isEditarMenu() {
        return editarMenu;
    }
    
    public void setEditarMenu(boolean editarMenu) {
        this.editarMenu = editarMenu;
    }
    
    public boolean isNuevoMenu() {
        return nuevoMenu;
    }
    
    public void setNuevoMenu(boolean nuevoMenu) {
        this.nuevoMenu = nuevoMenu;
    }
    
    public boolean isReunionMenu() {
        return reunionMenu;
    }
    
    public void setReunionMenu(boolean reunionMenu) {
        this.reunionMenu = reunionMenu;
    }
    
    public Proyecto getProyecto() {
        return proyecto;
    }
    
    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }
    
    public Sprint getSelected() {
        if (current == null) {
            current = new Sprint();
            selectedItemIndex = -1;
        }
        return current;
    }
    
    public Sprint getEditSprint() {
        return editSprint;
    }
    
    public boolean isTareaNueva() {
        return tareaNueva;
    }
    
    public void setTareaNueva(boolean tareaNueva) {
        this.tareaNueva = tareaNueva;
    }
    
    public boolean isTablon() {
        return tablon;
    }
    
    public void setTablon(boolean tablon) {
        this.tablon = tablon;
    }
    
    public boolean isTareas() {
        return tareas;
    }
    
    public void setTareas(boolean tareas) {
        this.tareas = tareas;
    }
    
    public void setEditSprint(Sprint editSprint) {
        this.editSprint = editSprint;
    }
    
    public Sprint getCurrent() {
        return current;
    }
    
    public void setCurrent(Sprint current) {
        this.current = current;
    }
    
    public Sprint getNewSprint() {
        return newSprint;
    }
    
    public void setNewSprint(Sprint newSprint) {
        this.newSprint = newSprint;
    }
    
    public Grupo getGrupoUnico() {
        return grupoUnico;
    }
    
    public void setGrupoUnico(Grupo grupoUnico) {
        this.grupoUnico = grupoUnico;
    }
    
    public List<Grupo> getGrupos() {
        return grupos;
    }
    
    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }
    
    public String getAsuntoReunion() {
        return asuntoReunion;
    }
    
    public void setAsuntoReunion(String asuntoReunion) {
        this.asuntoReunion = asuntoReunion;
    }
    
    public String getMensajeReunion() {
        return mensajeReunion;
    }
    
    public void setMensajeReunion(String mensajeReunion) {
        this.mensajeReunion = mensajeReunion;
    }
    
    public String getNombreGrupo() {
        return nombreGrupo;
    }
    
    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }
    
    private SprintFacade getFacade() {
        return ejbFacade;
    }
    
    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                
                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }
                
                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }
    
    public String prepareList() {
        recreateModel();
        return "List";
    }
    
    public String prepareView() {
        current = (Sprint) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }
    
    public Date getFfin() {
        return ffin;
    }
    
    public void setFfin(Date ffin) {
        this.ffin = ffin;
    }
    
    public Date getFechaReunion() {
        return fechaReunion;
    }
    
    public void setFechaReunion(Date fechaReunion) {
        this.fechaReunion = fechaReunion;
    }
    
    public Date getFinicio() {
        return finicio;
    }
    
    public void setFinicio(Date finicio) {
        this.finicio = finicio;
    }
    
    public String prepareCreate() {
        current = new Sprint();
        selectedItemIndex = -1;
        return "Create";
    }
    
    public boolean isEditar() {
        return editar;
    }
    
    public void setEditar(boolean editar) {
        this.editar = editar;
    }
    
    public boolean isReunion() {
        return reunion;
    }
    
    public void setReunion(boolean reunion) {
        this.reunion = reunion;
    }
    
    public boolean isNuevo() {
        return nuevo;
    }
    
    public void setNuevo(boolean nuevo) {
        this.nuevo = nuevo;
    }
    
    public DualListModel<Tarea> getTa() {
        List<Tarea> tar = new ArrayList<Tarea>();
        dest = new ArrayList<Tarea>();
        try {
            for (Tarea t : tareaFacade.findAll()) {
                if (t.getSprintId().getId().compareTo(editSprint.getId()) == 0) {
                    dest.add(t);
                }
            }
        } catch (Exception e) {
        }
        ta = new DualListModel<Tarea>(tar, dest);
        for (Tarea t : tareaFacade.findAll()) {
            if (t.getSprintId() == null) {
                tar.add(t);
            }
        }
        return ta;
    }
    
    public void setTa(DualListModel<Tarea> ta) {
        this.ta = ta;
    }
    
    public DualListModel<Sprint> getSprintToProyecto() {
        List<Sprint> origen = new ArrayList<Sprint>();
        destino = new ArrayList<Sprint>();
        try {
            for (Sprint s : ejbFacade.findAll()) {
                if (s.getProyectoId().getId().compareTo(proyecto.getId()) == 0) {
                    destino.add(s);
                }
            }
        } catch (Exception e) {
        }
        sprintToProyecto = new DualListModel<Sprint>(origen, destino);
        for (Sprint s : ejbFacade.findAll()) {
            if (s.getProyectoId() == null) {
                origen.add(s);
            }
        }
        return sprintToProyecto;
    }
    
    public void setSprintToProyecto(DualListModel<Sprint> sprintToProyecto) {
        this.sprintToProyecto = sprintToProyecto;
    }
    
    public Proyecto getSprintAProyecto() {
        return sprintAProyecto;
    }
    
    public void setSprintAProyecto(Proyecto sprintAProyecto) {
        this.sprintAProyecto = sprintAProyecto;
    }
    
    public List<Tarea> getTareasProyecto() {
        try {
            tareasProyecto = proyecto.getTareaList();
        } catch (Exception e) {
        }
        
        return tareasProyecto;
    }
    
    public void setTareasProyecto(List<Tarea> tareasProyecto) {
        this.tareasProyecto = tareasProyecto;
    }
    
    public Tarea getTareaSeleccionada() {
        return tareaSeleccionada;
    }
    
    public void setTareaSeleccionada(Tarea tareaSeleccionada) {
        this.tareaSeleccionada = tareaSeleccionada;
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msg");
        String exito = bundle.getString("exito");
        String tarea_editada = bundle.getString("Tarea_editada");
        FacesMessage msg = new FacesMessage(exito + ": ", tarea_editada);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("SprintCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String prepareEdit() {
        current = (Sprint) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }
    
    public void update() {
        ejbFacade.edit(editSprint);
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msg");
        String exito = bundle.getString("exito");
        String Sprint_editado = bundle.getString("sprint_editado");
        FacesMessage msg = new FacesMessage(exito + ": ", Sprint_editado);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public List<String> nombreProyectos() {
        proyectos = proyectoFacade.findAll();
        ArrayList<String> nombres = new ArrayList<String>();
        for (Proyecto p : proyectos) {
            nombres.add(p.getNombre());
        }
        return nombres;
    }
    
    public List<String> nombreGrupos() {
        grupos = proyecto.getGrupoList();
        ArrayList<String> nombres = new ArrayList<String>();
        for (Grupo g : grupos) {
            nombres.add(g.getNombre());
        }
        return nombres;
    }
    
    public void setProyectos(List<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }
    
    public String destroy() {
        current = (Sprint) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreateModel();
        return "List";
    }
    
    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }
    
    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("SprintDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }
    
    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }
    
    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }
    
    private void recreateModel() {
        items = null;
    }
    
    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }
    
    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }
    
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }
    
    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

//    @FacesConverter(forClass = Sprint.class)
//    public static class SprintControllerConverter implements Converter {
//
//        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
//            if (value == null || value.length() == 0) {
//                return null;
//            }
//            SprintController controller = (SprintController) facesContext.getApplication().getELResolver().
//                    getValue(facesContext.getELContext(), null, "sprintController");
//            return controller.ejbFacade.find(getKey(value));
//        }
//
//        java.math.BigDecimal getKey(String value) {
//            java.math.BigDecimal key;
//            key = new java.math.BigDecimal(value);
//            return key;
//        }
//
//        String getStringKey(java.math.BigDecimal value) {
//            StringBuffer sb = new StringBuffer();
//            sb.append(value);
//            return sb.toString();
//        }
//
//        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
//            if (object == null) {
//                return null;
//            }
//            if (object instanceof Sprint) {
//                Sprint o = (Sprint) object;
//                return getStringKey(o.getId());
//            } else {
//                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + SprintController.class.getName());
//            }
//        }
//    }
    public String redirigir(int estado) {
        if (estado == 1) {
            nuevo = true;
            editar = false;
            reunion = false;
            tablon = false;
        } else if (estado == 2) {
            nuevo = false;
            editar = true;
            tablon = false;
            reunion = false;
        } else if (estado == 3) {
            nuevo = false;
            editar = false;
            tablon = true;
            reunion = false;
        } else if (estado == 4) {
            nuevo = false;
            editar = false;
            tablon = false;
            reunion = true;
        }
        
        return "editar";
    }
    
    public void createNew() {
        newSprint = new Sprint();
        BigDecimal id;
        try {
            id = ejbFacade.maxId().add(BigDecimal.ONE);
        } catch (Exception e) {
            id = BigDecimal.ONE;
        }
        newSprint.setId(id);
        newSprint.setFin(ffin);
        newSprint.setInicio(finicio);
        log.info("PROYECTO -> " + proyecto);
        newSprint.setProyectoId(proyecto);
        if (ffin.after(proyecto.getEntrega())) {
            RequestContext request = RequestContext.getCurrentInstance();
            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msg");
            String error = bundle.getString("Error");
            String fecha_posterior = bundle.getString("Fecha_posterior");
            FacesMessage msg = new FacesMessage(error + ": ", fecha_posterior + ": " + proyecto.getEntrega());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            newLog = new Log();
            newLog.setProyectoId(proyecto);
            try {
                newLog.setId(logFacade.maxId().add(BigDecimal.ONE));
            } catch (Exception e3) {
                newLog.setId(BigDecimal.ONE);
            }
            newLog.setDescripcion("Creación de Sprint");
            newLog.setFecha(new Date());
            
            RequestContext request = RequestContext.getCurrentInstance();
            FacesContext context = FacesContext.getCurrentInstance();
            UsuarioController uc = (UsuarioController) context.getApplication().evaluateExpressionGet(context, "#{usuarioController}", UsuarioController.class);
            Usuario usuarioActual = usuarioFacade.findByMail(uc.getEmail());
            newLog.setUsuarioId(usuarioActual);
            
            logFacade.create(newLog);
            ejbFacade.create(newSprint);
            ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msg");
            String exito = bundle.getString("Exito");
            String Sprint_creado = bundle.getString("Sprint_creado");
            FacesMessage msg = new FacesMessage(exito + ": ", Sprint_creado);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        
        
        
    }
    
    public void createNewReunion() {
        FacesContext context = FacesContext.getCurrentInstance();
        UsuarioController uc = (UsuarioController) context.getApplication().evaluateExpressionGet(context, "#{usuarioController}", UsuarioController.class);
        usuario = usuarioFacade.findByMail(uc.getEmail());
        usuarios = usuarioFacade.findByGrupo(usuario.getGrupoId());
        
        for (Usuario usu : usuarios) {
            nuevosMensajes = new Mensajes();
            nuevosMensajes.setAsunto("Reunión");
            nuevosMensajes.setDestino(usu);
            nuevosMensajes.setOrigen(usuario);
            nuevosMensajes.setFecha(new Date());
            try{
               nuevosMensajes.setId(mensajesFacade.maxId().add(BigDecimal.ONE)); 
            }
            catch(Exception e2){
                nuevosMensajes.setId(BigDecimal.ONE);
            }
            char leido = '0';
            nuevosMensajes.setLeido(leido);
            nuevosMensajes.setMensaje("Se va a realizar una reunión del grupo " + usuario.getGrupoId().getNombre() + ", se celebrará en " + mensajeReunion + " y el dia " + fechaReunion);
            mensajesFacade.create(nuevosMensajes);
        }
        ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msg");
        String exito = bundle.getString("Exito");
        String reunionCreada = bundle.getString("Reunion_creada");
        
        FacesMessage msg = new FacesMessage(exito + ": ", reunionCreada);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public List<Sprint> listSprints() {
        return ejbFacade.findAll();
    }
    
    public void delete(Sprint sprint) {
        ejbFacade.remove(sprint);
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msg");
        String exito = bundle.getString("exito");
        String Sprint_eliminado = bundle.getString("Sprint_eliminado");
        FacesMessage msg = new FacesMessage(exito + ": ", Sprint_eliminado);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void asignar() {
        for (Object t : ta.getTarget()) {
            Tarea a = tareaFacade.findByOneName(t.toString());
            a.setSprintId(editSprint);
            tareaFacade.edit(a);
        }
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msg");
        String exito = bundle.getString("exito");
        String Tareas_asignadas = bundle.getString("Tareas_asignadas");
        FacesMessage msg = new FacesMessage(exito + ": ", Tareas_asignadas);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void asignarSprints() {
        for (Object t : sprintToProyecto.getTarget()) {
            Sprint s = ejbFacade.find(t.toString());
            s.setProyectoId(sprintAProyecto);
            ejbFacade.edit(s);
        }
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msg");
        String informacion = bundle.getString("Informacion");
        String sprints_asignados = bundle.getString("Sprints_asignados");
        FacesMessage msg = new FacesMessage(informacion + ": ", sprints_asignados);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void editaTarea() {
        try {
            tareaFacade.edit(tareaSeleccionada);
            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msg");
            String exito = bundle.getString("Exito");
            String cambios_guardados = bundle.getString("Cambios_guardados");
            FacesMessage msg = new FacesMessage(exito + ": ", cambios_guardados);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception e) {
        }
    }
    
    public boolean activo(Sprint s){
        return s.getFin().after(new Date());
    }
}
