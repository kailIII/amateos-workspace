/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author empollica
 */
@Entity
@Table(name = "PROYECTO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Proyecto.findAll", query = "SELECT p FROM Proyecto p"),
    @NamedQuery(name = "Proyecto.findById", query = "SELECT p FROM Proyecto p WHERE p.id = :id"),
    @NamedQuery(name = "Proyecto.findByDescripcion", query = "SELECT p FROM Proyecto p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "Proyecto.findByEntrega", query = "SELECT p FROM Proyecto p WHERE p.entrega = :entrega"),
    @NamedQuery(name = "Proyecto.findByNombre", query = "SELECT p FROM Proyecto p WHERE p.nombre = :nombre")})
public class Proyecto implements Serializable {
    @Column(name = "ENTREGA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date entrega;
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Size(max = 500)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 500)
    @Column(name = "NOMBRE")
    private String nombre;
    @OneToMany(mappedBy = "proyectoId")
    private List<Sprint> sprintList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proyectoId")
    private List<Tarea> tareaList;
    @OneToMany(mappedBy = "proyectoId")
    private List<Log> logList;
    @OneToMany(mappedBy = "proyectoId")
    private List<Grupo> grupoList;

    public Proyecto() {
    }

    public Proyecto(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<Sprint> getSprintList() {
        return sprintList;
    }

    public void setSprintList(List<Sprint> sprintList) {
        this.sprintList = sprintList;
    }

    @XmlTransient
    public List<Tarea> getTareaList() {
        return tareaList;
    }

    public void setTareaList(List<Tarea> tareaList) {
        this.tareaList = tareaList;
    }

    @XmlTransient
    public List<Log> getLogList() {
        return logList;
    }

    public void setLogList(List<Log> logList) {
        this.logList = logList;
    }

    @XmlTransient
    public List<Grupo> getGrupoList() {
        return grupoList;
    }

    public void setGrupoList(List<Grupo> grupoList) {
        this.grupoList = grupoList;
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
        if (!(object instanceof Proyecto)) {
            return false;
        }
        Proyecto other = (Proyecto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inftel.scrum.model.Proyecto[ id=" + id + " ]";
    }

    public Date getEntrega() {
        return entrega;
    }

    public void setEntrega(Date entrega) {
        this.entrega = entrega;
    }
    
}
