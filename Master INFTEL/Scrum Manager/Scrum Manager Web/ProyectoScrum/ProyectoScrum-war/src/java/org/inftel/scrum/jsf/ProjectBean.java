/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.jsf;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import org.inftel.scrum.ejb.LogFacade;
import org.inftel.scrum.model.Proyecto;
import org.inftel.scrum.ejb.ProyectoFacade;
import org.inftel.scrum.ejb.UsuarioFacade;
import org.inftel.scrum.model.Historico;
import org.inftel.scrum.model.Log;
import org.inftel.scrum.model.Usuario;

/**
 *
 * @author migueqm
 */
@ManagedBean
@ViewScoped
public class ProjectBean implements Serializable {

    @EJB
    private HistoricoFacade historicoFacade;
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private LogFacade logFacade;
    private Log newLog;
    private static final Logger log = Logger.getLogger("org.inftel.scrum.jsf.projectBean");
    private List<Proyecto> listProjects;
    private Proyecto newProject;
    @EJB
    private ProyectoFacade proyectoFacade;
    private Date now = new Date();
    private Proyecto[] selectedProjects;
    private Proyecto editProject;
    private boolean renderNew = true;
    private boolean renderTable = false;
    private boolean createTarea = false;
    private Usuario usuario;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        UsuarioController uc = (UsuarioController) context.getApplication().evaluateExpressionGet(context, "#{usuarioController}", UsuarioController.class);
        usuario = usuarioFacade.findByMail(uc.getEmail());
        refreshProjects();
    }

    public void refreshProjects() {
        listProjects = new ArrayList<Proyecto>();
        //listProjects.add(usuario.getGrupoId().getProyectoId());
        List<Historico> historicoList = usuario.getHistoricoList();
        for (Historico h : historicoList) {
            listProjects.add(h.getGrupo().getProyectoId());
        }
    }

    /**
     * Creates a new instance of projectBean
     */
    public ProjectBean() {
        newProject = new Proyecto();
        listProjects = new ArrayList<Proyecto>();
    }

    public Proyecto getEditProject() {
        refreshProjects();
        return editProject;
    }

    public boolean isCreateTarea() {
        return createTarea;
    }

    public void setCreateTarea(boolean createTarea) {
        this.createTarea = createTarea;
    }

    public void setEditProject(Proyecto editProject) {
        this.editProject = editProject;
    }

    public Date getNow() {
        now = new Date();
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    public Proyecto getNewProject() {
        return newProject;
    }

    public void setNewProject(Proyecto newProject) {
        this.newProject = newProject;
    }

    public Proyecto[] getSelectedProjects() {
        return selectedProjects;
    }

    public void setSelectedProjects(Proyecto[] selectedProjects) {
        this.selectedProjects = selectedProjects;
    }

    public List<Proyecto> getListProjects() {
        return listProjects;
    }

    public void setListProjects(List<Proyecto> listProjects) {
        this.listProjects = listProjects;
    }

    public boolean isRenderNew() {
        return renderNew;
    }

    public void setRenderNew(boolean renderNew) {
        this.renderNew = renderNew;
    }

    public boolean isRenderTable() {
        return renderTable;
    }

    public void setRenderTable(boolean renderTable) {
        this.renderTable = renderTable;
    }

    public void delete(Proyecto p) {
        log.log(Level.INFO, "selected: {0}", p);
        proyectoFacade.remove(p);
        listProjects.remove(p);
        FacesMessage msg = new FacesMessage("SUCCESS: ", "project deleted!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public String redirigir(int estado) {
        if (estado == 1) {
            renderNew = true;
            renderTable = false;
            createTarea = false;
        } else if (estado == 2) {
            renderNew = false;
            renderTable = true;
            createTarea = false;
        } else if (estado == 3) {
            renderNew = false;
            renderTable = false;
            createTarea = true;
        }
        return "project";
    }

    public void createNew() {
        if (newProject != null) {
            if (proyectoFacade.findByProyectoRepetido(newProject.getNombre()) == 0) {
                try {
                    newProject.setId(proyectoFacade.maxId().add(BigDecimal.ONE));
                } catch (Exception e) {
                    newProject.setId(BigDecimal.ONE);
                }

                log.log(Level.INFO, "Insertando proyecto {0}", newProject.getId());

                listProjects.add(newProject);
                proyectoFacade.create(newProject);
                FacesContext context = FacesContext.getCurrentInstance();
                UsuarioController uc = (UsuarioController) context.getApplication().evaluateExpressionGet(context, "#{usuarioController}", UsuarioController.class);

                // se asigna el rol scrum master al usuario que crea el grupo
                usuario = usuarioFacade.findByMail(uc.getEmail());
                usuario.setRol("scrum master");
                usuarioFacade.edit(usuario);

                FacesMessage msg = new FacesMessage("SUCCESS: ", "project created!");
                FacesContext.getCurrentInstance().addMessage(null, msg);

                newLog = new Log();

                try {
                    newLog.setProyectoId(newProject);
                } catch (Exception uno) {
                }
                try {
                    newLog.setId(logFacade.maxId().add(BigDecimal.ONE));
                } catch (Exception dos) {
                    newLog.setId(BigDecimal.ONE);
                }
                try {
                    newLog.setDescripcion("Creación de Proyecto " + newProject.getNombre());
                } catch (Exception tres) {
                }
                try {
                    newLog.setFecha(new Date());
                } catch (Exception cuatro) {
                }
                try {
                    newLog.setUsuarioId(usuario);
                } catch (Exception cinco) {
                }

                logFacade.create(newLog);
            } else {
                FacesMessage msg = new FacesMessage("FALLO: ", "El proyecto ya existe");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } else {
            log.log(Level.WARNING, "Error creating new project, cannot be null.");
        }

    }

    public void updateProjects() {
        proyectoFacade.edit(editProject);
        listProjects = proyectoFacade.findAll();
        FacesMessage msg = new FacesMessage("SUCCESS: ", "project edited!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public boolean activo(Proyecto p) {
        return p.getEntrega().after(getNow());
    }
}
