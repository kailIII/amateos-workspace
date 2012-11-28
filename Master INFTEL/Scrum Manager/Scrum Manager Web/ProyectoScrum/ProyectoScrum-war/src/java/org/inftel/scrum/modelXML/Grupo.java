/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inftel.scrum.modelXML;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author migueqm
 */

public class Grupo implements Serializable {
    private static final long serialVersionUID = 1L;

    private BigDecimal id;

    private String nombre;

    private String descripcion;

    private Proyecto proyectoId;

    public Grupo() {
    }

    public Grupo(BigDecimal id) {
        this.id = id;
    }

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        if (!(object instanceof Grupo)) {
            return false;
        }
        Grupo other = (Grupo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inftel.scrum.model.Grupo[ id=" + id + " ]";
    }

}
