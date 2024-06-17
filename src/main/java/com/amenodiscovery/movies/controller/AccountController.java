package com.amenodiscovery.movies.controller;

import com.amenodiscovery.movies.dto.AccountDto;
import com.amenodiscovery.movies.service.AccountService;
import com.amenodiscovery.movies.user.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.amenodiscovery.movies.dto.AccountDto.convertToDto;

@RestController
@RequestMapping("/v1/oauth")
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/user/info")
    public ResponseEntity<AccountDto> getUserInfo(Principal principal) {
        Account account = accountService.getAccount(Long.valueOf(principal.getName()));
        return ResponseEntity.ok().body(convertToDto(account));
    }
}
