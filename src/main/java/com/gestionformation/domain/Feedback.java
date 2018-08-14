package com.gestionformation.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JsonIgnoreProperties("questions")
    private Question Question;

    @ManyToOne
    @JsonIgnoreProperties("reservation")
    private Reservation Reservation;


    String reponseQuestion;




    public String getReponseQuestion() {
        return reponseQuestion;
    }

    public void setReponseQuestion(String reponseQuestion) {
        this.reponseQuestion = reponseQuestion;
    }

    public com.gestionformation.domain.Question getQuestion() {
        return Question;
    }

    public void setQuestion(com.gestionformation.domain.Question question) {
        Question = question;
    }

    public com.gestionformation.domain.Reservation getReservation() {
        return Reservation;
    }

    public void setReservation(com.gestionformation.domain.Reservation reservation) {
        Reservation = reservation;
    }
}
