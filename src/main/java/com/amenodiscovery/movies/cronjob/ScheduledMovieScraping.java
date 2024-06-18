package com.amenodiscovery.movies.cronjob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.amenodiscovery.movies.service.MovieService;

@Component
public class ScheduledMovieScraping {

    @Autowired
    private MovieService movieService;

    // @Scheduled(cron = "0 * * * * *") // Cron expression for running every minute
    @Scheduled(initialDelay = 1)
    public void execute() {
        movieService.scrapeImdb();
    }
}
