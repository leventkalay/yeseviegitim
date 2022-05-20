package com.yesevi.egitim.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EgitimTuru.
 */
@Entity
@Table(name = "egitim_turu")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EgitimTuru implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "adi")
    private String adi;

    @Column(name = "aciklama")
    private String aciklama;

    @Column(name = "aktif")
    private Boolean aktif;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EgitimTuru id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdi() {
        return this.adi;
    }

    public EgitimTuru adi(String adi) {
        this.setAdi(adi);
        return this;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public String getAciklama() {
        return this.aciklama;
    }

    public EgitimTuru aciklama(String aciklama) {
        this.setAciklama(aciklama);
        return this;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public Boolean getAktif() {
        return this.aktif;
    }

    public EgitimTuru aktif(Boolean aktif) {
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
        if (!(o instanceof EgitimTuru)) {
            return false;
        }
        return id != null && id.equals(((EgitimTuru) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EgitimTuru{" +
            "id=" + getId() +
            ", adi='" + getAdi() + "'" +
            ", aciklama='" + getAciklama() + "'" +
            ", aktif='" + getAktif() + "'" +
            "}";
    }
}
