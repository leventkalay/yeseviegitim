package com.yesevi.egitim.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Duyuru.
 */
@Entity
@Table(name = "duyuru")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Duyuru implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "duyuru_baslik", nullable = false)
    private String duyuruBaslik;

    @NotNull
    @Column(name = "duyuru_icerik", nullable = false)
    private String duyuruIcerik;

    @NotNull
    @Column(name = "duyuru_baslama_tarihi", nullable = false)
    private LocalDate duyuruBaslamaTarihi;

    @NotNull
    @Column(name = "duyuru_bitis_tarihi", nullable = false)
    private LocalDate duyuruBitisTarihi;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ders", "kurum", "egitimTuru", "egitmen", "applicationUser" }, allowSetters = true)
    private Egitim egitim;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Duyuru id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDuyuruBaslik() {
        return this.duyuruBaslik;
    }

    public Duyuru duyuruBaslik(String duyuruBaslik) {
        this.setDuyuruBaslik(duyuruBaslik);
        return this;
    }

    public void setDuyuruBaslik(String duyuruBaslik) {
        this.duyuruBaslik = duyuruBaslik;
    }

    public String getDuyuruIcerik() {
        return this.duyuruIcerik;
    }

    public Duyuru duyuruIcerik(String duyuruIcerik) {
        this.setDuyuruIcerik(duyuruIcerik);
        return this;
    }

    public void setDuyuruIcerik(String duyuruIcerik) {
        this.duyuruIcerik = duyuruIcerik;
    }

    public LocalDate getDuyuruBaslamaTarihi() {
        return this.duyuruBaslamaTarihi;
    }

    public Duyuru duyuruBaslamaTarihi(LocalDate duyuruBaslamaTarihi) {
        this.setDuyuruBaslamaTarihi(duyuruBaslamaTarihi);
        return this;
    }

    public void setDuyuruBaslamaTarihi(LocalDate duyuruBaslamaTarihi) {
        this.duyuruBaslamaTarihi = duyuruBaslamaTarihi;
    }

    public LocalDate getDuyuruBitisTarihi() {
        return this.duyuruBitisTarihi;
    }

    public Duyuru duyuruBitisTarihi(LocalDate duyuruBitisTarihi) {
        this.setDuyuruBitisTarihi(duyuruBitisTarihi);
        return this;
    }

    public void setDuyuruBitisTarihi(LocalDate duyuruBitisTarihi) {
        this.duyuruBitisTarihi = duyuruBitisTarihi;
    }

    public Egitim getEgitim() {
        return this.egitim;
    }

    public void setEgitim(Egitim egitim) {
        this.egitim = egitim;
    }

    public Duyuru egitim(Egitim egitim) {
        this.setEgitim(egitim);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Duyuru)) {
            return false;
        }
        return id != null && id.equals(((Duyuru) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Duyuru{" +
            "id=" + getId() +
            ", duyuruBaslik='" + getDuyuruBaslik() + "'" +
            ", duyuruIcerik='" + getDuyuruIcerik() + "'" +
            ", duyuruBaslamaTarihi='" + getDuyuruBaslamaTarihi() + "'" +
            ", duyuruBitisTarihi='" + getDuyuruBitisTarihi() + "'" +
            "}";
    }
}
