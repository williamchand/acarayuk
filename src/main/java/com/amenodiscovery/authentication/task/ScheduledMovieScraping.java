package com.amenodiscovery.authentication.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.amenodiscovery.authentication.service.MovieService;

@Component
public class ScheduledMovieScraping {

    @Autowired
    private MovieService movieService;

    // @Scheduled(cron = "${app.cron.scrape.expression}")
    @Scheduled(initialDelay = 1)
    public void execute() {
        movieService.scrapeImdb();
    }
}
