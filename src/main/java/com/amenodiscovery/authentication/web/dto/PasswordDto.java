package com.amenodiscovery.authentication.web.dto;

import com.amenodiscovery.authentication.validation.ValidPassword;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordDto {
    private String oldPassword;

    private String token;

    @ValidPassword
    private String newPassword;
}
