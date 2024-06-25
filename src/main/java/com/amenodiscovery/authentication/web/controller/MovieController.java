package com.amenodiscovery.authentication.web.controller;

import com.amenodiscovery.authentication.web.dto.GeneralDto;
import com.amenodiscovery.authentication.web.dto.MovieDto;
import com.amenodiscovery.authentication.persistence.model.User;
import com.amenodiscovery.authentication.persistence.model.Movie;
import com.amenodiscovery.authentication.service.UserService;
import com.amenodiscovery.authentication.service.MovieService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MovieController {

    private final static Logger LOGGER = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    MovieService movieService;

    @Autowired
    UserService accountService;

    @GetMapping("/v1/public/movie/recommend/{genre}")
    public ResponseEntity<GeneralDto<MovieDto>> getRecommendation(Principal principal, @PathVariable("genre") String genre) {
        User account = null;
        if (principal != null) {
            account = accountService.getAccount(Long.valueOf(principal.getName()));
        }
        Movie movie = movieService.getRecommendation(account, genre);
        return ResponseEntity.ok().body(GeneralDto.convertToDto(MovieDto.convertToDto(movie)));
    }
    
    @GetMapping("/v1/movie/history")
    public ResponseEntity<GeneralDto<List<MovieDto>>> getHistory(Principal principal) {
        final User account = accountService.getAccount(Long.valueOf(principal.getName()));
        List<Movie> movie = movieService.getHistory(account);

        return ResponseEntity.ok().body(GeneralDto.convertToDto(movie.stream()
            .map(x -> MovieDto.convertToDto(x))
            .collect(Collectors.toList())));
    }
}
