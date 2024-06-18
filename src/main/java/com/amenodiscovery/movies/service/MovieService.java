package com.amenodiscovery.movies.service;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amenodiscovery.movies.config.JWTUtils;
import com.amenodiscovery.movies.entity.Account;
import com.amenodiscovery.movies.entity.Movie;
import com.amenodiscovery.movies.entity.Role;
import com.amenodiscovery.movies.entity.ScrapeTemplate;
import com.amenodiscovery.movies.repository.AccountRepository;
import com.amenodiscovery.movies.repository.MovieRepository;
import com.amenodiscovery.movies.repository.RoleRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.transaction.Transactional;

@Service
public class MovieService {
    private static Logger logger = LoggerFactory.getLogger(MovieService.class);

    private final MovieRepository movieRepository;
    private final ScrapeTemplateService scrapeTemplateService;

    public MovieService(MovieRepository movieRepository, ScrapeTemplateService scrapeTemplateService) {
        this.movieRepository = movieRepository;
        this.scrapeTemplateService = scrapeTemplateService;
    }

    public void scrapeImdb() {
        logger.info("Scheduled task executed at: {}", new Date());
        WebDriver driver;
        WebDriverManager wdm = WebDriverManager.chromedriver().browserInDocker()
            .dockerDefaultArgs("--disable-gpu,--no-sandbox");
        driver = wdm.create();
        List<ScrapeTemplate> scrapeTemplates = scrapeTemplateService.getScrapeTemplates("imdb");
        for (ScrapeTemplate element : scrapeTemplates) {
            String url = element.getUrl();
            String genre = element.getGenre();
            driver.navigate().to(url);
            parseDataOnPage(driver, genre);
        }
        driver.close();
    }

    public void parseDataOnPage(WebDriver driver, String genre) {
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
