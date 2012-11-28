/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Juan Antonio
 */
@Entity
@Table(name = "LOCALIZACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Localizacion.findAll", query = "SELECT l FROM Localizacion l"),
    @NamedQuery(name = "Localizacion.findByLongitudP", query = "SELECT l FROM Localizacion l WHERE l.longitudP = :longitudP"),
    @NamedQuery(name = "Localizacion.findByLatitudP", query = "SELECT l FROM Localizacion l WHERE l.latitudP = :latitudP"),
    @NamedQuery(name = "Localizacion.findByLongitudM", query = "SELECT l FROM Localizacion l WHERE l.longitudM = :longitudM"),
    @NamedQuery(name = "Localizacion.findByLatitudM", query = "SELECT l FROM Localizacion l WHERE l.latitudM = :latitudM"),
    @NamedQuery(name = "Localizacion.findByFecha", query = "SELECT l FROM Localizacion l WHERE l.fecha = :fecha"),
    @NamedQuery(name = "Localizacion.findByHora", query = "SELECT l FROM Localizacion l WHERE l.hora = :hora"),
    @NamedQuery(name = "Localizacion.findByIdLocalizacion", query = "SELECT l FROM Localizacion l WHERE l.idLocalizacion = :idLocalizacion")})
public class Localizacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "LONGITUD_P")
    private BigInteger longitudP;
    @Column(name = "LATITUD_P")
    private BigInteger latitudP;
    @Column(name = "LONGITUD_M")
    private BigInteger longitudM;
    @Column(name = "LATITUD_M")
    private BigInteger latitudM;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "HORA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hora;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_LOCALIZACION")
    private BigDecimal idLocalizacion;
    @JoinColumn(name = "ID_ALARMA", referencedColumnName = "ID_ALARMA")
    @ManyToOne(optional = false)
    private Alarma idAlarma;

    public Localizacion() {
    }

    public Localizacion(BigDecimal idLocalizacion) {
        this.idLocalizacion = idLocalizacion;
    }

    public BigInteger getLongitudP() {
        return longitudP;
    }

    public void setLongitudP(BigInteger longitudP) {
        this.longitudP = longitudP;
    }

    public BigInteger getLatitudP() {
        return latitudP;
    }

    public void setLatitudP(BigInteger latitudP) {
        this.latitudP = latitudP;
    }

    public BigInteger getLongitudM() {
        return longitudM;
    }

    public void setLongitudM(BigInteger longitudM) {
        this.longitudM = longitudM;
    }

    public BigInteger getLatitudM() {
        return latitudM;
    }

    public void setLatitudM(BigInteger latitudM) {
        this.latitudM = latitudM;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public BigDecimal getIdLocalizacion() {
        return idLocalizacion;
    }

    public void setIdLocalizacion(BigDecimal idLocalizacion) {
        this.idLocalizacion = idLocalizacion;
    }

    public Alarma getIdAlarma() {
        return idAlarma;
    }

    public void setIdAlarma(Alarma idAlarma) {
        this.idAlarma = idAlarma;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLocalizacion != null ? idLocalizacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Localizacion)) {
            return false;
        }
        Localizacion other = (Localizacion) object;
        if ((this.idLocalizacion == null && other.idLocalizacion != null) || (this.idLocalizacion != null && !this.idLocalizacion.equals(other.idLocalizacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pasosServer.model.Localizacion[ idLocalizacion=" + idLocalizacion + " ]";
    }
    
}
