package com.gestionformation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Formation.
 */
@Entity
@Table(name = "formation")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Formation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "i_d_formation")
//    private Long iDFormation;

    @Column(name = "nom_formation")
    private String nomFormation;

    @Column(name = "description")
    private String description;

    @Column(name = "DateCreation")
    private String dateCreation;

    @Column(name = "Active")
    private Boolean Active;

    @ManyToOne
    @JsonIgnoreProperties("formations")
    private CentreDeFormation centreDeFormation;

    @ManyToOne
    @JsonIgnoreProperties("formations")
    private CategorieFormation categorieFormation;

//    @OneToMany(mappedBy = "formation")
//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//    private Set<Formulaire> formulaires = new HashSet<>();

//    @OneToMany(mappedBy = "commentaire")
//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//    private Set<Commentaire> commentaires = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Long getiDFormation() {
//        return iDFormation;
//    }
//
//    public Formation iDFormation(Long iDFormation) {
//        this.iDFormation = iDFormation;
//        return this;
//    }
//
//    public void setiDFormation(Long iDFormation) {
//        this.iDFormation = iDFormation;
//    }

    public String getNomFormation() {
        return nomFormation;
    }

    public Formation nomFormation(String nomFormation) {
        this.nomFormation = nomFormation;
        return this;
    }

    public void setNomFormation(String nomFormation) {
        this.nomFormation = nomFormation;
    }

    public String getDescription() {
        return description;
    }

    public Formation description(String information) {
        this.description = information;
        return this;
    }

    public void setDescription(String information) {
        this.description = information;
    }

    public CentreDeFormation getCentreDeFormation() {
        return centreDeFormation;
    }

    public Formation centreDeFormation(CentreDeFormation centreDeFormation) {
        this.centreDeFormation = centreDeFormation;
        return this;
    }

    public void setCentreDeFormation(CentreDeFormation centreDeFormation) {
        this.centreDeFormation = centreDeFormation;
    }

    public CategorieFormation getCategorieFormation() {
        return categorieFormation;
    }

    public Formation categorieFormation(CategorieFormation categorieFormation) {
        this.categorieFormation = categorieFormation;
        return this;
    }

    public void setCategorieFormation(CategorieFormation categorieFormation) {
        this.categorieFormation = categorieFormation;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }
    public Boolean getActive() {
        return Active;
    }

    public void setActive(Boolean active) {
        Active = active;
    }

    //    public Set<Commentaire> getCommentaires() {
//        return commentaires;
//    }
//
//    public Commentaire commentaires(Set<Commentaire> formulaires) {
//        this.commentaires = formulaires;
//        return this;
//    }
//
//    public Formation addCommentaire(Commentaire formulaire) {
//        this.commentaires.add(formulaire);
//        formulaire.setCommentaire(this);
//        return this;
//    }
//
//    public Formation removeCommentaire(Commentaire commentaire) {
//        this.commentaires.remove(commentaire);
//        commentaire.setFormation(null);
//        return this;
//    }
//
//    public void setCommentaires(Set<Commentaire> formulaires) {
//        this.commentaires = formulaires;
//    }
//
//    public Set<Reservation> getReservations() {
//        return reservations;
//    }
//
//    public Formation reservations(Set<Reservation> reservations) {
//        this.reservations = reservations;
//        return this;
//    }
//
//    public Formation addReservation(Reservation reservation) {
//        this.reservations.add(reservation);
//        reservation.setFormation(this);
//        return this;
//    }
//
//    public Formation removeReservation(Reservation reservation) {
//        this.reservations.remove(reservation);
//        reservation.setFormation(null);
//        return this;
//    }
//
//    public void setReservations(Set<Reservation> reservations) {
//        this.reservations = reservations;
//    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Formation formation = (Formation) o;
        if (formation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), formation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Formation{" +
            "id=" + getId() +
//            ", iDFormation=" + getiDFormation() +
            ", nomFormation='" + getNomFormation() + "'" +
            ", information='" + getDescription() + "'" +
            "}";
    }


}
