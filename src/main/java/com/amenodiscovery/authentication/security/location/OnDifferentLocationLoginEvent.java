package com.amenodiscovery.authentication.security.location;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import com.amenodiscovery.authentication.persistence.model.NewLocationToken;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class OnDifferentLocationLoginEvent extends ApplicationEvent {

    private final Locale locale;
    private final String username;
    private final String ip;
    private final NewLocationToken token;
    private final String appUrl;

    //

    public OnDifferentLocationLoginEvent(Locale locale, String username, String ip, NewLocationToken token, String appUrl) {
        super(token);
        this.locale = locale;
        this.username = username;
        this.ip = ip;
        this.token = token;
        this.appUrl = appUrl;
    }
}
