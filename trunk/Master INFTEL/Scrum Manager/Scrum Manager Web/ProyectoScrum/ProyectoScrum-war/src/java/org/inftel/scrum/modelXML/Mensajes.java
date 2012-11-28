/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inftel.scrum.modelXML;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author migueqm
 */
public class Mensajes implements Serializable {
    private static final long serialVersionUID = 1L;

    private BigDecimal id;

    private String mensaje;

    private Character leido;
    
    private Character notificado;

    private String asunto;

    private Date fecha;

    private Usuario origen;

    private Usuario destino;

    public Mensajes() {
    }

    public Mensajes(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Character getLeido() {
        return leido;
    }

    public void setLeido(Character leido) {
        this.leido = leido;
    }
    
     public Character getNotificado() {
        return notificado;
    }

    public void setNotificado(Character notificado) {
        this.notificado = notificado;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Usuario getOrigen() {
        return origen;
    }

    public void setOrigen(Usuario origen) {
        this.origen = origen;
    }

    public Usuario getDestino() {
        return destino;
    }

    public void setDestino(Usuario destino) {
        this.destino = destino;
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
        if (!(object instanceof Mensajes)) {
            return false;
        }
        Mensajes other = (Mensajes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inftel.scrum.model.Mensajes[ id=" + id + " ]";
    }

}
