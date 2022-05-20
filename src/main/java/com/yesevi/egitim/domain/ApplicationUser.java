package com.yesevi.egitim.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ApplicationUser.
 */
@Entity
@Table(name = "application_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApplicationUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "birimi")
    private String birimi;

    @OneToOne
    @JoinColumn(unique = true)
    private User internalUser;

    @OneToMany(mappedBy = "applicationUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ders", "kurum", "egitimTuru", "egitmen", "applicationUser" }, allowSetters = true)
    private Set<Egitim> egitims = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApplicationUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBirimi() {
        return this.birimi;
    }

    public ApplicationUser birimi(String birimi) {
        this.setBirimi(birimi);
        return this;
    }

    public void setBirimi(String birimi) {
        this.birimi = birimi;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public ApplicationUser internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public Set<Egitim> getEgitims() {
        return this.egitims;
    }

    public void setEgitims(Set<Egitim> egitims) {
        if (this.egitims != null) {
            this.egitims.forEach(i -> i.setApplicationUser(null));
        }
        if (egitims != null) {
            egitims.forEach(i -> i.setApplicationUser(this));
        }
        this.egitims = egitims;
    }

    public ApplicationUser egitims(Set<Egitim> egitims) {
        this.setEgitims(egitims);
        return this;
    }

    public ApplicationUser addEgitim(Egitim egitim) {
        this.egitims.add(egitim);
        egitim.setApplicationUser(this);
        return this;
    }

    public ApplicationUser removeEgitim(Egitim egitim) {
        this.egitims.remove(egitim);
        egitim.setApplicationUser(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUser)) {
            return false;
        }
        return id != null && id.equals(((ApplicationUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUser{" +
            "id=" + getId() +
            ", birimi='" + getBirimi() + "'" +
            "}";
    }
}
