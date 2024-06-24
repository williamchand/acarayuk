package com.amenodiscovery.authentication.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import com.amenodiscovery.authentication.persistence.model.NewLocationToken;
import com.amenodiscovery.authentication.persistence.model.PasswordResetToken;
import com.amenodiscovery.authentication.persistence.model.User;
import com.amenodiscovery.authentication.persistence.model.VerificationToken;
import com.amenodiscovery.authentication.web.dto.IdTokenRequestDto;
import com.amenodiscovery.authentication.web.dto.UserDto;

import jakarta.servlet.http.HttpServletRequest;

public interface IUserService {

    User registerNewUserAccount(UserDto accountDto);

    User getUser(String verificationToken);

    void saveRegisteredUser(User user);

    void deleteUser(User user);

    void createVerificationTokenForUser(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);

    VerificationToken generateNewVerificationToken(String userEmail);

    void createPasswordResetTokenForUser(User user, String token);

    String login(String email, String password, HttpServletRequest request);

    String loginOAuthGoogle(final IdTokenRequestDto requestBody);

    User findUserByEmail(String email);

    PasswordResetToken getPasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    Optional<User> getUserByID(long id);

    public User getAccount(Long id);

    public User getUserInfo(Long id);
    
    void changeUserPassword(User user, String password);

    boolean checkIfValidOldPassword(User user, String password);

    String validateVerificationToken(String token);

    String generateQRUrl(User user) throws UnsupportedEncodingException;

    User updateUser2FA(boolean use2FA);

    List<String> getUsersFromSessionRegistry();

    NewLocationToken isNewLoginLocation(String username, String ip);

    String isValidNewLocationToken(String token);

    void addUserLocation(User user, String ip);
}
