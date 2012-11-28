/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.model;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
 * @author Juan Antonio
 */
@Entity
@Table(name = "PROTEGIDO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Protegido.findAll", query = "SELECT p FROM Protegido p"),
    @NamedQuery(name = "Protegido.findByIdProtegido", query = "SELECT p FROM Protegido p WHERE p.idProtegido = :idProtegido"),
    @NamedQuery(name = "Protegido.findByNombre", query = "SELECT p FROM Protegido p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Protegido.findByTelefonoMovil", query = "SELECT p FROM Protegido p WHERE p.telefonoMovil = :telefonoMovil"),
    @NamedQuery(name = "Protegido.findByApellidos", query = "SELECT p FROM Protegido p WHERE p.apellidos = :apellidos"),
    @NamedQuery(name = "Protegido.findByFechaNacimiento", query = "SELECT p FROM Protegido p WHERE p.fechaNacimiento = :fechaNacimiento"),
    @NamedQuery(name = "Protegido.findByLongitud", query = "SELECT p FROM Protegido p WHERE p.longitud = :longitud"),
    @NamedQuery(name = "Protegido.findByLatitud", query = "SELECT p FROM Protegido p WHERE p.latitud = :latitud"),
    @NamedQuery(name = "Protegido.findByImei", query = "SELECT p FROM Protegido p WHERE p.imei = :imei")})
public class Protegido implements Serializable {
    @Column(name = "FECHA_NACIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaNacimiento;
    @Lob
    @Column(name = "FOTO")
    private byte[] foto;
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_PROTEGIDO")
    private BigDecimal idProtegido;
    @Size(max = 20)
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "TELEFONO_MOVIL")
    private BigInteger telefonoMovil;
    @Size(max = 30)
    @Column(name = "APELLIDOS")
    private String apellidos;
    @Column(name = "LONGITUD")
    private BigInteger longitud;
    @Column(name = "LATITUD")
    private BigInteger latitud;
    @Size(max = 18)
    @Column(name = "IMEI")
    private String imei;
    @OneToMany(fetch= FetchType.EAGER ,cascade = CascadeType.ALL, mappedBy = "idProtegido")
    private Collection<Maltratador> maltratadorCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProtegido")
    private Collection<Alarma> alarmaCollection;
    @OneToMany(fetch= FetchType.EAGER,mappedBy = "idProtegido")
    private Collection<Contacto> contactoCollection;

    public Protegido() {
    }

    public void addMaltratador(Maltratador maltratador) {
    if (maltratadorCollection == null) {
      maltratadorCollection = new ArrayList<Maltratador>();
    }
    maltratador.setIdProtegido(this);
    maltratadorCollection.add(maltratador);
  }
    
    public Protegido(BigDecimal idProtegido) {
        this.idProtegido = idProtegido;
    }

    public BigDecimal getIdProtegido() {
        return idProtegido;
    }

    public void setIdProtegido(BigDecimal idProtegido) {
        this.idProtegido = idProtegido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigInteger getTelefonoMovil() {
        return telefonoMovil;
    }

    public void setTelefonoMovil(BigInteger telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
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

    @XmlTransient
    public Collection<Maltratador> getMaltratadorCollection() {
        return maltratadorCollection;
    }

    public void setMaltratadorCollection(Collection<Maltratador> maltratadorCollection) {
        this.maltratadorCollection = maltratadorCollection;
    }

    @XmlTransient
    public Collection<Alarma> getAlarmaCollection() {
        return alarmaCollection;
    }

    public void setAlarmaCollection(Collection<Alarma> alarmaCollection) {
        this.alarmaCollection = alarmaCollection;
    }

    @XmlTransient
    public Collection<Contacto> getContactoCollection() {
        return contactoCollection;
    }

    public void setContactoCollection(Collection<Contacto> contactoCollection) {
        this.contactoCollection = contactoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProtegido != null ? idProtegido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Protegido)) {
            return false;
        }
        Protegido other = (Protegido) object;
        if ((this.idProtegido == null && other.idProtegido != null) || (this.idProtegido != null && !this.idProtegido.equals(other.idProtegido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pasosServer.model.Protegido[ idProtegido=" + idProtegido + " ]";
    }
    
    public Image getImage() throws IOException{
        ByteArrayInputStream bis = new ByteArrayInputStream(this.foto);
        Iterator readers = ImageIO.getImageReadersByFormatName("jpeg");
        ImageReader reader = (ImageReader) readers.next();
        Object source = bis; // File or InputStream
        ImageInputStream iis = ImageIO.createImageInputStream(source);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        /*if (isThumbnail) {

            param.setSourceSubsampling(4, 4, 0, 0);

        }*/
        return reader.read(0, param);
    }


    public Serializable getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

}
