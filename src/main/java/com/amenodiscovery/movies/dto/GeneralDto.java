package com.amenodiscovery.movies.dto;

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
public class GeneralDto<T> {
    private T data;

    public static <T> GeneralDto<T> convertToDto(T data) {
        GeneralDto<T> value = new GeneralDto<>();
        value.setData(data);
        return value;
    }
}