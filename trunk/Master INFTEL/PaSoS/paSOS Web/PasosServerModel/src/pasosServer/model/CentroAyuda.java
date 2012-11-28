/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Juan Antonio
 */
@Entity
@Table(name = "CENTRO_AYUDA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CentroAyuda.findAll", query = "SELECT c FROM CentroAyuda c"),
    @NamedQuery(name = "CentroAyuda.findByIdCentro", query = "SELECT c FROM CentroAyuda c WHERE c.idCentro = :idCentro"),
    @NamedQuery(name = "CentroAyuda.findByDireccion", query = "SELECT c FROM CentroAyuda c WHERE c.direccion = :direccion"),
    @NamedQuery(name = "CentroAyuda.findByNombre", query = "SELECT c FROM CentroAyuda c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "CentroAyuda.findByTelefono", query = "SELECT c FROM CentroAyuda c WHERE c.telefono = :telefono")})
public class CentroAyuda implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_CENTRO")
    private BigDecimal idCentro;
    @Size(max = 30)
    @Column(name = "DIRECCION")
    private String direccion;
    @Size(max = 20)
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "TELEFONO")
    private BigInteger telefono;

    public CentroAyuda() {
    }

    public CentroAyuda(BigDecimal idCentro) {
        this.idCentro = idCentro;
    }

    public BigDecimal getIdCentro() {
        return idCentro;
    }

    public void setIdCentro(BigDecimal idCentro) {
        this.idCentro = idCentro;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigInteger getTelefono() {
        return telefono;
    }

    public void setTelefono(BigInteger telefono) {
        this.telefono = telefono;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCentro != null ? idCentro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CentroAyuda)) {
            return false;
        }
        CentroAyuda other = (CentroAyuda) object;
        if ((this.idCentro == null && other.idCentro != null) || (this.idCentro != null && !this.idCentro.equals(other.idCentro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pasosServer.model.CentroAyuda[ idCentro=" + idCentro + " ]";
    }
    
}
