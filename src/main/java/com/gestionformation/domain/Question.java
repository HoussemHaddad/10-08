package com.gestionformation.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * not an ignored comment
 */
@ApiModel(description = "not an ignored comment")
@Entity
@Table(name = "question")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(name = "titre")
    private String titre;

    @ManyToOne
    @JsonIgnoreProperties("questions")
    private TypeQuestion typeQuestion;

    @OneToMany(mappedBy = "Question")
//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Feedback> ListeFeedback= new HashSet<>();

//    @ManyToMany(mappedBy = "questions")
//    @JsonIgnore
////    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//    private Set<Reservation> reservations = new HashSet<>();



    @ManyToMany(mappedBy = "questions")
    @JsonIgnore
//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Formulaire> formulaires = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String getTitre() {
        return titre;
    }

    public Question titre(String titre) {
        this.titre = titre;
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public TypeQuestion getTypeQuestion() {
        return typeQuestion;
    }

    public Question typeQuestion(TypeQuestion typeQuestion) {
        this.typeQuestion = typeQuestion;
        return this;
    }

//    public void setTypeQuestion(TypeQuestion typeQuestion) {
//        this.typeQuestion = typeQuestion;
//    }
//
//    public Set<Reservation> getReservations() {
//        return reservations;
//    }
//
//    public Question reservations(Set<Reservation> reservations) {
//        this.reservations = reservations;
//        return this;
//    }

//    public Question addReservation(Reservation reservation) {
//        this.reservations.add(reservation);
//        reservation.getQuestions().add(this);
//        return this;
//    }
//
//    public Question removeReservation(Reservation reservation) {
//        this.reservations.remove(reservation);
//        reservation.getQuestions().remove(this);
//        return this;
//    }

//    public void setReservations(Set<Reservation> reservations) {
//        this.reservations = reservations;
//    }

    public Set<Formulaire> getFormulaires() {
        return formulaires;
    }

    public Question formulaires(Set<Formulaire> formulaires) {
        this.formulaires = formulaires;
        return this;
    }

    public Question addFormulaire(Formulaire formulaire) {
        this.formulaires.add(formulaire);
        formulaire.getQuestions().add(this);
        return this;
    }

    public Question removeFormulaire(Formulaire formulaire) {
        this.formulaires.remove(formulaire);
        formulaire.getQuestions().remove(this);
        return this;
    }

    public void setFormulaires(Set<Formulaire> formulaires) {
        this.formulaires = formulaires;
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
        Question question = (Question) o;
        if (question.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), question.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +

            ", titre='" + getTitre() + "'" +
            "}";
    }

    public void setTypeQuestion(TypeQuestion typeQuestion) {
        this.typeQuestion = typeQuestion;
    }

    public Set<Feedback> getListeFeedback() {
        return ListeFeedback;
    }

    public void setListeFeedback(Set<Feedback> listeFeedback) {
        ListeFeedback = listeFeedback;
    }
}
