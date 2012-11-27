/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comprarador.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
 * @author albertomateos
 */
@Entity
@Table(name = "subsubcategorias")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Subsubcategorias.findAll", query = "SELECT s FROM Subsubcategorias s"),
    @NamedQuery(name = "Subsubcategorias.findById", query = "SELECT s FROM Subsubcategorias s WHERE s.id = :id"),
    @NamedQuery(name = "Subsubcategorias.findByNombre", query = "SELECT s FROM Subsubcategorias s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "Subsubcategorias.findByPosicion", query = "SELECT s FROM Subsubcategorias s WHERE s.posicion = :posicion"),
    @NamedQuery(name = "Subsubcategorias.findByCreado", query = "SELECT s FROM Subsubcategorias s WHERE s.creado = :creado"),
    @NamedQuery(name = "Subsubcategorias.findByCategoria", query = "SELECT s FROM Subsubcategorias s WHERE s.categoria = :categoria"),
    @NamedQuery(name = "Subsubcategorias.findBySubcategoria", query = "SELECT s FROM Subsubcategorias s WHERE s.subcategoria = :subcategoria")})
public class Subsubcategorias implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "posicion")
    private int posicion;
    @Basic(optional = false)
    @Column(name = "creado")
    @Temporal(TemporalType.DATE)
    private Date creado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "categoria")
    private int categoria;
    @Basic(optional = false)
    @NotNull
    @Column(name = "subcategoria")
    private int subcategoria;

    public Subsubcategorias() {
    }

    public Subsubcategorias(Integer id) {
        this.id = id;
    }

    public Subsubcategorias(Integer id, String nombre, int posicion, Date creado, int categoria, int subcategoria) {
        this.id = id;
        this.nombre = nombre;
        this.posicion = posicion;
        this.creado = creado;
        this.categoria = categoria;
        this.subcategoria = subcategoria;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public Date getCreado() {
        return creado;
    }

    public void setCreado(Date creado) {
        this.creado = creado;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public int getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(int subcategoria) {
        this.subcategoria = subcategoria;
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
        if (!(object instanceof Subsubcategorias)) {
            return false;
        }
        Subsubcategorias other = (Subsubcategorias) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.icompra.entities.Subsubcategorias[ id=" + id + " ]";
    }
    
}
