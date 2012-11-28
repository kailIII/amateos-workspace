package org.inftel.socialwind.server.domain;

import java.util.Date;
import java.util.logging.Logger;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * Entidad base para las entidades persistentes. Permite unificar el comportamiento y facilita el
 * desarrollo de los servicios DAO.<br>
 * 
 * Nota: Version no puede pornerse en esta entidad mapeada porque sino JPA no actualiza
 * correctamente el valor. Por tanto, solo se crean aquí el get/set abstracto.
 * 
 * @author ibaca
 * 
 */
@Entity
@MappedSuperclass
public abstract class BaseEntity {

    static final Logger log = Logger.getLogger(BaseEntity.class.getName());

    @Basic
    private Date created;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Basic
    private Date updated;

    public Date getCreated() {
        return created;
    }

    public Long getId() {
        return id;
    }

    public Date getUpdated() {
        return updated;
    }

    public abstract Long getVersion();

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public abstract void setVersion(Long version);

    @PrePersist
    void onCreate() {
        if (getId() != null) {
            throw new RuntimeException("Persist solo para nuevas entidades");
            // FIXME esta comprobacion debe hacerse a nivel DAO o superior, no aqui!
            // pero por ahora se deja para facilitar esta etapa del desarrollo
        }
        Date current = new Date();
        setCreated(current);
        setUpdated(current);
    }

    @PostPersist
    void onCreateLog() {
        // post para que se vea el identificador
        log.info("post persist " + toString());
    }

    @PreUpdate
    void onUpdate() {
        log.info("pre update " + toString());
        Date current = new Date();
        setUpdated(current);
    }

    public String toString() {
        return this.getClass().getSimpleName().toLowerCase() + " [id=" + getId() + ", version="
                + getVersion() + "]";
    }

}
