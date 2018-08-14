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
 * A Formulaire.
 */
@Entity
@Table(name = "formulaire")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Formulaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @ManyToOne
    @JsonIgnoreProperties("formulaires")
    private User utilisateur;

//    @ManyToOne
//    @JsonIgnoreProperties("formulaires")
//    private Formation formation;

    @ManyToMany
//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "formulaire_question",
               joinColumns = @JoinColumn(name = "formulaires_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "questions_id", referencedColumnName = "id"))
    private Set<Question> questions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public User getUtilisateur() {
        return utilisateur;
    }

    public Formulaire utilisateur(User utilisateur) {
        this.utilisateur = utilisateur;
        return this;
    }

    public void setUtilisateur(User utilisateur) {
        this.utilisateur = utilisateur;
    }

//    public Formation getFormation() {
//        return formation;
//    }
//
//    public Formulaire formation(Formation formation) {
//        this.formation = formation;
//        return this;
//    }
//
//    public void setFormation(Formation formation) {
//        this.formation = formation;
//    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public Formulaire questions(Set<Question> questions) {
        this.questions = questions;
        return this;
    }

    public Formulaire addQuestion(Question question) {
        this.questions.add(question);
        question.getFormulaires().add(this);
        return this;
    }

    public Formulaire removeQuestion(Question question) {
        this.questions.remove(question);
        question.getFormulaires().remove(this);
        return this;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
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
        Formulaire formulaire = (Formulaire) o;
        if (formulaire.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), formulaire.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Formulaire{" +
            "id=" + getId() +

            "}";
    }
}
