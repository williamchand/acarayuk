package com.amenodiscovery.movies.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.amenodiscovery.movies.config.JWTUtils;
import com.amenodiscovery.movies.dto.IdTokenRequestDto;
import com.amenodiscovery.movies.repository.AccountRepository;
import com.amenodiscovery.movies.repository.RoleRepository;
import com.amenodiscovery.movies.user.Account;
import com.amenodiscovery.movies.user.Role;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class AccountService {
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final JWTUtils jwtUtils;
    private final GoogleIdTokenVerifier verifier;

    public AccountService(@Value("${app.googleClientId}") String clientId, 
                          AccountRepository accountRepository,
                          RoleRepository roleRepository,
                          JWTUtils jwtUtils) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();
        verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    public Account getAccount(Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "account not found");
        }
        return account;
    }

    public String loginOAuthGoogle(IdTokenRequestDto requestBody) {
        Account account = verifyIDToken(requestBody.getIdToken());
        if (account == null) {
            throw new IllegalArgumentException();
        }
        account = createOrUpdateUser(account);
        return jwtUtils.createToken(account, false);
    }

    @Transactional
    public Account createOrUpdateUser(Account account) {
        Account existingAccount = accountRepository.findByEmail(account.getEmail()).orElse(null);
        if (existingAccount == null) {
            Role role = new Role(account, "ROLE_USER");
            Set<Role> rolesSet = new HashSet<>();
            rolesSet.add(role);
            account.setRoles(rolesSet);
            accountRepository.save(account);
            roleRepository.save(role);
            return account;
        }
        existingAccount.setFirstName(account.getFirstName());
        existingAccount.setLastName(account.getLastName());
        existingAccount.setPictureUrl(account.getPictureUrl());
        accountRepository.save(existingAccount);
        return existingAccount;
    }

    private Account verifyIDToken(String idToken) {
        try {
            GoogleIdToken idTokenObj = verifier.verify(idToken);
            if (idTokenObj == null) {
                return null;
            }
            GoogleIdToken.Payload payload = idTokenObj.getPayload();
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");
            String email = payload.getEmail();
            String pictureUrl = (String) payload.get("picture");

            return new Account(firstName, lastName, email, pictureUrl);
        } catch (GeneralSecurityException | IOException e) {
            return null;
        }
    }
}
