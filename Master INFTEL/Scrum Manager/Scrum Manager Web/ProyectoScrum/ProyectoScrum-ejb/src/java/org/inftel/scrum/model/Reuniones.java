/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Fernando
 */
@Entity
@Table(name = "REUNIONES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reuniones.findAll", query = "SELECT r FROM Reuniones r"),
    @NamedQuery(name = "Reuniones.findById", query = "SELECT r FROM Reuniones r WHERE r.id = :id"),
    @NamedQuery(name = "Reuniones.findByAsunto", query = "SELECT r FROM Reuniones r WHERE r.asunto = :asunto"),
    @NamedQuery(name = "Reuniones.findByFecha", query = "SELECT r FROM Reuniones r WHERE r.fecha = :fecha"),
    @NamedQuery(name = "Reuniones.findByLugar", query = "SELECT r FROM Reuniones r WHERE r.lugar = :lugar")})
public class Reuniones implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Size(max = 30)
    @Column(name = "ASUNTO")
    private String asunto;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 50)
    @Column(name = "LUGAR")
    private String lugar;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne
    private Usuario usuarioId;

    public Reuniones() {
    }

    public Reuniones(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
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

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Usuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Usuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reuniones)) {
            return false;
        }
        Reuniones other = (Reuniones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inftel.scrum.model.Reuniones[ id=" + id + " ]";
    }
    
}
