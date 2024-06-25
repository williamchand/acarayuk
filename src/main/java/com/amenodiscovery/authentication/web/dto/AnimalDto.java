package com.amenodiscovery.authentication.web.dto;

import com.amenodiscovery.authentication.persistence.model.Animal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalDto {

    private String name;

    private String url;

    public static final AnimalDto convertToDto(Animal animal) {
        return AnimalDto.builder()
                .name(animal.getName())
                .url(animal.getUrl())
                .build();
    }
}
