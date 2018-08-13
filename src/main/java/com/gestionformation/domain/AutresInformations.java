package com.gestionformation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AutresInformations.
 */
@Entity
@Table(name = "autres_informations")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AutresInformations implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(name = "nom_info")
    private String nomInfo;

    @Column(name = "contenu_info")
    private String contenuInfo;

    @ManyToOne
    @JsonIgnoreProperties("autresInformations")
    private Reservation reservation;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String getNomInfo() {
        return nomInfo;
    }

    public AutresInformations nomInfo(String nomInfo) {
        this.nomInfo = nomInfo;
        return this;
    }

    public void setNomInfo(String nomInfo) {
        this.nomInfo = nomInfo;
    }

    public String getContenuInfo() {
        return contenuInfo;
    }

    public AutresInformations contenuInfo(String contenuInfo) {
        this.contenuInfo = contenuInfo;
        return this;
    }

    public void setContenuInfo(String contenuInfo) {
        this.contenuInfo = contenuInfo;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public AutresInformations reservation(Reservation reservation) {
        this.reservation = reservation;
        return this;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
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
        AutresInformations autresInformations = (AutresInformations) o;
        if (autresInformations.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), autresInformations.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AutresInformations{" +
            "id=" + getId() +

            ", nomInfo='" + getNomInfo() + "'" +
            ", contenuInfo='" + getContenuInfo() + "'" +
            "}";
    }



}
