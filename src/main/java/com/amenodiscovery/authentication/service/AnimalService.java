package com.amenodiscovery.authentication.service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.amenodiscovery.authentication.persistence.model.Animal;
import com.amenodiscovery.authentication.persistence.model.AnimalHistory;
import com.amenodiscovery.authentication.persistence.dao.AnimalHistoryRepository;
import com.amenodiscovery.authentication.persistence.dao.AnimalRepository;
import java.util.Optional;


@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final AnimalHistoryRepository animalHistoryRepository;

    public AnimalService(AnimalRepository animalRepository, AnimalHistoryRepository animalHistoryRepository) {
        this.animalRepository = animalRepository;
        this.animalHistoryRepository = animalHistoryRepository;
    }

    public Animal getAnimalTypeByName(String name) {
        Date historyDate = new Date();
        if (name != null) {
            Optional<AnimalHistory> animalHistory = animalHistoryRepository.findByNameAndHistoryDate(name, historyDate);
            if (animalHistory.isPresent()) {
                return animalHistory.get().getAnimal();
            }
        }
        Optional<Animal> animal = getRandomAnimal();
        if (!animal.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "animal not found");
        }
        if (name != null) {
            AnimalHistory animalHistory = new AnimalHistory(animal.get(), historyDate, name);
            animalHistoryRepository.save(animalHistory);
        }
        return animal.get();
    }

    private Optional<Animal> getRandomAnimal() {
        Optional<Animal> animal = Optional.empty();
        List<Animal> animalList = animalRepository.findAll();
        if (!animalList.isEmpty()) {
            SecureRandom rand = new SecureRandom();
            animal = Optional.of(animalList.get(rand.nextInt(animalList.size())));
        }

        return animal;
    }

}
