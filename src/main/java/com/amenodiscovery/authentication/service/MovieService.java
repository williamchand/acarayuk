package com.amenodiscovery.authentication.service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.amenodiscovery.authentication.persistence.model.User;
import com.amenodiscovery.authentication.persistence.model.Movie;
import com.amenodiscovery.authentication.persistence.model.MovieHistory;
import com.amenodiscovery.authentication.persistence.model.ScrapeTemplate;
import com.amenodiscovery.authentication.persistence.dao.MovieHistoryRepository;
import com.amenodiscovery.authentication.persistence.dao.MovieRepository;
import java.util.Optional;

import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.transaction.Transactional;

@Service
public class MovieService {
    private static Logger logger = LoggerFactory.getLogger(MovieService.class);

    private final MovieRepository movieRepository;
    private final MovieHistoryRepository movieHistoryRepository;
    private final ScrapeTemplateService scrapeTemplateService;

    public MovieService(MovieRepository movieRepository, ScrapeTemplateService scrapeTemplateService, MovieHistoryRepository movieHistoryRepository) {
        this.movieRepository = movieRepository;
        this.scrapeTemplateService = scrapeTemplateService;
        this.movieHistoryRepository = movieHistoryRepository;
    }

    public void scrapeImdb() {
        logger.info("Scheduled task executed at: {}", new Date());
        WebDriver driver;
        driver = WebDriverManager.chromedriver().create();
        List<ScrapeTemplate> scrapeTemplates = scrapeTemplateService.getScrapeTemplates("imdb");
        for (ScrapeTemplate element : scrapeTemplates) {
            String url = element.getUrl();
            String genre = element.getGenre();
            driver.navigate().to(url);
            parseDataOnPage(driver, genre);
        }
        driver.quit();
    }

    private void parseDataOnPage(WebDriver driver, String genre) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement response = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[contains(@class,'ipc-metadata-list')]")));

        List<WebElement> lists = response.findElements(By.xpath(".//li[contains(@class,'ipc-metadata-list-summary-item')]"));
        for (WebElement list: lists) {
            String uniqueIdentifier = list.findElement(By.xpath(".//a[contains(@class,'ipc-title-link-wrapper')]")).getAttribute("href");
            String title = list.findElement(By.xpath(".//h3[@class='ipc-title__text']")).getText().split(" ", 2)[1];
            String pictureUrl = list.findElement(By.xpath(".//div[contains(@class,'ipc-media')]/img")).getAttribute("src");
            String yearRelease = parseElement(list, ".//span[contains(@class,'li-title-metadata-item')][1]");
            String playingTime = parseElement(list, ".//span[contains(@class,'li-title-metadata-item')][2]");
            String description = parseElement(list, ".//div[contains(@class,'ipc-html-content-inner-div')]");
            createOrUpdateMovie(new Movie(title, genre, uniqueIdentifier, pictureUrl, yearRelease, playingTime, description));
       }
    }

    public Movie getRecommendation(User account, String genre) {
        Date historyDate = new Date();
        if (account != null) {
            Optional<MovieHistory> movieHistory = movieHistoryRepository.findByAccountAndGenreAndHistoryDate(account, genre, historyDate);
            if (movieHistory.isPresent()) {
                return movieHistory.get().getMovie();
            }
        }
        Optional<Movie> movie = getRandomMovieByGenre(genre);
        if (!movie.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "movie not found");
        }
        if (account != null) {
            MovieHistory movieHistory = new MovieHistory(account, movie.get(), historyDate, genre);
            movieHistoryRepository.save(movieHistory);
        }
        return movie.get();
    }

    private Optional<Movie> getRandomMovieByGenre(String genre) {
        Optional<Movie> movie = Optional.empty();
        List<Movie> movieList = movieRepository.findByGenre(genre);
        if (!movieList.isEmpty()) {
            SecureRandom rand = new SecureRandom();
            movie = Optional.of(movieList.get(rand.nextInt(movieList.size())));
        }

        return movie;
    }

    public List<Movie> getHistory(User account) {
        List<MovieHistory> movieHistories = movieHistoryRepository.findByAccount(account);
        return movieHistories.stream()
            .map(MovieHistory::getMovie)
            .collect(Collectors.toList());
    }

    private String parseElement(WebElement elem, String xpath) {
        try {
            return elem.findElement(By.xpath(xpath)).getText();
        }
        catch (Exception e) {
            return "";
        }
    }

    @Transactional
    public Movie createOrUpdateMovie(Movie movie) {
        Movie existingMovie = movieRepository.findByUniqueIdentifierAndGenre(movie.getUniqueIdentifier(), movie.getGenre()).orElse(null);
        if (existingMovie == null) {
            movieRepository.save(movie);
            return movie;
        }
        if (!movie.getTitle().equals("")) {
            existingMovie.setTitle(movie.getTitle());
        }
        if (!movie.getDescription().equals("")) {
            existingMovie.setDescription(movie.getDescription());
        }
        if (!movie.getPictureUrl().equals("")) {
            existingMovie.setPictureUrl(movie.getPictureUrl());
        }
        if (!movie.getPlayingTime().equals("")) {
            existingMovie.setPlayingTime(movie.getPlayingTime());
        }
        if (!movie.getYearRelease().equals("")) {
            existingMovie.setYearRelease(movie.getYearRelease());
        }
        movieRepository.save(existingMovie);
        return existingMovie;
    }
}
