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
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.inftel.scrum.ejb.LogFacade;
import org.inftel.scrum.ejb.UsuarioFacade;
import org.inftel.scrum.model.Log;
import org.inftel.scrum.model.Usuario;

/**
 *
 * @author empollica
 */
@ManagedBean
@ViewScoped
public class LogBean implements Serializable{

    @EJB
    private UsuarioFacade usuarioFacade;
    private final static Logger LOGGER = Logger.getLogger(LogBean.class.getName());
    private Usuario usuario;
    @EJB
    private LogFacade logFacade;
    private List<Log> logs;

    public LogBean() {
    }

    @PostConstruct
    public void init() {
        UsuarioController usuarioController = (UsuarioController) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioController");
        usuario = usuarioFacade.findByMail(usuarioController.getEmail());
        LOGGER.info("OBTENIDO USUARIO " + usuario);
    }

    public List<Log> getLogs() {
        logs = logFacade.findByProyecto(usuario.getGrupoId().getProyectoId());
        LOGGER.info("LOGS -> "+logs);
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    public int comparar(Object o1, Object o2) {

        Date fecha1 = (Date) o1;
        Date fecha2 = (Date) o2;

        int result;
        LOGGER.info("COMPARACION DE FECHAS: fecha1=" + fecha1.toString() + " & fecha2=" + fecha2.toString());

        LOGGER.info("AFTER-> " + fecha1.after(fecha2));
        LOGGER.info("BEFORE-> " + fecha1.before(fecha2));


        if (fecha1.after(fecha2)) {
            result = 1;
        } else if (fecha1.before(fecha2)) {
            result = -1;
        } else {
            result = 0;
        }

        LOGGER.info("RESULTADO COMPARACION -> " + result);
        return result;
    }
}
