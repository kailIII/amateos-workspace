/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.jsf;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.inftel.scrum.ejb.LogFacade;
import org.inftel.scrum.ejb.UsuarioFacade;
import org.inftel.scrum.model.Log;
import org.inftel.scrum.model.Usuario;
import java.util.logging.Logger;
import org.inftel.scrum.model.Grupo;
import org.inftel.scrum.model.Proyecto;
import org.inftel.scrum.model.Sprint;
import org.inftel.scrum.model.Tarea;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author empollica
 */
@ManagedBean
@RequestScoped
public class DashboardBean implements Serializable {

    private final static Logger LOGGER = Logger.getLogger(DashboardBean.class.getName());
    @EJB
    private UsuarioFacade usuarioFacade;
    private Usuario usuario;
    private Proyecto proyecto;
    private CartesianChartModel graficoProyecto;
    private Sprint sprint;
    private CartesianChartModel graficoSprint;
    private Tarea tarea;
    private Grupo grupo;
    private List<Usuario> usuariosGrupo;

    public DashboardBean() {
    }

    @PostConstruct
    public void init() {
        UsuarioController usuarioController = (UsuarioController) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioController");
        usuario = usuarioFacade.findByMail(usuarioController.getEmail());
        LOGGER.info("OBTENIDO DASHBOARD USUARIO " + usuario);

        // GRUPO
        grupo = usuario.getGrupoId();
        if (grupo != null) {
            usuariosGrupo = grupo.getUsuarioList();
            usuariosGrupo.remove(usuario);
        }

        // PROYECTO
        if (grupo != null) {
            proyecto = usuario.getGrupoId().getProyectoId();
        } else {
            proyecto = null;
        }
        graficoProyecto = new CartesianChartModel();
        ChartSeries progresoProyecto = new ChartSeries();
        progresoProyecto.setLabel("Progreso");
        if (proyecto != null) {
            progresoProyecto.set("", calcularProgresoProyecto());
        } else {
            progresoProyecto.set("", 0);
        }
        graficoProyecto.addSeries(progresoProyecto);

        // SPRINT
        if (proyecto != null) {
            sprint = obtenerSprintActual();
        } else {
            sprint = null;
        }
        graficoSprint = new CartesianChartModel();
        ChartSeries progresoSprint = new ChartSeries();
        progresoSprint.setLabel("Progreso");
        if (sprint != null) {

            progresoSprint.set("", calcularProgresoSprint());
        } else {
            progresoSprint.set("", 0);
        }
        graficoSprint.addSeries(progresoSprint);

        // TAREA
        tarea = obtenerTareaActual();


    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Tarea getTarea() {
        return tarea;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public CartesianChartModel getGraficoProyecto() {
        return graficoProyecto;
    }

    public void setGraficoProyecto(CartesianChartModel graficoProyecto) {
        this.graficoProyecto = graficoProyecto;
    }

    public CartesianChartModel getGraficoSprint() {
        return graficoSprint;
    }

    public void setGraficoSprint(CartesianChartModel graficoSprint) {
        this.graficoSprint = graficoSprint;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public double calcularProgresoProyecto() {

        int totales = proyecto.getTareaList().size();
        List<Tarea> tareas = proyecto.getTareaList();
        int realizadas = 0;
        char finalizada = '2';
        for (Tarea t : tareas) {
            if (t.getDone() == finalizada) {
                realizadas++;
            }
        }
        double progreso;
        if (totales != 0) {
            progreso = (double) (realizadas * 100 / totales);
        } else {
            progreso = 0;
        }
        return progreso;
    }

    public double calcularProgresoSprint() {

        List<Tarea> tareas = sprint.getTareaList();
        int totales = tareas.size();
        int realizadas = 0;
        char finalizada = '2';
        for (Tarea t : tareas) {
            if (t.getDone() == finalizada) {
                realizadas++;
            }
        }
        double progreso;
        if (totales != 0) {
            progreso = (double) (realizadas * 100 / totales);
        } else {
            progreso = 0;
        }
        return progreso;
    }

    public Sprint obtenerSprintActual() {
        Sprint sprintActual = null;
        List<Sprint> sprintList = proyecto.getSprintList();
        if (!sprintList.isEmpty()) {
            for (Sprint sprint : sprintList) {
                if ((sprint.getInicio().before(new Date())) && ((sprint.getFin().after(new Date())))) {
                    sprintActual = sprint;
                    LOGGER.info("SPRINT EN CURSO: " + sprintActual);
                }
            }
        }
        return sprintActual;
    }

    public Tarea obtenerTareaActual() {
        Tarea tareaActual = null;
        List<Tarea> tareaList = usuario.getTareaList();
        if (!tareaList.isEmpty()) {
            for (Tarea tarea : tareaList) {
                if (tarea.getDone() == '1') { // done=1 -> estado "doing"
                    tareaActual = tarea;
                }
            }
        }
        return tareaActual;
    }
}
