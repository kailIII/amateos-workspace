/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.util;

import java.util.*;
import org.inftel.scrum.modelXML.*;

/**
 *
 * @author inftel
 */
public class Cast {
    
    public static Proyecto castProyectoFromEntity(org.inftel.scrum.model.Proyecto p) {
        Proyecto proyecto = new Proyecto();
        proyecto.setDescripcion(p.getDescripcion());
        proyecto.setEntrega(p.getEntrega());
        proyecto.setId(p.getId());
        proyecto.setNombre(p.getNombre());
        return proyecto;
    }

    public static Grupo castGrupoFromEntity(org.inftel.scrum.model.Grupo g) {
        Grupo grupo = new Grupo();
        grupo.setDescripcion(g.getDescripcion());
        grupo.setId(g.getId());
        grupo.setNombre(g.getNombre());
        grupo.setProyectoId(castProyectoFromEntity(g.getProyectoId()));
        return grupo;
    }

    public static Sprint castSprintFromEntity(org.inftel.scrum.model.Sprint s) {
        Sprint sprint = new Sprint();
        sprint.setFin(s.getFin());
        sprint.setId(s.getId());
        sprint.setInicio(s.getInicio());
        sprint.setProyectoId(castProyectoFromEntity(s.getProyectoId()));
        List<Tarea> listaTareas = new ArrayList<Tarea>();
        for (org.inftel.scrum.model.Tarea t : s.getTareaList()) {
            Tarea tarea = castTareaFromEntity(t, false);
            tarea.setSprintId(sprint);
        }
        sprint.setTareaList(listaTareas);
        return sprint;
    }

    public static org.inftel.scrum.model.Sprint castSprintToEntity(Sprint s) {
        org.inftel.scrum.model.Sprint sprint = new org.inftel.scrum.model.Sprint();
        sprint.setFin(s.getFin());
        sprint.setId(s.getId());
        sprint.setInicio(s.getInicio());
        sprint.setProyectoId(castProyectoToEntity(s.getProyectoId()));
        List<org.inftel.scrum.model.Tarea> listaTareas = new ArrayList<org.inftel.scrum.model.Tarea>();
        for (Tarea t : s.getTareaList()) {
            org.inftel.scrum.model.Tarea tarea = castTareaToEntity(t);
            tarea.setSprintId(sprint);
        }
        sprint.setTareaList(listaTareas);
        return sprint;
    }

    public static Tarea castTareaFromEntity(org.inftel.scrum.model.Tarea t, boolean proyect) {
        Tarea tarea = new Tarea();
        tarea.setDescripcion(t.getDescripcion());
        tarea.setDone(t.getDone());
        tarea.setDuracion(t.getDuracion());
        tarea.setDuracionEstimado(t.getDuracionEstimado());
        tarea.setPrioridad(t.getPrioridad());
        tarea.setId(t.getId());
        if (!proyect) {
            tarea.setProyectoId(castProyectoFromEntity(t.getProyectoId()));
        }
        tarea.setTotalHoras(t.getTotalHoras());
        return tarea;
    }

    public static org.inftel.scrum.model.Tarea castTareaToEntity(Tarea t) {
        org.inftel.scrum.model.Tarea tarea = new org.inftel.scrum.model.Tarea();
        tarea.setDescripcion(t.getDescripcion());
        tarea.setDone(t.getDone());
        tarea.setDuracion(t.getDuracion());
        tarea.setDuracionEstimado(t.getDuracionEstimado());
        tarea.setFechaFin(t.getFechaFin());
        tarea.setInicio(t.getInicio());
        tarea.setPrioridad(t.getPrioridad());
        tarea.setId(t.getId());
        tarea.setTotalHoras(t.getTotalHoras());
        return tarea;
    }

    public static Historico castHistoricoFromEntity(org.inftel.scrum.model.Historico h) {
        Historico historico = new Historico();
        historico.setId(h.getId());
        historico.setUsuario(castUsuarioFromEntity(h.getUsuario()));
        historico.setGrupo(castGrupoFromEntity(h.getGrupo()));
        return historico;
    }

    public static org.inftel.scrum.model.Historico castHistoricoToEntity(Historico h) {
        org.inftel.scrum.model.Historico historico = new org.inftel.scrum.model.Historico();
        historico.setId(h.getId());
        historico.setUsuario(castUsuarioToEntity(h.getUsuario()));
        historico.setGrupo(castGrupoToEntity(h.getGrupo()));
        return historico;
    }

