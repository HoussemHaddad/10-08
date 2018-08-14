package com.gestionformation.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A TypeQuestion.
 */
@Entity
@Table(name = "type_question")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TypeQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(name = "nom_type")
    private String nomType;

    @OneToMany(mappedBy = "typeQuestion")
//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Question> questions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String getNomType() {
        return nomType;
    }

    public TypeQuestion nomType(String nomType) {
        this.nomType = nomType;
        return this;
    }

    public void setNomType(String nomType) {
        this.nomType = nomType;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public TypeQuestion questions(Set<Question> questions) {
        this.questions = questions;
        return this;
    }

    public TypeQuestion addQuestion(Question question) {
        this.questions.add(question);
        question.setTypeQuestion(this);
        return this;
    }

    public TypeQuestion removeQuestion(Question question) {
        this.questions.remove(question);
        question.setTypeQuestion(null);
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
        TypeQuestion typeQuestion = (TypeQuestion) o;
        if (typeQuestion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), typeQuestion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TypeQuestion{" +
            "id=" + getId() +

            ", nomType='" + getNomType() + "'" +
            "}";
    }
}
