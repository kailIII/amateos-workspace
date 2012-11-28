/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.jsf;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.inftel.scrum.ejb.LogFacade;
import org.inftel.scrum.ejb.SprintFacade;
import org.inftel.scrum.ejb.UsuarioFacade;
import org.inftel.scrum.model.Log;
import org.inftel.scrum.model.Sprint;
import org.inftel.scrum.model.Tarea;
import org.inftel.scrum.model.Usuario;
import org.primefaces.context.RequestContext;
import org.primefaces.event.DragDropEvent;


/**
 *
 * @author empollica
 */
@ManagedBean
@ViewScoped
public class TareaBean implements Serializable{
    @EJB
    private SprintFacade sprintFacade;

    @EJB
    private LogFacade logFacade;
    private Log newLog;
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private org.inftel.scrum.ejb.TareaFacade tareaFacade;
    
    private List<Tarea> listaTareas;
    private List<Tarea> listaTareasTodo = new ArrayList<Tarea>();
    private List<Tarea> listaTareasDoing = new ArrayList<Tarea>();
    private List<Tarea> listaTareasDone = new ArrayList<Tarea>();
    private Tarea editTarea;
    private Usuario usuarioActual;
    private static final Logger log = Logger.getLogger("org.inftel.scrum.jsf.tareaBean");
    //Variables de Fernando
    private SprintBean sprint;
    private Usuario usuario;
    private Character done;
    private String descripcion;
    private BigInteger duracion;
    private BigInteger duracionEstimada;
    private Date inicio;
    private String nombreUsuario;
    private int prioridad;

    public TareaBean() {
        log.info("CREADO BEAN TAREA!");
    }

    public List<Tarea> getListaTareas() {
        return listaTareas;
    }

    @PostConstruct
    public void init() {
        RequestContext request = RequestContext.getCurrentInstance();
        FacesContext context = FacesContext.getCurrentInstance();
        UsuarioController uc = (UsuarioController) context.getApplication().evaluateExpressionGet(context, "#{usuarioController}", UsuarioController.class);
        usuarioActual = usuarioFacade.findByMail(uc.getEmail());
        log.log(Level.INFO, "USUARIO ACTUAL: {0}", usuarioActual.getEmail());
        cargarListasTareas();
    }

