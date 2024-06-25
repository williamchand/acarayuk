package com.amenodiscovery.authentication.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "animal_history")
public class AnimalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="animal_id")
    private Animal animal;

    @Temporal(TemporalType.DATE)
    private Date historyDate;

    private String name;

    public AnimalHistory(Animal animal, Date historyDate, String name) {
        this.animal = animal;
        this.historyDate = historyDate;
        this.name = name;
    }
}