package com.amenodiscovery.authentication.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amenodiscovery.authentication.persistence.model.Animal;

import java.util.List;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
    List<Animal> findAll();
}
