package com.amenodiscovery.movies.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.amenodiscovery.movies.user.Account;
import com.amenodiscovery.movies.user.Role;

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

    private Set<String> roles;

    public static final AccountDto convertToDto(Account account) {
        Set<String> rolesSet = account.getRoles().stream()
            .map(Role::getRoleName)
            .collect(Collectors.toSet());
        return AccountDto.builder()
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .email(account.getEmail())
                .roles(rolesSet)
                .build();
    }
}
