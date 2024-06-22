package com.amenodiscovery.authentication.web.dto;

import com.amenodiscovery.authentication.validation.ValidPassword;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    private String email;

    @ValidPassword
    private String password;
}
