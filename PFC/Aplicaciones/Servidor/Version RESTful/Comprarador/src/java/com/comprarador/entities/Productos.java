/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comprarador.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "productos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Productos.findAll", query = "SELECT p FROM Productos p"),
    @NamedQuery(name = "Productos.findById", query = "SELECT p FROM Productos p WHERE p.id = :id"),
    @NamedQuery(name = "Productos.findByMarca", query = "SELECT p FROM Productos p WHERE p.marca = :marca"),
    @NamedQuery(name = "Productos.findByDescripcion", query = "SELECT p FROM Productos p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "Productos.findByFormato", query = "SELECT p FROM Productos p WHERE p.formato = :formato"),
    @NamedQuery(name = "Productos.findByCategoria", query = "SELECT p FROM Productos p WHERE p.categoria = :categoria"),
    @NamedQuery(name = "Productos.findBySubcategoria", query = "SELECT p FROM Productos p WHERE p.subcategoria = :subcategoria"),
    @NamedQuery(name = "Productos.findBySubsubcategoria", query = "SELECT p FROM Productos p WHERE p.subsubcategoria = :subsubcategoria"),
    @NamedQuery(name = "Productos.findByCodigoBarras", query = "SELECT p FROM Productos p WHERE p.codigoBarras = :codigoBarras"),
    @NamedQuery(name = "Productos.findByImagenSrc", query = "SELECT p FROM Productos p WHERE p.imagenSrc = :imagenSrc"),
    @NamedQuery(name = "Productos.findByPrecioMercadona", query = "SELECT p FROM Productos p WHERE p.precioMercadona = :precioMercadona"),
    @NamedQuery(name = "Productos.findByPrecioCarrefour", query = "SELECT p FROM Productos p WHERE p.precioCarrefour = :precioCarrefour"),
    @NamedQuery(name = "Productos.findByPrecioHipercor", query = "SELECT p FROM Productos p WHERE p.precioHipercor = :precioHipercor"),
    @NamedQuery(name = "Productos.findByPreciocorteIngles", query = "SELECT p FROM Productos p WHERE p.preciocorteIngles = :preciocorteIngles"),
    @NamedQuery(name = "Productos.findByPrecioRelativoMercadona", query = "SELECT p FROM Productos p WHERE p.precioRelativoMercadona = :precioRelativoMercadona"),
    @NamedQuery(name = "Productos.findByPrecioRelativoCarrefour", query = "SELECT p FROM Productos p WHERE p.precioRelativoCarrefour = :precioRelativoCarrefour"),
    @NamedQuery(name = "Productos.findByPrecioRelativoHipercor", query = "SELECT p FROM Productos p WHERE p.precioRelativoHipercor = :precioRelativoHipercor"),
    @NamedQuery(name = "Productos.findByPreciorelativocorteIngles", query = "SELECT p FROM Productos p WHERE p.preciorelativocorteIngles = :preciorelativocorteIngles"),
    @NamedQuery(name = "Productos.findByCreado", query = "SELECT p FROM Productos p WHERE p.creado = :creado"),
    @NamedQuery(name = "Productos.findByCodigoCreado", query = "SELECT p FROM Productos p WHERE p.codigoCreado = :codigoCreado"),
    @NamedQuery(name = "Productos.findByOfertaMercadona", query = "SELECT p FROM Productos p WHERE p.ofertaMercadona = :ofertaMercadona"),
    @NamedQuery(name = "Productos.findByOfertaCarrefour", query = "SELECT p FROM Productos p WHERE p.ofertaCarrefour = :ofertaCarrefour"),
    @NamedQuery(name = "Productos.findByOfertaHipercor", query = "SELECT p FROM Productos p WHERE p.ofertaHipercor = :ofertaHipercor"),
    @NamedQuery(name = "Productos.findByOfertacorteIngles", query = "SELECT p FROM Productos p WHERE p.ofertacorteIngles = :ofertacorteIngles")})
