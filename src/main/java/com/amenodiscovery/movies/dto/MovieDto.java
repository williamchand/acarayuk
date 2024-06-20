package com.amenodiscovery.movies.dto;

import com.amenodiscovery.movies.entity.Movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {

    private String genre;

    private String uniqueIdentifier;

    private String title;

    private String pictureUrl;

    private String yearRelease;

    private String playingTime;

    private String description;

    public static final MovieDto convertToDto(Movie movie) {
        return MovieDto.builder()
                .genre(movie.getGenre())
                .uniqueIdentifier(movie.getUniqueIdentifier())
                .title(movie.getTitle())
                .pictureUrl(movie.getPictureUrl())
                .yearRelease(movie.getYearRelease())
                .playingTime(movie.getPlayingTime())
                .description(movie.getDescription())
                .build();
    }
}
