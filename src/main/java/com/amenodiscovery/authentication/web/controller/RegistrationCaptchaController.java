package com.amenodiscovery.authentication.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.amenodiscovery.authentication.captcha.CaptchaServiceV3;
import com.amenodiscovery.authentication.captcha.ICaptchaService;
import com.amenodiscovery.authentication.persistence.model.User;
import com.amenodiscovery.authentication.registration.OnRegistrationCompleteEvent;
import com.amenodiscovery.authentication.service.IUserService;
import com.amenodiscovery.authentication.web.dto.UserDto;
import com.amenodiscovery.authentication.web.util.GenericResponse;

@RestController
public class RegistrationCaptchaController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private IUserService userService;

    @Autowired
    private ICaptchaService captchaService;
    
    @Autowired
    private ICaptchaService captchaServiceV3;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private Environment env;
 
    public RegistrationCaptchaController() {
        super();
    }

    // Registration
    @PostMapping("/v1/user/registration/captcha")
    public GenericResponse captchaRegisterUserAccount(@RequestBody @Valid final UserDto accountDto, final HttpServletRequest request) {

        final String response = request.getParameter("g-recaptcha-response");
        captchaService.processResponse(response);

        return registerNewUserHandler(accountDto, request);
    }

    
    // Registration reCaptchaV3
    @PostMapping("/v1/user/registration/captchav3")
    public GenericResponse captchaV3RegisterUserAccount(@RequestBody @Valid final UserDto accountDto, final HttpServletRequest request) {

        final String response = request.getParameter("response");
        captchaServiceV3.processResponse(response, CaptchaServiceV3.REGISTER_ACTION);

        return registerNewUserHandler(accountDto, request);
    }
    
    private GenericResponse registerNewUserHandler(final UserDto accountDto, final HttpServletRequest request) {
        LOGGER.debug("Registering user account with information: {}", accountDto);

        final User registered = userService.registerNewUserAccount(accountDto);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl()));
        return new GenericResponse("success");
    }
    

    private String getAppUrl() {
        return env.getProperty("app.frontend.url");
    }

}
