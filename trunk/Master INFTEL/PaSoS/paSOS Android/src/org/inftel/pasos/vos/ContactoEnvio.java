package org.inftel.pasos.vos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class ContactoEnvio implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private BigInteger telefonoContacto;
    private String nombre;
    private String email; 
    private BigDecimal idContacto;

    public ContactoEnvio() {
    }

    public ContactoEnvio(BigDecimal idContacto) {
        this.idContacto = idContacto;
    }

    public BigInteger getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(BigInteger telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(BigDecimal idContacto) {
        this.idContacto = idContacto;
    }
    
    @Override
    public String toString() {
        return "pasosServer.model.Contacto[ idContacto=" + idContacto + " ]";
    }
    
}
