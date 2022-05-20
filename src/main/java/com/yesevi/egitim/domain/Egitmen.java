package com.yesevi.egitim.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Egitmen.
 */
@Entity
@Table(name = "egitmen")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Egitmen implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "adi_soyadi")
    private String adiSoyadi;

    @Column(name = "unvani")
    private String unvani;

    @Column(name = "puani")
    private Float puani;

    @Column(name = "aktif")
    private Boolean aktif;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Egitmen id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdiSoyadi() {
        return this.adiSoyadi;
    }

    public Egitmen adiSoyadi(String adiSoyadi) {
        this.setAdiSoyadi(adiSoyadi);
        return this;
    }

    public void setAdiSoyadi(String adiSoyadi) {
        this.adiSoyadi = adiSoyadi;
    }

    public String getUnvani() {
        return this.unvani;
    }

    public Egitmen unvani(String unvani) {
        this.setUnvani(unvani);
        return this;
    }

    public void setUnvani(String unvani) {
        this.unvani = unvani;
    }

    public Float getPuani() {
        return this.puani;
    }

    public Egitmen puani(Float puani) {
        this.setPuani(puani);
        return this;
    }

    public void setPuani(Float puani) {
        this.puani = puani;
    }

    public Boolean getAktif() {
        return this.aktif;
    }

    public Egitmen aktif(Boolean aktif) {
        this.setAktif(aktif);
        return this;
    }

    public void setAktif(Boolean aktif) {
        this.aktif = aktif;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Egitmen)) {
            return false;
        }
        return id != null && id.equals(((Egitmen) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Egitmen{" +
            "id=" + getId() +
            ", adiSoyadi='" + getAdiSoyadi() + "'" +
            ", unvani='" + getUnvani() + "'" +
            ", puani=" + getPuani() +
            ", aktif='" + getAktif() + "'" +
            "}";
    }
}
