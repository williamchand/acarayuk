package com.amenodiscovery.movies.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String genre;

    private String uniqueIdentifier;

    private String title;

    private String pictureUrl;

    private String yearRelease;

    private String playingTime;

    private String description;

    public Movie(String title, String genre, String uniqueIdentifier, String pictureUrl, String yearRelease, String playingTime, String description) {
        this.title = title;
        this.genre = genre;
        this.uniqueIdentifier = uniqueIdentifier;
        this.pictureUrl = pictureUrl;
        this.yearRelease = yearRelease;
        this.playingTime = playingTime;
        this.description = description;
    }
}