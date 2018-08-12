package com.gestionformation.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A CentreDeFormation.
 */
@Entity
@Table(name = "centre_de_formation")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CentreDeFormation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "i_d_centre")
//    private Long iDCentre;

    @Column(name = "nom_centre")
    private String nomCentre;

    @Column(name = "adresse")
    private String adresse;
    @Column(name = "websiteUrl")
    private String websiteUrl;
    @Column(name = "gpsCoodinates")
    private String gpsCoodinates;

    @OneToMany(mappedBy = "centreDeFormation")
//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Formation> formations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Long getiDCentre() {
//        return iDCentre;
//    }
//
//    public CentreDeFormation iDCentre(Long iDCentre) {
//        this.iDCentre = iDCentre;
//        return this;
//    }
//
//    public void setiDCentre(Long iDCentre) {
//        this.iDCentre = iDCentre;
//    }

    public String getNomCentre() {
        return nomCentre;
    }

    public CentreDeFormation nomCentre(String nomCentre) {
        this.nomCentre = nomCentre;
        return this;
    }

    public void setNomCentre(String nomCentre) {
        this.nomCentre = nomCentre;
    }

    public String getAdresse() {
        return adresse;
    }

    public CentreDeFormation adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getGpsCoodinates() {
        return gpsCoodinates;
    }

    public void setGpsCoodinates(String gpsCoodinates) {
        this.gpsCoodinates = gpsCoodinates;
    }


    public Set<Formation> getFormations() {
        return formations;
    }

    public CentreDeFormation formations(Set<Formation> formations) {
        this.formations = formations;
        return this;
    }

    public CentreDeFormation addFormation(Formation formation) {
        this.formations.add(formation);
        formation.setCentreDeFormation(this);
        return this;
    }

    public CentreDeFormation removeFormation(Formation formation) {
        this.formations.remove(formation);
        formation.setCentreDeFormation(null);
        return this;
    }

    public void setFormations(Set<Formation> formations) {
        this.formations = formations;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CentreDeFormation centreDeFormation = (CentreDeFormation) o;
        if (centreDeFormation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), centreDeFormation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CentreDeFormation{" +
            "id=" + getId() +
//            ", iDCentre=" + getiDCentre() +
            ", nomCentre='" + getNomCentre() + "'" +
            ", adresse='" + getAdresse() + "'" +
            "}";
    }
}
