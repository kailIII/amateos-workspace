/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inftel.scrum.modelXML;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author migueqm
 */
public class Tarea implements Serializable {
    private static final long serialVersionUID = 1L;

    private BigDecimal id;

    private Date inicio;

    private BigInteger duracionEstimado;

    private BigInteger duracion;

    private String descripcion;

    private BigInteger prioridad;

    private Character done;

    private Date fechaFin;

    private BigInteger totalhoras;

    private Usuario usuarioId;

    private Sprint sprintId;

    private Proyecto proyectoId;

    public Tarea() {
    }

    public Tarea(BigDecimal id) {
        this.id = id;
    }

    public Tarea(BigDecimal id, BigInteger prioridad) {
        this.id = id;
        this.prioridad = prioridad;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public BigInteger getDuracionEstimado() {
        return duracionEstimado;
    }

    public void setDuracionEstimado(BigInteger duracionEstimado) {
        this.duracionEstimado = duracionEstimado;
    }

    public BigInteger getDuracion() {
        return duracion;
    }

    public void setDuracion(BigInteger duracion) {
        this.duracion = duracion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigInteger getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(BigInteger prioridad) {
        this.prioridad = prioridad;
    }

    public Character getDone() {
        return done;
    }

    public void setDone(Character done) {
        this.done = done;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public BigInteger getTotalHoras() {
        return totalhoras;
    }

    public void setTotalHoras(BigInteger totalhoras) {
        this.totalhoras = totalhoras;
    }

    public Usuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Usuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Sprint getSprintId() {
        return sprintId;
    }

    public void setSprintId(Sprint sprintId) {
        this.sprintId = sprintId;
    }

    public Proyecto getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Proyecto proyectoId) {
        this.proyectoId = proyectoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof Tarea)) {
            return false;
        }
        Tarea other = (Tarea) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inftel.scrum.model.Tarea[ id=" + id + " ]";
    }

}
