/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * @author Juan Antonio
 */
@Entity
@Table(name = "ALARMA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alarma.findAll", query = "SELECT a FROM Alarma a"),
    @NamedQuery(name = "Alarma.findByIdAlarma", query = "SELECT a FROM Alarma a WHERE a.idAlarma = :idAlarma"),
    @NamedQuery(name = "Alarma.findByFechaHora", query = "SELECT a FROM Alarma a WHERE a.fechaHora = :fechaHora"),
    @NamedQuery(name = "Alarma.findByEstadoAlarma", query = "SELECT a FROM Alarma a WHERE a.estadoAlarma = :estadoAlarma"),
    @NamedQuery(name = "Alarma.findByTipo", query = "SELECT a FROM Alarma a WHERE a.tipo = :tipo"),
    @NamedQuery(name = "Alarma.findByDescripcion", query = "SELECT a FROM Alarma a WHERE a.descripcion = :descripcion")})
public class Alarma implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_ALARMA")
    private BigDecimal idAlarma;
    @Column(name = "FECHA_HORA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @Column(name = "ESTADO_ALARMA")
    private BigInteger estadoAlarma;
    @Column(name = "TIPO")
    private BigInteger tipo;
    @Size(max = 200)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @JoinColumn(name = "ID_PROTEGIDO", referencedColumnName = "ID_PROTEGIDO")
    @ManyToOne(optional = false)
    private Protegido idProtegido;
    @JoinColumn(name = "ID_OPERADOR", referencedColumnName = "ID_OPERADOR")
    @ManyToOne(optional = false)
    private Operador idOperador;
    @JoinColumn(name = "ID_MALTRATADOR", referencedColumnName = "ID_MALTRATADOR")
    @ManyToOne(optional = false)
    private Maltratador idMaltratador;

    public Alarma() {
    }

    public Alarma(BigDecimal idAlarma) {
        this.idAlarma = idAlarma;
    }

    public BigDecimal getIdAlarma() {
        return idAlarma;
    }

    public void setIdAlarma(BigDecimal idAlarma) {
        this.idAlarma = idAlarma;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public BigInteger getEstadoAlarma() {
        return estadoAlarma;
    }

    public void setEstadoAlarma(BigInteger estadoAlarma) {
        this.estadoAlarma = estadoAlarma;
    }

    public BigInteger getTipo() {
        return tipo;
    }

    public void setTipo(BigInteger tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Protegido getIdProtegido() {
        return idProtegido;
    }

    public void setIdProtegido(Protegido idProtegido) {
        this.idProtegido = idProtegido;
    }

    public Operador getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(Operador idOperador) {
        this.idOperador = idOperador;
    }

    public Maltratador getIdMaltratador() {
        return idMaltratador;
    }

    public void setIdMaltratador(Maltratador idMaltratador) {
        this.idMaltratador = idMaltratador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAlarma != null ? idAlarma.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alarma)) {
            return false;
        }
        Alarma other = (Alarma) object;
        if ((this.idAlarma == null && other.idAlarma != null) || (this.idAlarma != null && !this.idAlarma.equals(other.idAlarma))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pasosServer.model.Alarma[ idAlarma=" + idAlarma + " ]";
    }
    
}
