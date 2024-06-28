package com.amenodiscovery.authentication.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.http.HttpHeaders;

@Service
public class RestService {
    private static Logger logger = LoggerFactory.getLogger(RestService.class);

    @Autowired
    private Environment env;

    @Autowired
    protected RestOperations restTemplate;

    public ResponseEntity<String> getTrakteerPlainJSON(int page) {
        String uri = "https://api.trakteer.id/v1/public/supports";
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(uri)
                            .queryParam("limit","5")
                            .queryParam("page", page).build();
        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.set("Accept", MediaType.APPLICATION_JSON.toString());
        // set custom header
        headers.set("Key", env.getProperty("app.trakteerSecret"));
        headers.set("X-Requested-With", "XMLHttpRequest");
        // build the request
        HttpEntity<String> request = new HttpEntity<>("", headers);
        ResponseEntity<String> resp = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, String.class);
        logger.info("{}", resp);
        return resp;
    }
}