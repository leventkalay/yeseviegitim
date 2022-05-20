package com.yesevi.egitim.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Egitim.
 */
@Entity
@Table(name = "egitim")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Egitim implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "egitim_baslik")
    private String egitimBaslik;

    @Column(name = "egitim_alt_baslik")
    private String egitimAltBaslik;

    @Column(name = "egitim_baslama_tarihi")
    private LocalDate egitimBaslamaTarihi;

    @Column(name = "egitim_bitis_tarihi")
    private LocalDate egitimBitisTarihi;

    @Column(name = "ders_sayisi")
    private Integer dersSayisi;

    @Column(name = "egitim_suresi")
    private Float egitimSuresi;

    @Column(name = "egitim_yeri")
    private String egitimYeri;

    @Column(name = "egitim_puani")
    private Float egitimPuani;

    @Column(name = "aktif")
    private Boolean aktif;

    @OneToMany(mappedBy = "egitim")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "egitim" }, allowSetters = true)
    private Set<Ders> ders = new HashSet<>();

    @ManyToOne
    private Kurum kurum;

    @ManyToOne
    private EgitimTuru egitimTuru;

    @ManyToOne
    private Egitmen egitmen;

    @ManyToOne
    @JsonIgnoreProperties(value = { "internalUser", "egitims" }, allowSetters = true)
    private ApplicationUser applicationUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Egitim id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEgitimBaslik() {
        return this.egitimBaslik;
    }

    public Egitim egitimBaslik(String egitimBaslik) {
        this.setEgitimBaslik(egitimBaslik);
        return this;
    }

    public void setEgitimBaslik(String egitimBaslik) {
        this.egitimBaslik = egitimBaslik;
    }

    public String getEgitimAltBaslik() {
        return this.egitimAltBaslik;
    }

    public Egitim egitimAltBaslik(String egitimAltBaslik) {
        this.setEgitimAltBaslik(egitimAltBaslik);
        return this;
    }

    public void setEgitimAltBaslik(String egitimAltBaslik) {
        this.egitimAltBaslik = egitimAltBaslik;
    }

    public LocalDate getEgitimBaslamaTarihi() {
        return this.egitimBaslamaTarihi;
    }

    public Egitim egitimBaslamaTarihi(LocalDate egitimBaslamaTarihi) {
        this.setEgitimBaslamaTarihi(egitimBaslamaTarihi);
        return this;
    }

    public void setEgitimBaslamaTarihi(LocalDate egitimBaslamaTarihi) {
        this.egitimBaslamaTarihi = egitimBaslamaTarihi;
    }

    public LocalDate getEgitimBitisTarihi() {
        return this.egitimBitisTarihi;
    }

    public Egitim egitimBitisTarihi(LocalDate egitimBitisTarihi) {
        this.setEgitimBitisTarihi(egitimBitisTarihi);
        return this;
    }

    public void setEgitimBitisTarihi(LocalDate egitimBitisTarihi) {
        this.egitimBitisTarihi = egitimBitisTarihi;
    }

    public Integer getDersSayisi() {
        return this.dersSayisi;
    }

    public Egitim dersSayisi(Integer dersSayisi) {
        this.setDersSayisi(dersSayisi);
        return this;
    }

    public void setDersSayisi(Integer dersSayisi) {
        this.dersSayisi = dersSayisi;
    }

    public Float getEgitimSuresi() {
        return this.egitimSuresi;
    }

    public Egitim egitimSuresi(Float egitimSuresi) {
        this.setEgitimSuresi(egitimSuresi);
        return this;
    }

    public void setEgitimSuresi(Float egitimSuresi) {
        this.egitimSuresi = egitimSuresi;
    }

    public String getEgitimYeri() {
        return this.egitimYeri;
    }

    public Egitim egitimYeri(String egitimYeri) {
        this.setEgitimYeri(egitimYeri);
        return this;
    }

    public void setEgitimYeri(String egitimYeri) {
        this.egitimYeri = egitimYeri;
    }

    public Float getEgitimPuani() {
        return this.egitimPuani;
    }

    public Egitim egitimPuani(Float egitimPuani) {
        this.setEgitimPuani(egitimPuani);
        return this;
    }

    public void setEgitimPuani(Float egitimPuani) {
        this.egitimPuani = egitimPuani;
    }

    public Boolean getAktif() {
        return this.aktif;
    }

    public Egitim aktif(Boolean aktif) {
        this.setAktif(aktif);
        return this;
    }

    public void setAktif(Boolean aktif) {
        this.aktif = aktif;
    }

    public Set<Ders> getDers() {
        return this.ders;
    }

    public void setDers(Set<Ders> ders) {
        if (this.ders != null) {
            this.ders.forEach(i -> i.setEgitim(null));
        }
        if (ders != null) {
            ders.forEach(i -> i.setEgitim(this));
        }
        this.ders = ders;
    }

    public Egitim ders(Set<Ders> ders) {
        this.setDers(ders);
        return this;
    }

    public Egitim addDers(Ders ders) {
        this.ders.add(ders);
        ders.setEgitim(this);
        return this;
    }

    public Egitim removeDers(Ders ders) {
        this.ders.remove(ders);
        ders.setEgitim(null);
        return this;
    }

    public Kurum getKurum() {
        return this.kurum;
    }

    public void setKurum(Kurum kurum) {
        this.kurum = kurum;
    }

    public Egitim kurum(Kurum kurum) {
        this.setKurum(kurum);
        return this;
    }

    public EgitimTuru getEgitimTuru() {
        return this.egitimTuru;
    }

    public void setEgitimTuru(EgitimTuru egitimTuru) {
        this.egitimTuru = egitimTuru;
    }

    public Egitim egitimTuru(EgitimTuru egitimTuru) {
        this.setEgitimTuru(egitimTuru);
        return this;
    }

    public Egitmen getEgitmen() {
        return this.egitmen;
    }

    public void setEgitmen(Egitmen egitmen) {
        this.egitmen = egitmen;
    }

    public Egitim egitmen(Egitmen egitmen) {
        this.setEgitmen(egitmen);
        return this;
    }

    public ApplicationUser getApplicationUser() {
        return this.applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public Egitim applicationUser(ApplicationUser applicationUser) {
        this.setApplicationUser(applicationUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Egitim)) {
            return false;
        }
        return id != null && id.equals(((Egitim) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Egitim{" +
            "id=" + getId() +
            ", egitimBaslik='" + getEgitimBaslik() + "'" +
            ", egitimAltBaslik='" + getEgitimAltBaslik() + "'" +
            ", egitimBaslamaTarihi='" + getEgitimBaslamaTarihi() + "'" +
            ", egitimBitisTarihi='" + getEgitimBitisTarihi() + "'" +
            ", dersSayisi=" + getDersSayisi() +
            ", egitimSuresi=" + getEgitimSuresi() +
            ", egitimYeri='" + getEgitimYeri() + "'" +
            ", egitimPuani=" + getEgitimPuani() +
            ", aktif='" + getAktif() + "'" +
            "}";
    }
}
