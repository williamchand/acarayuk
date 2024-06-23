package com.amenodiscovery.authentication.web.dto;

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
public class GeneralMetadataDto<T,A> {
    private T data;

    private A metadata;

    public static <T, A> GeneralMetadataDto<T, A> convertToDto(T data, A metadata) {
        GeneralMetadataDto<T, A> value = new GeneralMetadataDto<>();
        value.setData(data);
        value.setMetadata(metadata);
        return value;
    }

}
