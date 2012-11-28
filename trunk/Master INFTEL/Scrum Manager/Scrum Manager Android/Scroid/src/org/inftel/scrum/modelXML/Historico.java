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
public class Historico implements Serializable {
    private static final long serialVersionUID = 1L;

    private BigDecimal id;
    private Usuario usuario;
    private Grupo grupo;

    public Historico() {
    }

    public Historico(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Historico)) {
            return false;
        }
        Historico other = (Historico) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inftel.scrum.model.Historico[ id=" + id + " ]";
    }

}