    public static Log castLogFromEntity(org.inftel.scrum.model.Log l) {
        Log log = new Log();
        log.setDescripcion(l.getDescripcion());
        log.setFecha(l.getFecha());
        log.setId(l.getId());
        log.setProyectoId(castProyectoFromEntity(l.getProyectoId()));
        return log;
    }

    public static org.inftel.scrum.model.Log castLogFromEntity(Log l) {
        org.inftel.scrum.model.Log log = new org.inftel.scrum.model.Log();
        log.setDescripcion(l.getDescripcion());
        log.setFecha(l.getFecha());
        log.setId(l.getId());
        log.setProyectoId(castProyectoToEntity(l.getProyectoId()));
        return log;
    }

    public static Mensajes castMensajesFromEntity(org.inftel.scrum.model.Mensajes m) {
        Mensajes m1 = new Mensajes();
        m1.setAsunto(m.getAsunto());
        m1.setFecha(m.getFecha());
        m1.setId(m.getId());
        m1.setLeido(m.getLeido());
        m1.setMensaje(m.getMensaje());
        m1.setNotificado(m.getNotificado());
        return m1;
    }

    public static org.inftel.scrum.model.Mensajes castMensajesToEntity(Mensajes m) {
        org.inftel.scrum.model.Mensajes m1 = new org.inftel.scrum.model.Mensajes();
        m1.setAsunto(m.getAsunto());
        m1.setFecha(m.getFecha());
        m1.setId(m.getId());
        m1.setLeido(m.getLeido());
        m1.setMensaje(m.getMensaje());
        m1.setNotificado(m.getNotificado());
        return m1;
    }

    public static Usuario castUsuarioFromEntity(org.inftel.scrum.model.Usuario u) {
        Usuario usuario = new Usuario();
//        usuario.setIdC(u.getId());
        usuario.setNombre(u.getNombre());
        usuario.setApellidos(u.getApellidos());
        usuario.setEmail(u.getEmail());
        usuario.setFechanac(u.getFechanac());
        usuario.setPassword(u.getPassword());
        usuario.setRol(u.getRol());
        usuario.setLoggedin(u.getLoggedin());
        usuario.setGrupoId(castGrupoFromEntity(u.getGrupoId()));
        return usuario;
    }

    public static org.inftel.scrum.model.Usuario castUsuarioToEntity(Usuario u) {
        org.inftel.scrum.model.Usuario usuario = new org.inftel.scrum.model.Usuario();
        usuario.setId(u.getId());
        usuario.setNombre(u.getNombre());
        usuario.setApellidos(u.getApellidos());
        usuario.setFechanac(u.getFechanac());
        usuario.setEmail(u.getEmail());
        usuario.setPassword(u.getPassword());
        usuario.setRol(u.getRol());
        usuario.setLoggedin(u.getLoggedin());
        return usuario;
    }

    public static org.inftel.scrum.model.Grupo castGrupoToEntity(Grupo g) {
        org.inftel.scrum.model.Grupo grupo = new org.inftel.scrum.model.Grupo();
        grupo.setDescripcion(g.getDescripcion());
        grupo.setId(g.getId());
        grupo.setNombre(g.getNombre());
        grupo.setProyectoId(castProyectoToEntity(g.getProyectoId()));
        return grupo;
    }

    public static org.inftel.scrum.model.Proyecto castProyectoToEntity(Proyecto p) {
        org.inftel.scrum.model.Proyecto proyecto = new org.inftel.scrum.model.Proyecto();
        proyecto.setDescripcion(p.getDescripcion());
        proyecto.setEntrega(p.getEntrega());
        proyecto.setId(p.getId());
        proyecto.setNombre(p.getNombre());
        return proyecto;
    }

    public static org.inftel.scrum.model.Reuniones castReunionToEntity(Reuniones reunion) {
        org.inftel.scrum.model.Reuniones reuniones = new org.inftel.scrum.model.Reuniones();
        reuniones.setAsunto(reunion.getAsunto());
        reuniones.setFecha(reunion.getFecha());
        reuniones.setLugar(reunion.getLugar());
        return reuniones;
    }

    public static Reuniones castReunionFromEntity(org.inftel.scrum.model.Reuniones reunion) {
        Reuniones reuniones = new Reuniones();
        reuniones.setAsunto(reunion.getAsunto());
        reuniones.setFecha(reunion.getFecha());
        reuniones.setLugar(reunion.getLugar());
        return reuniones;
    }
}
