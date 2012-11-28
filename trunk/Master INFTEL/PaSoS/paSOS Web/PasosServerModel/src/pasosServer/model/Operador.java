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
import javax.persistence.Id;
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
@Table(name = "OPERADOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Operador.findAll", query = "SELECT o FROM Operador o"),
    @NamedQuery(name = "Operador.findByIdOperador", query = "SELECT o FROM Operador o WHERE o.idOperador = :idOperador"),
    @NamedQuery(name = "Operador.findByNombre", query = "SELECT o FROM Operador o WHERE o.nombre = :nombre"),
    @NamedQuery(name = "Operador.findByApellidos", query = "SELECT o FROM Operador o WHERE o.apellidos = :apellidos"),
    @NamedQuery(name = "Operador.findByExtensionTelefono", query = "SELECT o FROM Operador o WHERE o.extensionTelefono = :extensionTelefono"),
    @NamedQuery(name = "Operador.findByLogin", query = "SELECT o FROM Operador o WHERE o.login = :login"),
    @NamedQuery(name = "Operador.findByPassword", query = "SELECT o FROM Operador o WHERE o.password = :password")})
public class Operador implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_OPERADOR")
    private BigDecimal idOperador;
    @Size(max = 20)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 30)
    @Column(name = "APELLIDOS")
    private String apellidos;
    @Column(name = "EXTENSION_TELEFONO")
    private BigInteger extensionTelefono;
    @Size(max = 20)
    @Column(name = "LOGIN")
    private String login;
    @Size(max = 20)
    @Column(name = "PASSWORD")
    private String password;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idOperador")
    private Collection<Alarma> alarmaCollection;

    public Operador() {
    }

    public Operador(BigDecimal idOperador) {
        this.idOperador = idOperador;
    }

    public BigDecimal getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(BigDecimal idOperador) {
        this.idOperador = idOperador;
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

    public BigInteger getExtensionTelefono() {
        return extensionTelefono;
    }

    public void setExtensionTelefono(BigInteger extensionTelefono) {
        this.extensionTelefono = extensionTelefono;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        hash += (idOperador != null ? idOperador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Operador)) {
            return false;
        }
        Operador other = (Operador) object;
        if ((this.idOperador == null && other.idOperador != null) || (this.idOperador != null && !this.idOperador.equals(other.idOperador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pasosServer.model.Operador[ idOperador=" + idOperador + " ]";
    }
    
}
