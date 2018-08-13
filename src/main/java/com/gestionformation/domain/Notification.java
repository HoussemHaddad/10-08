package com.gestionformation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "dateDeCreation")
    private String dateDeCreation;


    @Column(name = "etatDeVue")
    private Boolean etatDeVue;


    @ManyToOne
    @JsonIgnoreProperties("notifications")
    private User utilisateur;

    @ManyToOne
    @JsonIgnoreProperties("notifications")
    private Reservation reservation;

    @ManyToOne
    @JsonIgnoreProperties("notifications")
    private TypeDeNotification typeDeNotification;

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

    public Notification utilisateur(User utilisateur) {
        this.utilisateur = utilisateur;
        return this;
    }

    public void setUtilisateur(User utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public Notification reservation(Reservation reservation) {
        this.reservation = reservation;
        return this;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public TypeDeNotification getTypeDeNotification() {
        return typeDeNotification;
    }

    public Notification typeDeNotification(TypeDeNotification typeDeNotification) {
        this.typeDeNotification = typeDeNotification;
        return this;
    }

    public void setTypeDeNotification(TypeDeNotification typeDeNotification) {
        this.typeDeNotification = typeDeNotification;
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
        Notification notification = (Notification) o;
        if (notification.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notification.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +

            "}";
    }

    public String getDateDeCreation() {
        return dateDeCreation;
    }

    public void setDateDeCreation(String dateDeCreation) {
        this.dateDeCreation = dateDeCreation;
    }

    public Boolean getEtatDeVue() {
        return etatDeVue;
    }

    public void setEtatDeVue(Boolean etatDeVue) {
        this.etatDeVue = etatDeVue;
    }




}
