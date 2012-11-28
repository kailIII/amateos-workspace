/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Fernando
 */
@Entity
@Table(name = "TAREA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tarea.findAll", query = "SELECT t FROM Tarea t"),
    @NamedQuery(name = "Tarea.findById", query = "SELECT t FROM Tarea t WHERE t.id = :id"),
    @NamedQuery(name = "Tarea.findByInicio", query = "SELECT t FROM Tarea t WHERE t.inicio = :inicio"),
    @NamedQuery(name = "Tarea.findByDuracionEstimado", query = "SELECT t FROM Tarea t WHERE t.duracionEstimado = :duracionEstimado"),
    @NamedQuery(name = "Tarea.findByDuracion", query = "SELECT t FROM Tarea t WHERE t.duracion = :duracion"),
    @NamedQuery(name = "Tarea.findByDescripcion", query = "SELECT t FROM Tarea t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "Tarea.findByPrioridad", query = "SELECT t FROM Tarea t WHERE t.prioridad = :prioridad"),
    @NamedQuery(name = "Tarea.findByDone", query = "SELECT t FROM Tarea t WHERE t.done = :done"),
    @NamedQuery(name = "Tarea.findByFechaFin", query = "SELECT t FROM Tarea t WHERE t.fechaFin = :fechaFin"),
    @NamedQuery(name = "Tarea.findByTotalhoras", query = "SELECT t FROM Tarea t WHERE t.totalhoras = :totalhoras")})
public class Tarea implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inicio;
    @Column(name = "DURACION_ESTIMADO")
    private BigInteger duracionEstimado;
    @Column(name = "DURACION")
    private BigInteger duracion;
    @Size(max = 500)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRIORIDAD")
    private BigInteger prioridad;
    @Column(name = "DONE")
    private Character done;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Column(name = "TOTALHORAS")
    private BigInteger totalhoras;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne(fetch= FetchType.EAGER)
    private Usuario usuarioId;
    @JoinColumn(name = "SPRINT_ID", referencedColumnName = "ID")
    @ManyToOne(fetch= FetchType.EAGER)
    private Sprint sprintId;
    @JoinColumn(name = "PROYECTO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false,fetch= FetchType.EAGER)
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

    public BigInteger getTotalhoras() {
        return totalhoras;
    }

    public void setTotalhoras(BigInteger totalhoras) {
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
        // TODO: Warning - this method won't work in the case the id fields are not set
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
