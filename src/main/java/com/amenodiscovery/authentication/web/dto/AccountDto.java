package com.amenodiscovery.authentication.web.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.amenodiscovery.authentication.persistence.model.User;
import com.amenodiscovery.authentication.persistence.model.Role;

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
public class AccountDto {

    private String firstName;

    private String lastName;

    private String email;

    private boolean isEnabled;

    private Set<String> roles;

    public static final AccountDto convertToDto(User account) {
        Set<String> rolesSet = account.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toSet());
        return AccountDto.builder()
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .email(account.getEmail())
                .isEnabled(account.isEnabled())
                .roles(rolesSet)
                .build();
    }
}
