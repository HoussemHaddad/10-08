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
 * A Reservation.
 */
@Entity
@Table(name = "reservation")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DateCreation")
    private String dateCreation;

    @Column(name = "Etat")
    private String Etat;

    @ManyToOne
    @JsonIgnoreProperties("reservations")
    private User utilisateur;

    @ManyToOne
    @JsonIgnoreProperties("reservations")
    private Formation formation;

//    @ManyToMany
////    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//    @JoinTable(name = "Feedback",
//               joinColumns = @JoinColumn(name = "reservations_id", referencedColumnName = "id"),
//               inverseJoinColumns = @JoinColumn(name = "questions_id", referencedColumnName = "id"))
//    private Set<Question> questions = new HashSet<>();



    @OneToMany(fetch = FetchType.EAGER, mappedBy = "reservation", cascade = CascadeType.ALL)

//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Notification> notifications = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "reservation", cascade = CascadeType.ALL)

//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AutresInformations> autresInformations = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "Reservation", cascade = CascadeType.ALL)

//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Feedback> ListeFeedback= new HashSet<>();

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

    public Reservation utilisateur(User utilisateur) {
        this.utilisateur = utilisateur;
        return this;
    }

    public void setUtilisateur(User utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Formation getFormation() {
        return formation;
    }

    public Reservation formation(Formation formation) {
        this.formation = formation;
        return this;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

//    public Set<Question> getQuestions() {
//        return questions;
//    }
//
//    public Reservation questions(Set<Question> questions) {
//        this.questions = questions;
//        return this;
//    }

//    public Reservation addQuestion(Question question) {
//        this.questions.add(question);
//        question.getReservations().add(this);
//        return this;
//    }
//
//    public Reservation removeQuestion(Question question) {
//        this.questions.remove(question);
//        question.getReservations().remove(this);
//        return this;
//    }
//
//    public void setQuestions(Set<Question> questions) {
//        this.questions = questions;
//    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public Reservation notifications(Set<Notification> notifications) {
        this.notifications = notifications;
        return this;
    }

    public Reservation addNotification(Notification notification) {
        this.notifications.add(notification);
        notification.setReservation(this);
        return this;
    }

    public Reservation removeNotification(Notification notification) {
        this.notifications.remove(notification);
        notification.setReservation(null);
        return this;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    public Set<AutresInformations> getAutresInformations() {
        return autresInformations;
    }

    public Reservation autresInformations(Set<AutresInformations> autresInformations) {
        this.autresInformations = autresInformations;
        return this;
    }

    public Reservation addAutresInformations(AutresInformations autresInformations) {
        this.autresInformations.add(autresInformations);
        autresInformations.setReservation(this);
        return this;
    }

    public Reservation removeAutresInformations(AutresInformations autresInformations) {
        this.autresInformations.remove(autresInformations);
        autresInformations.setReservation(null);
        return this;
    }

    public void setAutresInformations(Set<AutresInformations> autresInformations) {
        this.autresInformations = autresInformations;
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
        Reservation reservation = (Reservation) o;
        if (reservation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reservation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Reservation{" +
            "id=" + getId() +
            "}";
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getEtat() {
        return Etat;
    }

    public void setEtat(String etat) {
        Etat = etat;
    }

    public Set<Feedback> getListeFeedback() {
        return ListeFeedback;
    }

    public void setListeFeedback(Set<Feedback> listeFeedback) {
        ListeFeedback = listeFeedback;
    }
}
