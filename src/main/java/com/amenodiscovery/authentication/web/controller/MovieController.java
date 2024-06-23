package com.amenodiscovery.authentication.web.controller;

import com.amenodiscovery.authentication.web.dto.GeneralDto;
import com.amenodiscovery.authentication.web.dto.MovieDto;
import com.amenodiscovery.authentication.persistence.model.User;
import com.amenodiscovery.authentication.persistence.model.Movie;
import com.amenodiscovery.authentication.service.UserService;
import com.amenodiscovery.authentication.service.MovieService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MovieController {

    @Autowired
    MovieService movieService;

    @Autowired
    UserService accountService;

    @GetMapping("/v1/public/movie/recommend/{genre}")
    public ResponseEntity<GeneralDto<MovieDto>> getRecommendation(@PathVariable("genre") String genre) {
        User principal = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        User account = null;
        if (principal != null) {
            account = accountService.findUserByEmail(principal.getEmail());
        }
        Movie movie = movieService.getRecommendation(account, genre);
        return ResponseEntity.ok().body(GeneralDto.convertToDto(MovieDto.convertToDto(movie)));
    }
    
    @GetMapping("/v1/movie/history")
    public ResponseEntity<GeneralDto<List<MovieDto>>> getHistory() {
        User account = accountService.findUserByEmail(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
        List<Movie> movie = movieService.getHistory(account);

        return ResponseEntity.ok().body(GeneralDto.convertToDto(movie.stream()
            .map(x -> MovieDto.convertToDto(x))
            .collect(Collectors.toList())));
    }
}
