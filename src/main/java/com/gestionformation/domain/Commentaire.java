package com.gestionformation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Commentaire.
 */
@Entity
@Table(name = "commentaire")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Commentaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "i_dcomm")
//    private Long iDcomm;

    @Column(name = "contenu")
    private String contenu;

    @ManyToOne
    @JsonIgnoreProperties("commentaires")
    private User utilisateur;

    @ManyToOne
    @JsonIgnoreProperties("Formations")
    private Formation commentaire;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Long getiDcomm() {
//        return iDcomm;
//    }
//
//    public Commentaire iDcomm(Long iDcomm) {
//        this.iDcomm = iDcomm;
//        return this;
//    }
//
//    public void setiDcomm(Long iDcomm) {
//        this.iDcomm = iDcomm;
//    }

    public String getContenu() {
        return contenu;
    }

    public Commentaire contenu(String contenu) {
        this.contenu = contenu;
        return this;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public User getUtilisateur() {
        return utilisateur;
    }

    public Commentaire utilisateur(User utilisateur) {
        this.utilisateur = utilisateur;
        return this;
    }

    public void setUtilisateur(User utilisateur) {
        this.utilisateur = utilisateur;
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
        Commentaire commentaire = (Commentaire) o;
        if (commentaire.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), commentaire.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Commentaire{" +
            "id=" + getId() +
//            ", iDcomm=" + getiDcomm() +
            ", contenu='" + getContenu() + "'" +
            "}";
    }

    public Formation getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(Formation commentaire) {
        this.commentaire = commentaire;
    }
}
