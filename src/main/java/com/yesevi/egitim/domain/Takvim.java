package com.yesevi.egitim.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Takvim.
 */
@Entity
@Table(name = "takvim")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Takvim implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "adi")
    private String adi;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ders", "kurum", "egitimTuru", "egitmen", "applicationUser" }, allowSetters = true)
    private Egitim egitim;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Takvim id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdi() {
        return this.adi;
    }

    public Takvim adi(String adi) {
        this.setAdi(adi);
        return this;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public Egitim getEgitim() {
        return this.egitim;
    }

    public void setEgitim(Egitim egitim) {
        this.egitim = egitim;
    }

    public Takvim egitim(Egitim egitim) {
        this.setEgitim(egitim);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Takvim)) {
            return false;
        }
        return id != null && id.equals(((Takvim) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Takvim{" +
            "id=" + getId() +
            ", adi='" + getAdi() + "'" +
            "}";
    }
}
