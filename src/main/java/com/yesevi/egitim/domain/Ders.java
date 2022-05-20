package com.yesevi.egitim.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Ders.
 */
@Entity
@Table(name = "ders")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ders implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "adi")
    private String adi;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "aciklama")
    private String aciklama;

    @Column(name = "ders_linki")
    private String dersLinki;

    @Lob
    @Column(name = "ders_videosu")
    private byte[] dersVideosu;

    @Column(name = "ders_videosu_content_type")
    private String dersVideosuContentType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ders", "kurum", "egitimTuru", "egitmen", "applicationUser" }, allowSetters = true)
    private Egitim egitim;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ders id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdi() {
        return this.adi;
    }

    public Ders adi(String adi) {
        this.setAdi(adi);
        return this;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public String getAciklama() {
        return this.aciklama;
    }

    public Ders aciklama(String aciklama) {
        this.setAciklama(aciklama);
        return this;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getDersLinki() {
        return this.dersLinki;
    }

    public Ders dersLinki(String dersLinki) {
        this.setDersLinki(dersLinki);
        return this;
    }

    public void setDersLinki(String dersLinki) {
        this.dersLinki = dersLinki;
    }

    public byte[] getDersVideosu() {
        return this.dersVideosu;
    }

    public Ders dersVideosu(byte[] dersVideosu) {
        this.setDersVideosu(dersVideosu);
        return this;
    }

    public void setDersVideosu(byte[] dersVideosu) {
        this.dersVideosu = dersVideosu;
    }

    public String getDersVideosuContentType() {
        return this.dersVideosuContentType;
    }

    public Ders dersVideosuContentType(String dersVideosuContentType) {
        this.dersVideosuContentType = dersVideosuContentType;
        return this;
    }

    public void setDersVideosuContentType(String dersVideosuContentType) {
        this.dersVideosuContentType = dersVideosuContentType;
    }

    public Egitim getEgitim() {
        return this.egitim;
    }

    public void setEgitim(Egitim egitim) {
        this.egitim = egitim;
    }

    public Ders egitim(Egitim egitim) {
        this.setEgitim(egitim);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ders)) {
            return false;
        }
        return id != null && id.equals(((Ders) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ders{" +
            "id=" + getId() +
            ", adi='" + getAdi() + "'" +
            ", aciklama='" + getAciklama() + "'" +
            ", dersLinki='" + getDersLinki() + "'" +
            ", dersVideosu='" + getDersVideosu() + "'" +
            ", dersVideosuContentType='" + getDersVideosuContentType() + "'" +
            "}";
    }
}