public class Productos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "marca")
    private String marca;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "formato")
    private String formato;
    @Basic(optional = false)
    @NotNull
    @Column(name = "categoria")
    private int categoria;
    @Basic(optional = false)
    @NotNull
    @Column(name = "subcategoria")
    private int subcategoria;
    @Basic(optional = false)
    @NotNull
    @Column(name = "subsubcategoria")
    private int subsubcategoria;
    @Basic(optional = false)
    @Size(min = 1, max = 13)
    @Column(name = "codigo_barras")
    private String codigoBarras;
    @Basic(optional = false)
    @Size(min = 1, max = 100)
    @Column(name = "imagen_src")
    private String imagenSrc;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "precio_mercadona")
    private BigDecimal precioMercadona;
    @Basic(optional = false)
    @Column(name = "precio_carrefour")
    private BigDecimal precioCarrefour;
    @Basic(optional = false)
    @Column(name = "precio_hipercor")
    private BigDecimal precioHipercor;
    @Basic(optional = false)
    @Column(name = "precio_corteIngles")
    private BigDecimal preciocorteIngles;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "precio_relativo_mercadona")
    private String precioRelativoMercadona;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "precio_relativo_carrefour")
    private String precioRelativoCarrefour;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "precio_relativo_hipercor")
    private String precioRelativoHipercor;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "precio_relativo_corteIngles")
    private String preciorelativocorteIngles;
    @Basic(optional = false)
    @Column(name = "creado")
    @Temporal(TemporalType.DATE)
    private Date creado;
    @Basic(optional = false)
    @Column(name = "codigo_creado")
    @Temporal(TemporalType.DATE)
    private Date codigoCreado;
    @Basic(optional = false)
    @Size(min = 1, max = 100)
    @Column(name = "oferta_mercadona")
    private String ofertaMercadona;
    @Basic(optional = false)
    @Size(min = 1, max = 100)
    @Column(name = "oferta_carrefour")
    private String ofertaCarrefour;
    @Basic(optional = false)
    @Size(min = 1, max = 100)
    @Column(name = "oferta_hipercor")
    private String ofertaHipercor;
    @Basic(optional = false)
    @Size(min = 1, max = 100)
    @Column(name = "oferta_corteIngles")
    private String ofertacorteIngles;

    public Productos() {
    }

    public Productos(Long id) {
        this.id = id;
    }

    public Productos(Long id, String marca, String descripcion, String formato, int categoria, int subcategoria, int subsubcategoria, String codigoBarras, String imagenSrc, BigDecimal precioMercadona, BigDecimal precioCarrefour, BigDecimal precioHipercor, BigDecimal preciocorteIngles, String precioRelativoMercadona, String precioRelativoCarrefour, String precioRelativoHipercor, String preciorelativocorteIngles, Date creado, Date codigoCreado, String ofertaMercadona, String ofertaCarrefour, String ofertaHipercor, String ofertacorteIngles) {
        this.id = id;
        this.marca = marca;
        this.descripcion = descripcion;
        this.formato = formato;
        this.categoria = categoria;
        this.subcategoria = subcategoria;
        this.subsubcategoria = subsubcategoria;
        this.codigoBarras = codigoBarras;
        this.imagenSrc = imagenSrc;
        this.precioMercadona = precioMercadona;
        this.precioCarrefour = precioCarrefour;
        this.precioHipercor = precioHipercor;
        this.preciocorteIngles = preciocorteIngles;
        this.precioRelativoMercadona = precioRelativoMercadona;
        this.precioRelativoCarrefour = precioRelativoCarrefour;
        this.precioRelativoHipercor = precioRelativoHipercor;
        this.preciorelativocorteIngles = preciorelativocorteIngles;
        this.creado = creado;
        this.codigoCreado = codigoCreado;
        this.ofertaMercadona = ofertaMercadona;
        this.ofertaCarrefour = ofertaCarrefour;
        this.ofertaHipercor = ofertaHipercor;
        this.ofertacorteIngles = ofertacorteIngles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
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

    public int getSubsubcategoria() {
        return subsubcategoria;
    }

    public void setSubsubcategoria(int subsubcategoria) {
        this.subsubcategoria = subsubcategoria;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getImagenSrc() {
        return imagenSrc;
    }

    public void setImagenSrc(String imagenSrc) {
        this.imagenSrc = imagenSrc;
    }

    public BigDecimal getPrecioMercadona() {
        return precioMercadona;
    }

    public void setPrecioMercadona(BigDecimal precioMercadona) {
        this.precioMercadona = precioMercadona;
    }

    public BigDecimal getPrecioCarrefour() {
        return precioCarrefour;
    }

    public void setPrecioCarrefour(BigDecimal precioCarrefour) {
        this.precioCarrefour = precioCarrefour;
    }

    public BigDecimal getPrecioHipercor() {
        return precioHipercor;
    }

    public void setPrecioHipercor(BigDecimal precioHipercor) {
        this.precioHipercor = precioHipercor;
    }

    public BigDecimal getPreciocorteIngles() {
        return preciocorteIngles;
    }

    public void setPreciocorteIngles(BigDecimal preciocorteIngles) {
        this.preciocorteIngles = preciocorteIngles;
    }

    public String getPrecioRelativoMercadona() {
        return precioRelativoMercadona;
    }

    public void setPrecioRelativoMercadona(String precioRelativoMercadona) {
        this.precioRelativoMercadona = precioRelativoMercadona;
    }

    public String getPrecioRelativoCarrefour() {
        return precioRelativoCarrefour;
    }

    public void setPrecioRelativoCarrefour(String precioRelativoCarrefour) {
        this.precioRelativoCarrefour = precioRelativoCarrefour;
    }

    public String getPrecioRelativoHipercor() {
        return precioRelativoHipercor;
    }

    public void setPrecioRelativoHipercor(String precioRelativoHipercor) {
        this.precioRelativoHipercor = precioRelativoHipercor;
    }

    public String getPreciorelativocorteIngles() {
        return preciorelativocorteIngles;
    }

    public void setPreciorelativocorteIngles(String preciorelativocorteIngles) {
        this.preciorelativocorteIngles = preciorelativocorteIngles;
    }

    public Date getCreado() {
        return creado;
    }

    public void setCreado(Date creado) {
        this.creado = creado;
    }

    public Date getCodigoCreado() {
        return codigoCreado;
    }

    public void setCodigoCreado(Date codigoCreado) {
        this.codigoCreado = codigoCreado;
    }

    public String getOfertaMercadona() {
        return ofertaMercadona;
    }

    public void setOfertaMercadona(String ofertaMercadona) {
        this.ofertaMercadona = ofertaMercadona;
    }

    public String getOfertaCarrefour() {
        return ofertaCarrefour;
    }

    public void setOfertaCarrefour(String ofertaCarrefour) {
        this.ofertaCarrefour = ofertaCarrefour;
    }

    public String getOfertaHipercor() {
        return ofertaHipercor;
    }

    public void setOfertaHipercor(String ofertaHipercor) {
        this.ofertaHipercor = ofertaHipercor;
    }

    public String getOfertacorteIngles() {
        return ofertacorteIngles;
    }

    public void setOfertacorteIngles(String ofertacorteIngles) {
        this.ofertacorteIngles = ofertacorteIngles;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Productos)) {
            return false;
        }
        
        Productos p = (Productos) object;
        if(p != null){
            if (this.marca.equals(p.marca) && this.descripcion.equals(p.descripcion) && this.formato.equals(p.formato) && this.imagenSrc.equals(p.imagenSrc)){
                return true;
            }
        }
        return false;
        
    }

    @Override
    public String toString() {
        return "com.icompra.entities.Productos[ id=" + id + " ]";
    }
    
}
