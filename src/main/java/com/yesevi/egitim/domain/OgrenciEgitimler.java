package com.yesevi.egitim.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OgrenciEgitimler.
 */
@Entity
@Table(name = "ogrenci_egitimler")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OgrenciEgitimler implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ders", "kurum", "egitimTuru", "egitmen", "applicationUser" }, allowSetters = true)
    private Egitim egitim;

    @ManyToOne
    @JsonIgnoreProperties(value = { "internalUser", "egitims" }, allowSetters = true)
    private ApplicationUser kullanici;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OgrenciEgitimler id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Egitim getEgitim() {
        return this.egitim;
    }

    public void setEgitim(Egitim egitim) {
        this.egitim = egitim;
    }

    public OgrenciEgitimler egitim(Egitim egitim) {
        this.setEgitim(egitim);
        return this;
    }

    public ApplicationUser getKullanici() {
        return this.kullanici;
    }

    public void setKullanici(ApplicationUser applicationUser) {
        this.kullanici = applicationUser;
    }

    public OgrenciEgitimler kullanici(ApplicationUser applicationUser) {
        this.setKullanici(applicationUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OgrenciEgitimler)) {
            return false;
        }
        return id != null && id.equals(((OgrenciEgitimler) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OgrenciEgitimler{" +
            "id=" + getId() +
            "}";
    }
}
