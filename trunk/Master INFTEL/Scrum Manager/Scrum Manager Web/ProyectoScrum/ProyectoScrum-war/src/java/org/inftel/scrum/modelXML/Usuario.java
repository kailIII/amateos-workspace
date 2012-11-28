/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inftel.scrum.modelXML;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author migueqm
 */
public class Usuario implements Serializable{

    private BigDecimal id;
    private static final long serialVersionUID = 8906526722842529827L;
    private String nombre;

    private String apellidos;

    private String email;

    private Date fechanac;

    private String password;

    private String rol;

    private char loggedin;

    private List<Historico> historicoList;

    private List<Tarea> tareaList;

    private Grupo grupoId;

    private List<Log> logList;

    private List<Mensajes> mensajesList;

    private List<Mensajes> mensajesList1;

    public Usuario() {
    }

//    public Usuario(BigDecimal id) {
//        this.id = id;
//    }
//
//    public Usuario(BigDecimal id, char loggedin) {
//        this.id = id;
//        this.loggedin = loggedin;
//    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFechanac() {
        return fechanac;
    }

    public void setFechanac(Date fechanac) {
        this.fechanac = fechanac;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public char getLoggedin() {
        return loggedin;
    }

    public void setLoggedin(char loggedin) {
        this.loggedin = loggedin;
    }

    public List<Historico> getHistoricoList() {
        return historicoList;
    }

    public void setHistoricoList(List<Historico> historicoList) {
        this.historicoList = historicoList;
    }

    public List<Tarea> getTareaList() {
        return tareaList;
    }

    public void setTareaList(List<Tarea> tareaList) {
        this.tareaList = tareaList;
    }

    public Grupo getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(Grupo grupoId) {
        this.grupoId = grupoId;
    }

    public List<Log> getLogList() {
        return logList;
    }

    public void setLogList(List<Log> logList) {
        this.logList = logList;
    }

    public List<Mensajes> getMensajesList() {
        return mensajesList;
    }

    public void setMensajesList(List<Mensajes> mensajesList) {
        this.mensajesList = mensajesList;
    }

    public List<Mensajes> getMensajesList1() {
        return mensajesList1;
    }

    public void setMensajesList1(List<Mensajes> mensajesList1) {
        this.mensajesList1 = mensajesList1;
    }
//
//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (id != null ? id.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are
//        // not set
//        if (!(object instanceof Usuario)) {
//            return false;
//        }
//        Usuario other = (Usuario) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }
}