    public void cargarListasTareas() {
        this.listaTareasTodo = new ArrayList<Tarea>();
        this.listaTareasDoing = new ArrayList<Tarea>();
        this.listaTareasDone = new ArrayList<Tarea>();
        this.listaTareas = new ArrayList<Tarea>();

        log.log(Level.INFO, "CARGANDO LISTA DE TAREAS");
        if (usuarioActual.getGrupoId() != null) {
            List<Sprint> sprintList = sprintFacade.findByProyecto(usuarioActual.getGrupoId().getProyectoId());
            log.info("OBTENIDA LISTA SPRINTS -> "+sprintList);
            for (Sprint sprint : sprintList) {
                if ((sprint.getInicio().before(new Date())) && ((sprint.getFin().after(new Date())))) {
                    for (Tarea t : sprint.getTareaList()) {
                        if (t.getDone() == '0') {
                            this.listaTareasTodo.add(t);
                            log.info("AGREGADA TAREA DONE 0-> "+t.getDescripcion());
                        } else if (t.getDone() == '1') {
                            log.info("AGREGADA TAREA DONE 1-> "+t.getDescripcion());
                            this.listaTareasDoing.add(t);
                        } else if (t.getDone() == '2') {
                            log.info("AGREGADA TAREA DONE 2-> "+t.getDescripcion());
                            this.listaTareasDone.add(t);
                        } else {
                            log.log(Level.WARNING, "Tarea en estado erroneo: debe ser TODO, DOING o DONE");
                        }
                    }
                }
            }

            this.listaTareas.addAll(this.listaTareasTodo);
            this.listaTareas.addAll(this.listaTareasDoing);
            this.listaTareas.addAll(this.listaTareasDone);
        } else {
            FacesMessage msg = new FacesMessage("Fallo: ", "el usuario no pertenece a ningún grupo");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void setListaTareas(List<Tarea> listaTareas) {
        this.listaTareas = listaTareas;
    }

    public List<Tarea> getListaTareasDoing() {
        //cargarListasTareas();
        return listaTareasDoing;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public void setListaTareasDoing(List<Tarea> listaTareasDoing) {
        this.listaTareasDoing = listaTareasDoing;
    }

    public List<Tarea> getListaTareasDone() {
        //cargarListasTareas();
        return listaTareasDone;
    }

    public void setListaTareasDone(List<Tarea> listaTareasDone) {
        this.listaTareasDone = listaTareasDone;
    }

    public List<Tarea> getListaTareasTodo() {
        //cargarListasTareas();
        return listaTareasTodo;
    }

    public void setListaTareasTodo(List<Tarea> listaTareasTodo) {
        this.listaTareasTodo = listaTareasTodo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TareaBean other = (TareaBean) obj;
        if (this.listaTareas != other.listaTareas && (this.listaTareas == null || !this.listaTareas.equals(other.listaTareas))) {
            return false;
        }
        if (this.listaTareasTodo != other.listaTareasTodo && (this.listaTareasTodo == null || !this.listaTareasTodo.equals(other.listaTareasTodo))) {
            return false;
        }
        if (this.listaTareasDoing != other.listaTareasDoing && (this.listaTareasDoing == null || !this.listaTareasDoing.equals(other.listaTareasDoing))) {
            return false;
        }
        if (this.listaTareasDone != other.listaTareasDone && (this.listaTareasDone == null || !this.listaTareasDone.equals(other.listaTareasDone))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.listaTareas != null ? this.listaTareas.hashCode() : 0);
        hash = 37 * hash + (this.listaTareasTodo != null ? this.listaTareasTodo.hashCode() : 0);
        hash = 37 * hash + (this.listaTareasDoing != null ? this.listaTareasDoing.hashCode() : 0);
        hash = 37 * hash + (this.listaTareasDone != null ? this.listaTareasDone.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "TareaController{" + "listaTareas=" + listaTareas + '}';
    }

    public void onDropTodo(DragDropEvent event) {
        Tarea tarea = (Tarea) event.getData();
        if (tarea.getDone() == '1') {
            log.log(Level.INFO, "DROP ON TODO:{0} {1}", new Object[]{tarea.getDone(), tarea.getDuracion()});
            tarea.setDone('0');
            tarea.setUsuarioId(null);

            listaTareasDoing.remove(tarea);
            listaTareasDone.add(tarea);
        }
    }

    public void onDropDoing(DragDropEvent event) {
        Tarea tarea = (Tarea) event.getData();
        if (tarea.getDone() == '0') {
            log.log(Level.INFO, "DROP ON DOING:{0} {1}", new Object[]{tarea.getDone(), tarea.getDuracion()});
            tarea.setDone('1');
            tarea.setUsuarioId(usuarioActual);
            tarea.setInicio(new Date());
            listaTareasTodo.remove(tarea);
            listaTareasDoing.add(tarea);
        }
    }

    public void onDropDone(DragDropEvent event) {
        Tarea tarea = (Tarea) event.getData();
        if (tarea.getDone() == '1') {
            log.log(Level.INFO, "DROP ON DONE:{0} {1}", new Object[]{tarea.getDone(), tarea.getDuracion()});
            tarea.setDone('2');
            tarea.setDuracion(BigInteger.valueOf(0));
            tarea.setFechaFin(new Date());

            listaTareasDoing.remove(tarea);
            listaTareasDone.add(tarea);
        }
    }

    public void save() {
        log.log(Level.INFO, "GUARDANDO MODIFICACIONES EN LA BBDD");
        this.listaTareas = new ArrayList<Tarea>();
        this.listaTareas.addAll(this.listaTareasTodo);
        this.listaTareas.addAll(this.listaTareasDoing);
        this.listaTareas.addAll(this.listaTareasDone);

        for (Tarea tarea : listaTareas) {
            tareaFacade.edit(tarea);
        }
        FacesMessage msg = new FacesMessage("SUCCESS: ", "guardando cambios");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void cancel() {
        log.log(Level.INFO, "CANCELANDO");
        cargarListasTareas();
        FacesMessage msg = new FacesMessage("SUCCESS: ", "cancelando cambios");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    //De aquí al final es de Fernando   
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Character getDone() {
        return done;
    }

    public void setDone(Character done) {
        this.done = done;
    }

    public BigInteger getDuracion() {
        return duracion;
    }

    public void setDuracion(BigInteger duracion) {
        this.duracion = duracion;
    }

    public BigInteger getDuracionEstimada() {
        return duracionEstimada;
    }

    public void setDuracionEstimada(BigInteger duracionEstimada) {
        this.duracionEstimada = duracionEstimada;
    }

    public Tarea getEditTarea() {
        return editTarea;
    }

    public void setEditTarea(Tarea editTarea) {
        this.editTarea = editTarea;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public SprintBean getSprint() {
        return sprint;
    }

    public void setSprint(SprintBean sprint) {
        this.sprint = sprint;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public void doEditTarea(Tarea tarea) {
        log.log(Level.INFO, "EDITANDO:{0} {1}", new Object[]{tarea.getDone(), tarea.getDuracion()});
    }

    public void creaTarea() {

        if (descripcion != null && duracionEstimada != null && usuarioActual.getGrupoId() != null) {
            Long repetida = tareaFacade.findByTareaRepetida(descripcion, usuarioActual.getGrupoId().getProyectoId());
            if (repetida == 0) {

                log.info("pasa el if");
                done = '0';
                FacesContext context = FacesContext.getCurrentInstance();
                sprint = (SprintBean) context.getApplication().evaluateExpressionGet(context, "#{sprintBean}", SprintBean.class);
                Tarea newTarea = new Tarea();
                try {
                    newTarea.setId(tareaFacade.maxId().add(BigDecimal.ONE));
                } catch (Exception e) {
                    newTarea.setId(BigDecimal.ONE);
                }
                newTarea.setDone(done);
                newTarea.setTotalHoras(BigInteger.ZERO);
                newTarea.setProyectoId(usuarioActual.getGrupoId().getProyectoId());
                log.info("SE VA A ASIGNAR LA TAREA AL SPRINT -> "+sprint.getEditSprint());
                newTarea.setSprintId(sprint.getEditSprint());
                newTarea.setPrioridad(new BigInteger(Integer.toString(prioridad)));
                newTarea.setDescripcion(descripcion);
                newTarea.setDuracionEstimado(duracionEstimada);
                newTarea.setDuracion(duracionEstimada);
                newTarea.setSprintId(sprint.getEditSprint());
                log.info("SE VA A CREAR LA NUEVA TAREA");
                tareaFacade.create(newTarea);
                log.info("SE HA CREADO LA TAREA");
                newLog = new Log();
                try {
                    newLog.setProyectoId(usuarioActual.getGrupoId().getProyectoId());
                } catch (Exception e1) {
                }
                try {
                    newLog.setId(logFacade.maxId().add(BigDecimal.ONE));
                } catch (Exception e2) {
                    newLog.setId(BigDecimal.ONE);
                }
                try {
                    newLog.setDescripcion("Creación de Tarea " + descripcion);
                } catch (Exception e3) {
                }
                try {
                    newLog.setFecha(new Date());
                } catch (Exception e5) {
                }
                try {
                    newLog.setUsuarioId(usuarioActual);
                } catch (Exception e4) {
                }
                logFacade.create(newLog);
                FacesMessage msg = new FacesMessage("SUCCESS: ", "Tarea creada");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                log.info("La tarea ya existe para el proyecto del usuario");
                FacesMessage msg = new FacesMessage("FALLO: ", "La tarea ya existe para el proyecto.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } else {
            log.info("Fallo comprobacion grupo y campos de tarea");
            FacesMessage msg = new FacesMessage("FALLO: ", "Compruebe que todos los campos están completos y que el usuario pertenece a un grupo.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    //devuelve a la vista si la tarea es urgente o no(prioridad 1)
    public boolean urgent(Tarea t) {
        return (t.getPrioridad().compareTo(BigInteger.ONE) == 0);
    }
}
