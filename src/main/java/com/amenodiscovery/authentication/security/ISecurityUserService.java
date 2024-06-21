package com.amenodiscovery.authentication.security;

public interface ISecurityUserService {

    String validatePasswordResetToken(String token);

}
