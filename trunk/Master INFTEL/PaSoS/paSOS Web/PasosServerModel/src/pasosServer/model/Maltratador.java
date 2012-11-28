/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Juan Antonio
 */
@Entity
@Table(name = "MALTRATADOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Maltratador.findAll", query = "SELECT m FROM Maltratador m"),
    @NamedQuery(name = "Maltratador.findByIdMaltratador", query = "SELECT m FROM Maltratador m WHERE m.idMaltratador = :idMaltratador"),
    @NamedQuery(name = "Maltratador.findByNombre", query = "SELECT m FROM Maltratador m WHERE m.nombre = :nombre"),
    @NamedQuery(name = "Maltratador.findByApellidos", query = "SELECT m FROM Maltratador m WHERE m.apellidos = :apellidos"),
    @NamedQuery(name = "Maltratador.findByDispositivo", query = "SELECT m FROM Maltratador m WHERE m.dispositivo = :dispositivo"),
    @NamedQuery(name = "Maltratador.findByDistanciaAlejamiento", query = "SELECT m FROM Maltratador m WHERE m.distanciaAlejamiento = :distanciaAlejamiento"),
    @NamedQuery(name = "Maltratador.findByLongitud", query = "SELECT m FROM Maltratador m WHERE m.longitud = :longitud"),
    @NamedQuery(name = "Maltratador.findByLatitud", query = "SELECT m FROM Maltratador m WHERE m.latitud = :latitud"),
    @NamedQuery(name = "Maltratador.findByImei", query = "SELECT m FROM Maltratador m WHERE m.imei = :imei")})
public class Maltratador implements Serializable {
    @Lob
    @Column(name = "FOTO")
    private byte[] foto;
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_MALTRATADOR")
    private BigDecimal idMaltratador;
    @Size(max = 20)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 30)
    @Column(name = "APELLIDOS")
    private String apellidos;
    @Column(name = "DISPOSITIVO")
    private BigInteger dispositivo;
    @Column(name = "DISTANCIA_ALEJAMIENTO")
    private BigInteger distanciaAlejamiento;
    @Column(name = "LONGITUD")
    private BigInteger longitud;
    @Column(name = "LATITUD")
    private BigInteger latitud;
    @Size(max = 18)
    @Column(name = "IMEI")
    private String imei;
    @JoinColumn(name = "ID_PROTEGIDO", referencedColumnName = "ID_PROTEGIDO")
    @ManyToOne(fetch= FetchType.EAGER,cascade = CascadeType.ALL, optional = false)
    private Protegido idProtegido;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idMaltratador")
    private Collection<Alarma> alarmaCollection;

    public Maltratador() {
    }

    public Maltratador(BigDecimal idMaltratador) {
        this.idMaltratador = idMaltratador;
    }

    public BigDecimal getIdMaltratador() {
        return idMaltratador;
    }

    public void setIdMaltratador(BigDecimal idMaltratador) {
        this.idMaltratador = idMaltratador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public BigInteger getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(BigInteger dispositivo) {
        this.dispositivo = dispositivo;
    }

    public BigInteger getDistanciaAlejamiento() {
        return distanciaAlejamiento;
    }

    public void setDistanciaAlejamiento(BigInteger distanciaAlejamiento) {
        this.distanciaAlejamiento = distanciaAlejamiento;
    }

    public BigInteger getLongitud() {
        return longitud;
    }

    public void setLongitud(BigInteger longitud) {
        this.longitud = longitud;
    }

    public BigInteger getLatitud() {
        return latitud;
    }

    public void setLatitud(BigInteger latitud) {
        this.latitud = latitud;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Protegido getIdProtegido() {
        return idProtegido;
    }

    public void setIdProtegido(Protegido idProtegido) {
        this.idProtegido = idProtegido;
    }

    @XmlTransient
    public Collection<Alarma> getAlarmaCollection() {
        return alarmaCollection;
    }

    public void setAlarmaCollection(Collection<Alarma> alarmaCollection) {
        this.alarmaCollection = alarmaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMaltratador != null ? idMaltratador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Maltratador)) {
            return false;
        }
        Maltratador other = (Maltratador) object;
        if ((this.idMaltratador == null && other.idMaltratador != null) || (this.idMaltratador != null && !this.idMaltratador.equals(other.idMaltratador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pasosServer.model.Maltratador[ idMaltratador=" + idMaltratador + " ]";
    }

    public Serializable getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
    
}
