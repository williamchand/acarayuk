package com.amenodiscovery.movies.controller;

import com.amenodiscovery.movies.dto.GeneralDto;
import com.amenodiscovery.movies.dto.MovieDto;
import com.amenodiscovery.movies.entity.Account;
import com.amenodiscovery.movies.entity.Movie;
import com.amenodiscovery.movies.entity.Role;
import com.amenodiscovery.movies.service.AccountService;
import com.amenodiscovery.movies.service.MovieService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import static com.amenodiscovery.movies.dto.MovieDto.convertToDto;
import static com.amenodiscovery.movies.dto.GeneralDto.convertToDto;

@RestController
@RequestMapping("/v1")
public class MovieController {

    @Autowired
    MovieService movieService;

    @Autowired
    AccountService accountService;

    @GetMapping("/public/movie/recommend/{genre}")
    public ResponseEntity<GeneralDto<MovieDto>> getRecommendation(Principal principal, @PathVariable("genre") String genre) {
        Account account = null;
        if (principal != null) {
            account = accountService.getAccount(Long.valueOf(principal.getName()));
        }
        Movie movie = movieService.getRecommendation(account, genre);
        return ResponseEntity.ok().body(convertToDto(convertToDto(movie)));
    }
    
    @GetMapping("/movie/history")
    public ResponseEntity<GeneralDto<List<MovieDto>>> getHistory(Principal principal) {
        Account account = accountService.getAccount(Long.valueOf(principal.getName()));
        List<Movie> movie = movieService.getHistory(account);

        return ResponseEntity.ok().body(convertToDto(movie.stream()
            .map(x -> convertToDto(x))
            .collect(Collectors.toList())));
    }
}
