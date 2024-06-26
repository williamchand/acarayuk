package com.amenodiscovery.authentication.web.controller;

import com.amenodiscovery.authentication.web.dto.AnimalDto;
import com.amenodiscovery.authentication.web.dto.GeneralDto;
import com.amenodiscovery.authentication.persistence.model.Animal;
import com.amenodiscovery.authentication.service.UserService;
import com.amenodiscovery.authentication.service.AnimalService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AnimalController {

    private final static Logger LOGGER = LoggerFactory.getLogger(AnimalController.class);

    @Autowired
    AnimalService animalService;

    @Autowired
    UserService accountService;

    @GetMapping("/v1/public/animal/recommend/{name}")
    public ResponseEntity<GeneralDto<AnimalDto>> getRecommendation(Principal principal, @PathVariable("name") String name) {
        Animal animal = animalService.getAnimalTypeByName(name.trim().toLowerCase());
        return ResponseEntity.ok().body(GeneralDto.convertToDto(AnimalDto.convertToDto(animal)));
    }
}
